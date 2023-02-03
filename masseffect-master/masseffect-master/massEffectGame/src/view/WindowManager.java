package view;

import java.util.ArrayList;

import audio.MusicManager;
import database.Database;

import guiItems.GameButtonText;

import javafx.event.EventHandler;

import javafx.geometry.Rectangle2D;

import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import playable.*;
import usables.WeaponPane;

/**
 * This class contains the mainStage in which the game operates from. This also
 * contains the scenes in which the playable game is used on. this includes the
 * singleplayer scenes and the multiplayer scenes.
 * 
 * @author Owain Edwards
 *
 */
public class WindowManager {

	public static ArrayList<String[]> data = new ArrayList<String[]>();

	public static String inGameName;

	


	private static Pane currentPane;
	MusicManager mm = new MusicManager();
	Database dbManager = new Database();

	// MAIN SCENE BUTTONS -- COME TO THIS WITH DATABASE LOGIN
	GameButtonText startButton;
	GameButtonText settingsButton;
	GameButtonText loginButton;
	GameButtonText quitButton;

	// UI CREATION

	public static Stage mainStage;

	public static AnchorPane singleplayerAnchorPane;
	public static Scene singleplayerScene;

	public static AnchorPane multiplayerAnchorPane;
	public static Scene multiplayerScene;

	public static StackPane singleplayerStack = new StackPane();
	public static PlayerPane singleplayerPlayerPane = new PlayerPane(0, 0, singleplayerScene);
	public static WeaponPane singleplayerWeaponPane = new WeaponPane(0, 0, singleplayerScene);

	public static StackPane multiplayerStack = new StackPane();
	public static PlayerPane multiplayerPlayerPane = new PlayerPane(0, 0, multiplayerScene);
	public static WeaponPane multiplayerWeaponPane = new WeaponPane(0, 0, multiplayerScene);

	private Rectangle2D screenSize;

	// New Scene Classes deceleration.
	PreScene preScene;
	static StartScene startScene;
	public static GameSelectScene gameSelectScene;
	static LoginSignupScene loginSignupScene;
	static LoginScene loginScene;
	static SignupScene signupScene;
	static SettingsScene settingsScene;
	static HowToScene howToScene;
	// OverLay Pane deceleration
	public static PausePane singleplayerPausePane;
	public static GameOverPane singleplayerGameOverPane;
	public static BlindSeekerPane singleplayerBlindSeekerPane;

	public static PausePane multiplayerPausePane;
	public static GameOverPane multiplayerGameOverPane;
	public static BlindSeekerPane multiplayerBlindSeekerPane;

