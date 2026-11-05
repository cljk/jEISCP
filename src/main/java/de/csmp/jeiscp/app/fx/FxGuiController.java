package de.csmp.jeiscp.app.fx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpConnector.ConnectionState;
import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.ReceiverState.Aspect;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Shows the JavaFX remote control for an open connection; closing the window exits the application.
 */
public class FxGuiController {
	private static final Logger log = LoggerFactory.getLogger(FxGuiController.class);

	private final EiscpConnector conn;
	private final EiscpDevice device;
	private final ReceiverState state = new ReceiverState();
	private final ReceiverControl control = new ReceiverControl(this::send, state);
	private RemoteControlView view;

	/** @param device the connected model, {@code null} if unknown */
	public FxGuiController(EiscpConnector conn, EiscpDevice device) {
		this.conn = conn;
		this.device = device;
	}

	/** Starts the JavaFX runtime and returns without waiting for the window. */
	public void start() {
		state.addListener(aspect -> {
			if (aspect == Aspect.INPUT) {
				control.queryNowPlayingIfMissing();
			}
		});
		state.setConnectionState(conn.getState());
		conn.addStateListener(this::connectionChanged);
		Platform.startup(this::showWindow);
		conn.addListener(state);
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
		view = new RemoteControlView(state, control, device, conn.getAddress());

		// borderless; the view brings its own close and minimize keys
		Stage stage = new Stage(StageStyle.TRANSPARENT);
		stage.getIcons().setAll(RemoteControlView.icons());
		stage.setScene(view.createScene());
		stage.titleProperty().bind(view.windowTitleProperty());
		stage.setOnCloseRequest(e -> {
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
