package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_OFF_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.AUDIO_MUTING_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.INPUT_SELECTOR_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_LEVEL_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_LEVEL_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_LEFT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_PAUSE_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_PLAY_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_RETURN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_RIGHT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_SELECT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_STOP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TOP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TRDN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_TRUP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.NETWORK_USB_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_DOWN_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_ENTER_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_EXIT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_HOME_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_LEFT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_MENU_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_QUICK_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_RIGHT_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SETUP_UP_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_ON_ISCP;
import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.SYSTEM_POWER_STANDBY_ISCP;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.Browser;
import de.csmp.jeiscp.app.LogBuffer;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.ReceiverState.Aspect;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * Remote-control style view of a {@link ReceiverState}; all methods must run on the JavaFX thread.
 */
public class RemoteControlView {

	/** Slider ceiling; the full scale of 100 is rarely sensible. */
	static final int MAX_VOLUME = 70;

	record Source(String label, String code) {
	}

	static final List<Source> SOURCES = List.of(
			new Source("BLUETOOTH", "2E"),
			new Source("BD/DVD", "10"),
			new Source("TV/CD", "23"),
			new Source("NET", "2B"),
			new Source("PC", "05"),
			new Source("AUX", "03"));

	private static final double DPAD_SIZE = 168;
	private static final double NET_LINE_HEIGHT = 19;
	private static final int NET_LINES_SHOWN = 5;

	private static final String ICON_POWER = "M7.05 6.05a7 7 0 1 0 9.9 0M12 3v9";
	private static final String ICON_UP = "M6 15l6-6 6 6";
	private static final String ICON_DOWN = "M6 9l6 6 6-6";
	private static final String ICON_LEFT = "M15 6l-6 6 6 6";
	private static final String ICON_RIGHT = "M9 6l6 6-6 6";
	private static final String ICON_HOME = "M3.5 11.5L12 4l8.5 7.5M6 10v9.5h12V10";
	private static final String ICON_RETURN = "M9 14L4 9l5-5M4 9h10.5a5.5 5.5 0 0 1 0 11H11";
	private static final String ICON_SPEAKER = "M3 9.5v5h4l5 4.5v-14L7 9.5z";
	private static final String ICON_CROSS = "M16 9.5l5 5M21 9.5l-5 5";
	private static final String ICON_PLAY = "M8 5.5v13l11-6.5z";
	private static final String ICON_PAUSE = "M6.5 5.5h3.5v13H6.5zM14 5.5h3.5v13H14z";
	private static final String ICON_STOP = "M6.5 6.5h11v11h-11z";
	private static final String ICON_PREV = "M6 6h2.2v12H6zM9.5 12l9 6V6z";
	private static final String ICON_NEXT = "M15.8 6H18v12h-2.2zM5.5 18l9-6-9-6z";
	private static final String ICON_MINIMIZE = "M6 12h12";
	private static final String ICON_CLOSE = "M7 7l10 10M17 7L7 17";
	private static final String ICON_MORE = "M4.5 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0M10 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0M15.5 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0";

	/** Target of the direction pad, OK, return and home keys: NET/USB lists or the receiver's menus on the TV. */
	enum Mode { NET, TV }

	private static final PseudoClass ON = PseudoClass.getPseudoClass("on");
	private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
	private static final PseudoClass CURSOR = PseudoClass.getPseudoClass("cursor");
	private static final PseudoClass STANDBY = PseudoClass.getPseudoClass("standby");
	private static final PseudoClass LIT = PseudoClass.getPseudoClass("lit");

	private final ReceiverState state;
	private final Consumer<String> sender;
	private final EiscpDevice device;
	private final String receiverAddress;
	private final StringProperty windowTitle = new SimpleStringProperty("jEISCP");

