package application;

import java.net.Socket;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import networking.*;
import view.*;

/**
 * @author joshua hamilton-brown the main class is used to start the javafx thread that is used to display the game in a window on the screen.
 * The update method is called every game tick so that all game objects can have their actions run and game logic run.
 *
 */
public class Main extends Application {

	public static final int PIXELS_WIDTH = 1920;
	public static final int PIXELS_HEIGHT = 1080;
	public static boolean running = false;
	
	public static boolean propsAndSceneryRendered = false;
	public static boolean hidersRendered = false;
	public static boolean seekersRendered = false;


	public static ObjectController objControl = new ObjectController();
	public static ObjectSpawner objSpawn = null;
	public static ObjectController clientObjControl = new ObjectController();
	public static ObjectSpawner clientObjSpawn = null;
	
	public static GameObjectMessage clientGameObjMsg = new GameObjectMessage(objControl);
		
	WindowManager wm = new WindowManager();

	/**
	 * the start method is called by the game to start the internal game clock that repeats continuosly so functions can be called during gameplay.
	 * It builds an instance of the window manager, the class that handles all GUI components.
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {

			primaryStage = wm.getStartStage();
			primaryStage.setResizable(true);

			primaryStage.setMaximized(true);

			primaryStage.show();

			running = true;

			AnimationTimer timer = new AnimationTimer() {
				public void handle(long currentNanoTime) {
					update();

				}
			};
			

			timer.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The update method is called every game tick to run game logic and handle all game components.
	 * When escape is pressed the pause pane is made visible, and game updating is paused until the pane is made invisible so gameplay can resume.
	 * The update method is also stopped from running when the game over pane is visible
	 * 
	 * With networking the update method is also used to send the individual instances game states to the server, then the server sends identical messages
	 * to each instance connected to a port.
	 */
	public void update() {
		if (WindowManager.mainStage.getScene().equals(WindowManager.singleplayerScene)) {

			if (!WindowManager.singleplayerPausePane.isVisible() && !WindowManager.singleplayerGameOverPane.isVisible()) {
				objControl.update();
				objSpawn.update();
			}
		}
		if(WindowManager.mainStage.getScene().equals(WindowManager.multiplayerScene)) {
			if (!WindowManager.multiplayerPausePane.isVisible() && !WindowManager.multiplayerGameOverPane.isVisible()) {
				if (LobbyScene.isServer) {
					objControl.update();
					objSpawn.update();
					for(Socket sc : Server.socketList) {
						Server.serverGameObjMsg[0] = new GameObjectMessage(objControl);
			    		Server.send(sc, Server.serverMessage);
			    	}
				} else if(LobbyScene.isClient) {
					clientObjControl.update();
					clientObjSpawn.update();
					LobbyScene.joinGameClient.messageThread.updateServer(clientObjControl);
				} 
			}
		}
	}

	/**
	 * @param minimum  - the minimum value that the variable is to be clamped to.
	 * @param maximum  - the maximum value that the variable is to be clamped to.
	 * @param variable - the input variable that is to be clamped so that it remains
	 *                 within a set range defined by the minimum and maximum values.
	 * @return - the variable returned is set within the range if it was to exceed a
	 *         minimum or maximum
	 */
	public static float clampVariable(float minimum, float maximum, float variable) {
		if (variable >= maximum) {
			return variable = maximum;
		} else if (variable <= minimum) {
			return variable = minimum;
		} else {
			return variable;
		}
	}

	public static void main(String[] args) {// this function stays at bottom.
		launch(args);
	}

}
