package view;

import application.Main;
import application.ObjectSpawner;
import guiItems.GameButtonCircle;
import guiItems.GameButtonText;
import guiItems.LobbyPlayer;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * SinglePlayerLobbyScene extends @GameScene
 * This class contains Lots of UI elements to display and separate different operations within the SinglePlayerlobby.
 * You can switch sides; change role and start the game if you're the host.
 * @author Owain Edwards
 *
 */
public class SinglePlayerLobbyScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane singlePlayerLobbyStack = new StackPane();
	private HBox singlePlayerLobbyPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition singlePlayerLobbySceneAnimations = new ParallelTransition();
	ParallelTransition singlePlayerLobbySceneBackgroundAnimation = new ParallelTransition();
	
	static Image chosenSpriteImage;	

	public SinglePlayerLobbyScene() {
		super(singlePlayerLobbyStack);
		
		singlePlayerLobbyPane = new HBox();
		singlePlayerLobbyPane.setAlignment(Pos.CENTER);
		singlePlayerLobbyPane.setSpacing(40);
		
		createUI();
		createBackground();
		
	}

	@Override
	void createUI() {
		
		int[] playersArray = new int[7];//this array is used to store the number of each type of player that the game will have.
		
		// VBox will contain Vboxes of players - each v box = 1 player
		final String menuStyle = "-fx-background-color:rgba(0,0,0,0.5);";
		
		// Left Side of Lobby Menu ---------------------------------------------------------------
		final BorderPane lobbyLeft = new BorderPane();
		lobbyLeft.setPrefWidth((screenSize.getWidth() / 2)  - 100);
		lobbyLeft.setStyle(menuStyle);
		
		Image playerListLblImage = new Image(getClass().getResourceAsStream("resources/text/Player-List.png"));
		Label playerListLbl = new Label();
		playerListLbl.setGraphic(new ImageView(playerListLblImage));
		lobbyLeft.setAlignment(playerListLbl, Pos.CENTER);
		
		VBox centerBorder = new VBox();
		centerBorder.setAlignment(Pos.TOP_CENTER);
		centerBorder.setSpacing(20);
		
		// Seeker Pane ---
		VBox seekerPane = new VBox();
		seekerPane.setAlignment(Pos.TOP_CENTER);
		seekerPane.setSpacing(20);
		
		Image seekerLblImage = new Image(getClass().getResourceAsStream("resources/text/Seekers.png"));
		Label seekerLbl = new Label();
		seekerLbl.setGraphic(new ImageView(seekerLblImage));
		
		seekerPane.getChildren().addAll(seekerLbl);
		
		//Hider Pane ---
		VBox hiderPane = new VBox();
		hiderPane.setAlignment(Pos.TOP_CENTER);
		hiderPane.setSpacing(20);
		
		Image hiderLblImage = new Image(getClass().getResourceAsStream("resources/text/Hiders.png"));
		Label hiderLbl = new Label();
		hiderLbl.setGraphic(new ImageView(hiderLblImage));
		
		hiderPane.getChildren().addAll(hiderLbl);
		
		//test player VBox ---------------------------------------------------------------------------
		LobbyPlayer playable = new LobbyPlayer(WindowManager.inGameName, "No class selected" , "Hider");
		final String playerSlotStyle = "-fx-background-color:rgba(255, 200, 255, 1); -fx-border-color: black; -fx-border-width: 3px;";
		playable.setStyle(playerSlotStyle);
				
		seekerPane.getChildren().addAll(playable);
				
		//ArrayList<VBox> lobbyAI = new ArrayList<VBox>();
		
		centerBorder.getChildren().addAll(seekerPane, hiderPane);
		
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> { WindowManager.mainStage.setScene(WindowManager.gameSelectScene); WindowManager.gameSelectScene.getUserInterfaceAnimation().play();  WindowManager.gameSelectScene.getBackgroundAnimation().play();});
		lobbyLeft.setAlignment(backButton, Pos.CENTER);
		
		// Right Side Of Lobby Menu ---------------------------------------------------------------
		final BorderPane lobbyRight = new BorderPane();
		lobbyRight.setPrefWidth((screenSize.getWidth() / 2)  - 100);
		lobbyRight.setStyle(menuStyle);
		
		Image gameSetupLblImage = new Image(getClass().getResourceAsStream("resources/text/Game-Setup.png"));
		Label gameSetupLbl = new Label();
		gameSetupLbl.setGraphic(new ImageView(gameSetupLblImage));
		
		Image classInfoLblImage = new Image(getClass().getResourceAsStream("resources/text/classInfo.png"));
		Label classInfoLbl = new Label();
		classInfoLbl.setGraphic(new ImageView(classInfoLblImage));
		
		lobbyRight.setAlignment(gameSetupLbl, Pos.CENTER);
		
		VBox playerOptions = new VBox();
		playerOptions.setAlignment(Pos.CENTER);
		playerOptions.setSpacing(20);
		
		// SIDE SELECTION
		Image sideLblImage = new Image(getClass().getResourceAsStream("resources/text/Side.png"));
		Label sideLbl = new Label();
		sideLbl.setGraphic(new ImageView(sideLblImage));
	
		HBox sideSelectPane = new HBox();
		sideSelectPane.setAlignment(Pos.CENTER);
		sideSelectPane.setSpacing(40);
		
		Image selectHiderImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Hider.png"));
		GameButtonCircle selectHider = new GameButtonCircle(selectHiderImage ,70);
		selectHider.setOnMouseClicked(e -> { 
			
			playersArray[0] = 1;
			playersArray[1] = 4;
			playersArray[2] = 0;
			playersArray[3] = 1;
			playersArray[4] = 0;
			playersArray[5] = 0;
			playersArray[6] = 0;
			
			if(!hiderPane.getChildren().contains(playable)) {
				hiderPane.getChildren().add(playable);
			}
		});
		
		Image selectSeekerImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Seeker.png"));
		GameButtonCircle selectSeeker = new GameButtonCircle(selectSeekerImage ,70);
		selectSeeker.setOnMouseClicked(e -> { 
			
			playersArray[0] = 0;
			playersArray[1] = 5;
			playersArray[2] = 1;
			playersArray[3] = 0;
			playersArray[4] = 0;
			playersArray[5] = 0;
			playersArray[6] = 0;
					
			if(!seekerPane.getChildren().contains(playable)) {
				seekerPane.getChildren().add(playable);
			}
		});
				
		sideSelectPane.getChildren().addAll(selectHider, selectSeeker);

		Image classLblImage = new Image(getClass().getResourceAsStream("resources/text/Class.png"));
		Label classLbl = new Label();
		classLbl.setGraphic(new ImageView(classLblImage));
		
		// CLASS SELECTION -----
		HBox classSelectPane = new HBox();
		classSelectPane.setAlignment(Pos.CENTER);
		classSelectPane.setSpacing(40);
		
		VBox selectedCharacterDisplayPane = new VBox();
		selectedCharacterDisplayPane.setAlignment(Pos.CENTER);
		
		Image selectSpeedSeekerImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Speed-Seeker.png"));
		GameButtonCircle selectSpeedSeeker = new GameButtonCircle(selectSpeedSeekerImage ,100);
		selectSpeedSeeker.setOnMouseClicked(e -> { 
			
			playersArray[0] = 0;
			playersArray[1] = 5;
			playersArray[2] = 0;
			playersArray[3] = 0;
			playersArray[4] = 1;
			playersArray[5] = 0;
			playersArray[6] = 0;
			 
			//images / in lobby.
			Image speedSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/speedSeekerSprite.png"));
			ImageView chosenSpriteImageView = new ImageView(speedSeekerImage); 
			
			if(selectedCharacterDisplayPane.getChildren().size() > 0) {
				for(int i = 0; i < selectedCharacterDisplayPane.getChildren().size(); i++) {
					selectedCharacterDisplayPane.getChildren().remove(i);
				}
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
				
			}else {
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
			}
			
			// Updating LobbyPlayer - class role.
			playable.getChildren().remove(1); // 1 will remove the role. (0 is the name / email.)
			Label playerClass = new Label("Speed Seeker Selected"); // new label to replace it.
			playable.getChildren().add(playerClass);

		});
		Image selectHealthSeekerImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Health-Seeker.png"));
		GameButtonCircle selectHealthSeeker = new GameButtonCircle(selectHealthSeekerImage ,100);
		selectHealthSeeker.setOnMouseClicked(e -> { 
			
			playersArray[0] = 0;
			playersArray[1] = 5;
			playersArray[2] = 0;
			playersArray[3] = 0;
			playersArray[4] = 0;
			playersArray[5] = 0;
			playersArray[6] = 1;
			
			Image healthSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/healthSeekerSprite.png"));
			ImageView chosenSpriteImageView = new ImageView(healthSeekerImage); 
			
			if(selectedCharacterDisplayPane.getChildren().size() > 0) {
				for(int i = 0; i < selectedCharacterDisplayPane.getChildren().size(); i++) {
					selectedCharacterDisplayPane.getChildren().remove(i);
				}
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
				
			}else {
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
			}
			
			// Updating LobbyPlayer - class role.
			playable.getChildren().remove(1); // 1 will remove the role. (0 is the name / email.)
			Label playerClass = new Label("Health Seeker Selected"); // new label to replace it.
			playable.getChildren().add(playerClass);
			
			
		});
		Image selectGunSeekerImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Gun-Seeker.png"));
		GameButtonCircle selectGunSeeker = new GameButtonCircle(selectGunSeekerImage ,100);
		selectGunSeeker.setOnMouseClicked(e -> { 
			
			playersArray[0] = 0;
			playersArray[1] = 5;
			playersArray[2] = 0;
			playersArray[3] = 0;
			playersArray[4] = 0;
			playersArray[5] = 1;
			playersArray[6] = 0;
			
			Image gunSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/gunSeekerSprite.png"));
			ImageView chosenSpriteImageView = new ImageView(gunSeekerImage); 
			
			if(selectedCharacterDisplayPane.getChildren().size() > 0) {
				for(int i = 0; i < selectedCharacterDisplayPane.getChildren().size(); i++) {
					selectedCharacterDisplayPane.getChildren().remove(i);
				}
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
				
			}else {
				selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
			}
			
			// Updating LobbyPlayer - class role.
			playable.getChildren().remove(1); // 1 will remove the role. (0 is the name / email.)
			Label playerClass = new Label("Gun Seeker Selected"); // new label to replace it.
			playable.getChildren().add(playerClass);
			
		});
		
		classSelectPane.getChildren().addAll(selectSpeedSeeker, selectHealthSeeker, selectGunSeeker);
		
		if(chosenSpriteImage != null) {
			
		}

		
		playerOptions.getChildren().addAll(sideLbl, sideSelectPane, classLbl, classInfoLbl, classSelectPane, selectedCharacterDisplayPane);
		
		final String startGameButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Start-Game.png'); -fx-background-size: 382px 63px;";
		GameButtonText startGameButton = new GameButtonText(startGameButtonStyle, 382, 63);
		startGameButton.setOnAction(e ->{
			
			WindowManager.mainStage.setScene(WindowManager.singleplayerScene);
			Main.objSpawn = new ObjectSpawner(Main.objControl, "Singleplayer", playersArray);
			
		});
		lobbyRight.setAlignment(startGameButton, Pos.CENTER);
		
		// Margin gap for BorderPane
		Insets insets = new Insets(40);
		
		lobbyLeft.setTop(playerListLbl);
		lobbyLeft.setBottom(backButton);
		lobbyLeft.setCenter(centerBorder);
		BorderPane.setMargin(backButton, insets);
		
		lobbyRight.setTop(gameSetupLbl);
		lobbyRight.setCenter(playerOptions);
		lobbyRight.setBottom(startGameButton);
		BorderPane.setMargin(startGameButton, insets);
		
		singlePlayerLobbyPane.getChildren().addAll(lobbyLeft, lobbyRight);
		
	}

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		singlePlayerLobbySceneBackgroundAnimation = sm.loopBackground( singlePlayerLobbyStack, backgroundImage )[0];
		singlePlayerLobbyStack.getChildren().add(singlePlayerLobbyPane);
		
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return singlePlayerLobbySceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return singlePlayerLobbySceneBackgroundAnimation;
	}

}
