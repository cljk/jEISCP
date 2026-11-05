package de.csmp.jeiscp.app.fx;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/** One line that slowly scrolls back and forth when its text is wider than the room it gets. */
final class Marquee extends Region {

	private static final double PIXELS_PER_SECOND = 30;
	private static final Duration PAUSE = Duration.seconds(2.5);

	private final Label label;
	private Timeline scroll;
	private double scrolledWidth = -1;

	Marquee(String... styleClasses) {
		label = new Label();
		label.getStyleClass().addAll(styleClasses);
		label.setMinWidth(USE_PREF_SIZE);	// the full text, never an ellipsis
		getChildren().add(label);
		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(widthProperty());
		clip.heightProperty().bind(heightProperty());
		setClip(clip);
		setMinWidth(0);
		setMaxWidth(Double.MAX_VALUE);
	}

	void setText(String text) {
		if (!text.equals(label.getText())) {
			label.setText(text);
			scrolledWidth = -1;
			requestLayout();
		}
	}

	String getText() {
		return label.getText();
	}

	@Override
	protected double computePrefWidth(double height) {
		return 0;	// takes the room it is given
	}

	@Override
	protected double computePrefHeight(double width) {
		return label.prefHeight(-1);
	}

	@Override
	protected double computeMinHeight(double width) {
		return computePrefHeight(width);
	}

	@Override
	protected void layoutChildren() {
		double textWidth = label.prefWidth(-1);
		label.resizeRelocate(0, 0, textWidth, getHeight());
		double overflow = textWidth - getWidth();
		if (overflow != scrolledWidth) {
			scrolledWidth = overflow;
			restart(overflow);
		}
	}

	private void restart(double overflow) {
		if (scroll != null) {
			scroll.stop();
			scroll = null;
		}
		label.setTranslateX(0);
		if (overflow <= 1 || getWidth() <= 0) {
			return;
		}
		Duration travel = Duration.seconds(overflow / PIXELS_PER_SECOND);
		scroll = new Timeline(
				new KeyFrame(PAUSE, new KeyValue(label.translateXProperty(), 0)),
				new KeyFrame(PAUSE.add(travel), new KeyValue(label.translateXProperty(), -overflow, Interpolator.EASE_BOTH)),
				new KeyFrame(PAUSE.add(travel).add(PAUSE)));
		scroll.setAutoReverse(true);	// back to the start the same way
		scroll.setCycleCount(Animation.INDEFINITE);
		scroll.play();
	}
}
