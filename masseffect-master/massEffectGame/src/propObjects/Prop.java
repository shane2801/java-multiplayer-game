package propObjects;
import java.awt.Rectangle;
import java.util.LinkedList;

import application.ObjectController;
import application.ObjectSpawner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import networking.Client;
import networking.GameObjectMessage;
import networking.Server;
import view.LobbyScene;
import view.WindowManager;

/**
 * @author joshua hamilton-brown
 * @author kavish muthoora
 * The prop class is used for all prop objects that are built in the game. It contains all relevant functions to the prop objects within the game, including getters and setters.
 */
public abstract class Prop{
	
	protected int x, y;
	protected int width, height;
	protected int health;
	protected ImageView img;
	protected String filePath;
	protected PropObjectID id;
	private Pane pane;
	public float uniqueId;
	private ObjectController objControl;
	
	/**
	 * @param x - this is the x coordiante of where the prop is drawn in the game.
	 * @param y - this is the y coordinate of where the prop is drawn in the game.
	 * @param health - all props have a health point value that is used for when a seeker is attempting to hit a prop object.
	 * @param filePath_ - this is the file url passed as a string of the prop objects image that is used to draw the prop onto the javafx pane.
	 * @param width - this is the width in pixels of the props image in the game window. The prop files can be larger than what is needed on screen so the image can be scaled down using width and height values.
	 * @param height - this is the height in pixels of the props image in the game window. As with width, the height value can be used to change the scale of the prop image from its original png file size.
	 * @param id - this is the prop id that is used in physics and game logic to check what kind of prop is being interacted with by a player.
	 */
	public Prop(int x, int y, int health , String filePath_ , int width, int height, PropObjectID id, Pane pane, ObjectController objControl){
		this.x = x;
		this.y = y;
		this.health = health;
		this.filePath = getClass().getResource(filePath_).toString();
		this.width = width;
		this.height = height;
		this.id = id;
		this.pane = pane;
		this.objControl = objControl;
		
		img = new ImageView();
		Image image = new Image(filePath, (double) width, (double) height, false, false); 
		img.setImage(image);
		img.setX(x);
		img.setY(y);
		pane.getChildren().add(img);
		
		uniqueId = x*y*propIdToPrime(id);
	}
	
	public void update() {
		
		if(LobbyScene.isClient) {
			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);
		} else if (LobbyScene.isServer) {
			updateFromClient(Server.serverMessage.getServerGameObjMsg());
		}
		
		
	}
	
	private void updateFromClient(GameObjectMessage[] serverGameObjMsg) {
		
		for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {
			LinkedList<Float> serverUniquePropIds = new LinkedList<Float>();
			
			for(String serverPropData: serverGameObjMsg[i].propData) {
				String[] propDataArray = serverPropData.split("-");
				serverUniquePropIds.add(Float.parseFloat(propDataArray[3]));
			}
			
			if (serverGameObjMsg[i].getPropData().size() == 0) {
				return;
			}
			
			if (serverUniquePropIds.indexOf(getUniqueId()) == -1) {
				objControl.removeProp(this);
				return;
			}
		}

	}

	private void updateFromServer(GameObjectMessage serverGameObjectMessage) {
		
		LinkedList<Float> serverUniquePropIds = new LinkedList<Float>();
		
		for(String serverPropData: serverGameObjectMessage.propData) {
			String[] propDataArray = serverPropData.split("-");
			serverUniquePropIds.add(Float.parseFloat(propDataArray[3]));
		}
		
		if (serverGameObjectMessage.getPropData().size() == 0) {
			return;
		}
		
		if (serverUniquePropIds.indexOf(getUniqueId()) == -1) {
			objControl.removeProp(this);
			return;
		}
	}

	/**
	 * The unrender method for props can be called to remove the prop image from the java fx game pane, making sure the prop is no longer visible once called.
	 */
	public void unrender() {
		pane.getChildren().remove(img);
	}
	
	/**
	 *The getBounds function for prop object returns a 2D rectangle object with dimensions WIDTH x HEIGHT, the same measurements that are final values in the specific prop objects.
	 * @return The returned 2D rectangle has an x,y coordinate that designates where on screen the rectangle is drawn from.
	 *This returned rectangle is used in physics interactions and collisions in the game logic as the game can use built in Rectangle methods for shape interactions with other game objects.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public float propIdToPrime(PropObjectID id) {
		float prime = 0.0f;
		
		switch (id) {
		case BlueFridge:
			prime = 3;
			break;
		case ErrorProp:
			prime = 5;
			break;
		case Microwave:
			prime = 7;
			break;
		case OrangeBox:
			prime = 11;
			break;
		case SilverFridge:
			prime = 13;
			break;
		case Wardrobe:
			prime = 17;
			break;
		default:
			break;
		}
		
		
		return prime;
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
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public PropObjectID getId() {
		return id;
	}
	
	public void setId(PropObjectID id) {
		this.id = id;
	}

	public float getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(float uniqueId) {
		this.uniqueId = uniqueId;
	}
}
