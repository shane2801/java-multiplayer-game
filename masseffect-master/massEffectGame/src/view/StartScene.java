package view;

import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * StartScene extends @GameScene
 * This class is created after the @preScene and gives the user the initial game option state.
 * This allows the user to select whether they wish to edit settings; play the game; quit the game or login and sign-up.
 * @author Owain Edwards
 *
 */
public class StartScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane startStack = new StackPane();
	VBox startPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition startSceneAnimations = new ParallelTransition();
	ParallelTransition startSceneBackgroundAnimation = new ParallelTransition();
	
	//buttons to be seen by Log
	GameButtonText startButton;
	GameButtonText loginButton;
	GameButtonText howToButton;
	GameButtonText quitButton;

	public StartScene() {
		super(startStack);
		
		startPane = new VBox();
		startPane.setAlignment(Pos.TOP_CENTER);
		startPane.setSpacing(40);
		

		createUI();
		createBackground();
	}

	@Override
	void createUI() {
		
		//LostAndFound
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(startPane, gameTitleImage).play();
				
		//Translate Animation.
				
		final String startButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Play.png'); -fx-background-size: 158px 79px;";
		startButton = new GameButtonText(startButtonStyle, 158, 79);
		//startButton.setOnAction(e -> {mStage.setScene(gameSelectScene); selectGameAnimations.play(); gameSelectBackgroundAnimations.play(); mainSceneAnimations.stop();});
		startButton.setOnAction(e -> {WindowManager.mainStage.setScene(WindowManager.gameSelectScene); WindowManager.gameSelectScene.getUserInterfaceAnimation().play(); WindowManager.gameSelectScene.getBackgroundAnimation().play(); startSceneAnimations.stop();});

		final String loginButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Login.png'); -fx-background-size: 474px 78px;";
		loginButton = new GameButtonText(loginButtonStyle, 474, 78);
		loginButton.setOnAction(e -> { WindowManager.mainStage.setScene(WindowManager.loginSignupScene); WindowManager.loginSignupScene.getUserInterfaceAnimation().play(); WindowManager.loginSignupScene.getBackgroundAnimation().play(); startSceneAnimations.stop();});
				
		final String howToButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/How-To.png'); -fx-background-size: 262px 61px;";
		howToButton = new GameButtonText(howToButtonStyle, 262, 61);
		howToButton.setOnAction(e -> {
					
			WindowManager.startScene.getBackgroundAnimation().stop();
			WindowManager.mainStage.setScene(WindowManager.howToScene);
			WindowManager.howToScene.getBackgroundAnimation().play();
			WindowManager.howToScene.getUserInterfaceAnimation().play();
				
		});

//		final String settingsButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Settings.png'); -fx-background-size: 286px 78px;";
//		GameButtonText settingsButton = new GameButtonText(settingsButtonStyle, 286, 78);
//		settingsButton.setOnAction(e -> {WindowManager.startScene.getBackgroundAnimation().stop(); WindowManager.mainStage.setScene(WindowManager.settingsScene);});
				
		final String quitButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Quit.png'); -fx-background-size: 152px 71px;";
		quitButton = new GameButtonText(quitButtonStyle, 152, 71);
		quitButton.setOnAction(e -> System.exit(0));
				 
		// animate ui items. 
		startPane.getChildren().addAll( loginButton, howToButton, quitButton);
		startSceneAnimations = sm.swoopNodes(loginButton, howToButton, quitButton);
		startSceneAnimations.play();
					
		
	}

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		startSceneBackgroundAnimation = sm.loopBackground( startStack, backgroundImage )[0];
		startStack.getChildren().add(startPane);
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return startSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return startSceneBackgroundAnimation;
	}

}
