package guiItems;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class GameButtonBlue extends Button{
	
	//private final String PATH_TO_FONT = "src/guiItems/resources/blue_button01.png";
	private final String PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image; url('/guiItems/guiResources/blue_button01.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 190px 47px;";
	private final String RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/blue_button00.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 190px 47px;";
	private final String UNPRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/blue_button02.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 190px 47px;";

	int buttonWidth = 190;
	int buttonHeight = 49;
	
	public GameButtonBlue(String buttonText) {
		
		setText(buttonText);
		//setFont();
		setPrefWidth(buttonWidth); // image width
		setPrefHeight(buttonHeight); // image height
		setStyle(UNPRESSED_STYLE); // default style.
		
		startButtonListeners();
		
	}
	
	private void setButtonPressedStyle() {
		
		setStyle(PRESSED_STYLE);
		setPrefHeight(buttonHeight);
		
		
	}
	
	private void setButtonReleasedStyle(){
		
		setStyle(RELEASED_STYLE);
		setPrefHeight(buttonHeight);
		
		
		
	}
	
	private void startButtonListeners() {
		
		setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					
					setButtonPressedStyle();
					
				}
				
			}
			
		});
		setOnMouseReleased(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton().equals(MouseButton.PRIMARY)) {
					
					setButtonReleasedStyle();
					
				}
				
			}
			
		});
		setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				setEffect(new DropShadow());
				
			}
			
		});
		setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				
				setEffect(null);
				
			}
			
		});
	}
	
	public int getButtonWidth() {
		return buttonWidth;
	}
	



}
