package view;

import database.Database;
import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * SignupScene extends @GameScene
 * This class can be created to display a scene in which the user can enter information to sign-up
 * This information will be entered into a database.
 * @author Owain Edwards
 *
 */
public class SignupScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane signupStack = new StackPane();
	private VBox signupPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition signupSceneAnimations = new ParallelTransition();
	ParallelTransition signupSceneBackgroundAnimation = new ParallelTransition();
	
	public static SignupErrorPane signupErrorPane = new SignupErrorPane();
	public static SignupSuccessPane signupSuccessPane = new SignupSuccessPane();

	public SignupScene() {
		super(signupStack);
		
		signupPane = new VBox();
		signupPane.setAlignment(Pos.TOP_CENTER);
		signupPane.setSpacing(15);
		
		createUI();
		createBackground();
	}

	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(signupPane, gameTitleImage).play();
		
		// ui items
		Image firstNameLblImage = new Image(getClass().getResourceAsStream("resources/text/First-Name.png"));
		Label firstNameLbl = new Label();
		firstNameLbl.setGraphic(new ImageView(firstNameLblImage));
		
		TextField firstNameField = new TextField();
		firstNameField.setPrefWidth(400);
		firstNameField.setPrefHeight(50);
		
		Image secondNameLblImage = new Image(getClass().getResourceAsStream("resources/text/Second-Name.png"));
		Label secondNameLbl = new Label();
		secondNameLbl.setGraphic(new ImageView(secondNameLblImage));
		
		TextField secondNameField = new TextField();
		secondNameField.setPrefWidth(400);
		secondNameField.setPrefHeight(50);
		
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
		
		Image confirmPasswordLblImage = new Image(getClass().getResourceAsStream("resources/text/Confirm-Password.png"));
		Label confirmPasswordLbl = new Label();
		confirmPasswordLbl.setGraphic(new ImageView(confirmPasswordLblImage));
		
		PasswordField confirmPasswordField = new PasswordField();
		confirmPasswordField.setPrefWidth(400);
		confirmPasswordField.setPrefHeight(50);
		
		final String createButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Create.png'); -fx-background-size: 224px 62px;";
		GameButtonText createButton = new GameButtonText(createButtonStyle, 224, 62);
		createButton.setOnAction(e -> {
			
			Database dbManager = new Database();
			if(dbManager.createAccount(firstNameField.getText(), secondNameField.getText(), emailField.getText(), passwordField.getText(), confirmPasswordField.getText())) {
				WindowManager.mainStage.setScene(WindowManager.loginScene);
			}
			
			firstNameField.clear();
			secondNameField.clear();
			emailField.clear();
			passwordField.clear();
			confirmPasswordField.clear();
		});	
		
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> { 
			
			WindowManager.mainStage.setScene(WindowManager.loginSignupScene);
			WindowManager.loginSignupScene.getUserInterfaceAnimation().play();
			signupSceneBackgroundAnimation.stop();
			WindowManager.loginSignupScene.getBackgroundAnimation().play();
			
			firstNameField.clear();
			secondNameField.clear();
			emailField.clear();
			passwordField.clear();
			confirmPasswordField.clear();
		
		});
		
		signupPane.getChildren().addAll(firstNameLbl, firstNameField, secondNameLbl, secondNameField, emailLbl, emailField, passwordLbl, passwordField, confirmPasswordLbl, confirmPasswordField, createButton, backButton);
		
		//Animations	
		signupSceneAnimations = sm.swoopNodes(firstNameLbl, firstNameField, secondNameLbl, secondNameField, emailLbl, emailField, passwordLbl, passwordField, confirmPasswordLbl, confirmPasswordField, createButton,backButton);
		signupSceneAnimations.play();	
		
	}

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		signupSceneBackgroundAnimation = sm.loopBackground( signupStack, backgroundImage )[0];
		signupStack.getChildren().addAll(signupPane, signupErrorPane, signupSuccessPane);
	
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return signupSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return signupSceneBackgroundAnimation;
	}

}
