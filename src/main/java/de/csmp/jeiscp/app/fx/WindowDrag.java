package de.csmp.jeiscp.app.fx;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/** Moves a borderless window while it is dragged by a part of {@code handle} that is not a control. */
final class WindowDrag {

	private double offsetX;
	private double offsetY;
	private boolean dragging;

	private WindowDrag() {
	}

	static void install(Node handle) {
		WindowDrag drag = new WindowDrag();
		handle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> drag.start(handle, e));
		handle.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> drag.move(handle, e));
	}

	private void start(Node handle, MouseEvent event) {
		Window window = handle.getScene().getWindow();
		dragging = event.isPrimaryButtonDown() && !isInteractive(event.getPickResult().getIntersectedNode());
		if (dragging) {
			offsetX = event.getScreenX() - window.getX();
			offsetY = event.getScreenY() - window.getY();
		}
	}

	private void move(Node handle, MouseEvent event) {
		if (dragging) {
			Window window = handle.getScene().getWindow();
			window.setX(event.getScreenX() - offsetX);
			window.setY(event.getScreenY() - offsetY);
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
}
