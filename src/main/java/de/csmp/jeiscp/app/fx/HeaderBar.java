package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.app.fx.Ui.icon;
import static de.csmp.jeiscp.app.fx.Ui.iconKey;
import static de.csmp.jeiscp.app.fx.Ui.line;
import static de.csmp.jeiscp.app.fx.Ui.spacer;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.Browser;
import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverState;
import javafx.animation.PauseTransition;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/** Top row of the remote: power key, traffic LED (opens the log), model and address link, minimize and close keys. */
final class HeaderBar {

	private static final String ICON_POWER = "M7.05 6.05a7 7 0 1 0 9.9 0M12 3v9";
	private static final String ICON_MINIMIZE = "M6 12h12";
	private static final String ICON_CLOSE = "M7 7l10 10M17 7L7 17";

	private static final PseudoClass ON = PseudoClass.getPseudoClass("on");
	private static final PseudoClass LIT = PseudoClass.getPseudoClass("lit");

	private final ReceiverState state;
	private final HBox node;
	private final Button powerButton;
	private final Circle led = new Circle(4);
	private final PauseTransition ledOff = new PauseTransition(Duration.millis(140));

	/** @param receiverAddress shown as a link to the receiver's web interface; {@code null} hides it */
	HeaderBar(ReceiverState state, ReceiverControl control, EiscpDevice device, String receiverAddress, Runnable openLog) {
		this.state = state;

		powerButton = iconKey(icon(22, line(ICON_POWER)), "Power", e -> control.togglePower());
		powerButton.getStyleClass().addAll("round", "power");

		// the LED blinks with every command sent; clicking it opens the log of that traffic
		led.getStyleClass().add("led");
		ledOff.setOnFinished(e -> led.pseudoClassStateChanged(LIT, false));
		Button ledKey = new Button();
		ledKey.setGraphic(led);
		ledKey.getStyleClass().setAll("button", "led-key");
		ledKey.setFocusTraversable(false);
		ledKey.setTooltip(new Tooltip("eISCP log (L)"));
		ledKey.setOnAction(e -> openLog.run());

		Label brand = new Label("jEISCP");
		brand.getStyleClass().add("brand");
		Label model = new Label(device != null ? device.getDeviceType() : "");
		model.getStyleClass().add("model");

		Hyperlink address = new Hyperlink(receiverAddress);
		address.getStyleClass().add("address");
		address.setFocusTraversable(false);
		address.setVisible(receiverAddress != null);
		address.setManaged(receiverAddress != null);
		if (receiverAddress != null) {
			String url = Browser.webInterfaceUrl(receiverAddress);
			address.setTooltip(new Tooltip("Open the receiver's web interface: " + url));
			address.setOnAction(e -> {
				Browser.open(url);
				address.setVisited(false);
			});
		}

		Button minimize = iconKey(icon(14, line(ICON_MINIMIZE)), "Minimize", e -> ((Stage) window()).setIconified(true));
		minimize.getStyleClass().setAll("button", "window-key");
		Button close = iconKey(icon(14, line(ICON_CLOSE)), "Quit jEISCP", e -> closeWindow());
		close.getStyleClass().setAll("button", "window-key", "close");

		// name above model and address, so the narrow remote has room for all of them
		HBox modelAndAddress = new HBox(6, model, address);
		modelAndAddress.setAlignment(Pos.CENTER_LEFT);
		VBox identity = new VBox(-1, brand, modelAndAddress);
		identity.setAlignment(Pos.CENTER_LEFT);
		node = new HBox(8, powerButton, ledKey, identity, spacer(), minimize, close);
		node.setAlignment(Pos.CENTER_LEFT);
	}

	Node getNode() {
		return node;
	}

	void refresh() {
		powerButton.pseudoClassStateChanged(ON, Boolean.TRUE.equals(state.getPower()));
	}

	void flashLed() {
		led.pseudoClassStateChanged(LIT, true);
		ledOff.playFromStart();
	}

	private Window window() {
		return node.getScene().getWindow();
	}

	/** Goes through the stage's close request, so the controller can shut down. */
	private void closeWindow() {
		Window window = window();
		window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
