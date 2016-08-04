/*
 * Copyright (c) 2016, Embraer. All rights reserved. Embraer S/A proprietary/confidential. Use is subject to license terms.
 */

package org.jugvale;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 * @author william
 * @author pedro-hos
 */
public class App extends Application {
	
	private final String AUDIO_URL = getClass().getResource("/ssantos.mp3").toString();
	
	public static void main( final String[] args ) {
		launch(args);
	}
	
	@Override
	public void start( final Stage primaryStage ) throws Exception {
		
		final Group root = new Group();
		final Scene scene = new Scene(root, 500, 200);
		
		final AudioClip clip = new AudioClip(AUDIO_URL);
		clip.play();
		
		// show the stage
		primaryStage.setTitle("Media Player");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
}
