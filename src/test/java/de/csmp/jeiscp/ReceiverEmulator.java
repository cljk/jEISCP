package de.csmp.jeiscp;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

/**
 * Emulates a TX-NR626 on 127.0.0.1, TCP and autodiscovery on one port, with its quirks: it ignores {@code NSTQSTN}
 * and {@code NJAREQ} and repeats the old cover before a new one. JDK only: {@code java ReceiverEmulator.java [port|0]}.
 */
public final class ReceiverEmulator implements AutoCloseable {

	static final String MODEL = "TX-NR626";
	private static final int RECEIVER_PORT = 60128;	// not EiscpConstants, so the file runs on its own
	private static final String NET = "2B";
	private static final int MAX_VOLUME = 0x50;
	private static final int COVER_BLOCK_BYTES = 128;

	record Track(String artist, String title, String album, int seconds, Color from, Color to) {
	}

	/** The first two share an album and so their cover. */
	static final List<Track> PLAYLIST = List.of(
			new Track("Daft Punk", "Instant Crush", "Random Access Memories", 337, new Color(0xF5B700), new Color(0xC2185B)),
			new Track("Daft Punk", "Get Lucky", "Random Access Memories", 369, new Color(0xF5B700), new Color(0xC2185B)),
			new Track("Jean du Voyage", "Anitya", "Mantra", 166, new Color(0x2DD4BF), new Color(0x1E3A8A)),
			new Track("Simone Vitullo", "Understanding", "Understanding", 208, new Color(0x9333EA), new Color(0x111827)));

	private final ServerSocket server;
	private final DatagramSocket discovery;
	private final List<Client> clients = new CopyOnWriteArrayList<>();
	private final ScheduledExecutorService ticker = Executors.newSingleThreadScheduledExecutor(ReceiverEmulator::daemon);
	private final ExecutorService coverSender = Executors.newSingleThreadExecutor(ReceiverEmulator::daemon);
	private final Duration coverBlockDelay;
	private final boolean verbose;
	private final Random random = new Random();

	// receiver state, guarded by this
	private boolean power = true;
	private int volume = 0x1A;
	private boolean muted;
	private String input = NET;
	private int track;
	private int elapsed = 102;
	private char play = 'P';
	private char repeat = '-';
	private char shuffle = '-';
	private byte[] lastCover;

	/** @param port {@code 0} for a free one */
	public ReceiverEmulator(int port) throws IOException {
		this(port, Duration.ofSeconds(1), Duration.ofMillis(20), true);
	}

