package view;

import database.Database;
import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * LoginScene extends @GameScene
 * This class creates the scene in which the user can login from.
 * @author Owain Edwards
 *
 */
public class LoginScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane loginStack = new StackPane();
	private VBox loginPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition loginSceneAnimations = new ParallelTransition();
	ParallelTransition loginSceneBackgroundAnimation = new ParallelTransition();
	
	public static LoginErrorPane loginErrorPane = new LoginErrorPane();
	
	static SinglePlayerLobbyScene singlePlayerLobbyScene;
	static LobbyScene lobbyScene;

	public LoginScene() {
		super(loginStack);
		
		loginPane = new VBox();
		loginPane.setAlignment(Pos.TOP_CENTER);
		loginPane.setSpacing(15);
		
		createUI();
		createBackground();
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(loginPane, gameTitleImage).play();
		
		//Login Title. // THE IMAGE FILE NEEDS TO BE CHANGED
		Image LOGINLblImage = new Image(getClass().getResourceAsStream("resources/text/LOGIN.png"));
		Label LOGINLbl = new Label();
		LOGINLbl.setGraphic(new ImageView(LOGINLblImage));
		
		//UI
		Image emailLblImage = new Image(getClass().getResourceAsStream("resources/text/Email.png"));
		Label emailLbl = new Label();
		emailLbl.setGraphic(new ImageView(emailLblImage));
		
		TextField emailField = new TextField();
		emailField.setPrefWidth(400);
		emailField.setPrefHeight(50);
		
		Image passwordLblImage = new Image(getClass().getResourceAsStream("resources/text/Password.png"));
		Label passwordLbl = new Label();
		passwordLbl.setGraphic(new ImageView(passwordLblImage));
		
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefWidth(400);
		passwordField.setPrefHeight(50);
		
		final String submitButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Submit.png'); -fx-background-size: 253px 65px;";
		GameButtonText submitButton = new GameButtonText(submitButtonStyle, 253, 65);
		submitButton.setOnAction(e -> {

			Database dbManager = new Database();
			String email = dbManager.login(emailField.getText(), passwordField.getText()); // returned email
			
			if(!email.equals("")) {
				System.out.println(email);
				WindowManager.inGameName = email;
				setMainLogin();
				WindowManager.mainStage.setScene(WindowManager.startScene);
				
				singlePlayerLobbyScene = new SinglePlayerLobbyScene();
				lobbyScene = new LobbyScene();
				
		}
			
			emailField.clear();
			passwordField.clear();
			
		});
		
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> { 
			
			WindowManager.mainStage.setScene(WindowManager.loginSignupScene);
			WindowManager.loginSignupScene.getUserInterfaceAnimation().play();
			loginSceneBackgroundAnimation.stop(); 
			WindowManager.loginSignupScene.getBackgroundAnimation().play();
			
			emailField.clear();
			passwordField.clear();
		
		});
		
		loginPane.getChildren().addAll(LOGINLbl, emailLbl, emailField, passwordLbl, passwordField, submitButton, backButton);
		
		//Animations
		loginSceneAnimations = sm.swoopNodes(LOGINLbl, emailLbl, emailField, passwordLbl, passwordField, submitButton, backButton);
		loginSceneAnimations.play();
		
	}
	/**
	 * creates the scrolling Background for the Scene.
	 */
	@Override
	void createBackground() {

		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);		
		loginSceneBackgroundAnimation = sm.loopBackground( loginStack, backgroundImage )[0];
		loginStack.getChildren().addAll(loginPane, loginErrorPane);
		
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return loginSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return loginSceneBackgroundAnimation;
	}
	
	private void setMainLogin() {
		
		WindowManager.startScene.startPane.getChildren().removeAll(WindowManager.startScene.startButton, WindowManager.startScene.loginButton, WindowManager.startScene.howToButton, WindowManager.startScene.quitButton);
	
		final String loggedInButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Logged-In.png'); -fx-background-size: 360px 78px;";
		GameButtonText loggedInButton = new GameButtonText(loggedInButtonStyle, 360, 78);
		WindowManager.startScene.startSceneAnimations = sm.swoopNodes(WindowManager.startScene.startButton, loggedInButton, WindowManager.startScene.howToButton, WindowManager.startScene.quitButton);
		WindowManager.startScene.getUserInterfaceAnimation().play();
		WindowManager.startScene.startPane.getChildren().addAll(WindowManager.startScene.startButton, loggedInButton, WindowManager.startScene.howToButton, WindowManager.startScene.quitButton);
		
	}

}
