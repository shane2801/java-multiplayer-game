package application;

import java.awt.Rectangle;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.Node;

/**
 * @author joshua hamilton-brown
 * @author kavish muthoora
 * The GameObject class is used as the base type for all player objects in the game, each player object has the base values stored in this type.
 *
 */
public abstract class GameObject extends Pane{
	
	protected Node node;	
	protected ImageView imageView;
	public int width, height;
	protected int x, y;
	protected float velY, accY;
	
	/**
	 * @param x this is the x coordinate stored that all game objects have.
	 * @param y this is the y coordinate stored that all game objects have.
	 */
	public GameObject(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * The update method for each game object is called every game tick and all actions for each object are then carried out.
	 */
	public abstract void update();
	/**
	 * The getBounds method is used for each game objects collisions and physics interactions.
	 * @return the method returns a 2D rectangle that can be used in conjunction with other objects rectangles for collisions.
	 */
	public abstract Rectangle getBounds();
	
	/**
	 * @return width of game object, this getter was created to return the width of the game object it is called on.
	 */
	public int getGameObjectWidth() {
		return width;
	}

	/**
	 * setGameObjectWidth is a setter method to force set the width of a game object.
	 * @param width this method takes a width as its parameter and sets the game objects width to that value.
	 */
	public void setGameObjectWidth(int width) {
		this.width = width;
	}

	/**
	 * @return height of game object, this getter was created to return the height of the game object it is called on.
	 */
	public int getGameObjectHeight() {
		return height;
	}
	/**
	 * setGameObjectHeight is a setter method to force set the height of a game object.
	 * @param height this method takes a height as its parameter and sets the game objects height to that value.
	 */
	public void setGameObjectHeight(int height) {
		this.height = height;
	}

	/**
	 * @return this method returns the x coordinate value of the game object it is called on.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * setX is a setter method to force set the x coordinate of a game object.
	 * @param x this method takes a x coordinate as its parameter and sets the game objects x coord to that value.
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return this method returns the y coordinate value of the game object it is called on.
	 */
	public int getY() {
		return this.y;
	}
	/**
	 * setX is a setter method to force set the x coordinate of a game object.
	 * @param x this method takes a x coordinate as its parameter and sets the game objects x coord to that value.
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return this method returns the y axis velocity of the game object it is called on.
	 */
	public float getVelY() {
		return velY;
	}
	/**
	 * this is a setter method for the game object that it is called on to set its y axis velocity.
	 * @param velY the method takes a float for the y axis velocity and sets the game objects value to this float variable.
	 */
	public void setVelY(float velY) {
		this.velY = velY;
	}
	/**
	 * this is a getter method for the game objects y axis acceleration and it will return the y axis acceleration as a float variable.
	 * @return the method returns the y axis acceleration when called on a game object.
	 */
	public float getAccY() {
		return accY;
	}
	/**
	 * @param acceleration:  the acceleration to set for the y axis is taken as a parameter and passed to the function to set the value for the game objects y axis acceleration.
	 */
	public void setAccY(float acceleration) {
		this.accY = acceleration;
	}

}
