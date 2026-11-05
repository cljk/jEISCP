package de.csmp.jeiscp;

import static de.csmp.jeiscp.EiscpConstants.AUTODISCOVER_QSTN;
import static de.csmp.jeiscp.EiscpConstants.DEFAULT_EISCP_PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds a receiver by UDP broadcast, resending the query until one answers or the timeout expires.
 * Queries go to every interface's directed broadcast address, because Windows sends 255.255.255.255 out of one interface only.
 */
public class EiscpAutodiscovery {
	private static final Logger log = LoggerFactory.getLogger(EiscpAutodiscovery.class);

	public static final Duration DEFAULT_RESEND_INTERVAL = Duration.ofSeconds(1);
	public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

	private static final byte[] LIMITED_BROADCAST = { (byte) 255, (byte) 255, (byte) 255, (byte) 255 };

	private final List<InetAddress> targets;
	private final int port;
	private final Duration resendInterval;
	private final Duration timeout;

	public EiscpAutodiscovery() {
		this(broadcastAddresses(), DEFAULT_EISCP_PORT, DEFAULT_RESEND_INTERVAL, DEFAULT_TIMEOUT);
	}

	public EiscpAutodiscovery(List<InetAddress> targets, int port, Duration resendInterval, Duration timeout) {
		this.targets = List.copyOf(targets);
		this.port = port;
		this.resendInterval = resendInterval;
		this.timeout = timeout;
	}

	/** Asks one receiver for its model, e.g. after {@code -a <ip>}; {@code null} if it does not answer within two seconds. */
	public static EiscpDevice identify(String address) {
		return identify(address, DEFAULT_EISCP_PORT, Duration.ofSeconds(2));
	}

	static EiscpDevice identify(String address, int port, Duration timeout) {
		try {
			List<InetAddress> target = List.of(InetAddress.getByName(address));
			return new EiscpAutodiscovery(target, port, Duration.ofMillis(500), timeout).discover();
		} catch (IOException ex) {
			log.info("{} did not report its model: {}", address, ex.getMessage());
			return null;
		}
	}

	/** @throws SocketTimeoutException if no receiver answered within the timeout */
	public EiscpDevice discover() throws IOException {
		byte[] query = EiscpProtocolHelper.iscpToEiscpMessage(AUTODISCOVER_QSTN);
		long deadline = System.nanoTime() + timeout.toNanos();

		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setBroadcast(true);
			log.info("autodiscovery: sending {} to {} port {}", AUTODISCOVER_QSTN, hostAddresses(), port);

			for (int attempt = 1; System.nanoTime() < deadline; attempt++) {
				sendQuery(socket, query);
				long resendAt = Math.min(deadline, System.nanoTime() + resendInterval.toNanos());
				EiscpDevice device = awaitAnswer(socket, resendAt);
				if (device != null) {
					log.info("autodiscovered {} after {} queries", device, attempt);
					return device;
				}
				log.debug("no answer to query #{}", attempt);
			}
		}
		throw new SocketTimeoutException("no receiver answered the autodiscovery within " + timeout.toSeconds() + "s");
	}

	private void sendQuery(DatagramSocket socket, byte[] query) {
		for (InetAddress target : targets) {
			try {
				socket.send(new DatagramPacket(query, query.length, target, port));
			} catch (IOException ex) {
				// e.g. an interface that went down; the others may still work
				log.debug("cannot send to {}: {}", target.getHostAddress(), ex.getMessage());
			}
		}
	}

	private static EiscpDevice awaitAnswer(DatagramSocket socket, long untilNanos) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			long remainingMillis = (untilNanos - System.nanoTime()) / 1_000_000;
			if (remainingMillis <= 0) {
				return null;
			}
			socket.setSoTimeout((int) remainingMillis);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException ex) {
				return null;
			}
			EiscpDevice device = parseAnswer(packet);
			if (device != null) {
				return device;
			}
		}
	}

	/** @return {@code null} if the datagram is no autodiscovery answer */
	static EiscpDevice parseAnswer(DatagramPacket packet) {
		String sender = packet.getAddress().getHostAddress();
		try {
			byte[] data = Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet.getOffset() + packet.getLength());
			String iscp = EiscpProtocolHelper.interpreteEiscpResponse(data);
			if (iscp.startsWith("ECN")) {
				return new EiscpDevice(iscp, sender);
			}
			log.debug("ignore {} from {}", iscp, sender);
		} catch (Exception ex) {
			log.debug("ignore datagram from {}: {}", sender, ex.getMessage());
		}
		return null;
	}

	/** Directed broadcast address of every active IPv4 interface, plus 255.255.255.255. */
	public static List<InetAddress> broadcastAddresses() {
		Set<InetAddress> addresses = new LinkedHashSet<>();
		try {
			for (NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (!nif.isUp() || nif.isLoopback()) {
					continue;
				}
				for (InterfaceAddress address : nif.getInterfaceAddresses()) {
					if (address.getAddress() instanceof Inet4Address && address.getBroadcast() != null) {
						addresses.add(address.getBroadcast());
					}
				}
			}
		} catch (SocketException ex) {
			log.warn("cannot list network interfaces: {}", ex.getMessage());
		}
		try {
			addresses.add(InetAddress.getByAddress(LIMITED_BROADCAST));
		} catch (IOException ex) {
			throw new IllegalStateException(ex);	// cannot happen for a 4-byte address
		}
		return List.copyOf(addresses);
	}

	private List<String> hostAddresses() {
		return targets.stream().map(InetAddress::getHostAddress).toList();
	}
}
