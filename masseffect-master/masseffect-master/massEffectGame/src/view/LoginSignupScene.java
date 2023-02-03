package view;

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
/**
 * LoginSignupPane extends @GameScene
 * This class displays a scene with options to log in or sign up.
 * @author Owain Edwards
 *
 */
public class LoginSignupScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane loginSignupStack = new StackPane();
	private VBox loginSignupPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition loginSignupSceneAnimations = new ParallelTransition();
	ParallelTransition loginSignupSceneBackgroundAnimation = new ParallelTransition();

	public LoginSignupScene() {
		super(loginSignupStack);
		
		//SIgnUpLogin Scene
		loginSignupPane = new VBox();
		loginSignupPane.setAlignment(Pos.TOP_CENTER);
		loginSignupPane.setSpacing(40);
		
		createUI();
		createBackground();
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(loginSignupPane, gameTitleImage).play();
		
		//Buttons
		final String loginButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/LoginTwo.png'); -fx-background-size: 200px 78px;";
		GameButtonText loginButton = new GameButtonText(loginButtonStyle, 200, 78);
		loginSignupPane.getChildren().add(loginButton);
		loginButton.setOnAction(e -> { WindowManager.mainStage.setScene(WindowManager.loginScene); WindowManager.loginScene.getUserInterfaceAnimation().play(); loginSignupSceneBackgroundAnimation.stop(); WindowManager.loginScene.getBackgroundAnimation().play();});
		
		final String signupButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Sign-Up.png'); -fx-background-size: 277px 78px;";
		GameButtonText signupButton = new GameButtonText(signupButtonStyle, 277, 78);
		loginSignupPane.getChildren().add(signupButton);
		signupButton.setOnAction(e -> {WindowManager.mainStage.setScene(WindowManager.signupScene); WindowManager.signupScene.getUserInterfaceAnimation().play(); loginSignupSceneBackgroundAnimation.stop(); WindowManager.signupScene.getBackgroundAnimation().play();});
	
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		loginSignupPane.getChildren().add(backButton);
		backButton.setOnAction(e -> { WindowManager.mainStage.setScene(WindowManager.startScene); WindowManager.startScene.getUserInterfaceAnimation().play(); loginSignupSceneBackgroundAnimation.stop(); });
		
		//ANIMATION		
		loginSignupSceneAnimations = sm.swoopNodes(loginButton, signupButton, backButton);
		loginSignupSceneAnimations.play();
		
	}
	/**
	 * creates the scrolling Background for the Scene.
	 */
	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);		
		loginSignupSceneBackgroundAnimation = sm.loopBackground( loginSignupStack, backgroundImage )[0];
		loginSignupStack.getChildren().add(loginSignupPane);
		
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return loginSignupSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return loginSignupSceneBackgroundAnimation;
	}

}
