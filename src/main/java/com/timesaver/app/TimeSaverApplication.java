package com.timesaver.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class TimeSaverApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		TimeSaverSceneFactory.createAndSetScene(primaryStage);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show();
	}

	
}
