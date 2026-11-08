package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.app.fx.Ui.icon;
import static de.csmp.jeiscp.app.fx.Ui.iconKey;
import static de.csmp.jeiscp.app.fx.Ui.key;
import static de.csmp.jeiscp.app.fx.Ui.label;
import static de.csmp.jeiscp.app.fx.Ui.line;
import static de.csmp.jeiscp.app.fx.Ui.rockerKey;
import static de.csmp.jeiscp.app.fx.Ui.solid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.Zone;
import de.csmp.jeiscp.app.ZoneState;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** One foldable row per additional zone of the model: power, input, volume and mute. */
final class ZonePanel {

	/** Input code for "whatever the main zone plays". */
	private static final String MAIN_SOURCE = "80";
	private static final String MAIN_SOURCE_LABEL = "MAIN";
	private static final String UNKNOWN = "–";

	private static final PseudoClass ON = PseudoClass.getPseudoClass("on");

	private final ReceiverState state;
	private final ReceiverControl control;
	private final ToggleButton handle = new ToggleButton("ZONES");
	private final VBox node = new VBox(6);
	private final List<Runnable> rowRefreshers = new ArrayList<>();
	private final boolean empty;

	/**
	 * @param zones the model's zones; none: no handle, nothing to show
	 * @param shown whether the rows start unfolded
	 * @param toggled told when the handle folds or unfolds them
	 */
	ZonePanel(ReceiverState state, ReceiverControl control, List<Zone> zones, boolean shown, Consumer<Boolean> toggled) {
		this.state = state;
		this.control = control;
		this.empty = zones.isEmpty();
		zones.forEach(zone -> node.getChildren().add(row(zone)));

		handle.getStyleClass().addAll("key", "drawer");
		handle.setFocusTraversable(false);
		handle.setTooltip(new Tooltip("Additional zones"));
		handle.setMaxWidth(Double.MAX_VALUE);
		handle.setSelected(shown);
		show(shown);
		handle.selectedProperty().addListener((obs, was, now) -> {
			show(now);
			toggled.accept(now);
		});
	}

	private void show(boolean shown) {
		node.setVisible(shown);
		node.setManaged(shown);
	}

	boolean isEmpty() {
		return empty;
	}

	ToggleButton getHandle() {
		return handle;
	}

	Node getNode() {
		return node;
	}

	void refresh() {
		rowRefreshers.forEach(Runnable::run);
	}

	private Node row(Zone zone) {
		ZoneState zoneState = state.zone(zone);

		Label name = label("zone-name");
		name.setText(zone.label().toUpperCase());

		Button power = iconKey(icon(16, line(HeaderBar.ICON_POWER)), zone.label() + " power", e -> control.toggleZonePower(zone));
		power.getStyleClass().addAll("round-small", "power");

		Button input = key(UNKNOWN, null);
		input.getStyleClass().add("zone-input");
		input.setTooltip(new Tooltip(zone.label() + " input"));
		input.setMaxWidth(Double.MAX_VALUE);
		input.setMinWidth(0);
		HBox.setHgrow(input, Priority.ALWAYS);
		ContextMenu inputs = inputMenu(zone);
		input.setOnAction(e -> inputs.show(input, Side.BOTTOM, 0, 2));

		Button down = rockerKey("−", () -> control.zoneVolumeDown(zone), zone.label() + " volume down");
		down.getStyleClass().add("zone-volume");
		Button up = rockerKey("+", () -> control.zoneVolumeUp(zone), zone.label() + " volume up");
		up.getStyleClass().add("zone-volume");
		Label volume = label("zone-level");
		volume.setAlignment(Pos.CENTER);

		Button mute = iconKey(icon(14, solid(KeyPad.ICON_SPEAKER), line(KeyPad.ICON_CROSS)), zone.label() + " mute",
				e -> control.toggleZoneMute(zone));
		mute.getStyleClass().addAll("round-small", "mute");

		rowRefreshers.add(() -> {
			power.pseudoClassStateChanged(ON, Boolean.TRUE.equals(zoneState.getPower()));
			mute.pseudoClassStateChanged(ON, Boolean.TRUE.equals(zoneState.getMuted()));
			input.setText(inputLabel(zoneState.getInput()));
			volume.setText(zoneState.getVolume() != null ? String.valueOf(zoneState.getVolume()) : UNKNOWN);
		});
		rowRefreshers.get(rowRefreshers.size() - 1).run();

		HBox row = new HBox(5, power, name, input, down, volume, up, mute);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

	private ContextMenu inputMenu(Zone zone) {
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(inputItem(zone, MAIN_SOURCE_LABEL, MAIN_SOURCE));
		KeyPad.SOURCES.forEach(source -> menu.getItems().add(inputItem(zone, source.label(), source.code())));
		return menu;
	}

	private MenuItem inputItem(Zone zone, String text, String code) {
		MenuItem item = new MenuItem(text);
		item.setOnAction(e -> control.selectZoneInput(zone, code));
		return item;
	}

	private static String inputLabel(String code) {
		if (code == null) {
			return UNKNOWN;
		}
		return MAIN_SOURCE.equals(code) ? MAIN_SOURCE_LABEL : KeyPad.sourceLabel(code);
	}
}
