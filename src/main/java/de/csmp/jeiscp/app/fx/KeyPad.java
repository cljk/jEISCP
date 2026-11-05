package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.app.fx.Ui.icon;
import static de.csmp.jeiscp.app.fx.Ui.iconKey;
import static de.csmp.jeiscp.app.fx.Ui.key;
import static de.csmp.jeiscp.app.fx.Ui.label;
import static de.csmp.jeiscp.app.fx.Ui.line;
import static de.csmp.jeiscp.app.fx.Ui.rockerKey;
import static de.csmp.jeiscp.app.fx.Ui.solid;
import static de.csmp.jeiscp.app.fx.Ui.spacer;
import static de.csmp.jeiscp.app.fx.Ui.vspacer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverControl.Direction;
import de.csmp.jeiscp.app.ReceiverControl.Playback;
import de.csmp.jeiscp.app.NetPlayback.PlayStatus;
import de.csmp.jeiscp.app.NetPlayback.Repeat;
import de.csmp.jeiscp.app.NetPlayback.Shuffle;
import de.csmp.jeiscp.app.ReceiverControl.Target;
import de.csmp.jeiscp.app.ReceiverState;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

/**
 * Keys below the display: inputs, SETUP/QUICK/more, volume rocker, direction pad with its NET/TV switch,
 * home/return/mute, playback and number keys.
 */
final class KeyPad {

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
	private static final String ICON_REPEAT = "M17 2l4 4-4 4M3 11v-1a4 4 0 0 1 4-4h14M7 22l-4-4 4-4M21 13v1a4 4 0 0 1-4 4H3";
	private static final String ICON_REPEAT_ONE = "M11 10h1v4";
	private static final String ICON_SHUFFLE = "M2 18h1.4c1.3 0 2.5-.6 3.3-1.7l6.1-8.6c.7-1.1 2-1.7 3.3-1.7H22M18 2l4 4-4 4"
			+ "M2 6h1.9c1.5 0 2.9.9 3.6 2.2M22 18h-5.9c-1.3 0-2.6-.7-3.3-1.8l-.5-.8M18 14l4 4-4 4";
	private static final String ICON_PREV = "M6 6h2.2v12H6zM9.5 12l9 6V6z";
	private static final String ICON_NEXT = "M15.8 6H18v12h-2.2zM5.5 18l9-6-9-6z";
	private static final String ICON_MORE = "M4.5 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0M10 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0M15.5 12a2 2 0 1 0 4 0a2 2 0 1 0-4 0";

	private static final PseudoClass ON = PseudoClass.getPseudoClass("on");
	private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

	private final ReceiverState state;
	private final ReceiverControl control;
	private final Runnable showFunctions;
	private final Runnable dismissPopup;
	private final VBox node;

	private final Map<String, Button> sourceButtons = new LinkedHashMap<>();
	private final Map<Target, Button> modeButtons = new LinkedHashMap<>();
	private final ToggleButton numbersKey = new ToggleButton("123");
	private final Node numbers;
	private final Map<PlayStatus, Button> playStatusKeys = new EnumMap<>(PlayStatus.class);
	private final SVGPath repeatOne = line(ICON_REPEAT_ONE);
	private Button repeatKey;
	private Button shuffleKey;
	private final Label okMode = label("dpad-ok-mode");
	private Button muteButton;
	private Target mode = Target.NET;
	private String followedInput;

	/** @param dismissPopup closes a shown NET popup; OK and return close it on the receiver too */
	/**
	 * @param numbersShown whether the number keys start unfolded
	 * @param numbersToggled told when the "123" key folds or unfolds them
	 */
	KeyPad(ReceiverState state, ReceiverControl control, Runnable showFunctions, Runnable dismissPopup,
			boolean numbersShown, Consumer<Boolean> numbersToggled) {
		this.state = state;
		this.control = control;
		this.showFunctions = showFunctions;
		this.dismissPopup = dismissPopup;
		numbers = numberKeys();
		numbersKey.setSelected(numbersShown);
		showNumbers(numbersShown);
		numbersKey.selectedProperty().addListener((obs, was, shown) -> {
			showNumbers(shown);
			numbersToggled.accept(shown);
		});
		node = new VBox(14, sourceKeys(), functionKeys(), controlCluster(), playbackKeys());
	}

	private void showNumbers(boolean shown) {
		numbers.setVisible(shown);
		numbers.setManaged(shown);
	}

	Node getNode() {
		return node;
	}

	/** Display name of an SLI code, e.g. {@code NET} for {@code 2B}. */
	static String sourceLabel(String input) {
		if (input == null) {
			return "";
		}
		return SOURCES.stream().filter(s -> s.code().equals(input)).map(Source::label).findFirst()
				.orElse("SLI " + input);
	}

	// ---- operations, also used by the keyboard shortcuts

	void navigate(Direction direction) {
		control.navigate(mode, direction);
	}

	void pressOk() {
		control.ok(mode);
		dismissPopup.run();
	}

	void pressBack() {
		control.back(mode);
		dismissPopup.run();
	}

