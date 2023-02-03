package view;

import java.io.IOException;
import java.net.Socket;

import application.Main;
import application.ObjectSpawner;
import guiItems.GameButtonCircle;
import guiItems.GameButtonText;
import guiItems.LobbyPlayer;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import networking.Client;
import networking.LobbyManager;
import networking.Server;
/**
 * LobbyScene extends @GameScene
 * This class contains Lots of UI elements to display and separate different operations within the lobby.
 * You can switch sides; change role and start the game if you're the host.
 * @author Owain Edwards
 *
 */
public class LobbyScene extends GameScene {

	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane lobbyStack = new StackPane();
	private HBox lobbyPane;
	private SceneManager sm = new SceneManager();

	ParallelTransition lobbySceneAnimations = new ParallelTransition();
	ParallelTransition lobbySceneBackgroundAnimation = new ParallelTransition();

	// Global for Other Classes to access.
	public static VBox seekerPane = new VBox();
	public static VBox hiderPane = new VBox();
	public static LobbyPlayer playableYou;

	public static boolean isServer, isClient = false;
	public static Client joinGameClient;
	
	public static int[] multiPlayersArray = new int[2];// multiPlayersArray[0]: number of hiders
													   // multiPlayersArray[1] = 0: base seeker, = 1: speed seeker, = 2:
	public LobbyScene() {
		super(lobbyStack);

		lobbyPane = new HBox();
		lobbyPane.setAlignment(Pos.CENTER);
		lobbyPane.setSpacing(40);

		createUI();
		createBackground();

	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	@Override
	void createUI() {
		
		// VBox will contain Vboxes of players - each v box = 1 player
		final String menuStyle = "-fx-background-color:rgba(0,0,0,0.5);";

		// Left Side of Lobby Menu
		// ---------------------------------------------------------------
		final BorderPane lobbyLeft = new BorderPane();
		lobbyLeft.setPrefWidth((screenSize.getWidth() / 2) - 100);
		lobbyLeft.setStyle(menuStyle);

		Image playerListLblImage = new Image(getClass().getResourceAsStream("resources/text/Player-List.png"));
		Label playerListLbl = new Label();
		playerListLbl.setGraphic(new ImageView(playerListLblImage));
		lobbyLeft.setAlignment(playerListLbl, Pos.CENTER);

		VBox centerBorder = new VBox();
		centerBorder.setAlignment(Pos.TOP_CENTER);
		centerBorder.setSpacing(20);

		// Seeker Pane ---
		seekerPane.setAlignment(Pos.TOP_CENTER);
		seekerPane.setSpacing(20);

		Image seekerLblImage = new Image(getClass().getResourceAsStream("resources/text/Seekers.png"));
		Label seekerLbl = new Label();
		seekerLbl.setGraphic(new ImageView(seekerLblImage));

		seekerPane.getChildren().addAll(seekerLbl);

		// Hider Pane ---
		hiderPane.setAlignment(Pos.TOP_CENTER);
		hiderPane.setSpacing(20);

		Image hiderLblImage = new Image(getClass().getResourceAsStream("resources/text/Hiders.png"));
		Label hiderLbl = new Label();
		hiderLbl.setGraphic(new ImageView(hiderLblImage));

		hiderPane.getChildren().addAll(hiderLbl);

		// test player VBox
		// ---------------------------------------------------------------------------
		playableYou = new LobbyPlayer(WindowManager.inGameName, "", "Seeker");
		final String playerSlotStyle = "-fx-background-color:rgba(255, 200, 255, 1); -fx-border-color: black; -fx-border-width: 3px;";
		playableYou.setStyle(playerSlotStyle);

		seekerPane.getChildren().add(playableYou);

		// ArrayList<VBox> lobbyAI = new ArrayList<VBox>();

		centerBorder.getChildren().addAll(seekerPane, hiderPane);

		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> { 
			WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
			WindowManager.gameSelectScene.getUserInterfaceAnimation().play(); 
			WindowManager.gameSelectScene.getBackgroundAnimation().play();
			
			if(isServer) {
				
				try {
					GameSelectScene.server.server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	               
			}
			else if(isClient) {
				try {
					joinGameClient.socket.close();
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		lobbyLeft.setAlignment(backButton, Pos.CENTER);
		// Right Side Of Lobby Menu
		// ---------------------------------------------------------------
		final BorderPane lobbyRight = new BorderPane();
		lobbyRight.setPrefWidth((screenSize.getWidth() / 2) - 100);
		lobbyRight.setStyle(menuStyle);

		Image gameSetupLblImage = new Image(getClass().getResourceAsStream("resources/text/Game-Setup.png"));
		Label gameSetupLbl = new Label();
		gameSetupLbl.setGraphic(new ImageView(gameSetupLblImage));
		lobbyRight.setAlignment(gameSetupLbl, Pos.CENTER);

		VBox playerOptions = new VBox();
		playerOptions.setAlignment(Pos.CENTER);
		playerOptions.setSpacing(40);

		Image sideLblImage = new Image(getClass().getResourceAsStream("resources/text/Side.png"));
		Label sideLbl = new Label();
		sideLbl.setGraphic(new ImageView(sideLblImage));

		// SIDE SELECTION
		HBox sideSelectPane = new HBox();
		sideSelectPane.setAlignment(Pos.CENTER);
		sideSelectPane.setSpacing(40);
		
		VBox selectedCharacterDisplayPane = new VBox();
		selectedCharacterDisplayPane.setAlignment(Pos.CENTER);

		Image selectHiderImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Hider.png"));
		GameButtonCircle selectHider = new GameButtonCircle(selectHiderImage, 70);
		selectHider.setOnMouseClicked(e -> {

			if (!hiderPane.getChildren().contains(playableYou)) {

				playableYou.changeSide("Hider"); // 1 = hider
				hiderPane.getChildren().add(playableYou);
				String[] playerData = new String[3];
				playerData[0] = playableYou.getName();
				playerData[1] = playableYou.getClassRole();
				playerData[2] = playableYou.getSide();

				if (isClient) {
					joinGameClient.messageThread.requestSideChange(playableYou);
				} else if (isServer) {
					LobbyManager lm = new LobbyManager();
					lm.serverRequestSideChange();
				}
				
				Image hiderImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/hiderSprite.png"));
				ImageView chosenSpriteImageView = new ImageView(hiderImage); 
				
				if(selectedCharacterDisplayPane.getChildren().size() > 0) {
					for(int i = 0; i < selectedCharacterDisplayPane.getChildren().size(); i++) {
						selectedCharacterDisplayPane.getChildren().remove(i);
					}
					selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
					
				}else {
					selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
				}

			}
		});

		Image selectSeekerImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/Seeker.png"));
		GameButtonCircle selectSeeker = new GameButtonCircle(selectSeekerImage, 70);
		selectSeeker.setOnMouseClicked(e -> {

			if (!seekerPane.getChildren().contains(playableYou)) {

				playableYou.changeSide("Seeker"); // 0 = seeker;
				seekerPane.getChildren().add(playableYou);

				String[] playerData = new String[3];
				playerData[0] = playableYou.getName();
				playerData[1] = playableYou.getClassRole();
				playerData[2] = playableYou.getSide();

				if (isClient) {
					joinGameClient.messageThread.requestSideChange(playableYou);
				} else if (isServer) {
					LobbyManager lm = new LobbyManager();
					lm.serverRequestSideChange();
				}
				
				Image seekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/baseSeekerSprite.png"));
				ImageView chosenSpriteImageView = new ImageView(seekerImage); 
				
				if(selectedCharacterDisplayPane.getChildren().size() > 0) {
					for(int i = 0; i < selectedCharacterDisplayPane.getChildren().size(); i++) {
						selectedCharacterDisplayPane.getChildren().remove(i);
					}
					selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
					
				}else {
					selectedCharacterDisplayPane.getChildren().add(chosenSpriteImageView);
				}
			}
		});

		sideSelectPane.getChildren().addAll(selectHider, selectSeeker);

		Image classLblImage = new Image(getClass().getResourceAsStream("resources/text/Class.png"));
		Label classLbl = new Label();
		classLbl.setGraphic(new ImageView(classLblImage));

		// CLASS SELECTION ----- Not used in multiplayer.
//		HBox classSelectPane = new HBox();
//		classSelectPane.setAlignment(Pos.CENTER);
//		classSelectPane.setSpacing(40);

		Image selectSpeedSeekerImage = new Image(
				getClass().getResourceAsStream("/guiItems/guiResources/Invisible-Seeker.png"));
		GameButtonCircle selectSpeedSeeker = new GameButtonCircle(selectSpeedSeekerImage, 100);
		selectSpeedSeeker.setOnMouseClicked(e -> {

		});
		Image selectHealthSeekerImage = new Image(
				getClass().getResourceAsStream("/guiItems/guiResources/Health-Seeker.png"));
		GameButtonCircle selectHealthSeeker = new GameButtonCircle(selectHealthSeekerImage, 100);
		selectHealthSeeker.setOnMouseClicked(e -> {

		});
		Image selectRadarSeekerImage = new Image(
				getClass().getResourceAsStream("/guiItems/guiResources/Radar-Seeker.png"));
		GameButtonCircle selectRadarSeeker = new GameButtonCircle(selectRadarSeekerImage, 100);
		selectRadarSeeker.setOnMouseClicked(e -> {

		});

		//classSelectPane.getChildren().addAll(selectSpeedSeeker, selectHealthSeeker, selectRadarSeeker);

		playerOptions.getChildren().addAll(sideLbl, sideSelectPane, selectedCharacterDisplayPane);

		final String startGameButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Start-Game.png'); -fx-background-size: 382px 63px;";
		GameButtonText startGameButton = new GameButtonText(startGameButtonStyle, 382, 63);
		startGameButton.setOnAction(e -> {


			if(seekerPane.getChildren().size() -1 == 1 ){
                if (LobbyScene.isServer) {

                	
                	
                    multiPlayersArray[0] = (hiderPane.getChildren().size() - 1) + (seekerPane.getChildren().size() - 1);
                    WindowManager.mainStage.setScene(WindowManager.multiplayerScene);
                    
                    LobbyManager lm = new LobbyManager();

                    for (Socket sc : Server.socketList) {

                        Server.serverMessage.setClientName("STARTGAME");
                        lm.send(sc, Server.serverMessage);

                    }
                    
                    Main.objSpawn = new ObjectSpawner(Main.objControl, "Multiplayer", multiPlayersArray);
	
			    }

            }else{
                
            	SeekerErrorPane seekerErrorPane = new SeekerErrorPane();
            	lobbyStack.getChildren().add(seekerErrorPane);
            	seekerErrorPane.setVisible(true);
            	
            }

		});
		
		lobbyRight.setAlignment(startGameButton, Pos.CENTER);
		// MARGIN GAP FOR BORDER PANE
		Insets insets = new Insets(40);

		lobbyLeft.setTop(playerListLbl);
		lobbyLeft.setBottom(backButton);
		lobbyLeft.setCenter(centerBorder);
		BorderPane.setMargin(backButton, insets);

		lobbyRight.setTop(gameSetupLbl);
		lobbyRight.setCenter(playerOptions);
		lobbyRight.setBottom(startGameButton);
		lobbyPane.getChildren().addAll(lobbyLeft, lobbyRight);
		BorderPane.setMargin(startGameButton, insets);

	}
	/**
	 * creates the scrolling Background for the Scene.
	 */
	@Override
	void createBackground() {

		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth() * 2,
				screenSize.getHeight(), false, true);
		lobbySceneBackgroundAnimation = sm.loopBackground(lobbyStack, backgroundImage)[0];
		lobbyStack.getChildren().add(lobbyPane);

	}
	/**
	 * This adds the @LobbyPlayer passed as a parameter to the seekerPane. if not already added.
	 * @param lp takes @LobbyPlayer 
	 */
	public static void addToSeekerPane(LobbyPlayer lp) {

		if (!seekerPane.getChildren().contains(lp)) {
			seekerPane.getChildren().add(lp);
		}
	}
	/**
	 * This adds the @LobbyPlayer passed as a parameter to the hiderPane. if not already added.
	 * @param lp takes @LobbyPlayer 
	 */
	public static void addToHiderPane(LobbyPlayer lp) {
		if (!hiderPane.getChildren().contains(lp)) {
			hiderPane.getChildren().add(lp);
		}
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return lobbySceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return lobbySceneBackgroundAnimation;
	}

}
