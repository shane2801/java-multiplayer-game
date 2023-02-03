package view;

import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * PreScene extends @GameScene
 * This class is the scene that is presented upon launching the game.
 * @author Owain Edwards
 *
 */
public class PreScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane preStack = new StackPane();
	private VBox prePane;
	private SceneManager sm = new SceneManager();
	
	ParallelTransition preSceneAnimations = new ParallelTransition();
	ParallelTransition preSceneBackgroundAnimation = new ParallelTransition();

	public PreScene() {
		super(preStack);
		
		prePane = new VBox();
		prePane.setAlignment(Pos.TOP_CENTER);
		prePane.setSpacing(screenSize.getHeight() - 600);
		
		createUI();
		createBackground();
		
	}

	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(prePane, gameTitleImage).play();
		
		Image pressAnyKeyImage = new Image(getClass().getResourceAsStream("resources/text/pressAnyKey.png"));
		Label pressKeyLbl = new Label();
		pressKeyLbl.setGraphic(new ImageView(pressAnyKeyImage));
		prePane.getChildren().add( pressKeyLbl );
		
	}

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth(), screenSize.getHeight(), false, true);
		
		preSceneBackgroundAnimation = sm.loopBackground( preStack, backgroundImage )[0];
		preStack.getChildren().add( prePane );
		preSceneBackgroundAnimation.play();
		
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		
		return null;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		
		return preSceneBackgroundAnimation;
	}
	
	
	
	

}
