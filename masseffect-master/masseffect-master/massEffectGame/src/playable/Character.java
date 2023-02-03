package playable;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * @author joshua hamilton-brown 
 * 		   The character class is used in conjunction with
 *         the player object to be able to drawn sprites onto the screen and
 *         control their movement within the game GUI. The object contains
 *         relevant getters and setters for x and y coordinates along with
 *         functions to control the movement along the x and y axis of the
 *         player sprite.
 */
public class Character extends Pane {

	ImageView imageView;
	int count, columns, offsetX, offsetY, width, height;
	public SpriteAnimation animation;
	int x, y;

	/**
	 * @param imageView :the image file that is parsed to the character for creating
	 *                  sprite animations and displaying them correctly on screen.
	 * @param x         :the x coordinate that the sprite is to be drawn on screen.
	 * @param y         :the y coordinate that the sprite is to be drawn on screen.
	 * @count :number of sprite animations
	 * @columns :number of columns in the sprite-sheet
	 * @offsetX :x-position of the column on the sprite-sheet
	 * @offsetY :y-position of the row on the sprite-sheet
	 * @width :width of a single sprite-sheet character
	 * @height :height of a single sprite-sheet character
	 */
	public Character(ImageView imageView, int x, int y, int count, int columns, int offsetX, int offsetY, int width,
			int height) {
		this.imageView = imageView;
		this.x = x;
		this.y = y;
		this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
		this.count = count;
		this.columns = columns;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;
		imageView.setX(x);
		imageView.setY(y);

		animation = new SpriteAnimation(imageView, Duration.millis(400), count, columns, offsetX, offsetY, width,
				height);
		getChildren().addAll(imageView);

	}

	/**
	 * the moveX function is used to move the players sprite on screen.
	 * 
	 * @param x - the double variable x is parsed to the moveX function to control
	 *          how many pixels it moves along the x axis. An absolute value is
	 *          taken of the parameter x and then a for loop is incremented that
	 *          many times and the x coordinate value is incremented accordingly
	 *          positively or negatively to move right or left respectively.
	 */
	public void moveX(double x) {
		boolean leftMovement = x > 0 ? true : false;
		for (int i = 0; i < Math.abs(x); i++) {
			if (leftMovement) {
				setX(getX() + 1);
			} else {
				setX(getX() - 1);
			}
		}
	}

	/**
	 * the moveY function is used in controlling the movement of the players sprite
	 * along the y axis.
	 * 
	 * @param y - the double variable y parameter is used to control how many pixels
	 *          along the y axis the sprite is to move when the function is called.
	 *          An absolute value is taken of the parameter y and then a for loop is
	 *          incremented that many times and the y coordinate value is
	 *          incremented accordingly positively or negatively to move down or up
	 *          respectively.
	 */
	public void moveY(double y) {
		boolean moveY = y > 0 ? true : false;
		for (int i = 0; i < Math.abs(y); i++) {
			if (moveY) {
				setY(getY() + 1);
			} else {
				setY(getY() - 1);
			}
		}
	}

	public int getX() {
		int x = (int) imageView.getX();

		return x;
	}

	public void setX(int x) {
		imageView.setX((double) x);
	}

	public int getY() {
		int y = (int) imageView.getY();

		return y;
	}

	public void setY(int y) {
		imageView.setY((double) y);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

}