	ReceiverEmulator(int port, Duration tick, Duration coverBlockDelay, boolean verbose) throws IOException {
		this.coverBlockDelay = coverBlockDelay;
		this.verbose = verbose;
		InetAddress loopback = InetAddress.getLoopbackAddress();
		ServerSocket tcp;
		DatagramSocket udp = null;
		for (int attempt = 1;; attempt++) {
			tcp = new ServerSocket(port, 5, loopback);
			try {
				udp = new DatagramSocket(tcp.getLocalPort(), loopback);
				break;
			} catch (SocketException ex) {
				// Windows excludes other port ranges for UDP than for TCP
				if (port == 0 && attempt < 20) {
					tcp.close();
					continue;
				}
				if (verbose) {
					System.err.println("No autodiscovery: UDP port " + tcp.getLocalPort() + " is not available (" + ex.getMessage() + ").");
				}
				break;
			}
		}
		server = tcp;
		discovery = udp;
		start("emulator-accept", this::accept);
		if (discovery != null) {
			start("emulator-discovery", this::answerDiscovery);
		}
		ticker.scheduleAtFixedRate(this::tick, tick.toMillis(), tick.toMillis(), TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) throws Exception {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : RECEIVER_PORT;
		ReceiverEmulator started;
		try {
			started = new ReceiverEmulator(port);
		} catch (java.net.BindException ex) {
			System.err.println("Port " + port + " is not available (" + ex.getMessage() + "). On Windows it may lie in an"
					+ " excluded port range; pass another one, or 0 for a free one.");
			System.exit(1);
			return;
		}
		try (ReceiverEmulator emulator = started) {
			System.out.println(MODEL + " emulated on 127.0.0.1:" + emulator.port()
					+ ". Start jEISCP with -a 127.0.0.1:" + emulator.port() + ", stop this with Ctrl+C.");
			new CountDownLatch(1).await();
		}
	}

	public int port() {
		return server.getLocalPort();
	}

	@Override
	public void close() {
		ticker.shutdownNow();
		coverSender.shutdownNow();
		if (discovery != null) {
			discovery.close();
		}
		try {
			server.close();
		} catch (IOException ignored) {
			// closing anyway
		}
		clients.forEach(Client::close);
	}

	// ---- network

	private void accept() {
		while (!server.isClosed()) {
			try {
				Client client = new Client(server.accept());
				clients.add(client);
				synchronized (this) {
					if (power && NET.equals(input)) {
						client.send(serviceTitle());	// like the TX-NR626: unasked, right after the connect
					}
				}
				start("emulator-client", client::read);
			} catch (IOException ex) {
				return;	// closed
			}
		}
	}

	private void answerDiscovery() {
		byte[] buffer = new byte[512];
		while (!discovery.isClosed()) {
			try {
				DatagramPacket query = new DatagramPacket(buffer, buffer.length);
				discovery.receive(query);
				if (payload(query.getData(), query.getLength()).contains("ECNQSTN")) {
					byte[] answer = frame("!1ECN" + MODEL + "/" + port() + "/XX/0009B0000001\u0019\r\n");
					discovery.send(new DatagramPacket(answer, answer.length, query.getSocketAddress()));
				}
			} catch (IOException ex) {
				return;	// closed
			}
		}
	}

	private final class Client {
		private final Socket socket;
		private final OutputStream out;

		Client(Socket socket) throws IOException {
			this.socket = socket;
			this.out = socket.getOutputStream();
		}

		void read() {
			try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
				while (true) {
					byte[] header = new byte[16];
					in.readFully(header);
					ByteBuffer fields = ByteBuffer.wrap(header);
					int headerSize = fields.getInt(4);
					int dataSize = fields.getInt(8);
					in.skipNBytes(Math.max(0, headerSize - 16));
					byte[] data = new byte[dataSize];
					in.readFully(data);
					String message = payload(data, data.length);
					handle(message.startsWith("!1") ? message.substring(2) : message, this);
				}
			} catch (IOException ex) {
				close();
			}
		}

		synchronized void send(String iscp) {
			try {
				out.write(frame("!1" + iscp + "\u001a\r\n"));
				out.flush();
			} catch (IOException ex) {
				close();
			}
		}

		void close() {
			clients.remove(this);
			try {
				socket.close();
			} catch (IOException ignored) {
				// gone anyway
			}
		}
	}

	private void broadcast(String iscp) {
		if (verbose && !iscp.startsWith("NJA") && !iscp.startsWith("NTM")) {	// no cover blocks and time ticks
			System.out.println(">> " + iscp);
		}
		clients.forEach(client -> client.send(iscp));
	}

	// ---- commands

