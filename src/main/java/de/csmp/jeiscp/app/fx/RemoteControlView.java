package de.csmp.jeiscp.app.fx;

import java.util.List;
import java.util.stream.IntStream;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.LogBuffer;
import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverControl.Direction;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.ReceiverState.Aspect;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Remote-control style view of a {@link ReceiverState}: header, display, keys and the functions page,
 * in a borderless window. All methods must run on the JavaFX thread.
 */
public class RemoteControlView {

	private final ReceiverControl control;
	private final StackPane root;
	private final HeaderBar header;
	private final DisplayPanel display;
	private final KeyPad keys;
	private final FunctionsPage functionsPage;

	/** @param device the connected model, {@code null} if unknown; decides which functions are offered */
	public RemoteControlView(ReceiverState state, ReceiverControl control, EiscpDevice device) {
		this(state, control, device, device != null ? device.getIpAddress() : null);
	}

	/** @param receiverAddress IP of the connected receiver, shown as a link to its web interface; {@code null} hides it */
	public RemoteControlView(ReceiverState state, ReceiverControl control, EiscpDevice device, String receiverAddress) {
		this.control = control;
		header = new HeaderBar(state, control, device, receiverAddress, this::openLog);
		display = new DisplayPanel(state, control);
		keys = new KeyPad(state, control, () -> showFunctions(true), display::dismissPopupIfShown);
		functionsPage = new FunctionsPage(state, device, control, () -> showFunctions(false));
		functionsPage.getRoot().setVisible(false);
		StackPane pages = new StackPane(keys.getNode(), functionsPage.getRoot());

		VBox remote = new VBox(header.getNode(), display.getNode(), pages);
		remote.getStyleClass().add("remote");
		VBox.setVgrow(display.getNode(), Priority.ALWAYS);

		// borderless window: the remote's own shape and shadow float on the desktop, dragging moves it
		root = new StackPane(remote);
		root.getStyleClass().add("backdrop");
		WindowDrag.install(remote);

		control.addSentListener(command -> {
			if (Platform.isFxApplicationThread()) {
				header.flashLed();
			} else {
				Platform.runLater(header::flashLed);
			}
		});
		refreshAll();
	}

	public Scene createScene() {
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add(RemoteControlView.class.getResource("remote.css").toExternalForm());
		scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
		return scene;
	}

	/** App icon in all rendered sizes; the window system picks the best fit. */
	public static List<Image> icons() {
		return IntStream.of(16, 24, 32, 48, 64, 128, 256)
				.mapToObj(size -> new Image(RemoteControlView.class.getResource("icons/icon-" + size + ".png").toExternalForm()))
				.toList();
	}

	public Parent getRoot() {
		return root;
	}

	public ReadOnlyStringProperty windowTitleProperty() {
		return display.windowTitleProperty();
	}

	public void refresh(Aspect aspect) {
		switch (aspect) {
			case CONNECTION, POWER -> {
				header.refresh();
				display.refreshStandby();
				display.refreshText();
			}
			case VOLUME, MUTE -> {
				display.refreshVolume();
				keys.refreshMute();
			}
			case INPUT -> {
				display.refreshPopup();
				keys.followInput();
				keys.refreshSources();
				display.refreshText();
			}
			case NOW_PLAYING -> display.refreshText();
			case NET_LIST -> {
				display.refreshPopup();
				display.refreshNetList();
				display.refreshText();
			}
			case COVER_ART -> display.refreshScreen();
			case POPUP -> display.refreshPopup();
			case OTHER -> {
				// only the functions page shows these values
			}
		}
		functionsPage.refresh();
	}

	public void refreshAll() {
		keys.followInput();
		display.refreshPopup();
		header.refresh();
		display.refreshStandby();
		display.refreshVolume();
		keys.refreshMute();
		display.refreshNetList();
		keys.refreshSources();
		display.refreshText();
	}

	private void showFunctions(boolean show) {
		functionsPage.getRoot().setVisible(show);
		keys.getNode().setVisible(!show);
		if (show) {
			functionsPage.focusSearch();
		} else {
			root.requestFocus();
		}
	}

	private void openLog() {
		LogWindow.show(root.getScene().getWindow(), LogBuffer.install());
	}

	private void onKeyPressed(KeyEvent event) {
		if (event.getTarget() instanceof TextInputControl) {
			if (event.getCode() == KeyCode.ESCAPE) {
				showFunctions(false);
				event.consume();
			}
			return;	// typing in the search field
		}
		KeyCode code = event.getCode();
		Runnable action = switch (code) {
			case L -> this::openLog;
			case UP -> () -> keys.navigate(Direction.UP);
			case DOWN -> () -> keys.navigate(Direction.DOWN);
			case LEFT -> () -> keys.navigate(Direction.LEFT);
			case RIGHT -> () -> keys.navigate(Direction.RIGHT);
			case ENTER -> keys::pressOk;
			case BACK_SPACE, ESCAPE -> keys::pressBack;
			case PLUS, ADD, PAGE_UP -> control::volumeUp;
			case MINUS, SUBTRACT, PAGE_DOWN -> control::volumeDown;
			case M -> control::toggleMute;
			default -> code.isDigitKey() ? digitKey(code) : null;
		};
		if (action != null) {
			action.run();
			event.consume();
		}
	}

	/** DIGIT1 is named "1", NUMPAD1 "Numpad 1". */
	private Runnable digitKey(KeyCode code) {
		int digit = Character.digit(code.getName().charAt(code.getName().length() - 1), 10);
		return () -> control.numberKey(digit);
	}
}
