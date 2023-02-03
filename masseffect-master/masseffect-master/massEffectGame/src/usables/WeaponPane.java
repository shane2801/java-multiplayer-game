package usables;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * @author joshua hamilton-brown
 * The weapon pane acts as the stackpane used in the javafx scene where all weapon objects are drawn and animations are played out.
 * This weapon pane is then overlayed on the single player pane that is used to draw on props and scenery objects, this allows player sprites to appear over these objects in the same window.
 *
 */
public class WeaponPane extends Pane{
	
	public static Pane root;
	Scene scene;
	
	/**
	 * @param x - this is the x coordinate of where the weapon pane is to be drawn on the game window.
	 * @param y - this is the y coordinate of where the weapon pane is to be drawn on the game window.
	 * @param scene - the scene refers to the JavaFX scene that contains all javafx panes where all game UI objects are drawn along with game objects.
	 */
	public WeaponPane(int x, int y, Scene scene) {
		
		WeaponPane.root = this;
		this.scene = scene;
		
	}

}