	public WindowManager() {

		screenSize = Screen.getPrimary().getVisualBounds();

		// scenes
		preScene = new PreScene();
		startScene = new StartScene();
		gameSelectScene = new GameSelectScene();
		loginSignupScene = new LoginSignupScene();
		loginScene = new LoginScene();
		signupScene = new SignupScene();
		settingsScene = new SettingsScene();
		howToScene = new HowToScene();
		// overlay panes
		singleplayerPausePane = new PausePane();
		singleplayerGameOverPane = new GameOverPane();
		singleplayerBlindSeekerPane = new BlindSeekerPane();

		multiplayerPausePane = new PausePane();
		multiplayerGameOverPane = new GameOverPane();
		multiplayerBlindSeekerPane = new BlindSeekerPane();

		preScene.setOnKeyPressed(e -> {
			mainStage.setScene(startScene); // THIS WILL HAVE TO go in window manager
			startScene.getUserInterfaceAnimation().play();
			startScene.getBackgroundAnimation().play();
			preScene.getBackgroundAnimation().stop();
<<<<<<< HEAD
			mm.playMenuSong();
=======
			//mm.playMenuSong();
	
		});
		
		singleplayerAnchorPane = new AnchorPane();
		singleplayerScene = new Scene(singleplayerStack, screenSize.getWidth(), screenSize.getHeight() );
		
		multiplayerAnchorPane = new AnchorPane();
		multiplayerScene = new Scene(multiplayerStack, screenSize.getWidth(), screenSize.getHeight() );
		
>>>>>>> refs/remotes/origin/Audio2

		});

		singleplayerAnchorPane = new AnchorPane();
		singleplayerScene = new Scene(singleplayerStack, screenSize.getWidth(), screenSize.getHeight());

		multiplayerAnchorPane = new AnchorPane();
		multiplayerScene = new Scene(multiplayerStack, screenSize.getWidth(), screenSize.getHeight());

		// main stage window
		mainStage = new Stage();
		mainStage.setScene(preScene);
		mainStage.setMaximized(true);

		Scale scale = new Scale(1, 1);
		scale.xProperty().bind(mainStage.widthProperty().divide(screenSize.getWidth()));
		scale.yProperty().bind(mainStage.heightProperty().divide(screenSize.getHeight()));

		setBackground(singleplayerStack);
		singleplayerStack.getChildren().add(singleplayerAnchorPane);
		singleplayerStack.getChildren().add(singleplayerPlayerPane);
		singleplayerStack.getChildren().add(singleplayerWeaponPane);
		singleplayerStack.getChildren().add(singleplayerPausePane);
		singleplayerStack.getChildren().add(singleplayerGameOverPane);
		singleplayerStack.getChildren().add(singleplayerBlindSeekerPane);

		setBackground(multiplayerStack);
		multiplayerStack.getChildren().add(multiplayerAnchorPane);
		multiplayerStack.getChildren().add(multiplayerPlayerPane);
		multiplayerStack.getChildren().add(multiplayerWeaponPane);
		multiplayerStack.getChildren().add(multiplayerPausePane);
		multiplayerStack.getChildren().add(multiplayerGameOverPane);
		multiplayerStack.getChildren().add(multiplayerBlindSeekerPane);

	}

	/**
	 * @return mainStage
	 */
	public Stage getStartStage() {
		return mainStage;
	}

	/**
	 * Sets the Buttons and Other UI for the SinglePlayer UI - this includes
	 * pausing.
	 */
	static void setSingleplayerUI() {

		singleplayerPausePane.setVisible(false);
		singleplayerGameOverPane.setVisible(false);

		singleplayerScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				if (key == KeyCode.ESCAPE && !singleplayerPausePane.isVisible()) {
					singleplayerPausePane.setVisible(true);

				} else if (key == KeyCode.ESCAPE && singleplayerPausePane.isVisible()) {
					singleplayerPausePane.setVisible(false);

				}
			}

		});
	}

	// SINGLE PLAYER SCENE
	private void setBackground(StackPane stack) {

		SceneManager sm = new SceneManager();

		Image skyLayer = new Image("view/resources/backgrounds/castle/layer07_Sky.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);
		Image rocksLayer = new Image("view/resources/backgrounds/castle/layer06_Rocks.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);
		Image hillsLayer = new Image("view/resources/backgrounds/castle/layer05_Hills.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);
		Image cloudsLayer = new Image("view/resources/backgrounds/castle/layer04_Clouds.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);
		Image castleLayer = new Image("view/resources/backgrounds/castle/layer03_Hills_Castle.png",
				screenSize.getWidth(), screenSize.getHeight(), false, true);
		Image treesLayer = new Image("view/resources/backgrounds/castle/layer02_Trees_rocks.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);
		Image groundLayer = new Image("view/resources/backgrounds/castle/layer01_Ground.png", screenSize.getWidth(),
				screenSize.getHeight(), false, true);

		sm.loopBackground(stack, skyLayer, rocksLayer, hillsLayer, cloudsLayer, castleLayer, treesLayer, groundLayer);

	}

	// below return an array of Parallel Transition in future ?
	/**
	 * 
	 * @return currentPane
	 */
	public static Pane getPane() {
		return currentPane;
	}

	/**
	 * sets the currentPane to the passed parameter
	 * 
	 * @param @pane to be set.
	 */
	public void setPane(Pane pane) {
		this.currentPane = pane;
	}

	/**
	 * 
	 * @return mainStage
	 */
	public static Stage getStage() {
		return mainStage;
	}

	/**
	 * sets the mainstage to a passed in stage.
	 * 
	 * @param stage takes @Stage to be set.
	 */
	public void setStage(Stage stage) {
		this.mainStage = stage;
	}

}
