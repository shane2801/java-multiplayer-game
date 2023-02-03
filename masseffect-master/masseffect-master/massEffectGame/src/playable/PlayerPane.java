package playable;

import java.awt.Rectangle;

import application.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * @author joshua hamilton-brown
 * The player pane acts as the pane used in the javafx scene where all player objects are drawn and animations are played out.
 * This player pane is then overlayed on the single player pane that is used to draw on props and scenery objects, this allows player sprites to appear over these objects in the same window.
 *
 */
public class PlayerPane extends GameObject{
	
	public static Pane root;
	Scene scene;
	
	/**
	 * @param x - this is the x coordinate of where the pane is to be drawn on the game window.
	 * @param y - this is the y coordinate of where the pane is to be drawn on the game window.
	 * @param scene - the scene refers to the JavaFX scene that contains all javafx panes where all game UI objects are drawn along with game objects.
	 */
	public PlayerPane(int x, int y, Scene scene) {
		super(x, y);
		PlayerPane.root = this;
		this.scene = scene;
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