	private final StackPane root;
	private final Circle led = new Circle(4);
	private final PauseTransition ledOff = new PauseTransition(Duration.millis(140));
	private final Map<String, Button> sourceButtons = new LinkedHashMap<>();
	private Button powerButton;
	private Button muteButton;
	private final Map<Mode, Button> modeButtons = new LinkedHashMap<>();
	private Mode mode = Mode.NET;
	private final Label okMode = label("dpad-ok-mode");
	private String followedInput;
	private Node keys;
	private FunctionsPage functionsPage;

	private VBox lcd;
	private final Label lcdSource = label("lcd-source");
	private final Label lcdVolume = label("lcd-volume");
	private final Label lcdPrimary = label("lcd-primary");
	private final Label lcdSecondary = label("lcd-secondary");
	private final Label lcdTime = label("lcd-time");
	private final Slider volumeSlider = new Slider(0, MAX_VOLUME, 0);
	private final ProgressBar volumeBar = new ProgressBar(0);
	private final ListView<String> netList = new ListView<>();
	private final Region cover = new Region();
	private final Label popupTop = label("lcd-popup-top");
	private final Label popupTitle = label("lcd-popup-title");
	private final Label popupMessage = label("lcd-popup-message");
	private final Label popupButtons = label("lcd-popup-buttons");
	private final VBox popup = new VBox(3, popupTop, popupTitle, popupMessage, popupButtons);
	private final PauseTransition popupTimeout = new PauseTransition(Duration.seconds(30));
	private final Tooltip popupText = new Tooltip();
	private byte[] shownCoverArt;
	private boolean hasCover;
	private boolean updatingVolume;
	private boolean dragging;
	private double dragOffsetX;
	private double dragOffsetY;

	/** @param device the connected model, {@code null} if unknown; decides which functions are offered */
	public RemoteControlView(ReceiverState state, Consumer<String> sender, EiscpDevice device) {
		this(state, sender, device, device != null ? device.getIpAddress() : null);
	}

