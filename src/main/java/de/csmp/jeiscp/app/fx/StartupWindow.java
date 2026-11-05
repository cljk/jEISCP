package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.app.fx.Ui.icon;
import static de.csmp.jeiscp.app.fx.Ui.iconKey;
import static de.csmp.jeiscp.app.fx.Ui.key;
import static de.csmp.jeiscp.app.fx.Ui.label;
import static de.csmp.jeiscp.app.fx.Ui.line;
import static de.csmp.jeiscp.app.fx.Ui.spacer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Borderless status window while jEISCP searches for or connects to the receiver; on failure it asks for an address
 * or another search. Its methods may be called from any thread.
 */
public final class StartupWindow {

	private static final String ICON_CLOSE = "M7 7l10 10M17 7L7 17";

	private final CountDownLatch shown = new CountDownLatch(1);
	private volatile CompletableFuture<String> choice;
	// built and used on the FX thread
	private Stage stage;
	private Label state;
	private Label countdown;
	private Label detail;
	private ProgressBar progress;
	private TextField address;
	private HBox choices;
	private Timeline timer;

	private StartupWindow() {
	}

	/** Starts JavaFX and shows the window; returns once it is visible. */
	public static StartupWindow open() throws InterruptedException {
		StartupWindow window = new StartupWindow();
		Ui.onFxThread(window::build);
		window.shown.await();
		return window;
	}

	/** Autodiscovery with a countdown to {@code timeout}. */
	public void searching(java.time.Duration timeout) {
		Platform.runLater(() -> {
			show("SEARCHING", "Autodiscovery in all networks", false);
			timer = new Timeline(
					new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), 0)),
					new KeyFrame(Duration.millis(timeout.toMillis()), new KeyValue(progress.progressProperty(), 1, Interpolator.LINEAR)));
			timer.currentTimeProperty().addListener((obs, old, time) ->
					countdown.setText((int) Math.ceil((timeout.toMillis() - time.toMillis()) / 1000) + " s"));
			timer.play();
		});
	}

	public void connecting(String receiver) {
		Platform.runLater(() -> {
			show("CONNECTING", receiver, false);
			progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		});
	}

	/**
	 * Shows the problem and waits for the user.
	 * @return the address to connect to, as {@code host} or {@code host:port}; {@code null} to search again
	 */
	public String failed(String problem) {
		CompletableFuture<String> decided = new CompletableFuture<>();
		choice = decided;
		Platform.runLater(() -> {
			show("NO RECEIVER", problem, true);
			address.requestFocus();
		});
		return decided.join();
	}

	/** Hides the window; call it after the remote is shown, or JavaFX ends with its last window. */
	public void close() {
		Platform.runLater(() -> {
			stopTimer();
			stage.hide();
		});
	}

	private void build() {
		Label brand = new Label("jEISCP");
		brand.getStyleClass().add("brand");
		Button close = iconKey(icon(14, line(ICON_CLOSE)), "Quit jEISCP", e -> quit());
		close.getStyleClass().setAll("button", "window-key", "close");
		HBox header = new HBox(8, brand, spacer(), close);
		header.setAlignment(Pos.CENTER_LEFT);

		state = label("lcd-source");
		countdown = label("lcd-volume");
		detail = label("lcd-secondary");
		detail.setWrapText(true);
		detail.setMinHeight(Region.USE_PREF_SIZE);
		progress = new ProgressBar(0);
		progress.setMaxWidth(Double.MAX_VALUE);
		progress.setMouseTransparent(true);
		VBox lcd = new VBox(8, new HBox(8, state, spacer(), countdown), detail, progress);
		lcd.getStyleClass().addAll("lcd", "startup-lcd");

		address = new TextField();
		address.setPromptText("IP address, e.g. 192.168.0.97");
		address.getStyleClass().add("fn-search");
		address.setOnAction(e -> connectTo());
		HBox.setHgrow(address, Priority.ALWAYS);
		Button connect = key("CONNECT", e -> connectTo());
		Button search = key("SEARCH AGAIN", e -> decide(null));
		choices = new HBox(8, address, connect, search);
		choices.setAlignment(Pos.CENTER_LEFT);
		choices.setVisible(false);
		choices.setManaged(false);

		VBox panel = new VBox(12, header, lcd, choices);
		panel.getStyleClass().addAll("remote", "startup");
		WindowDrag.install(panel);
		StackPane root = new StackPane(panel);
		root.getStyleClass().add("backdrop");

		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add(StartupWindow.class.getResource("remote.css").toExternalForm());
		stage = new Stage(StageStyle.TRANSPARENT);
		stage.getIcons().setAll(RemoteControlView.icons());
		stage.setTitle("jEISCP");
		stage.setScene(scene);
		stage.setOnCloseRequest(e -> quit());
		stage.show();
		shown.countDown();
	}

	private void show(String title, String text, boolean ask) {
		stopTimer();
		state.setText(title);
		detail.setText(text);
		countdown.setText("");
		progress.setVisible(!ask);
		progress.setManaged(!ask);
		choices.setVisible(ask);
		choices.setManaged(ask);
		stage.setTitle("jEISCP · " + title.toLowerCase());
		stage.sizeToScene();
	}

	private void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	private void connectTo() {
		String text = address.getText().strip();
		if (!text.isEmpty()) {
			decide(text);
		}
	}

	private void decide(String receiver) {
		CompletableFuture<String> decided = choice;
		if (decided != null) {
			choice = null;
			decided.complete(receiver);
		}
	}

	private static void quit() {
		Platform.exit();
		System.exit(0);
	}
}
