package sceneryObjects;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Scenery{
	
	protected int x, y;
	protected int width, height;
	protected ImageView img;
	protected String filePath;
	protected SceneryID id;
	private Pane pane;
	
	/**
	 * @param x - this is the x coordinate value for where the scenery object is to be drawn on screen.
	 * @param y - this is the y coordinate value for where the scenery object is to be drawn on screen.
	 * @param filePath_ - this is the file url passed as a string that is loaded from the games asset files and then displayed in the games JavaFX pane.
	 * @param width - this is the width in pixels of object to be displayed on screen, usually passed as the same pixel value as the files actual width, but can be changed to change the ratio of the image.
	 * @param height - this is the height in pixels of object to be displayed on screen, usually passed as the same pixel value as the files actual width, but can be changed to change the ratio of the image.
	 * @param id - this is the scenery id value passed to the scenery object for use in collisions and identifying the scenery object type.
	 * 
	 * When a scenery object is created the parameters are passed to the object and the image is drawn on screen at the x,y coordinates.
	 * 
	 * The object also includes basic getters and setters for its x and y coordinates.
	 */
	public Scenery(int x, int y, String filePath_, int width, int height, SceneryID id, Pane pane) {
		this.x = x;
		this.y = y;
		this.filePath = getClass().getResource(filePath_).toString();
		this.width = width;
		this.height = height;
		this.id = id;
		this.pane = pane;
		
		img = new ImageView();
		Image image = new Image(filePath, (double) width, (double) height, false, false); 
		img.setImage(image);
		img.setX(x);
		img.setY(y);
		pane.getChildren().add(img);
		
	}
	
	public void unrender() {
		pane.getChildren().remove(img);
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
		img.setX(x);
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
		img.setY(y);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public SceneryID getId() {
		return id;
	}

	public void setId(SceneryID id) {
		this.id = id;
	}

}