	private synchronized void handle(String command, Client from) {
		if (verbose) {
			System.out.println("<< " + command);
		}
		if (command.length() < 3) {
			return;
		}
		String head = command.substring(0, 3);
		String arg = command.substring(3);
		boolean query = arg.equals("QSTN");
		if (!power && !head.equals("PWR")) {
			return;	// in standby only power answers
		}
		switch (head) {
			case "PWR" -> {
				if (query) {
					from.send(power());
				} else if (arg.equals("00") || arg.equals("01")) {
					power = arg.equals("01");
					broadcast(power());
				}
			}
			case "MVL" -> {
				switch (arg) {
					case "QSTN" -> from.send(volume());
					case "UP", "UP1" -> setVolume(volume + 1);
					case "DOWN", "DOWN1" -> setVolume(volume - 1);
					default -> {
						if (arg.matches("[0-9A-F]{2}")) {
							setVolume(Integer.parseInt(arg, 16));
						}
					}
				}
			}
			case "AMT" -> {
				if (query) {
					from.send(mute());
				} else if (arg.matches("0[01]|TG")) {
					muted = arg.equals("TG") ? !muted : arg.equals("01");
					broadcast(mute());
				}
			}
			case "SLI" -> {
				if (query) {
					from.send("SLI" + input);
				} else if (arg.matches("[0-9A-F]{2}")) {
					selectInput(arg);
				}
			}
			case "IFV" -> answer(from, query, "IFVHDMI 2,1920 x 1080p  60 Hz,RGB,36 bit,HDMI Main,1920 x 1080p  60 Hz,RGB,36 bit,Custom,");
			case "RES" -> answer(from, query, "RES04");
			case "LMD" -> answer(from, query, "LMD00");
			case "NAT" -> answer(from, query && onNet(), "NAT" + current().artist());
			case "NTI" -> answer(from, query && onNet(), "NTI" + current().title());
			case "NAL" -> answer(from, query && onNet(), "NAL" + current().album());
			case "NTM" -> answer(from, query && onNet(), time());
			case "NTC" -> netKey(arg);
			case "NTS" -> seek(arg);
			default -> {
				// like the receiver: no answer, e.g. to NSTQSTN, NJAREQ or the OSD keys
			}
		}
	}

	private static void answer(Client to, boolean condition, String message) {
		if (condition) {
			to.send(message);
		}
	}

	private void netKey(String key) {
		if (!onNet()) {
			return;
		}
		switch (key) {
			case "PLAY" -> setPlay('P');
			case "PAUSE" -> setPlay('p');
			case "STOP" -> {
				elapsed = 0;
				setPlay('S');
			}
			case "TRUP" -> changeTrack(nextStep());
			case "TRDN" -> changeTrack(-1);
			case "REPEAT" -> {
				repeat = switch (repeat) {
					case '-' -> 'R';
					case 'R' -> '1';
					default -> '-';
				};
				broadcast(playStatus());
			}
			case "RANDOM" -> {
				shuffle = shuffle == '-' ? 'S' : '-';
				broadcast(playStatus());
			}
			default -> {
				// list navigation is not emulated
			}
		}
	}

	private void seek(String clock) {
		int seconds = 0;
		for (String field : clock.split(":")) {
			try {
				seconds = seconds * 60 + Integer.parseInt(field);
			} catch (NumberFormatException ex) {
				return;
			}
		}
		if (onNet()) {
			elapsed = Math.min(seconds, current().seconds() - 1);
			broadcast(time());
		}
	}

	private void setVolume(int value) {
		volume = Math.max(0, Math.min(MAX_VOLUME, value));
		broadcast(volume());
	}

	private void setPlay(char status) {
		if (play != status) {
			play = status;
			broadcast(playStatus());
		}
	}

	private void selectInput(String code) {
		input = code;
		broadcast("SLI" + code);
		if (onNet()) {
			broadcast(serviceTitle());
			broadcast(playStatus());
			nowPlaying();
		}
	}

	private int nextStep() {
		return shuffle == 'S' ? 1 + random.nextInt(PLAYLIST.size() - 1) : 1;
	}

	/** Like the TX-NR626: status and titles at once, the cover a few seconds later, the previous one first. */
	private void changeTrack(int step) {
		track = Math.floorMod(track + step, PLAYLIST.size());
		elapsed = 0;
		play = 'P';
		broadcast(playStatus());
		nowPlaying();
		broadcast("NTM--:--/" + clock(current().seconds()));

		byte[] previous = lastCover;
		byte[] next = cover(current());
		lastCover = next;
		coverSender.execute(() -> {
			if (previous != null) {
				transfer(previous);
			}
			transfer(next);
		});
	}

