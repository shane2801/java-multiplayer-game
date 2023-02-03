package view;

import javafx.animation.ParallelTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * abstract GameScene extends @Scene this class will take a @Parent as it's root and pass the grabbed screen size to the superclass @Scene.
 * This also forces any Classes extending it to force 4 methods.
 * @author Owain Edwards
 *
 */
public abstract class GameScene extends Scene{
	
	private static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	
	public GameScene(Parent root) {
		super(root, screenSize.getWidth(), screenSize.getHeight());
		
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	abstract void createUI();
	/**
	 * creates the scrolling Background for the Scene.
	 */
	abstract void createBackground();
	/**
	 * 
	 * @return @ParallelTransition to allow the game to animate The GUI
	 */
	abstract ParallelTransition getUserInterfaceAnimation();
	/**
	 * 
	 * @return @ParallelTransition to allow the game to animate the Background.
	 */
	abstract ParallelTransition getBackgroundAnimation();

	
	

}
