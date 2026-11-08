package de.csmp.jeiscp.app;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpAutodiscovery;
import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpConstants;
import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.fx.FxGuiController;
import de.csmp.jeiscp.app.fx.StartupWindow;


public class ThreadedConsoleApp {
	private static final Logger log = LoggerFactory.getLogger(ThreadedConsoleApp.class);
	
	
	
	
	public static void main(String[] args) throws Exception {
		// init connection
		
		
		String address = null;
		EiscpConnector conn = null;

		boolean ENABLE_GUI = true;
		boolean ENABLE_CONSOLE = true;

		
		for (int i=0; i<args.length; i++) {
			if ("-a".equals(args[i])) {
				i++;
				address = args[i];
			} else if ("-nogui".equals(args[i])){
				ENABLE_GUI = false;
			} else if ("-noconsole".equals(args[i])){
				ENABLE_CONSOLE = false;
			} else if ("-?".equals(args[i])){
				displayParameterHelp();
				
				System.exit(0);
			} else {
				System.err.println("unrecognized parameter " + args[i] + "\n");
				
				displayParameterHelp();
				
				System.exit(1);
			}
		}
		
		
		File logFile = LogFile.install();
		log.info("log file {}", logFile.getAbsolutePath());
		Settings settings = Settings.user();

		StartupWindow startup = null;	// with the GUI, search and connection show there instead of only on the console
		if (ENABLE_GUI) {
			LogBuffer.install();	// from the start, so the GUI's log window also shows the autodiscovery
			startup = StartupWindow.open();
		}

		// the last receiver first, so a start needs no search while it is still there
		String remembered = address == null ? settings.lastReceiver() : null;
		FxGuiController gui = null;
		while (conn == null) {
			String target = address != null ? address : remembered;
			boolean fromMemory = address == null && remembered != null;
			EiscpDevice device = null;	// model, for the functions the GUI offers
			EiscpConnector candidate;
			if (target == null) {
				if (startup != null) {
					startup.searching(EiscpAutodiscovery.DEFAULT_TIMEOUT);
				}
				try {
					device = new EiscpAutodiscovery().discover();
				} catch (SocketTimeoutException ex) {
					address = giveUp(startup, ex.getMessage() + ". Is the receiver on (or network standby enabled)"
							+ " and in this network?", "Otherwise enter its address.", "Otherwise connect directly with -a {ip-address}.");
					continue;
				}
				candidate = new EiscpConnector(device);
				target = device.getIpAddress() + (device.getPort() == EiscpConstants.DEFAULT_EISCP_PORT ? "" : ":" + device.getPort());
			} else {
				if (startup != null) {
					startup.connecting(target);
				}
				try {
					candidate = new EiscpConnector(host(target), port(target));
				} catch (NumberFormatException ex) {
					remembered = null;
					if (!fromMemory) {
						address = giveUp(startup, "Not an address: " + target, "", "");
					}
					continue;
				}
				if (ENABLE_GUI) {
					device = EiscpAutodiscovery.identify(host(target), port(target));
				}
			}
			if (startup != null) {
				startup.connecting(target + (device != null ? " · " + device.getDeviceType() : ""));
			}
			// listens before connecting: right after the connect the receiver pushes the NET list title with the service
			gui = ENABLE_GUI ? new FxGuiController(candidate, device, settings) : null;
			try {
				candidate.connect();	// later losses are reconnected in the background
				conn = candidate;
				settings.rememberReceiver(target);
			} catch (IOException | IllegalArgumentException ex) {
				candidate.close();
				if (fromMemory) {
					log.info("last receiver {} not reachable ({}), searching", target, ex.getMessage());
					remembered = null;
				} else {
					address = giveUp(startup, "Cannot connect to " + target + ": " + ex.getMessage(), "", "");
				}
			}
		}

		if (ENABLE_CONSOLE) {
			ConsoleController consoleController = new ConsoleController(conn, !ENABLE_GUI);
			Thread consoleThread = new Thread(consoleController);
			consoleThread.start();
		}

		if (gui != null) {
			gui.start();
			startup.close();	// after the remote is shown: both run on the FX thread in this order
		}
		
		log.debug("controller threads started");
	}




	/**
	 * Without the GUI prints the problem and exits; with it shows the problem and waits.
	 * @return the address the user entered, {@code null} to search again
	 */
	private static String giveUp(StartupWindow startup, String problem, String guiHint, String consoleHint) {
		log.warn(problem);
		if (startup == null) {
			System.err.println((problem + " " + consoleHint).strip());
			System.exit(1);
		}
		return startup.failed((problem + " " + guiHint).strip());
	}

	/** {@code -a} takes {@code host} or {@code host:port}, e.g. for the ReceiverEmulator in the test sources. */
	static String host(String address) {
		return hasPort(address) ? address.substring(0, address.lastIndexOf(':')) : address;
	}

	static int port(String address) {
		return hasPort(address) ? Integer.parseInt(address.substring(address.lastIndexOf(':') + 1)) : EiscpConstants.DEFAULT_EISCP_PORT;
	}

	/** One colon only: an IPv6 address has several. */
	private static boolean hasPort(String address) {
		int colon = address.lastIndexOf(':');
		return colon > 0 && address.indexOf(':') == colon;
	}

	private static void displayParameterHelp() {
		System.out.println("available parameters:");
		System.out.println("\t-?             \t\tdisplay parameter help (this page)");
		System.out.println("\t-a {ip-address}[:port]\tconnect to this receiver, no autodiscovery");
		System.out.println("\t-nogui         \t\tdisable gui");
		System.out.println("\t-noconsole     \t\tdisable console (manual ISCP in-/output)");
	}    
}
