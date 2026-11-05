package de.csmp.jeiscp.app.fx;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.csmp.jeiscp.app.LogBuffer;
import de.csmp.jeiscp.app.LogBuffer.Entry;
import de.csmp.jeiscp.app.LogBuffer.Kind;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Borderless window with the live application log, sent (→) and received (←) ISCP commands highlighted.
 * Must be used on the JavaFX thread; there is at most one.
 */
final class LogWindow {

	private static final int MAX_LINES = 5000;
	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
	private static final PseudoClass SENT = PseudoClass.getPseudoClass("sent");
	private static final PseudoClass RECEIVED = PseudoClass.getPseudoClass("received");
	private static final PseudoClass WARN = PseudoClass.getPseudoClass("warn");
	private static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");

	private static final String ICON_CLOSE = "M7 7l10 10M17 7L7 17";
	private static final String ICON_CLEAR = "M5 7h14M10 7V5h4v2M7 7l1 12h8l1-12";
	private static final String ICON_COPY = "M8 8h10v12H8zM5 16V4h10";

	private static LogWindow open;

	private final Stage stage;
	private final LogBuffer buffer;
	private final ObservableList<Entry> lines = FXCollections.observableArrayList();
	private final FilteredList<Entry> visible = new FilteredList<>(lines);
	private final ListView<Entry> list = new ListView<>(visible);
	private final ToggleButton follow = new ToggleButton("FOLLOW");
	private final Queue<Entry> pending = new ConcurrentLinkedQueue<>();
	private final AtomicBoolean flushScheduled = new AtomicBoolean();
	private final Consumer<Entry> listener = this::enqueue;
	private double dragX;
	private double dragY;

	/** Shows the log window next to {@code owner}, or brings the open one to the front. */
	static void show(Window owner, LogBuffer buffer) {
		if (open == null) {
			open = new LogWindow(owner, buffer);
		}
		open.stage.show();
		open.stage.toFront();
	}

