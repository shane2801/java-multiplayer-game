package propObjects;

import java.awt.Rectangle;

import application.ObjectController;
import javafx.scene.layout.Pane;

/**
 * The ErrorProp object is used for testing
 *         purposes, but is kept in the game code for easy debugging purposes,
 *         it operates the same as any other prop, but has a distinctive image
 *         so it is clear when testing.
 *
 */
public class ErrorProp extends Prop {

	public static final int WIDTH = 57, HEIGHT = 57;
	public static final String errorPropFilePath = "/assets/ErrorProp.png";
	public static int HEALTH = 100 * (WIDTH * HEIGHT) / (BlueFridge.WIDTH * BlueFridge.HEIGHT);

	/**
	 * @param x - this is the x coordinate parsed to the prop object that sets where the prop object is to be drawn on screen.
	 * @param y - this is the y coordinate parsed to the prop object that sets where the prop object is to be drawn on screen.
	 * @param id - this is the prop object id that is parsed to the prop object that is used in physics, collisions and game logic for prop interactions with other game objects.
	 */
	public ErrorProp(int x, int y, PropObjectID id, Pane pane, ObjectController objControl) {
		super(x, y, HEALTH,  "/assets/ErrorProp.png", WIDTH, HEIGHT, id, pane, objControl);
		this.filePath = getClass().getResource(errorPropFilePath).toString();
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