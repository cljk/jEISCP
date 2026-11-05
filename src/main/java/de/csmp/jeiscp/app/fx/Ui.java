package de.csmp.jeiscp.app.fx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/** Small building blocks of the remote's look; styles live in {@code remote.css}. */
final class Ui {

	private Ui() {
	}

	/** Keys never take the focus, so the arrow keys stay shortcuts for the direction pad. */
	/** Runs {@code task} on the JavaFX thread, starting JavaFX first if needed; tasks run in the order given. */
	static void onFxThread(Runnable task) {
		try {
			Platform.startup(task);
		} catch (IllegalStateException alreadyStarted) {
			Platform.runLater(task);
		}
	}

	static Button key(String text, EventHandler<ActionEvent> action) {
		Button button = new Button(text);
		button.getStyleClass().add("key");
		button.setFocusTraversable(false);
		button.setOnAction(action);
		return button;
	}

	static Button iconKey(Node icon, String tooltip, EventHandler<ActionEvent> action) {
		Button button = key(null, action);
		button.setGraphic(icon);
		button.setTooltip(new Tooltip(tooltip));
		return button;
	}

	/** Sends once on press and repeats while the key is held. */
	static Button rockerKey(String text, Runnable action, String tooltip) {
		Button button = new Button(text);
		button.getStyleClass().setAll("button", "rocker-key");
		button.setFocusTraversable(false);
		button.setTooltip(new Tooltip(tooltip));

		Timeline repeat = new Timeline(new KeyFrame(Duration.millis(180), e -> action.run()));
		repeat.setCycleCount(Animation.INDEFINITE);
		PauseTransition delay = new PauseTransition(Duration.millis(450));
		delay.setOnFinished(e -> repeat.play());

		button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.isPrimaryButtonDown()) {
				action.run();
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

	static Label label(String styleClass) {
		Label label = new Label();
		label.getStyleClass().add(styleClass);
		return label;
	}

	static Region spacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	static Region vspacer() {
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
