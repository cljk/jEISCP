package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MONITOR_OUT_RESOLUTION_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_ARTIST_NAME_INFO_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NET_USB_TITLE_NAME_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_QUERY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.VIDEO_INFORMATION_QUERY_ISCP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpConnector.ConnectionState;
import de.csmp.jeiscp.EiscpDevice;
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
	private RemoteControlView view;

	/** @param device the connected model, {@code null} if unknown */
	public FxGuiController(EiscpConnector conn, EiscpDevice device) {
		this.conn = conn;
		this.device = device;
	}

	/** Starts the JavaFX runtime and returns without waiting for the window. */
	public void start() {
		state.addListener(this::queryNowPlayingOnNetworkInput);
		state.setConnectionState(conn.getState());
		conn.addStateListener(this::connectionChanged);
		Platform.startup(this::showWindow);
		conn.addListener(state);
		queryState();
	}

	private void connectionChanged(ConnectionState next) {
		state.setConnectionState(next);
		if (next == ConnectionState.CONNECTED) {
			queryState();	// after a reconnect the receiver may have changed meanwhile
		}
	}

	private void queryState() {
		send(SYSTEM_POWER_QUERY_ISCP);
		send(MASTER_VOLUME_QUERY_ISCP);
		send(AUDIO_MUTING_QUERY_ISCP);
		send(VIDEO_INFORMATION_QUERY_ISCP);
		send(MONITOR_OUT_RESOLUTION_QUERY_ISCP);
		send(INPUT_SELECTOR_QUERY_ISCP);
	}

	private void showWindow() {
		// queued via runLater, so the first refresh runs after the view below exists
		state.addListener(aspect -> Platform.runLater(() -> view.refresh(aspect)));
		view = new RemoteControlView(state, this::send, device, conn.getAddress());

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

	private void queryNowPlayingOnNetworkInput(Aspect aspect) {
		if (aspect == Aspect.INPUT && state.isNetworkInput()) {
			if (state.getArtist() == null) {
				send(NET_USB_ARTIST_NAME_INFO_QUERY_ISCP);
			}
			if (state.getTitle() == null) {
				send(NET_USB_TITLE_NAME_QUERY_ISCP);
			}
		}
	}

	private void send(String command) {
		try {
			conn.sendIscpCommand(command);
		} catch (Exception ex) {
			log.warn("not sent: {} ({})", command, ex.getMessage());
		}
	}
}
