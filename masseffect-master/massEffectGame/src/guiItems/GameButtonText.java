package guiItems;

import java.awt.GraphicsDevice;
import java.io.File;

import audio.AudioButtonManager;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;

/**
 * GameButtonText is a custom button that looks like any image that you use.
 * However this is only used with "text images"
 * Text images are just images that are text - examples of what these images look like
 * can be found in the package (view/resources/text)
 * @author Owain Edwards
 *
 */
public class GameButtonText extends Button{
	
	private int buttonWidth, buttonHeight;
	
	private AudioButtonManager abm = new  AudioButtonManager();
	
	/**
	 * 
	 * @param style takes in the string CSS which will determine the style of the button
	 * @param width takes in width of the image for the button to be made the same width as the image to stop distortion. 
	 * @param height takes in height of the image for the button to be made the same height as the image to stop distortion. 
	 */
	public GameButtonText(String style, int width, int height) {
	
		buttonWidth = width;
		buttonHeight = height;
		
		setPrefWidth(buttonWidth); // image width
		setPrefHeight(buttonHeight); // image height
		setStyle(style); // default style.
		
		startButtonListeners();
		
		Scale scale = new Scale(1, 1);
		scale.setPivotX(0);
		scale.setPivotY(0);
		
	}
	
	/**
	 * This allows the customText to be interacted with like a button.
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
	
	/**
	 * gets the button width
	 * @return buttonWidth 
	 */
	public int getButtonWidth() {
		return buttonWidth;
	}
	





}
