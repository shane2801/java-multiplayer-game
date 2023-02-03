package view;

import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class HowToScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane howToStack = new StackPane();
	private VBox howToPane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition howToSceneAnimations = new ParallelTransition();
	ParallelTransition howToSceneBackgroundAnimation = new ParallelTransition();

	public HowToScene() {
		super(howToStack);
		
		howToPane = new VBox();
		howToPane.setAlignment(Pos.TOP_CENTER);
		howToPane.setSpacing(40);
		
		createUI();
		createBackground();
		
	}

	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(howToPane, gameTitleImage).play();
		
		
		
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> {WindowManager.mainStage.setScene(WindowManager.startScene); WindowManager.startScene.getUserInterfaceAnimation().play(); howToSceneBackgroundAnimation.stop();});
		
		Image keyboardLayoutImage = new Image(getClass().getResourceAsStream("/guiItems/guiResources/KeyBoardLayout.png"));
		ImageView keyboardLayoutImageView = new ImageView(keyboardLayoutImage); 
		
		howToPane.getChildren().addAll(keyboardLayoutImageView, backButton);
		howToSceneAnimations = sm.swoopNodes(keyboardLayoutImageView, backButton);
	}

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		howToSceneBackgroundAnimation = sm.loopBackground( howToStack, backgroundImage )[0];
		howToStack.getChildren().add(howToPane);
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return howToSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return howToSceneBackgroundAnimation;
	}

}
