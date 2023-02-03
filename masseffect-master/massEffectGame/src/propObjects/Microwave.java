package propObjects;

import java.awt.Rectangle;

import application.ObjectController;
import javafx.scene.layout.Pane;

/**
 * @author joshua hamilton-brown, kavish muthoora the BlueFridge object is a prop object that is used in gameplay for hiders and seekers to interact with.
 * The microwave prop object contains final variables WIDTH and HEIGHT to set the dimensions in pixels of the prop when drawn on screen.
 * The final String microwaveFilePath designates the file path where the .png file is stored for drawing the prop on screen.
 * The final HEALTH variable is out of a maximum of 100, and then is calculated as a ratio of the size of this prop object to the largest prop object.
 */
public class Microwave extends Prop{
	
	public static final int WIDTH = 57, HEIGHT = 31;
	public static final String microwaveFilePath = "/assets/Microwave.png";
	public static int HEALTH = 100  * (WIDTH*HEIGHT) / (BlueFridge.WIDTH*BlueFridge.HEIGHT);

	/**
	 * @param x - this is the x coordinate parsed to the prop object that sets where the prop object is to be drawn on screen.
	 * @param y - this is the y coordinate parsed to the prop object that sets where the prop object is to be drawn on screen.
	 * @param id - this is the prop object id that is parsed to the prop object that is used in physics, collisions and game logic for prop interactions with other game objects.
	 */
	public Microwave(int x, int y, PropObjectID id, Pane pane, ObjectController objControl) {
		super(x, y, HEALTH,  "/assets/Microwave.png",  WIDTH, HEIGHT, id, pane, objControl);
		this.filePath = getClass().getResource(microwaveFilePath).toString();
		this.height = HEIGHT;
		this.width = WIDTH;
		
	}
	
	/**
	 *The getBounds function for prop object returns a 2D rectangle object with dimensions WIDTH x HEIGHT, the same measurements that are final values in the specific prop objects.
	 * @return The returned 2D rectangle has an x,y coordinate that designates where on screen the rectangle is drawn from.
	 *This returned rectangle is used in physics interactions and collisions in the game logic as the game can use built in Rectangle methods for shape interactions with other game objects.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}