	/** @param receiverAddress IP of the connected receiver, shown as a link to its web interface; {@code null} hides it */
	public RemoteControlView(ReceiverState state, Consumer<String> sender, EiscpDevice device, String receiverAddress) {
		this.state = state;
		this.sender = sender;
		this.device = device;
		this.receiverAddress = receiverAddress;

		VBox keyPage = new VBox(14, sourceKeys(), functionKeys(), controlCluster(), transportKeys(), numberKeys());
		keys = keyPage;
		functionsPage = new FunctionsPage(state, device, this::send, () -> showFunctions(false));
		functionsPage.getRoot().setVisible(false);
		StackPane pages = new StackPane(keyPage, functionsPage.getRoot());

		VBox remote = new VBox(header(), display(), pages);
		remote.getStyleClass().add("remote");
		VBox.setVgrow(lcd, Priority.ALWAYS);

		// borderless window: the remote's own shape and shadow float on the desktop, dragging moves it
		root = new StackPane(remote);
		root.getStyleClass().add("backdrop");
		remote.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startDrag);
		remote.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);

		ledOff.setOnFinished(e -> led.pseudoClassStateChanged(LIT, false));
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
		return windowTitle;
	}

	public void refresh(Aspect aspect) {
		switch (aspect) {
			case CONNECTION, POWER -> {
				refreshPower();
				refreshText();
			}
			case VOLUME, MUTE -> refreshVolume();
			case INPUT -> {
				refreshPopup();
				followInput();
				refreshText();
			}
			case NOW_PLAYING -> refreshText();
			case NET_LIST -> {
				refreshPopup();
				refreshNetList();
				refreshText();
			}
			case COVER_ART -> refreshScreen();
			case POPUP -> refreshPopup();
			case OTHER -> {
				// only the functions page shows these values
			}
		}
		functionsPage.refresh();
	}

	public void refreshAll() {
		followInput();
		refreshPopup();
		refreshPower();
		refreshVolume();
		refreshNetList();
		refreshText();
	}

	// ---- layout

	private Node header() {
		// the LED blinks with every command sent; clicking it opens the log of that traffic
		led.getStyleClass().add("led");
		Button ledKey = new Button();
		ledKey.setGraphic(led);
		ledKey.getStyleClass().setAll("button", "led-key");
		ledKey.setFocusTraversable(false);
		ledKey.setTooltip(new Tooltip("eISCP log (L)"));
		ledKey.setOnAction(e -> openLog());
		Label brand = new Label("jEISCP");
		brand.getStyleClass().add("brand");

		powerButton = iconKey(icon(22, line(ICON_POWER)), "Power", e -> send(
				Boolean.TRUE.equals(state.getPower()) ? SYSTEM_POWER_STANDBY_ISCP : SYSTEM_POWER_ON_ISCP));
		powerButton.getStyleClass().addAll("round", "power");

		Label model = new Label(device != null ? device.getDeviceType() : "");
		model.getStyleClass().add("model");

		Button minimize = iconKey(icon(14, line(ICON_MINIMIZE)), "Minimize", e -> ((Stage) window()).setIconified(true));
		minimize.getStyleClass().setAll("button", "window-key");
		Button close = iconKey(icon(14, line(ICON_CLOSE)), "Quit jEISCP", e -> closeWindow());
		close.getStyleClass().setAll("button", "window-key", "close");

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

		HBox header = new HBox(8, powerButton, ledKey, brand, model, address, spacer(), minimize, close);
		header.setAlignment(Pos.CENTER_LEFT);
		return header;
	}

	private Node display() {
		HBox top = new HBox(lcdSource, spacer(), lcdVolume);
		top.setAlignment(Pos.BASELINE_LEFT);

		volumeSlider.setFocusTraversable(false);
		volumeSlider.setBlockIncrement(1);
		volumeSlider.setMajorTickUnit(10);
		volumeSlider.setMinorTickCount(9);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.valueProperty().addListener((obs, old, value) -> {
			showVolume(value.intValue());
			if (!updatingVolume && !volumeSlider.isValueChanging()) {
				sendVolume();
			}
		});
		volumeSlider.valueChangingProperty().addListener((obs, was, changing) -> {
			if (!changing && !updatingVolume) {
				sendVolume();
			}
		});
		volumeBar.setMaxWidth(Double.MAX_VALUE);
		volumeBar.setMouseTransparent(true);
		StackPane volume = new StackPane(volumeBar, volumeSlider);
		volume.getStyleClass().add("lcd-volume-bar");

		lcdPrimary.setMaxWidth(Double.MAX_VALUE);
		lcdSecondary.setMaxWidth(Double.MAX_VALUE);
		lcdTime.setMinWidth(Region.USE_PREF_SIZE);
		HBox.setHgrow(lcdSecondary, Priority.ALWAYS);
		HBox bottom = new HBox(8, lcdSecondary, lcdTime);
		bottom.setAlignment(Pos.BASELINE_LEFT);

		netList.setCellFactory(list -> new NetListCell());
		netList.setFocusTraversable(false);
		netList.setFixedCellSize(NET_LINE_HEIGHT);
		netList.setPrefHeight(NET_LINE_HEIGHT * NET_LINES_SHOWN);
		netList.setPlaceholder(new Label(""));

		// NET popups (NPU), e.g. a service's connection error, cover the list until dismissed
		popup.getStyleClass().add("lcd-popup");
		popupMessage.setWrapText(true);
		popupMessage.setMinHeight(0);
		// fill the list area instead of sizing it, so a long message neither widens nor grows the display
		popup.setPrefSize(1, 0);
		popup.setMinSize(0, 0);
		popup.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		popup.setOnMouseClicked(e -> dismissPopup());
		popupText.setWrapText(true);
		popupText.setMaxWidth(360);
		Tooltip.install(popup, popupText);
		popupTimeout.setOnFinished(e -> dismissPopup());
		StackPane.setAlignment(popup, Pos.CENTER);
		StackPane screen = new StackPane(netList, cover, popup);
		screen.getStyleClass().add("lcd-screen");
		VBox.setVgrow(screen, Priority.ALWAYS);

		lcd = new VBox(top, volume, lcdPrimary, bottom, screen);
		lcd.getStyleClass().add("lcd");
		return lcd;
	}

	private Node sourceKeys() {
		GridPane grid = new GridPane();
		grid.setHgap(8);
		grid.setVgap(8);
		for (int i = 0; i < 3; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100.0 / 3);
			grid.getColumnConstraints().add(column);
		}
		for (int i = 0; i < SOURCES.size(); i++) {
			Source source = SOURCES.get(i);
			Button button = key(source.label(), e -> send(INPUT_SELECTOR_ISCP + source.code()));
			button.getStyleClass().add("source");
			button.setMaxWidth(Double.MAX_VALUE);
			grid.add(button, i % 3, i / 3);
			sourceButtons.put(source.code(), button);
		}
		return grid;
	}

	private Node controlCluster() {
		Button volumeUp = rockerKey("+", MASTER_VOLUME_LEVEL_UP_ISCP, "Volume up");
		Button volumeDown = rockerKey("−", MASTER_VOLUME_LEVEL_DOWN_ISCP, "Volume down");
		Label volumeLabel = new Label("VOL");
		volumeLabel.getStyleClass().add("rocker-label");
		VBox rocker = new VBox(volumeUp, vspacer(), volumeLabel, vspacer(), volumeDown);
		rocker.getStyleClass().add("rocker");
		rocker.setPrefHeight(DPAD_SIZE);
		rocker.setMaxHeight(DPAD_SIZE);

		Button top = iconKey(icon(20, line(ICON_HOME)), "Home (NET: top menu)", e -> send(byMode(NETWORK_USB_TOP_ISCP, SETUP_HOME_ISCP)));
		top.getStyleClass().add("round");
		Button back = iconKey(icon(20, line(ICON_RETURN)), "Return (TV: exit)", e -> send(byMode(NETWORK_USB_RETURN_ISCP, SETUP_EXIT_ISCP)));
		back.getStyleClass().add("round");
		muteButton = iconKey(icon(20, solid(ICON_SPEAKER), line(ICON_CROSS)), "Mute", e -> send(muteCommand()));
		muteButton.getStyleClass().addAll("round", "mute");
		VBox right = new VBox(18, top, back, muteButton);
		right.setAlignment(Pos.CENTER);

		HBox cluster = new HBox(rocker, spacer(), dpad(), spacer(), right);
		cluster.setAlignment(Pos.CENTER);
		return cluster;
	}

	private Node dpad() {
		Region ring = new Region();
		ring.getStyleClass().add("dpad-ring");

		Button ok = key(null, e -> send(byMode(NETWORK_USB_SELECT_ISCP, SETUP_ENTER_ISCP)));
		ok.getStyleClass().setAll("button", "dpad-ok");
		VBox okLabel = new VBox(-2, label("dpad-ok-label"), okMode);
		((Label) okLabel.getChildren().get(0)).setText("OK");
		okLabel.setAlignment(Pos.CENTER);
		ok.setGraphic(okLabel);

		// the switch sits in the pad's top corners, outside the ring, so it reads as part of the pad
		Button net = modeKey(Mode.NET, "Direction keys control the NET/USB lists");
		Button tv = modeKey(Mode.TV, "Direction keys control the receiver's menus on the TV");
		StackPane.setAlignment(net, Pos.TOP_LEFT);
		StackPane.setAlignment(tv, Pos.TOP_RIGHT);
		StackPane.setMargin(net, new Insets(-6, 0, 0, -10));
		StackPane.setMargin(tv, new Insets(-6, -10, 0, 0));

		StackPane pad = new StackPane(ring,
				dpadKey(ICON_UP, NETWORK_USB_UP_ISCP, SETUP_UP_ISCP, Pos.TOP_CENTER),
				dpadKey(ICON_DOWN, NETWORK_USB_DOWN_ISCP, SETUP_DOWN_ISCP, Pos.BOTTOM_CENTER),
				dpadKey(ICON_LEFT, NETWORK_USB_LEFT_ISCP, SETUP_LEFT_ISCP, Pos.CENTER_LEFT),
				dpadKey(ICON_RIGHT, NETWORK_USB_RIGHT_ISCP, SETUP_RIGHT_ISCP, Pos.CENTER_RIGHT),
				ok, net, tv);
		pad.setMinSize(DPAD_SIZE, DPAD_SIZE);
		pad.setPrefSize(DPAD_SIZE, DPAD_SIZE);
		pad.setMaxSize(DPAD_SIZE, DPAD_SIZE);
		return pad;
	}

	private Button dpadKey(String iconPath, String netCommand, String menuCommand, Pos position) {
		Button button = key(null, e -> send(byMode(netCommand, menuCommand)));
		button.getStyleClass().setAll("button", "dpad-key");
		button.setGraphic(icon(22, line(iconPath)));
		StackPane.setAlignment(button, position);
		return button;
	}

	/** SETUP and QUICK open the receiver's menus on the TV and switch the direction pad there; "more" opens all functions. */
	private Node functionKeys() {
		Button setup = key("SETUP", e -> {
			setMode(Mode.TV);
			send(SETUP_MENU_ISCP);
		});
		setup.setTooltip(new Tooltip("Setup menu on the TV"));
		Button quick = key("QUICK", e -> {
			setMode(Mode.TV);
			send(SETUP_QUICK_ISCP);
		});
		quick.setTooltip(new Tooltip("Quick setup on the TV"));
		Button more = iconKey(icon(18, solid(ICON_MORE)), "All functions of this model", e -> showFunctions(true));

		HBox row = new HBox(8, setup, quick, more);
		for (Node node : row.getChildren()) {
			Button button = (Button) node;
			button.getStyleClass().add("fn-key");
			button.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(button, Priority.ALWAYS);
		}
		return row;
	}

	/** Small key in a corner of the direction pad; the selected one is lit. */
	private Button modeKey(Mode target, String tooltip) {
		Button button = key(target.name(), e -> setMode(target));
		button.getStyleClass().setAll("button", "key", "mode");
		button.setTooltip(new Tooltip(tooltip));
		modeButtons.put(target, button);
		return button;
	}

	private Node transportKeys() {
		HBox row = new HBox(8,
				transportKey(ICON_PREV, NETWORK_USB_TRDN_ISCP, "Previous"),
				transportKey(ICON_PLAY, NETWORK_USB_PLAY_ISCP, "Play"),
				transportKey(ICON_PAUSE, NETWORK_USB_PAUSE_ISCP, "Pause"),
				transportKey(ICON_STOP, NETWORK_USB_STOP_ISCP, "Stop"),
				transportKey(ICON_NEXT, NETWORK_USB_TRUP_ISCP, "Next"));
		return row;
	}

	private Button transportKey(String iconPath, String command, String tooltip) {
		Button button = iconKey(icon(16, solid(iconPath)), tooltip, e -> send(command));
		button.getStyleClass().add("transport");
		button.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(button, Priority.ALWAYS);
		return button;
	}

	private Node numberKeys() {
		GridPane grid = new GridPane();
		grid.setHgap(8);
		grid.setVgap(8);
		for (int i = 0; i < 5; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(20);
			grid.getColumnConstraints().add(column);
		}
		for (int i = 0; i < 10; i++) {
			String digit = String.valueOf((i + 1) % 10);
			Button button = key(digit, e -> send(NETWORK_USB_ISCP + digit));
			button.getStyleClass().add("num");
			button.setMaxWidth(Double.MAX_VALUE);
			grid.add(button, i % 5, i / 5);
		}
		return grid;
	}

	// ---- state -> view

	private void refreshPower() {
		powerButton.pseudoClassStateChanged(ON, Boolean.TRUE.equals(state.getPower()));
		lcd.pseudoClassStateChanged(STANDBY, Boolean.FALSE.equals(state.getPower()) || !state.isConnected());
	}

	private void refreshVolume() {
		muteButton.pseudoClassStateChanged(ON, Boolean.TRUE.equals(state.getMuted()));

		Integer volume = state.getVolume();
		if (volume != null && !volumeSlider.isValueChanging()) {
			updatingVolume = true;
			try {
				volumeSlider.setValue(Math.min(volume, MAX_VOLUME));
			} finally {
				updatingVolume = false;
			}
		}
		showVolume(volumeSlider.isValueChanging() ? Integer.valueOf((int) volumeSlider.getValue()) : volume);
	}

	private void showVolume(Integer volume) {
		if (Boolean.TRUE.equals(state.getMuted())) {
			lcdVolume.setText("MUTE");
		} else {
			lcdVolume.setText(volume == null ? "VOL --" : "VOL " + volume);
		}
		volumeBar.setProgress(volume == null ? 0 : Math.min(1.0, volume / (double) MAX_VOLUME));
	}

	private void refreshText() {
		String input = state.getInput();
		String source = switch (state.getConnectionState()) {
			case CONNECTED -> Boolean.FALSE.equals(state.getPower()) ? "STANDBY" : sourceLabel(input);
			case CONNECTING -> "CONNECTING";
			default -> "OFFLINE";
		};
		lcdSource.setText(source);
		sourceButtons.forEach((code, button) -> button.pseudoClassStateChanged(SELECTED, code.equals(input)));

		String primary = "";
		String secondary = "";
		String time = "";
		if (state.isNetworkInput()) {
			primary = firstNonEmpty(state.getTitle(), state.getListTitle());
			secondary = Stream.of(state.getArtist(), state.getAlbum())
					.filter(s -> s != null && !s.isBlank())
					.collect(Collectors.joining(" · "));
			time = state.getTime() != null ? state.getTime() : "";
		}
		lcdPrimary.setText(primary);
		lcdSecondary.setText(secondary);
		lcdTime.setText(time);

		windowTitle.set(source + (primary.isEmpty() ? "" : " :: " + primary) + " - jEISCP");
	}

	private void refreshNetList() {
		List<String> lines = state.getNetLines();
		if (!netList.getItems().equals(lines)) {
			netList.getItems().setAll(lines);
		}
		netList.refresh();
		int cursor = state.getNetCursor();
		if (cursor >= 0 && cursor < lines.size()) {
			netList.scrollTo(Math.max(0, cursor - 2));
		}
		refreshScreen();
	}

	private void refreshScreen() {
		byte[] art = state.getCoverArt();
		if (art != shownCoverArt) {
			shownCoverArt = art;
			Image image = art == null ? null : new Image(new ByteArrayInputStream(art));
			hasCover = image != null && !image.isError();
			cover.setBackground(hasCover ? new Background(new BackgroundImage(image,
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
					new BackgroundSize(1, 1, true, true, true, false))) : null);
		}
		boolean showCover = hasCover && state.isCoverArtCurrent();
		cover.setVisible(showCover);
		netList.setVisible(!showCover);
	}

	// ---- modes and pages

	private String byMode(String netCommand, String menuCommand) {
		return mode == Mode.NET ? netCommand : menuCommand;
	}

	private void setMode(Mode target) {
		mode = target;
		modeButtons.forEach((m, button) -> button.pseudoClassStateChanged(SELECTED, m == mode));
		okMode.setText(target.name());
	}

	/** A new input picks the fitting mode; the mode keys override it until the input changes again. */
	private void followInput() {
		String input = state.getInput();
		if (input != null && !input.equals(followedInput)) {
			followedInput = input;
			setMode(state.isNetworkInput() ? Mode.NET : Mode.TV);
		} else {
			setMode(mode);
		}
	}

	private void showFunctions(boolean show) {
		functionsPage.getRoot().setVisible(show);
		keys.setVisible(!show);
		if (show) {
			functionsPage.focusSearch();
		} else {
			root.requestFocus();
		}
	}

	// ---- window

	private Window window() {
		return root.getScene().getWindow();
	}

	private void openLog() {
		LogWindow.show(window(), LogBuffer.install());
	}

	/** Goes through the stage's close request, so the controller can shut down. */
	private void closeWindow() {
		Window window = window();
		window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private void startDrag(MouseEvent event) {
		dragging = event.isPrimaryButtonDown() && !isInteractive(event.getPickResult().getIntersectedNode());
		if (dragging) {
			dragOffsetX = event.getScreenX() - window().getX();
			dragOffsetY = event.getScreenY() - window().getY();
		}
	}

	private void drag(MouseEvent event) {
		if (dragging) {
			window().setX(event.getScreenX() - dragOffsetX);
			window().setY(event.getScreenY() - dragOffsetY);
		}
	}

	static boolean isInteractive(Node node) {
		for (Node n = node; n != null; n = n.getParent()) {
			if (n instanceof ButtonBase || n instanceof Slider || n instanceof ListView || n instanceof ScrollBar
					|| n instanceof ScrollPane || n instanceof TextInputControl) {
				return true;
			}
		}
		return false;
	}

	// ---- view -> receiver

	private void send(String command) {
		led.pseudoClassStateChanged(LIT, true);
		ledOff.playFromStart();
		sender.accept(command);
		// OK and return also close the popup on the receiver
		if (popup.isVisible() && (command.equals(NETWORK_USB_SELECT_ISCP) || command.equals(NETWORK_USB_RETURN_ISCP))) {
			dismissPopup();
		}
	}

	private void refreshPopup() {
		ReceiverState.Popup current = state.getPopup();
		boolean show = current != null;
		if (show) {
			popupTop.setText(current.topTitle());
			popupTitle.setText(current.title());
			popupMessage.setText(current.message());
			popupButtons.setText(current.buttons().stream().map(b -> "[ " + b + " ]").collect(Collectors.joining("  ")));
			popupText.setText(Stream.of(current.title(), current.message()).filter(t -> !t.isEmpty())
					.collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() + "(click to close)");
			for (Label label : List.of(popupTop, popupTitle, popupMessage, popupButtons)) {
				label.setManaged(!label.getText().isEmpty());
				label.setVisible(!label.getText().isEmpty());
			}
			if (!popup.isVisible()) {
				popupTimeout.playFromStart();
			}
		} else {
			popupTimeout.stop();
		}
		popup.setVisible(show);
	}

	private void dismissPopup() {
		state.dismissPopup();
		refreshPopup();
	}

	private void sendVolume() {
		int volume = (int) Math.round(volumeSlider.getValue());
		Integer current = state.getVolume();
		if (current == null || current != volume) {
			send(MASTER_VOLUME_ISCP + String.format("%02X", volume));
		}
	}

	private String muteCommand() {
		return Boolean.TRUE.equals(state.getMuted()) ? AUDIO_MUTING_OFF_ISCP : AUDIO_MUTING_ON_ISCP;
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
		if (code == KeyCode.L) {
			openLog();
			event.consume();
			return;
		}
		String command = switch (code) {
			case UP -> byMode(NETWORK_USB_UP_ISCP, SETUP_UP_ISCP);
			case DOWN -> byMode(NETWORK_USB_DOWN_ISCP, SETUP_DOWN_ISCP);
			case LEFT -> byMode(NETWORK_USB_LEFT_ISCP, SETUP_LEFT_ISCP);
			case RIGHT -> byMode(NETWORK_USB_RIGHT_ISCP, SETUP_RIGHT_ISCP);
			case ENTER -> byMode(NETWORK_USB_SELECT_ISCP, SETUP_ENTER_ISCP);
			case BACK_SPACE, ESCAPE -> byMode(NETWORK_USB_RETURN_ISCP, SETUP_EXIT_ISCP);
			case PLUS, ADD, PAGE_UP -> MASTER_VOLUME_LEVEL_UP_ISCP;
			case MINUS, SUBTRACT, PAGE_DOWN -> MASTER_VOLUME_LEVEL_DOWN_ISCP;
			case M -> muteCommand();
			default -> code.isDigitKey() ? NETWORK_USB_ISCP + code.getName().charAt(code.getName().length() - 1) : null;
		};
		if (command != null) {
			send(command);
			event.consume();
		}
	}

	// ---- helpers

	private final class NetListCell extends ListCell<String> {
		NetListCell() {
			// the cursor is driven by the receiver, not by clicks
			addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
		}

		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? null : item);
			pseudoClassStateChanged(CURSOR, !empty && getIndex() == state.getNetCursor());
		}
	}

	private static String sourceLabel(String input) {
		if (input == null) {
			return "";
		}
		return SOURCES.stream().filter(s -> s.code().equals(input)).map(Source::label).findFirst()
				.orElse("SLI " + input);
	}

	private static String firstNonEmpty(String... values) {
		for (String value : values) {
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		return "";
	}

	private Button key(String text, EventHandler<ActionEvent> action) {
		Button button = new Button(text);
		button.getStyleClass().add("key");
		button.setFocusTraversable(false);
		button.setOnAction(action);
		return button;
	}

	private Button iconKey(Node icon, String tooltip, EventHandler<ActionEvent> action) {
		Button button = key(null, action);
		button.setGraphic(icon);
		button.setTooltip(new Tooltip(tooltip));
		return button;
	}

	/** Sends once on press and repeats while the key is held. */
	private Button rockerKey(String text, String command, String tooltip) {
		Button button = new Button(text);
		button.getStyleClass().setAll("button", "rocker-key");
		button.setFocusTraversable(false);
		button.setTooltip(new Tooltip(tooltip));

		Timeline repeat = new Timeline(new KeyFrame(Duration.millis(180), e -> send(command)));
		repeat.setCycleCount(Animation.INDEFINITE);
		PauseTransition delay = new PauseTransition(Duration.millis(450));
		delay.setOnFinished(e -> repeat.play());

		button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.isPrimaryButtonDown()) {
				send(command);
				delay.playFromStart();
			}
		});
		EventHandler<MouseEvent> stop = e -> {
			delay.stop();
			repeat.stop();
		};
		button.addEventHandler(MouseEvent.MOUSE_RELEASED, stop);
		button.addEventHandler(MouseEvent.MOUSE_EXITED, stop);
		return button;
	}

	private static Label label(String styleClass) {
		Label label = new Label();
		label.getStyleClass().add(styleClass);
		return label;
	}

	private static Region spacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	private static Region vspacer() {
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	static SVGPath line(String path) {
		SVGPath svg = new SVGPath();
		svg.setContent(path);
		svg.getStyleClass().addAll("icon", "line");
		return svg;
	}

	static SVGPath solid(String path) {
		SVGPath svg = new SVGPath();
		svg.setContent(path);
		svg.getStyleClass().addAll("icon", "solid");
		return svg;
	}

	/** Paths are drawn on a 24x24 grid and scaled to {@code size}. */
	static Node icon(double size, SVGPath... parts) {
		Group shapes = new Group(parts);
		shapes.setScaleX(size / 24);
		shapes.setScaleY(size / 24);
		return new Group(shapes);
	}
}
