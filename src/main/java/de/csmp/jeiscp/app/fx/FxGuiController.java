package de.csmp.jeiscp.app.fx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpConnector.ConnectionState;
import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverProfile;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.ReceiverState.Aspect;
import de.csmp.jeiscp.app.Settings;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Shows the JavaFX remote control for an open connection; closing the window exits the application.
 */
public class FxGuiController {
	private static final Logger log = LoggerFactory.getLogger(FxGuiController.class);

	private final EiscpConnector conn;
	private final EiscpDevice device;
	private final Settings settings;
	private final ReceiverState state = new ReceiverState();
	private final ReceiverControl control;
	private RemoteControlView view;

	public FxGuiController(EiscpConnector conn, EiscpDevice device) {
		this(conn, device, null);
	}

	/**
	 * Listens to the connector at once; create it before {@code connect()} to get what the receiver pushes first.
	 * @param settings keeps the window position; {@code null} for none
	 */
	public FxGuiController(EiscpConnector conn, EiscpDevice device, Settings settings) {
		this.conn = conn;
		this.device = device;
		this.settings = settings;
		control = new ReceiverControl(this::send, state, ReceiverProfile.of(device != null ? device.getDeviceType() : null));
		conn.addListener(state);
	}

	/** Starts the JavaFX runtime and returns without waiting for the window. */
	public void start() {
		state.addListener(aspect -> {
			if (aspect == Aspect.INPUT) {
				control.queryNowPlayingIfMissing();
			} else if (aspect == Aspect.NOW_PLAYING) {
				control.requestCoverIfMissing();	// the time shows that something plays
			}
		});
		state.setConnectionState(conn.getState());
		conn.addStateListener(this::connectionChanged);
		Ui.onFxThread(this::showWindow);	// JavaFX may already run for the StartupWindow
		control.queryStatus();
	}

	private void connectionChanged(ConnectionState next) {
		state.setConnectionState(next);
		if (next == ConnectionState.CONNECTED) {
			control.queryStatus();	// after a reconnect the receiver may have changed meanwhile
		}
	}

	private void showWindow() {
		// queued via runLater, so the first refresh runs after the view below exists
		state.addListener(aspect -> Platform.runLater(() -> view.refresh(aspect)));
		view = new RemoteControlView(state, control, device, conn.getAddress(), settings);

		// borderless; the view brings its own close and minimize keys
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.getIcons().setAll(RemoteControlView.icons());
		stage.setScene(view.createScene());
		stage.titleProperty().bind(view.windowTitleProperty());
		Settings.Position position = settings != null ? settings.windowPosition() : null;
		if (position != null && !Screen.getScreensForRectangle(position.x(), position.y(), 40, 40).isEmpty()) {
			stage.setX(position.x());	// only while that spot is still on a screen
			stage.setY(position.y());
		}
		stage.setOnCloseRequest(e -> {
			if (settings != null) {
				settings.rememberWindowPosition(stage.getX(), stage.getY());
			}
			conn.close();
			Platform.exit();
			System.exit(0);
		});
		stage.show();
	}

	private void send(String command) {
		try {
			conn.sendIscpCommand(command);
		} catch (Exception ex) {
			log.warn("not sent: {} ({})", command, ex.getMessage());
		}
	}
}
