package guiItems;

import java.io.Serializable;

import audio.AudioButtonManager;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

/**
 * LobbyPlayer is a custom UI element which implements VBox.
 * This allows me to display a "LobbyPlayer" to represent a player in the lobby
 * This custom UI element will allow the lobby to have player's names, Roles, and side displayed. 
 * @author Owain Edwards
 *
 */
public class LobbyPlayer extends VBox{
	
	private static final long serialVersionUID = 1L;
	String name, role, side;
	Label playerName;
	Label playerClass;
	
	private AudioButtonManager abm = new  AudioButtonManager();
	/**
	 * 
	 * @param aiName takes the name of the player to be displayed in the UI element.
	 * @param classRole takes the role of the player to be displayed in the UI element. 
	 * @param side takes the side of the player to be displayed in the UI element.
	 */
	public LobbyPlayer( String aiName, String classRole, String side ) {
		
		name = aiName;
		role = classRole;
		this.side = side;
		
		makePlayer();
		startListeners();
	}
	
	/**
	 * makePlayer() "builds" or creates the GUI element that will be displayed in the lobby. 
	 * This uses a style and Labels to be displayed in the GUI element. 
	 * This also sets spacing, alignment and size of the element.
	 */
	public void makePlayer() {
		
		Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
		final String playerSlotStyle = "-fx-background-color:rgba(255, 255, 255, 1); -fx-border-color: black; -fx-border-width: 3px;";

		Label playerName = new Label(name);
		Label playerClass = new Label(role);
		
		setStyle(playerSlotStyle);
		setPrefHeight(screenSize.getHeight() / 20);
		setAlignment(Pos.CENTER);
		setSpacing(20);
		getChildren().addAll(playerName, playerClass);
	}
	/**
	 * This allows the GUI element to be interacted with like a button.
	 * Applying a shadow border when hovering and playing audio.
	 * the ability to perform actions on mouse exit.
	 */
	private void startListeners() {
		
		
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
	 * This can be "Hider" or "Seeker"
	 * @param sideChange takes the String of which side the player wishes to change to
	 */
	public void changeSide(String sideChange) {
		side = sideChange;
	}
	/** This will be their email or for AI - any string.
	 * @return the name of the player (UI Element)
	 */
	public String getName() {
		return name;
	}
	/** This can be selected based on 3 options: ("Speed Seeker", "Health Seeker", "Radar Seeker")
	 * @return the role of the player
	 */
	public String getClassRole() {
		return role;
	}
	/**
	 * 
	 * @return the side of the player
	 */
	public String getSide() {
		return side;
	}
	/**
	 * produces: ("Name: " + name + " Role: " + role + " Side: " + side;)
	 */
	@Override
	public String toString() {
		return "Name: " + name + " Role: " + role + " Side: " + side;
	}
	/**
	 * 
	 * @return the array representation of the GUI element's data. 
	 */
	public String[] toStringArray() {
		
		String[] array = new String[3];
		array[0] = name;
		array[1] = role;
		array[2] = side;
		
		return array;
	}
}
