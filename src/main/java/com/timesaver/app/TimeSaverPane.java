package com.timesaver.app;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.Timer;

import com.fo.controls.fontpicker.FontPicker;
import com.timesaver.app.file.Configuration;
import com.timesaver.app.file.ConfigurationFileHandler;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TimeSaverPane extends AnchorPane {

	private Stage stage;

	private GridPane gp;
	private Label fullscreenTime;

	private Configuration configuration;

	private FontPicker fontPicker;

	private Button maximiseButton;

	private ColorPicker colorPicker;

	private Button closeButton;

	public TimeSaverPane(Stage stage) {
		this.stage = stage;
		super.setMinWidth(400);
	}

	public void initialiseFX() {
		loadOrInitConfig();
		initialiseControlHBox();
		initialiseFullscreenTime();
		initialiseFullScreenListener();
		switchToControlPanel();

		Platform.runLater(() -> {
			fontPicker.setValue(configuration.getFont());
			maximiseButton.requestFocus();
		});
	}

	private void loadOrInitConfig() {
		if (ConfigurationFileHandler.getInstance().isConfigurationPresent()) {
			configuration = ConfigurationFileHandler.getInstance().loadConfiguration();
		} else {
			configuration = new Configuration();
		}
	}

	private void initialiseFullScreenListener() {
		stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().equals(KeyCode.ESCAPE)) {
				stage.setFullScreen(false);
				switchToControlPanel();
				stage.setAlwaysOnTop(false);
				stage.setAlwaysOnTop(true);
			}
		});
	}

	private void initialiseControlHBox() {
		fontPicker = createFontPicker();
		colorPicker = createColorPicker();
		maximiseButton = createMaximiseButton();
		closeButton = createCloseButton();

		layoutContent();
	}

	private void layoutContent() {
		gp = new GridPane();
		gp.setHgap(20);
		gp.setVgap(20);

		Label fontLabel = new Label("Font: ");
		Label colorLabel = new Label("Color: ");

		gp.add(fontLabel, 0, 0);
		gp.add(fontPicker, 1, 0);
		gp.add(colorLabel, 0, 1);
		gp.add(colorPicker, 1, 1);
		gp.add(maximiseButton, 0, 2);
		gp.add(closeButton, 1, 2);

		AnchorPane.setBottomAnchor(gp, 20.0);
		AnchorPane.setRightAnchor(gp, 20.0);
		AnchorPane.setLeftAnchor(gp, 20.0);
		AnchorPane.setTopAnchor(gp, 20.0);

		super.getChildren().addAll(gp);
	}

	private ColorPicker createColorPicker() {
		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(configuration.getColor());
		colorPicker.valueProperty().addListener((obsV, oldV, newV) -> setColor(newV));

		return colorPicker;
	}

	private void setColor(Color newV) {
		configuration.setColor(newV);
		ConfigurationFileHandler.getInstance().saveConfiguration(configuration);
	}

	private FontPicker createFontPicker() {
		FontPicker fontPicker = new FontPicker();

		fontPicker.valueProperty().addListener((obsV, oldV, newV) -> setFont(newV));

		return fontPicker;
	}

	private void setFont(Font newV) {
		configuration.setFont(newV);
		ConfigurationFileHandler.getInstance().saveConfiguration(configuration);
	}

	private void initialiseFullscreenTime() {
		fullscreenTime = new Label("");
		fullscreenTime.setTextFill(Color.RED);
		fullscreenTime.setAlignment(Pos.CENTER);
		fullscreenTime.setTextAlignment(TextAlignment.CENTER);

		AnchorPane.setBottomAnchor(fullscreenTime, 0.0);
		AnchorPane.setRightAnchor(fullscreenTime, 0.0);
		AnchorPane.setLeftAnchor(fullscreenTime, 0.0);
		AnchorPane.setTopAnchor(fullscreenTime, 0.0);

		this.widthProperty().addListener(event -> changeFontSize(fullscreenTime));
		this.heightProperty().addListener(event -> changeFontSize(fullscreenTime));

		ActionListener listener = event -> updateTime();
		Timer t = new Timer(100, listener);
		t.start();
	}

	private void changeFontSize(Label label) {
		Double newFontSizeDouble = Math.hypot(this.getWidth(), this.getHeight()) / 10;
		int newFontSizeInt = newFontSizeDouble.intValue();

		configuration.setFont(new Font(configuration.getFont().getName(), newFontSizeInt));

		label.setFont(configuration.getFont());
	}

	private void updateTime() {
		LocalDateTime time = LocalDateTime.now();
		String timeString = String.format("%02d/%02d/%04d%n%02d:%02d:%02d", time.getDayOfMonth(), time.getMonthValue(),
				time.getYear(), time.getHour(), time.getMinute(), time.getSecond());

		Platform.runLater(() -> fullscreenTime.setText(timeString));
	}

	private Button createMaximiseButton() {
		Button b = new Button("Maximise");
		b.setOnAction(this::maximiseAction);
		return b;
	}

	private Button createCloseButton() {
		Button b = new Button("Close");
		b.setOnAction(this::closeAction);
		return b;
	}

	private void maximiseAction(ActionEvent event) {
		stage.setFullScreen(true);
		switchToFullscreen();
		event.consume();
	}

	private void switchToFullscreen() {
		getChildren().clear();
		updateFullScreenTime();
		getChildren().addAll(fullscreenTime);
		setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private void updateFullScreenTime() {
		fullscreenTime.setFont(configuration.getFont());
		fullscreenTime.setTextFill(configuration.getColor());
	}

	private void switchToControlPanel() {
		getChildren().clear();
		getChildren().addAll(gp);
		setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

	}

	private void closeAction(ActionEvent event) {
		stage.close();
		Platform.exit();
		event.consume();
	}
}
