package view;

import application.Main;
import guiItems.GameButtonCircle;
import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import networking.Client;
import networking.Server;
/**
 * GameSelectScene extends @GameScene
 * This class contains buttons to select how you wish to play the game. ("SinglePLayer", "Join a Game", "Host a Game ")
 * @author Owain Edwards
 *
 */
public class GameSelectScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane gameSelectStack = new StackPane();
	private VBox gameSelectPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition gameSelectSceneAnimations = new ParallelTransition();
	ParallelTransition gameSelectSceneBackgroundAnimation = new ParallelTransition();
	
	public static ConnectionLostPane connectionLostPane = new ConnectionLostPane();
	public static Server server;
	
	public GameSelectScene() {
		super(gameSelectStack);
		
		gameSelectPane = new VBox();
		gameSelectPane.setAlignment(Pos.TOP_CENTER);
		gameSelectPane.setSpacing(40);
		
		createUI();
		createBackground();
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png")); // Change Game title animation into class aswell like the buttons.
		sm.scaleGameLogo(gameSelectPane, gameTitleImage).play();
		
		//Single Player button
		Image singleGameImage = new Image(getClass().getResourceAsStream("resources/text/Single-Player.png"));
		GameButtonCircle singleGameCircle = new GameButtonCircle(singleGameImage, 100.0f);
		gameSelectPane.getChildren().add(singleGameCircle);

		singleGameCircle.setOnMouseClicked(e -> {
			WindowManager.mainStage.setScene(LoginScene.singlePlayerLobbyScene);
			gameSelectSceneBackgroundAnimation.stop();
			WindowManager.setSingleplayerUI(); });
		
		//join button
		Image joinGameImage = new Image(getClass().getResourceAsStream("resources/text/Join-a-game.png"));
		GameButtonCircle joinGameCircle = new GameButtonCircle(joinGameImage, 100.0f);
		gameSelectPane.getChildren().add(joinGameCircle);
		joinGameCircle.setOnMouseClicked(e -> {
			
			WindowManager.mainStage.setScene(LoginScene.lobbyScene);
			String[] playableYouData = new String[3];
			playableYouData[0] = LobbyScene.playableYou.getName();
			playableYouData[1] = LobbyScene.playableYou.getClassRole();
			playableYouData[2] = LobbyScene.playableYou.getSide();
			
			//Client joinGameClient = new Client("25.66.38.192", 5001, inGameName); -> moved to global
			LobbyScene.joinGameClient = new Client("localhost", 5001, WindowManager.inGameName, Main.objControl);
			LobbyScene.joinGameClient.start();
			
			Server.lobbyPlayers.add(LobbyScene.playableYou);
			LobbyScene.joinGameClient.messageThread.getInitialData(playableYouData, Main.clientGameObjMsg);
			
			LobbyScene.isClient = true;
			
		});
		
		//host button
		Image hostGameImage = new Image(getClass().getResourceAsStream("resources/text/Host-a-game.png"));
		GameButtonCircle hostGameCircle = new GameButtonCircle(hostGameImage, 100.0f);
		gameSelectPane.getChildren().add(hostGameCircle);
		hostGameCircle.setOnMouseClicked(e -> {
			
			WindowManager.mainStage.setScene(LoginScene.lobbyScene); 
			WindowManager.gameSelectScene.getBackgroundAnimation().stop();
			
			server = new Server(5001, Main.objControl); // port number as parameter.
			server.start();
			
			Server.lobbyPlayers.add(LobbyScene.playableYou); // server host lobbyPlayer.
			
			LobbyScene.isServer = true; // if server side then we will do something different for changing sides.
			
			
		});
		
		//Back Button
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		gameSelectPane.getChildren().add(backButton);
		backButton.setOnAction(e -> {WindowManager.mainStage.setScene(WindowManager.startScene); WindowManager.startScene.getUserInterfaceAnimation().play(); gameSelectSceneBackgroundAnimation.stop();});
		
		gameSelectSceneAnimations = sm.swoopNodes( singleGameCircle, joinGameCircle, hostGameCircle, backButton);
		gameSelectSceneAnimations.play();
		
	}
	/**
	 * creates the scrolling Background for the Scene.
	 */
	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		gameSelectSceneBackgroundAnimation = sm.loopBackground( gameSelectStack, backgroundImage )[0];
		gameSelectStack.getChildren().addAll(gameSelectPane, connectionLostPane);
	}
	
	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return gameSelectSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return gameSelectSceneBackgroundAnimation;
	}

}