	private void nowPlaying() {
		broadcast("NAT" + current().artist());
		broadcast("NAL" + current().album());
		broadcast("NTI" + current().title());
	}

	private synchronized void tick() {
		if (!power || !onNet() || play != 'P') {
			return;
		}
		elapsed++;
		if (elapsed < current().seconds()) {
			broadcast(time());
		} else if (repeat == '1') {
			elapsed = 0;
			broadcast(time());
		} else {
			changeTrack(nextStep());
		}
	}

	/** NJA blocks: type 0 (BMP), then 0 for the first block, 1 for the following, 2 for the last. */
	private void transfer(byte[] image) {
		HexFormat hex = HexFormat.of().withUpperCase();
		for (int offset = 0; offset < image.length; offset += COVER_BLOCK_BYTES) {
			int end = Math.min(image.length, offset + COVER_BLOCK_BYTES);
			char flag = offset == 0 ? '0' : end == image.length ? '2' : '1';
			broadcast("NJA0" + flag + hex.formatHex(image, offset, end));
			pause(coverBlockDelay);
		}
	}

	// ---- messages and data

	private boolean onNet() {
		return NET.equals(input);
	}

	private Track current() {
		return PLAYLIST.get(track);
	}

	private String power() {
		return power ? "PWR01" : "PWR00";
	}

	private String volume() {
		return String.format("MVL%02X", volume);
	}

	private String mute() {
		return muted ? "AMT01" : "AMT00";
	}

	private String time() {
		return "NTM" + clock(elapsed) + "/" + clock(current().seconds());
	}

	private String playStatus() {
		return "NST" + play + repeat + shuffle;
	}

	/** NLT of Spotify as the TX-NR626 sends it: service type 0A, then the list title. */
	private static String serviceTitle() {
		return "NLT0A31000000010200FF0A00Spotify";
	}

	static String clock(int seconds) {
		return String.format("%02d:%02d", seconds / 60, seconds % 60);
	}

	/** 128×128, 8-bit BMP like the TX-NR626's; one per album. */
	static byte[] cover(Track track) {
		BufferedImage art = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = art.createGraphics();
		g.setPaint(new GradientPaint(0, 0, track.from(), 128, 128, track.to()));
		g.fillRect(0, 0, 128, 128);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
		g.drawString(track.album(), 8, 118);
		g.dispose();

		BufferedImage indexed = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_INDEXED);
		Graphics2D copy = indexed.createGraphics();
		copy.drawImage(art, 0, 0, null);
		copy.dispose();
		try {
			ByteArrayOutputStream bmp = new ByteArrayOutputStream();
			if (!ImageIO.write(indexed, "bmp", bmp)) {
				ImageIO.write(art, "bmp", bmp);
			}
			return bmp.toByteArray();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private static byte[] frame(String payload) {
		byte[] data = payload.getBytes(StandardCharsets.UTF_8);
		return ByteBuffer.allocate(16 + data.length)
				.put("ISCP".getBytes(StandardCharsets.US_ASCII)).putInt(16).putInt(data.length)
				.put(new byte[] { 1, 0, 0, 0 }).put(data).array();
	}

	/** The message of a frame (or of plain data), without terminators. */
	private static String payload(byte[] data, int length) {
		int start = length >= 16 && data[0] == 'I' && data[1] == 'S' && data[2] == 'C' && data[3] == 'P'
				? ByteBuffer.wrap(data).getInt(4) : 0;
		return new String(data, start, Math.max(0, length - start), StandardCharsets.UTF_8).strip()
				.replaceAll("[\\x19\\x1a\\r\\n]+$", "");
	}

	private static void pause(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	private static void start(String name, Runnable task) {
		Thread thread = daemon(task);
		thread.setName(name);
		thread.start();
	}

	private static Thread daemon(Runnable task) {
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		return thread;
	}
}
