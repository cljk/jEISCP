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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverControl.Direction;
import de.csmp.jeiscp.app.ReceiverControl.Playback;
import de.csmp.jeiscp.app.ReceiverControl.Target;
import de.csmp.jeiscp.app.ReceiverState;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
	private final Label okMode = label("dpad-ok-mode");
	private Button muteButton;
	private Target mode = Target.NET;
	private String followedInput;

	/** @param dismissPopup closes a shown NET popup; OK and return close it on the receiver too */
	KeyPad(ReceiverState state, ReceiverControl control, Runnable showFunctions, Runnable dismissPopup) {
		this.state = state;
		this.control = control;
		this.showFunctions = showFunctions;
		this.dismissPopup = dismissPopup;
		node = new VBox(14, sourceKeys(), functionKeys(), controlCluster(), transportKeys(), numberKeys());
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

	/** A new input picks the fitting target; the NET/TV keys override it until the input changes again. */
	void followInput() {
		String input = state.getInput();
		if (input != null && !input.equals(followedInput)) {
			followedInput = input;
			setMode(state.isNetworkInput() ? Target.NET : Target.TV);
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
			Button button = key(source.label(), e -> control.selectInput(source.code()));
			button.getStyleClass().add("source");
			button.setMaxWidth(Double.MAX_VALUE);
			grid.add(button, i % 3, i / 3);
			sourceButtons.put(source.code(), button);
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

		HBox row = new HBox(8, setup, quick, more);
		for (Node child : row.getChildren()) {
			Button button = (Button) child;
			button.getStyleClass().add("fn-key");
			button.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(button, Priority.ALWAYS);
		}
		return row;
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

		StackPane pad = new StackPane(ring,
				dpadKey(ICON_UP, Direction.UP, Pos.TOP_CENTER),
				dpadKey(ICON_DOWN, Direction.DOWN, Pos.BOTTOM_CENTER),
				dpadKey(ICON_LEFT, Direction.LEFT, Pos.CENTER_LEFT),
				dpadKey(ICON_RIGHT, Direction.RIGHT, Pos.CENTER_RIGHT),
				ok, net, tv);
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

	private Node transportKeys() {
		return new HBox(8,
				transportKey(ICON_PREV, Playback.PREVIOUS, "Previous"),
				transportKey(ICON_PLAY, Playback.PLAY, "Play"),
				transportKey(ICON_PAUSE, Playback.PAUSE, "Pause"),
				transportKey(ICON_STOP, Playback.STOP, "Stop"),
				transportKey(ICON_NEXT, Playback.NEXT, "Next"));
	}

	private Button transportKey(String iconPath, Playback playback, String tooltip) {
		Button button = iconKey(icon(16, solid(iconPath)), tooltip, e -> control.playback(playback));
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
			int digit = (i + 1) % 10;
			Button button = key(String.valueOf(digit), e -> control.numberKey(digit));
			button.getStyleClass().add("num");
			button.setMaxWidth(Double.MAX_VALUE);
			grid.add(button, i % 5, i / 5);
		}
		return grid;
	}
}
