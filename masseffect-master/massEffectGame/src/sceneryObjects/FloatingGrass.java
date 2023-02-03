package sceneryObjects;

import javafx.scene.layout.Pane;

public class FloatingGrass extends Scenery{
	
	public static final String floatingGroundFilePath = "/assets/FloatingGround.png";
	public static int WIDTH = 960;
	public static int HEIGHT = 540;

	/**
	 * the FloatingGrass class is used when creating an instance of this scenery object in the game. It contains a file url to the png of the scenery object this class represents.
	 * @param x - this is the x coordinate of where the scenery object is to be drawn on screen.
	 * @param y - this is the y coordinate of where the scenery object is to be drawn on screen.
	 * @param id - this is the SceneryID value passed to the GrassGround object for use in collisions.
	 */
	public FloatingGrass(int x, int y, SceneryID id, Pane pane) {
		super(x, y, "/assets/FloatingGrass.png", WIDTH, HEIGHT, id, pane);
		this.filePath = getClass().getResource("/assets/FloatingGrass.png").toString();
		this.height = HEIGHT;
		this.width = WIDTH;		
		
	}
}