	// ---- state -> keys

	void refreshSources() {
		String input = state.getInput();
		sourceButtons.forEach((code, button) -> button.pseudoClassStateChanged(SELECTED, code.equals(input)));
	}

	void refreshMute() {
		muteButton.pseudoClassStateChanged(ON, Boolean.TRUE.equals(state.getMuted()));
	}

	/** Play, pause or stop is lit while a NET input reports it; repeat and shuffle are lit while on and usable only on NET. */
	void refreshPlayStatus() {
		boolean net = state.isNetworkInput() && state.isConnected() && !Boolean.FALSE.equals(state.getPower());
		PlayStatus status = net ? state.playback().getPlayStatus() : null;
		playStatusKeys.forEach((shown, key) -> key.pseudoClassStateChanged(ON, shown == status));

		Repeat repeat = net ? state.playback().getRepeat() : null;
		refreshModeKey(repeatKey, "Repeat", repeat, net && repeat != Repeat.UNAVAILABLE, repeat != null && repeat.isOn());
		repeatOne.setVisible(repeat == Repeat.ONE);
		Shuffle shuffle = net ? state.playback().getShuffle() : null;
		refreshModeKey(shuffleKey, "Shuffle", shuffle, net && shuffle != Shuffle.UNAVAILABLE, shuffle != null && shuffle.isOn());
	}

	private static void refreshModeKey(Button key, String name, Enum<?> mode, boolean usable, boolean on) {
		key.setDisable(!usable);
		key.pseudoClassStateChanged(ON, on);
		key.getTooltip().setText(mode == null ? name : name + ": " + mode.name().toLowerCase());
	}

	/** NET is the default and returns with every new input; SETUP, QUICK and the TV key switch to TV. */
	void followInput() {
		String input = state.getInput();
		if (input != null && !input.equals(followedInput)) {
			followedInput = input;
			setMode(Target.NET);
		} else {
			setMode(mode);
		}
	}

	private void setMode(Target target) {
		mode = target;
		modeButtons.forEach((m, button) -> button.pseudoClassStateChanged(SELECTED, m == mode));
		okMode.setText(target.name());
	}

	// ---- layout

	private Node sourceKeys() {
		List<Button> keys = new ArrayList<>();
		for (Source source : SOURCES) {
			Button button = key(source.label(), e -> control.selectInput(source.code()));
			button.getStyleClass().add("source");
			sourceButtons.put(source.code(), button);
			keys.add(button);
		}
		return grid(3, keys);
	}

