package de.csmp.jeiscp.app.fx;

import static de.csmp.jeiscp.app.fx.Ui.label;
import static de.csmp.jeiscp.app.fx.Ui.spacer;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.csmp.jeiscp.app.ReceiverControl;
import de.csmp.jeiscp.app.ReceiverState;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** The LCD: input and volume, now playing, the NET list or cover art, and NET popups. Also yields the window title. */
final class DisplayPanel {

	/** Slider ceiling; the full scale of 100 is rarely sensible. */
	static final int MAX_VOLUME = 70;

	private static final double NET_LINE_HEIGHT = 19;
	private static final int NET_LINES_SHOWN = 5;

	private static final PseudoClass CURSOR = PseudoClass.getPseudoClass("cursor");
	private static final PseudoClass STANDBY = PseudoClass.getPseudoClass("standby");

	private final ReceiverState state;
	private final ReceiverControl control;
	private final StringProperty windowTitle = new SimpleStringProperty("jEISCP");

	private final VBox lcd;
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

	DisplayPanel(ReceiverState state, ReceiverControl control) {
		this.state = state;
		this.control = control;

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
	}

	Node getNode() {
		return lcd;
	}

	ReadOnlyStringProperty windowTitleProperty() {
		return windowTitle;
	}

	/** Dims the display in standby and while the connection is lost. */
	void refreshStandby() {
		lcd.pseudoClassStateChanged(STANDBY, Boolean.FALSE.equals(state.getPower()) || !state.isConnected());
	}

	void refreshVolume() {
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

	private void sendVolume() {
		control.setVolume((int) Math.round(volumeSlider.getValue()));
	}

	void refreshText() {
		String source = switch (state.getConnectionState()) {
			case CONNECTED -> Boolean.FALSE.equals(state.getPower()) ? "STANDBY" : KeyPad.sourceLabel(state.getInput());
			case CONNECTING -> "CONNECTING";
			default -> "OFFLINE";
		};
		lcdSource.setText(source);

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

	void refreshNetList() {
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

	void refreshScreen() {
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

	void refreshPopup() {
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

	/** OK and return also close a popup on the receiver. */
	void dismissPopupIfShown() {
		if (popup.isVisible()) {
			dismissPopup();
		}
	}

	private void dismissPopup() {
		state.dismissPopup();
		refreshPopup();
	}

	private static String firstNonEmpty(String... values) {
		for (String value : values) {
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		return "";
	}

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
}
