package guiItems;

import audio.AudioButtonManager;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * GameButtonCircle is a custom button made with some text and a javaFX Circle.
 * It Does this by using a StackPane to display the Text on top of the Circle.
 * @author Owain Edwards
 *
 */
public class GameButtonCircle extends StackPane{
	
	private Image image;
	private float size;
	
	private AudioButtonManager abm = new  AudioButtonManager();
	
	/**
	 * 
	 * @param image takes the image of the text being used for the button that stacks on top of the button.
	 * @param size  takes the size you wish to make the button.
	 */
	public GameButtonCircle(Image image, float size) {
		
		this.image = image;
		this.size = size;
		
		startButtonListeners();
		
		createStack();
		
	}
	
	/**
	 * This will create the StackPane in which the text is put on top of the Circle for the button.
	 */
	public void createStack() {
		
		Circle circleButton =  new Circle(600.0f, 600.0f, size);
		circleButton.setFill(Color.WHITE);
		circleButton.setStroke(Color.BLACK);
		circleButton.setStrokeWidth(3);
		
		Label circleLbl = new Label();
		circleLbl.setGraphic(new ImageView(image));
		
		getChildren().addAll(circleButton, circleLbl);
		
	}
	/**
	 * This allows the Circle to be interacted with like a button.
	 * Allowing actions to be done when a mouse presses it;
	 * Applying a shadow border when hovering and
	 * the ability to perform actions on mouse release and exit.
	 */
	private void startButtonListeners() {
		
		setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					
					abm.playClick();
					
				}
				
			}
			
		});
		setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					
					//setButtonReleasedStyle();
					
				}
				
			}
			
		});
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				setEffect(new DropShadow());
				abm.playHover();
				
			}
			
		});
		setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				setEffect(null);
				
			}
			
		});
	}

}
