package application;

import usables.*;
import playable.*;
import propObjects.*;
import sceneryObjects.*;

import java.util.*;

/**
 * @author joshua hamilton-brown
 * @author kavish muthoora
 * The object controller is the class that handles all game objects and is called whenever game objects are referred to within the game.
 * The class includes methods to add and remove any type of game object from their respective linked lists.
 *
 */
public class ObjectController{//this class handles all objects in the game during play
	
	
	/**
	 * These LinkedLists are used to store all game objects that are included in the game, listed below are the lists for scenery objects, props, weapons, and players.
	 */
	public LinkedList<Scenery> scenery = new LinkedList<Scenery>();
	public LinkedList<Prop> props = new LinkedList<Prop>();
	public LinkedList<Player> players = new LinkedList<Player>();
	public LinkedList<Bullet> bullets = new LinkedList<Bullet>();	
	
	
	
	/* 
	 * The update method in the object controller class is used to call all game objects update methods, each object type has an update method that is called every game tick that controls all objects actions during gameplay.
	 */
	public void update() {

		for(int i = 0; i < players.size(); i++) {
			Player tempPlayer = players.get(i);
			tempPlayer.update();
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			Bullet tempBullet = bullets.get(i);
			tempBullet.update();
		}
		
		for(int i = 0; i < props.size(); i++) {
			Prop tempProp = props.get(i);
			tempProp.update();
		}
	}
	
	
	
	public void addScenery(Scenery sceneryObject) {
		scenery.add(sceneryObject);
	}
	
	public void removeScenery(Scenery sceneryObject) {
		scenery.remove(sceneryObject);
		sceneryObject.unrender();
	}
	
	public void addProp(Prop propObject) {
		props.add(propObject);
	}
	
	public void removeProp(Prop propObject) {
		props.remove(propObject);
		propObject.unrender();
	}
	
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public void removeBullet(Bullet bulletObject) {
		bulletObject.bulletShape.setRadius(0);
		bullets.remove(bulletObject);
	}
	
	public void addPlayer(Player playerObject) {
		players.add(playerObject);
	}
	
	public void removePlayer(Player playerObject) {
		players.remove(playerObject);
		PlayerPane.root.getChildren().remove(playerObject.getCharacter());
	}
	
	/**
	 * The clearObjects method is used when the game ends and all game objects need to be cleared from the scene to make way for loading the rest of the games GUI.
	 * It recursively calls all remove methods for each object type in the game.
	 */
	public void clearObjects() {
		for(int n = scenery.size()-1; n >= 0; n--) {
			Scenery tempScene = scenery.get(n);
			removeScenery(tempScene);
		}
		
		for(int n = props.size()-1; n >= 0; n--) {
			Prop tempProp = props.get(n);
			removeProp(tempProp);
		}
		for(int n = players.size()-1; n >= 0; n--) {
			Player tempPlayer = players.get(n);
			removePlayer(tempPlayer);
		}
		for(int i = 0; i < bullets.size(); i++) {
			Bullet tempBullet = bullets.get(i);
			removeBullet(tempBullet);
		}
	}

}
