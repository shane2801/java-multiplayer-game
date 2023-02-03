package view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
/**
 * this class contains methods to deal with any operations done by the scenes.
 * This includes animations.
 * @author Owain Edwards
 *
 */
public class SceneManager {
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	/**
	 * @param pane Takes the pane to put the Game logo on.
	 * @param titleImage takes the image to animate - this image will usually be the game logo.
	 * @return @ParallelTransition to be stored by each scene which can then be played to animate the logo.
	 */
	public ParallelTransition scaleGameLogo( Pane pane, Image titleImage ) {
		
		Label gameTitleLbl = new Label();
		gameTitleLbl.setGraphic(new ImageView(titleImage));
		gameTitleLbl.setFont(new Font("Verdana", 150));
		pane.getChildren().add(gameTitleLbl);
		
		final Duration animationTime = Duration.millis(2000);
		ScaleTransition scaleAnimation = new ScaleTransition(animationTime);
		
		scaleAnimation.setByX(-0.20f);
		scaleAnimation.setByY(-0.20f);
		scaleAnimation.setCycleCount(Animation.INDEFINITE);
		scaleAnimation.setAutoReverse(true);
		
		ParallelTransition pt = new ParallelTransition(gameTitleLbl,scaleAnimation);
		pt.setCycleCount(Animation.INDEFINITE);
		
		return pt;
	}
	/**
	 * 
	 * @param stackPane takes the pane in which the background will be made on.
	 * @param backgroundImages takes an Image array of any length of background images. it will then parallax animate these based on order in the array. 
	 * @return @ParallelTransition to be played by the scene.
	 */
	public ParallelTransition[] loopBackground( StackPane stackPane,  Image... backgroundImages ) {

		ParallelTransition pt[] = new ParallelTransition[backgroundImages.length];
		int i = 0;
		
		int startTime = 50000;
			
		for(Image image : backgroundImages) {
			
			ImageView viewOne = new ImageView(image);
			ImageView viewTwo = new ImageView(image);

			stackPane.getChildren().addAll(viewOne, viewTwo);
			
			TranslateTransition translateViewOne = new TranslateTransition(Duration.millis(startTime), viewOne);
			translateViewOne.setFromX(0);
			translateViewOne.setToX(-screenSize.getWidth());
			translateViewOne.setInterpolator(Interpolator.LINEAR);
					
			TranslateTransition translateViewTwo = new TranslateTransition(Duration.millis(startTime), viewTwo);
			translateViewTwo.setFromX(screenSize.getWidth());
			translateViewTwo.setToX(0);
			translateViewTwo.setInterpolator(Interpolator.LINEAR);
			
			ParallelTransition pTransition = new ParallelTransition(translateViewOne, translateViewTwo);
			pTransition.setCycleCount(Animation.INDEFINITE);
			pTransition.play();
			
			pt[i] = pTransition;
			startTime -= 1000;
		}
		
		return pt;
	}
	/**
	 * 
	 * @param nodes takes in an array of GUI nodes of any size node to perform the swoop animation that can be seen when the game begins.
	 * This animate the nodes from the right to the centre of the screen.
	 * @return @ParallelTransition to be played by the scene
	 */
	public ParallelTransition swoopNodes(Node... nodes) {
		
		ParallelTransition pt = new ParallelTransition();
		int startTime;
		
		if(nodes.length < 10) {
			startTime = 1000 - (100 * nodes.length);
			
		}else {
			startTime = 50;
		}
		
		for( Node node : nodes ) {
			
			TranslateTransition translateQuit = new TranslateTransition(Duration.millis(startTime), node);
			translateQuit.setFromX(screenSize.getWidth());
			translateQuit.setToX(0);
			translateQuit.setInterpolator(Interpolator.LINEAR);
			
			pt.getChildren().add(translateQuit);
			
			startTime += 100;
		}
		
		return pt;
				
	}

}