	private LogWindow(Window owner, LogBuffer buffer) {
		this.buffer = buffer;
		stage = new Stage(StageStyle.TRANSPARENT);
		lines.setAll(buffer.subscribe(listener));

		Label title = new Label("eISCP LOG");
		title.getStyleClass().add("log-title");
		ToggleButton iscpOnly = toggle("ISCP ONLY", "Only sent and received commands");
		iscpOnly.selectedProperty().addListener((obs, old, only) ->
				visible.setPredicate(only ? e -> e.kind() != Kind.OTHER : null));
		follow.getStyleClass().addAll("key", "log-toggle");
		follow.setFocusTraversable(false);
		follow.setTooltip(new Tooltip("Keep the newest line in view"));
		follow.setSelected(true);
		follow.selectedProperty().addListener((obs, old, on) -> scrollToEnd());

		Region gap = new Region();
		HBox.setHgrow(gap, Priority.ALWAYS);
		HBox header = new HBox(6, title, gap, iscpOnly, follow,
				iconButton(ICON_COPY, "Copy the shown lines (Ctrl+C copies the selection)", () -> copy(visible)),
				iconButton(ICON_CLEAR, "Clear the window", lines::clear),
				iconButton(ICON_CLOSE, "Close", this::close));
		header.setAlignment(Pos.CENTER_LEFT);
		header.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startDrag);
		header.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);

		list.getStyleClass().add("log-list");
		list.setFixedCellSize(18);
		list.setCellFactory(v -> new LogCell());
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		list.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.isShortcutDown() && e.getCode() == KeyCode.C) {
				copy(list.getSelectionModel().getSelectedItems());
				e.consume();
			}
		});
		VBox.setVgrow(list, Priority.ALWAYS);

		Region grip = new Region();
		grip.getStyleClass().add("log-grip");
		grip.setCursor(Cursor.SE_RESIZE);
		grip.setOnMouseDragged(e -> {
			stage.setWidth(Math.max(360, e.getScreenX() - stage.getX() + 6));
			stage.setHeight(Math.max(180, e.getScreenY() - stage.getY() + 6));
		});
		StackPane.setAlignment(grip, Pos.BOTTOM_RIGHT);

		VBox panel = new VBox(6, header, list);
		panel.getStyleClass().add("log-window");
		StackPane root = new StackPane(panel, grip);
		root.getStyleClass().add("backdrop");

		Scene scene = new Scene(root, 600, 340);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add(RemoteControlView.class.getResource("remote.css").toExternalForm());
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				close();
			}
		});

		stage.initOwner(owner);
		stage.setTitle("jEISCP log");
		stage.getIcons().setAll(RemoteControlView.icons());
		stage.setScene(scene);
		stage.setOnHidden(e -> {
			buffer.unsubscribe(listener);
			open = null;
		});
		placeNextTo(owner);
		scrollToEnd();
	}

	/** Right of the remote if it fits on that screen, otherwise left of it. */
	private void placeNextTo(Window owner) {
		Rectangle2D screen = Screen.getScreensForRectangle(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight())
				.stream().findFirst().orElse(Screen.getPrimary()).getVisualBounds();
		double width = stage.getScene().getWidth();
		double x = owner.getX() + owner.getWidth() - 12;
		if (x + width > screen.getMaxX()) {
			x = Math.max(screen.getMinX(), owner.getX() - width + 12);
		}
		stage.setX(x);
		stage.setY(Math.max(screen.getMinY(), owner.getY() + 40));
	}

	private void close() {
		stage.hide();
	}

	/** Called on the logging thread; collects lines and updates the list in one go on the FX thread. */
	private void enqueue(Entry entry) {
		pending.add(entry);
		if (flushScheduled.compareAndSet(false, true)) {
			Platform.runLater(this::flush);
		}
	}

	private void flush() {
		flushScheduled.set(false);
		List<Entry> batch = new ArrayList<>();
		for (Entry entry; (entry = pending.poll()) != null;) {
			batch.add(entry);
		}
		lines.addAll(batch);
		if (lines.size() > MAX_LINES) {
			lines.remove(0, lines.size() - MAX_LINES);
		}
		scrollToEnd();
	}

	private void scrollToEnd() {
		if (follow.isSelected() && !visible.isEmpty()) {
			list.scrollTo(visible.size() - 1);
		}
	}

	private static void copy(List<Entry> entries) {
		ClipboardContent content = new ClipboardContent();
		content.putString(entries.stream().map(LogWindow::format).collect(Collectors.joining(System.lineSeparator())));
		Clipboard.getSystemClipboard().setContent(content);
	}

	static String format(Entry entry) {
		String marker = switch (entry.kind()) {
			case SENT -> "->";
			case RECEIVED -> "<-";
			case OTHER -> entry.level() + " " + entry.source();
		};
		return TIME.format(Instant.ofEpochMilli(entry.time())) + " " + marker + " " + entry.message();
	}

	private ToggleButton toggle(String text, String tooltip) {
		ToggleButton button = new ToggleButton(text);
		button.getStyleClass().addAll("key", "log-toggle");
		button.setFocusTraversable(false);
		button.setTooltip(new Tooltip(tooltip));
		return button;
	}

	private Button iconButton(String iconPath, String tooltip, Runnable action) {
		Button button = new Button();
		button.setGraphic(RemoteControlView.icon(14, RemoteControlView.line(iconPath)));
		button.getStyleClass().setAll("button", "window-key");
		button.setFocusTraversable(false);
		button.setTooltip(new Tooltip(tooltip));
		button.setOnAction(e -> action.run());
		return button;
	}

	private void startDrag(MouseEvent event) {
		dragX = event.getScreenX() - stage.getX();
		dragY = event.getScreenY() - stage.getY();
	}

	private void drag(MouseEvent event) {
		if (!(event.getTarget() instanceof Node node) || !RemoteControlView.isInteractive(node)) {
			stage.setX(event.getScreenX() - dragX);
			stage.setY(event.getScreenY() - dragY);
		}
	}

	private static final class LogCell extends ListCell<Entry> {
		private final Label time = label("log-time");
		private final Label marker = label("log-marker");
		private final Label source = label("log-source");
		private final Label message = label("log-message");
		private final HBox row = new HBox(8, time, marker, source, message);

		LogCell() {
			getStyleClass().add("log-cell");
			row.setAlignment(Pos.CENTER_LEFT);
			time.setMinWidth(Region.USE_PREF_SIZE);
			marker.setMinWidth(Region.USE_PREF_SIZE);
			source.setMinWidth(Region.USE_PREF_SIZE);
			setText(null);
		}

		@Override
		protected void updateItem(Entry entry, boolean empty) {
			super.updateItem(entry, empty);
			if (empty || entry == null) {
				setGraphic(null);
				return;
			}
			time.setText(TIME.format(Instant.ofEpochMilli(entry.time())));
			boolean other = entry.kind() == Kind.OTHER;
			marker.setText(switch (entry.kind()) {
				case SENT -> "→";
				case RECEIVED -> "←";
				case OTHER -> entry.level();
			});
			source.setText(other ? entry.source() : "");
			source.setVisible(other);
			source.setManaged(other);
			message.setText(entry.message());
			pseudoClassStateChanged(SENT, entry.kind() == Kind.SENT);
			pseudoClassStateChanged(RECEIVED, entry.kind() == Kind.RECEIVED);
			pseudoClassStateChanged(WARN, "WARN".equals(entry.level()));
			pseudoClassStateChanged(ERROR, "ERROR".equals(entry.level()) || "FATAL".equals(entry.level()));
			setGraphic(row);
		}

		private static Label label(String styleClass) {
			Label label = new Label();
			label.getStyleClass().add(styleClass);
			return label;
		}
	}
}
