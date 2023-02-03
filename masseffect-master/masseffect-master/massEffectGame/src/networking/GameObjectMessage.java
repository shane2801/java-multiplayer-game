package networking;

import java.io.Serializable;
import java.util.LinkedList;

import application.*;
import playable.*;
import propObjects.*;
import sceneryObjects.*;
import usables.*;
import view.*;

public class GameObjectMessage implements Serializable {

	public LinkedList<String> sceneryData = new LinkedList<String>();
	public LinkedList<String> propData = new LinkedList<String>();
	public LinkedList<String> playerData = new LinkedList<String>();
	public LinkedList<String> hidingPlayerData = new LinkedList<String>();
	public LinkedList<String> bulletData = new LinkedList<String>();
	public LinkedList<String> gameStateData = new LinkedList<String>();
	
	/**
	 * The game object message is where all game data is serialized into string linked lists so that this data can be sent as a byte array to other instances of the game connected over a network..
	 * 
	 * @param objControl
	 */
	public GameObjectMessage(ObjectController objControl) {

		for (Scenery tempScenery : objControl.scenery) {
			String sceneryObjectData = tempScenery.getX() + "-" + tempScenery.getY() + "-" + tempScenery.getId();
			sceneryData.add(sceneryObjectData);
		}
		for (Prop tempProp : objControl.props) {
			String propObjectData = tempProp.getX() + "-" + tempProp.getY() + "-" + tempProp.getId()
			+ "-" + tempProp.getUniqueId();
			propData.add(propObjectData);

		}
		for (int i = 0; i < objControl.players.size(); i++) {
			Player tempPlayer = objControl.players.get(i);
//			if(!tempPlayer.getPlayableID().equals(PlayableID.HidingPlayer)) {
				
				String playerObjectData =  tempPlayer.getCharacter().getX() + "-" + tempPlayer.getCharacter().getY() + "-" + Main.clampVariable((float) 19, (float) 1080, (float)Main.clampVariable((float) 0, (float) 200, (float)tempPlayer.getHealth()))
				+ "-" + tempPlayer.getPlayableID() + "-" + tempPlayer.getInGameName()
				+ "-" + tempPlayer.getCharacter().animation.getOffSetY() + "-" + tempPlayer.getCharacter().animation.getOffSetX()
				+ "-" + tempPlayer.getUniqueId();
				
				playerData.add(playerObjectData);
				
//			}
			

		}
		
		for (int i = 0; i < objControl.players.size(); i++) {
			
			 
			Player tempPlayer = objControl.players.get(i);
			if(tempPlayer.getPlayableID().equals(PlayableID.HidingPlayer)) {
				
				HidingPlayer tempHidPlay = (HidingPlayer) tempPlayer;
				
				String hidingPlayerObjectData = tempHidPlay.getX() + "-" + Main.clampVariable((float) 19, (float) 1080, (float)tempHidPlay.getY()) 
				+ "-" + Main.clampVariable((float) 0, (float) 200, (float)tempHidPlay.getHealth())  + "-" + tempHidPlay.getPlayableID() + "-" + tempHidPlay.getInGameName()
				+ "-" + tempHidPlay.getCharacter().animation.getOffSetY() + "-" + tempHidPlay.getCharacter().animation.getOffSetX()
				+ "-" + tempHidPlay.getUniqueId() + "-" + tempHidPlay.getPropID() + "-" + tempHidPlay.getGameObjectWidth() + "-" + tempHidPlay.getGameObjectHeight();				
				hidingPlayerData.add(hidingPlayerObjectData);
				
			}
		}
		
		for (Bullet tempBullet : objControl.bullets) {
			String bulletObjectData = tempBullet.getX() + "-" + tempBullet.getY() + "-" + tempBullet.getAngleOfFire()
			+ "-" + tempBullet.getUniqueId();
			bulletData.add(bulletObjectData);

		}
		gameStateData.add(ObjectSpawner.currentLevelClock + "-" + WindowManager.inGameName);
	}

	public LinkedList<String> getSceneryData() {
		return sceneryData;
	}

	public void setSceneryData(LinkedList<String> sceneryData) {
		this.sceneryData = sceneryData;
	}

	public LinkedList<String> getPropData() {
		return propData;
	}

	public void setPropData(LinkedList<String> propData) {
		this.propData = propData;
	}

	public LinkedList<String> getPlayerData() {
		return playerData;
	}

	public void setPlayerData(LinkedList<String> playerData) {
		this.playerData = playerData;
	}

	public LinkedList<String> getHidingPlayerData() {
		return hidingPlayerData;
	}

	public void setHidingPlayerData(LinkedList<String> hidingPlayerData) {
		this.hidingPlayerData = hidingPlayerData;
	}

	public LinkedList<String> getBulletData() {
		return bulletData;
	}

	public void setBulletData(LinkedList<String> bulletData) {
		this.bulletData = bulletData;
	}

	public LinkedList<String> getGameStateData() {
		return gameStateData;
	}

	public void setGameStateData(LinkedList<String> gameStateData) {
		this.gameStateData = gameStateData;
	}
}
