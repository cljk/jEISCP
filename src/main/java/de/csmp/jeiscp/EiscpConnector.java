package de.csmp.jeiscp;

import static de.csmp.jeiscp.EiscpConstants.DEFAULT_EISCP_PORT;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TCP connection to one receiver. Once {@link #connect()} succeeded, a lost connection is re-established in the
 * background with growing pauses until {@link #close()}; listeners stay registered across reconnects.
 */
public class EiscpConnector {
	private static final Logger log = LoggerFactory.getLogger(EiscpConnector.class);

	public enum ConnectionState { DISCONNECTED, CONNECTING, CONNECTED, LOST, CLOSED }

	private static final int CONNECT_TIMEOUT_MILLIS = 5000;

	private final String host;
	private final int port;
	private final List<EiscpListener> listeners = new CopyOnWriteArrayList<>();
	private final List<Consumer<ConnectionState>> stateListeners = new CopyOnWriteArrayList<>();
	private Duration firstRetry = Duration.ofSeconds(1);
	private Duration maxRetry = Duration.ofSeconds(30);

	private final Object lock = new Object();
	private ConnectionState state = ConnectionState.DISCONNECTED;
	private Socket socket;
	private BufferedOutputStream socketOut;
	private EiscpConnectorSocketReaderThread reader;
	private Thread reconnecting;
	private volatile String address;
	private final EiscpDevice device;

	public EiscpConnector(String host) {
		this(host, DEFAULT_EISCP_PORT);
	}

	public EiscpConnector(String host, int port) {
		this(host, port, null);
	}

	/** For a receiver found by {@link EiscpAutodiscovery}: its address and port, and {@link #getDevice()} knows its model. */
	public EiscpConnector(EiscpDevice device) {
		this(device.getIpAddress(), device.getPort(), device);
	}

	private EiscpConnector(String host, int port, EiscpDevice device) {
		this.host = host;
		this.port = port;
		this.address = host;
		this.device = device;
	}

	/**
	 * Connects to the first receiver that answers the broadcast.
	 * @throws java.net.SocketTimeoutException if none answers within {@link EiscpAutodiscovery#DEFAULT_TIMEOUT}
	 */
	public static EiscpConnector autodiscover() throws IOException {
		EiscpConnector conn = new EiscpConnector(new EiscpAutodiscovery().discover());
		conn.connect();
		return conn;
	}

	/** First connection attempt; its failure is thrown, later losses are retried in the background. */
	public void connect() throws IOException {
		setState(ConnectionState.CONNECTING);
		try {
			if (open()) {
				setState(ConnectionState.CONNECTED);
			}
		} catch (IOException ex) {
			setState(ConnectionState.DISCONNECTED);
			throw ex;
		}
	}

	/** @return {@code false} if the connector was closed meanwhile */
	private boolean open() throws IOException {
		log.debug("connect to {}:{}", host, port);
		Socket s = new Socket();
		try {
			s.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MILLIS);
		} catch (IOException ex) {
			s.close();
			throw ex;
		}
		synchronized (lock) {
			if (state == ConnectionState.CLOSED) {
				s.close();
				return false;
			}
			socket = s;
			socketOut = new BufferedOutputStream(s.getOutputStream());
			reader = new EiscpConnectorSocketReaderThread(new BufferedInputStream(s.getInputStream()),
					this::fireReceived, this::readerStopped);
			address = s.getInetAddress().getHostAddress();
			Thread thread = new Thread(reader, "eiscp-reader");
			thread.setDaemon(true);
			thread.start();
		}
		log.debug("connected");
		return true;
	}

	/** Called by a reader that stopped without {@link #close()}: the connection is gone. */
	private void readerStopped(EiscpConnectorSocketReaderThread stopped, Exception cause) {
		synchronized (lock) {
			if (stopped != reader || state == ConnectionState.CLOSED) {
				return;
			}
			closeQuietly(socket);
			socket = null;
			socketOut = null;
			reader = null;
		}
		log.warn("connection to {}:{} lost: {}", host, port, cause.getMessage());
		setState(ConnectionState.LOST);
		scheduleReconnect();
	}

	private void scheduleReconnect() {
		synchronized (lock) {
			if (reconnecting != null || state == ConnectionState.CLOSED) {
				return;
			}
			reconnecting = new Thread(this::reconnectLoop, "eiscp-reconnect");
			reconnecting.setDaemon(true);
			reconnecting.start();
		}
	}

	private void reconnectLoop() {
		long delay = firstRetry.toMillis();
		try {
			while (getState() != ConnectionState.CLOSED) {
				Thread.sleep(delay);
				setState(ConnectionState.CONNECTING);
				try {
					if (open()) {
						log.info("reconnected to {}:{}", host, port);
						setState(ConnectionState.CONNECTED);
					}
					return;
				} catch (IOException ex) {
					delay = Math.min(delay * 2, maxRetry.toMillis());
					log.info("reconnect to {}:{} failed: {} - next try in {} s", host, port, ex.getMessage(), delay / 1000);
					setState(ConnectionState.LOST);
				}
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		} finally {
			synchronized (lock) {
				reconnecting = null;
			}
		}
	}

	/** Pauses between reconnect attempts: doubling from {@code first} up to {@code max}. */
	void setRetryDelays(Duration first, Duration max) {
		this.firstRetry = first;
		this.maxRetry = max;
	}

	public void addListener(EiscpListener listener) {
		listeners.add(listener);
	}

	public void removeListener(EiscpListener listener) {
		listeners.remove(listener);
	}

	/** Listeners run on the thread that changes the state: reader, reconnect or caller thread. */
	public void addStateListener(Consumer<ConnectionState> listener) {
		stateListeners.add(listener);
	}

	public void removeStateListener(Consumer<ConnectionState> listener) {
		stateListeners.remove(listener);
	}

	public ConnectionState getState() {
		synchronized (lock) {
			return state;
		}
	}

	/** IP address of the receiver, e.g. {@code 192.168.0.97}; the configured host until connected. */
	public String getAddress() {
		return address;
	}

	/** @return the discovered receiver, {@code null} if this connector was created with a plain address */
	public EiscpDevice getDevice() {
		return device;
	}

	/** A failing listener is logged and does not keep the message from the others. */
	private void fireReceived(String message) {
		for (EiscpListener listener : listeners) {
			try {
				listener.receivedIscpMessage(message);
			} catch (RuntimeException ex) {
				log.error("error in listener {}", ex.getMessage(), ex);
			}
		}
	}

	private void setState(ConnectionState next) {
		synchronized (lock) {
			if (state == ConnectionState.CLOSED || state == next) {
				return;
			}
			state = next;
		}
		fireState(next);
	}

	private void fireState(ConnectionState next) {
		for (Consumer<ConnectionState> listener : stateListeners) {
			try {
				listener.accept(next);
			} catch (RuntimeException ex) {
				log.error("error in state listener {}", ex.getMessage(), ex);
			}
		}
	}

	public void sendIscpCommand(String command) throws IOException {
		log.debug("sendIscpCommand: " + command);
		sendIscpMessage("!1" + command);
	}

	/**
	 * @param message already has prepended !1
	 * @throws IOException also while the connection is lost
	 */
	public void sendIscpMessage(String message) throws IOException {
		byte[] eiscpMessage = EiscpProtocolHelper.iscpToEiscpMessage(message);
		log.trace("sendIscpMessage: {} - eISCP message: {} bytes - {}",
				message, eiscpMessage.length, EiscpProtocolHelper.convertToHexString(eiscpMessage));

		synchronized (lock) {
			if (socketOut == null) {
				throw new IOException("not connected to " + host + ":" + port + " (" + state + ")");
			}
			socketOut.write(eiscpMessage);
			socketOut.flush();
		}
	}

	/** Ends the connection for good; no reconnect follows. */
	public void close() {
		log.debug("-- close");
		Thread retry;
		synchronized (lock) {
			if (state == ConnectionState.CLOSED) {
				return;
			}
			state = ConnectionState.CLOSED;
			if (reader != null) {
				reader.quit();
			}
			closeQuietly(socket);
			socket = null;
			socketOut = null;
			reader = null;
			retry = reconnecting;
		}
		if (retry != null) {
			retry.interrupt();
		}
		fireState(ConnectionState.CLOSED);
	}

	private static void closeQuietly(Socket s) {
		if (s != null) {
			try {
				s.close();
			} catch (IOException ex) {
				log.trace("close: {}", ex.getMessage());
			}
		}
	}
}
