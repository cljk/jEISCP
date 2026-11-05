package de.csmp.jeiscp.app.fx;

import java.util.Locale;
import java.util.function.Consumer;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.DeviceFunctions;
import de.csmp.jeiscp.app.DeviceFunctions.Function;
import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.eiscp.Command;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Page with every function the connected model offers per catalogue; selecting one queries its current value.
 * Must be used on the JavaFX thread.
 */
final class FunctionsPage {

	private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
	private static final String ICON_CLOSE = "M6 6l12 12M18 6L6 18";

	private final ReceiverState state;
	private final Consumer<String> sender;
	private final VBox root;
	private final TextField search = new TextField();
	private final ListView<Function> list = new ListView<>();
	private final Label title = new Label();
	private final Label description = new Label();
	private final Label current = new Label();
	private final FlowPane actions = new FlowPane(6, 6);

	FunctionsPage(ReceiverState state, EiscpDevice device, Consumer<String> sender, Runnable close) {
		this.state = state;
		this.sender = sender;

		FilteredList<Function> filtered = new FilteredList<>(FXCollections.observableArrayList(DeviceFunctions.forDevice(device)));

		Label heading = new Label("FUNCTIONS");
		heading.getStyleClass().add("fn-heading");
		Label scope = new Label(filtered.size() + (device != null ? " for " + device.getDeviceType() : ", model unknown"));
		scope.getStyleClass().add("fn-scope");
		Button closeButton = new Button();
		closeButton.setGraphic(RemoteControlView.icon(16, RemoteControlView.line(ICON_CLOSE)));
		closeButton.getStyleClass().addAll("key", "round-small");
		closeButton.setFocusTraversable(false);
		closeButton.setTooltip(new Tooltip("Back to the remote"));
		closeButton.setOnAction(e -> close.run());
		Region gap = new Region();
		HBox.setHgrow(gap, Priority.ALWAYS);
		HBox header = new HBox(8, heading, scope, gap, closeButton);
		header.setAlignment(Pos.CENTER_LEFT);

		search.setPromptText("Search, e.g. listening, dimmer, LMD");
		search.getStyleClass().add("fn-search");
		search.textProperty().addListener((obs, old, text) -> filtered.setPredicate(f -> matches(f, text)));

		list.setItems(filtered);
		list.getStyleClass().add("fn-list");
		list.setFocusTraversable(false);
		list.setFixedCellSize(24);
		list.setPrefHeight(24 * 6 + 8);
		list.setMinHeight(24 * 6 + 8);
		list.setCellFactory(v -> new FunctionCell());
		list.getSelectionModel().selectedItemProperty().addListener((obs, old, function) -> select(function));

		title.getStyleClass().add("fn-title");
		description.getStyleClass().add("fn-description");
		description.setWrapText(true);
		current.getStyleClass().add("fn-current");
		ScrollPane scroll = new ScrollPane(actions);
		scroll.getStyleClass().add("fn-scroll");
		scroll.setFitToWidth(true);
		scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		VBox.setVgrow(scroll, Priority.ALWAYS);
		VBox detail = new VBox(4, title, description, current, scroll);
		detail.getStyleClass().add("fn-detail");
		VBox.setVgrow(detail, Priority.ALWAYS);

		root = new VBox(10, header, search, list, detail);
		root.getStyleClass().add("functions");
		// the remote's keys decide the height; this page only fills it
		root.setMinHeight(0);
		root.setPrefHeight(0);
		select(null);
	}

	Node getRoot() {
		return root;
	}

	void focusSearch() {
		search.requestFocus();
	}

	/** Updates the current value of the selected function. */
	void refresh() {
		Function function = list.getSelectionModel().getSelectedItem();
		if (function != null) {
			showCurrent(function);
		}
	}

	private void select(Function function) {
		actions.getChildren().clear();
		if (function == null) {
			title.setText("Select a function");
			description.setText("Only functions with fixed values are listed; the catalogue's model data is coarse.");
			current.setText("");
			return;
		}
		title.setText(function.label());
		description.setText(function.block().getDescription());
		for (Command cmd : function.actions()) {
			Button button = new Button(DeviceFunctions.actionLabel(cmd));
			button.getStyleClass().addAll("key", "fn-action");
			button.setFocusTraversable(false);
			button.setUserData(cmd.getCommand());
			button.setTooltip(new Tooltip(cmd.getIscpCommand() + (cmd.getDescription() != null ? ": " + cmd.getDescription() : "")));
			button.setOnAction(e -> sender.accept(cmd.getIscpCommand()));
			actions.getChildren().add(button);
		}
		showCurrent(function);
		if (function.queryable()) {
			sender.accept(function.prefix() + "QSTN");
		}
	}

	private void showCurrent(Function function) {
		String raw = state.getLastValue(function.prefix());
		String value = DeviceFunctions.describeValue(function.prefix(), raw);
		if (value != null) {
			current.setText("Current: " + value);
		} else {
			current.setText(function.queryable() ? "Current: ..." : "");
		}
		for (Node button : actions.getChildren()) {
			button.pseudoClassStateChanged(SELECTED, raw != null && raw.equals(button.getUserData()));
		}
	}

	private static boolean matches(Function function, String text) {
		if (text == null || text.isBlank()) {
			return true;
		}
		String needle = text.trim().toLowerCase(Locale.ROOT);
		String description = function.block().getDescription();
		return function.label().toLowerCase(Locale.ROOT).contains(needle)
				|| function.prefix().toLowerCase(Locale.ROOT).contains(needle)
				|| (description != null && description.toLowerCase(Locale.ROOT).contains(needle));
	}

	private static final class FunctionCell extends ListCell<Function> {
		private final Label prefix = new Label();

		FunctionCell() {
			prefix.getStyleClass().add("fn-prefix");
			setContentDisplay(ContentDisplay.RIGHT);
			setGraphicTextGap(8);
		}

		@Override
		protected void updateItem(Function item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? null : item.label());
			prefix.setText(empty ? null : item.prefix());
			setGraphic(empty ? null : prefix);
		}
	}
}
