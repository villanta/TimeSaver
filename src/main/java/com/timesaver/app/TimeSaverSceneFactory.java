package com.timesaver.app;

import javafx.stage.Stage;

public class TimeSaverSceneFactory {

	private TimeSaverSceneFactory() {
		
	}
	
	public static void createAndSetScene(Stage stage) {
		TimeSaverPane pane = new TimeSaverPane(stage);
		TimeSaverScene scene = new TimeSaverScene(pane);
		
		stage.setScene(scene);
		pane.initialiseFX();
	}
}