	/** Keys of one width in {@code columns} columns: rows with the same count line up (3 for text, 5 for icons and digits). */
	private static GridPane grid(int columns, List<? extends Region> keys) {
		GridPane grid = new GridPane();
		grid.setHgap(8);
		grid.setVgap(8);
		for (int i = 0; i < columns; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100.0 / columns);
			grid.getColumnConstraints().add(column);
		}
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).setMaxWidth(Double.MAX_VALUE);
			grid.add(keys.get(i), i % columns, i / columns);
		}
		return grid;
	}

	/** SETUP and QUICK open the receiver's menus on the TV and switch the direction pad there; "more" opens all functions. */
	private Node functionKeys() {
		Button setup = key("SETUP", e -> {
			setMode(Target.TV);
			control.openSetupMenu();
		});
		setup.setTooltip(new Tooltip("Setup menu on the TV"));
		Button quick = key("QUICK", e -> {
			setMode(Target.TV);
			control.openQuickMenu();
		});
		quick.setTooltip(new Tooltip("Quick setup on the TV"));
		Button more = iconKey(icon(18, solid(ICON_MORE)), "All functions of this model", e -> showFunctions.run());

		List<Button> keys = List.of(setup, quick, more);
		keys.forEach(key -> key.getStyleClass().add("fn-key"));
		return grid(3, keys);
	}

	private Node controlCluster() {
		Button volumeUp = rockerKey("+", control::volumeUp, "Volume up");
		Button volumeDown = rockerKey("−", control::volumeDown, "Volume down");
		Label volumeLabel = new Label("VOL");
		volumeLabel.getStyleClass().add("rocker-label");
		VBox rocker = new VBox(volumeUp, vspacer(), volumeLabel, vspacer(), volumeDown);
		rocker.getStyleClass().add("rocker");
		rocker.setPrefHeight(DPAD_SIZE);
		rocker.setMaxHeight(DPAD_SIZE);

		Button top = iconKey(icon(20, line(ICON_HOME)), "Home (NET: top menu)", e -> control.home(mode));
		top.getStyleClass().add("round");
		Button back = iconKey(icon(20, line(ICON_RETURN)), "Return (TV: exit)", e -> pressBack());
		back.getStyleClass().add("round");
		muteButton = iconKey(icon(20, solid(ICON_SPEAKER), line(ICON_CROSS)), "Mute", e -> control.toggleMute());
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

		Button ok = key(null, e -> pressOk());
		ok.getStyleClass().setAll("button", "dpad-ok");
		Label okText = label("dpad-ok-label");
		okText.setText("OK");
		VBox okLabel = new VBox(-2, okText, okMode);
		okLabel.setAlignment(Pos.CENTER);
		ok.setGraphic(okLabel);

		// the switch sits in the pad's top corners, outside the ring, so it reads as part of the pad
		Button net = modeKey(Target.NET, "Direction keys control the NET/USB lists");
		Button tv = modeKey(Target.TV, "Direction keys control the receiver's menus on the TV");
		StackPane.setAlignment(net, Pos.TOP_LEFT);
		StackPane.setAlignment(tv, Pos.TOP_RIGHT);
		StackPane.setMargin(net, new Insets(-6, 0, 0, -10));
		StackPane.setMargin(tv, new Insets(-6, -10, 0, 0));
		// repeat and shuffle mirror them in the bottom corners
		Button repeat = repeatKey();
		Button shuffle = shuffleKey();
		StackPane.setAlignment(repeat, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(shuffle, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(repeat, new Insets(0, 0, -6, -10));
		StackPane.setMargin(shuffle, new Insets(0, -10, -6, 0));

		StackPane pad = new StackPane(ring,
				dpadKey(ICON_UP, Direction.UP, Pos.TOP_CENTER),
				dpadKey(ICON_DOWN, Direction.DOWN, Pos.BOTTOM_CENTER),
				dpadKey(ICON_LEFT, Direction.LEFT, Pos.CENTER_LEFT),
				dpadKey(ICON_RIGHT, Direction.RIGHT, Pos.CENTER_RIGHT),
				ok, net, tv, repeat, shuffle);
		pad.setMinSize(DPAD_SIZE, DPAD_SIZE);
		pad.setPrefSize(DPAD_SIZE, DPAD_SIZE);
		pad.setMaxSize(DPAD_SIZE, DPAD_SIZE);
		return pad;
	}

	private Button dpadKey(String iconPath, Direction direction, Pos position) {
		Button button = key(null, e -> navigate(direction));
		button.getStyleClass().setAll("button", "dpad-key");
		button.setGraphic(icon(22, line(iconPath)));
		StackPane.setAlignment(button, position);
		return button;
	}

	/** Small key in a corner of the direction pad; the selected one is lit. */
	private Button modeKey(Target target, String tooltip) {
		Button button = key(target.name(), e -> setMode(target));
		button.getStyleClass().setAll("button", "key", "mode");
		button.setTooltip(new Tooltip(tooltip));
		modeButtons.put(target, button);
		return button;
	}

	/** Playback in five columns, then the "123" handle over the full width and the digits, again in five columns. */
	private Node playbackKeys() {
		GridPane transport = grid(5, List.of(
				transportKey(ICON_PREV, Playback.PREVIOUS, "Previous"),
				statusKey(PlayStatus.PLAY, transportKey(ICON_PLAY, Playback.PLAY, "Play")),
				statusKey(PlayStatus.PAUSE, transportKey(ICON_PAUSE, Playback.PAUSE, "Pause")),
				statusKey(PlayStatus.STOP, transportKey(ICON_STOP, Playback.STOP, "Stop")),
				transportKey(ICON_NEXT, Playback.NEXT, "Next")));
		numbersKey.getStyleClass().addAll("key", "drawer");
		numbersKey.setFocusTraversable(false);
		numbersKey.setTooltip(new Tooltip("Number keys"));
		numbersKey.setMaxWidth(Double.MAX_VALUE);
		return new VBox(8, transport, numbersKey, numbers);
	}

	private Button statusKey(PlayStatus status, Button key) {
		playStatusKeys.put(status, key);
		return key;
	}

	private Button repeatKey() {
		repeatOne.setVisible(false);
		repeatKey = cornerKey(icon(13, line(ICON_REPEAT), repeatOne), "Repeat", control::toggleRepeat);
		return repeatKey;
	}

	private Button shuffleKey() {
		shuffleKey = cornerKey(icon(13, line(ICON_SHUFFLE)), "Shuffle", control::toggleShuffle);
		return shuffleKey;
	}

	/** Small key in a bottom corner of the direction pad, lit while its mode is on. */
	private static Button cornerKey(Node icon, String tooltip, Runnable action) {
		Button button = iconKey(icon, tooltip, e -> action.run());
		button.getStyleClass().setAll("button", "key", "mode", "corner");
		return button;
	}

	private Button transportKey(String iconPath, Playback playback, String tooltip) {
		return transportKey(icon(16, solid(iconPath)), tooltip, () -> control.playback(playback));
	}

	private static Button transportKey(Node icon, String tooltip, Runnable action) {
		Button button = iconKey(icon, tooltip, e -> action.run());
		button.getStyleClass().add("transport");
		return button;
	}

	private Node numberKeys() {
		List<Button> keys = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int digit = (i + 1) % 10;
			Button button = key(String.valueOf(digit), e -> control.numberKey(digit));
			button.getStyleClass().add("num");
			keys.add(button);
		}
		return grid(5, keys);
	}
}
