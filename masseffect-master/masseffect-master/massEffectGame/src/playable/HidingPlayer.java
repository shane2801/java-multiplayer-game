package playable;

import java.awt.Rectangle;
import java.util.LinkedList;

import application.*;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import networking.Client;
import networking.GameObjectMessage;
import networking.Server;
import playable.Character;
import propObjects.*;
import view.LobbyScene;
import view.WindowManager;

public class HidingPlayer extends Player {

	public static int HEALTH;
	public String inGameName;

	public PropObjectID propID;
	public PlayableID id;
	Character hidingPlayerCharacter = null;

	public HidingPlayer(int x, int y, int width, int height, int health, PlayableID id, PropObjectID propID, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, width, height, health, id, scene, root, objControl, inGameName);

		this.HEALTH = health;
		this.inGameName = inGameName;
		this.propID = propID;
		this.id = id;

		hidingPlayerCharacter = spawnAsProp(x, y, width, height, propID);
		root.getChildren().addAll(hidingPlayerCharacter);

	}

	public void update() {
		if (id.equals(PlayableID.HidingPlayer) && inGameName.equals(WindowManager.inGameName) && ObjectSpawner.gameMode.equals("Singleplayer")) {

			requestFocus();
			playerInput(hidingPlayerCharacter, this, id);

		} else if (id.equals(PlayableID.HidingPlayer) && inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			requestFocus();
			playerInput(hidingPlayerCharacter, this, id);
			updateClientFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (id.equals(PlayableID.HidingPlayer) && inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {

			requestFocus();
			playerInput(hidingPlayerCharacter, this, id);
			updateHealthFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {


			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AIHidingPlayer)) {
			hidingPlayerAIControls(hidingPlayerCharacter, this, id);
		}
		hidingPlayerLogic();
	}

	

	public void updateFromServer(GameObjectMessage serverGameObjectMessage) {
		LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

		for (String serverHidPlayerData : serverGameObjectMessage.playerData) {
			String[] playerDataArray = serverHidPlayerData.split(("-"));
			serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

		}

		if (serverGameObjectMessage.getHidingPlayerData().size() == 0) {
			return;
		}

		if (serverUniquePlayerIds.indexOf(getUniqueId()) == -1 && ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime) {
			objControl.removePlayer(this);
			return;
		}
		for (String serverPlayerData : serverGameObjectMessage.hidingPlayerData) {

			String[] playerDataArray = serverPlayerData.split(("-"));
			serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

			if (getInGameName().equals(playerDataArray[4])) {

				if (getCharacter().animation.getOffSetY() != Integer.parseInt(playerDataArray[5])) {
					getCharacter().animation.play();
				} else {
					getCharacter().animation.stop();
				}
				if (getCharacter().getX() != Integer.parseInt(playerDataArray[0]) || getCharacter().getY() != (int) Float.parseFloat(playerDataArray[1])) {
					getCharacter().setX(Integer.parseInt(playerDataArray[0]));
					getCharacter().setY((int) Float.parseFloat(playerDataArray[1]));
					getCharacter().animation.setOffsetY(Integer.parseInt(playerDataArray[5]));
				} // also need else statements for shooting, and other animations.
				if (getHealth() > (int) Float.parseFloat(playerDataArray[2])) {
					setHealth((int) Float.parseFloat(playerDataArray[2]));

				}

			}
		}

	}

	public void updateClientFromServer(GameObjectMessage serverGameObjectMessage) {

		LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

		for (String serverHidPlayerData : serverGameObjectMessage.playerData) {
			String[] playerDataArray = serverHidPlayerData.split(("-"));
			serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

		}
		if (serverGameObjectMessage.getPlayerData().size() == 0) {
			return;
		}
		if (serverUniquePlayerIds.indexOf(getUniqueId()) == -1 && ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime) {
			objControl.removePlayer(this);
			return;

		}
		for (String serverPlayerData : serverGameObjectMessage.playerData) {

			String[] playerDataArray = serverPlayerData.split(("-"));
			serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

			if (getInGameName().equals(playerDataArray[4])) {
				if (getHealth() > (int) Float.parseFloat(playerDataArray[2])) {
					setHealth((int) Float.parseFloat(playerDataArray[2]));
				}
			}
		}
	}

	public void updateFromClients(GameObjectMessage[] clientGameObjectMessages) {

		for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {
			LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

			for (String serverHidPlayerData : clientGameObjectMessages[i].playerData) {
				String[] playerDataArray = serverHidPlayerData.split(("-"));
				serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

			}
			if (serverUniquePlayerIds.indexOf(getUniqueId()) == -1) {
				objControl.removePlayer(this);
				return;
			}

			for (String serverPlayerData : clientGameObjectMessages[i].hidingPlayerData) {

				String[] playerDataArray = serverPlayerData.split(("-"));
				serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

				if (getInGameName().equals(playerDataArray[4])) {

					if (getCharacter().animation.getOffSetY() != Integer.parseInt(playerDataArray[5])) {
						getCharacter().animation.play();
					} else {
						getCharacter().animation.stop();
					}
					if (getCharacter().getX() != (int) Float.parseFloat(playerDataArray[0]) || getCharacter().getY() != (int) Float.parseFloat(playerDataArray[1])) {
						getCharacter().setX((int) Float.parseFloat(playerDataArray[0]));
						getCharacter().setY((int) Float.parseFloat(playerDataArray[1]));
						getCharacter().animation.setOffsetY(Integer.parseInt(playerDataArray[5]));
					} // also need else statements for shooting, and other animations.
					if (getHealth() > (int) Float.parseFloat(playerDataArray[2])) {
						setHealth((int) Float.parseFloat(playerDataArray[2]));

					}
				}
			}
		}
	}

	public void updateHealthFromClients(GameObjectMessage[] clientGameObjectMessages) {

		for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {
			LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

			for (String serverHidPlayerData : clientGameObjectMessages[i].playerData) {
				String[] playerDataArray = serverHidPlayerData.split(("-"));
				serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

			}

			if (clientGameObjectMessages[i].getPlayerData().size() == 0) {
				return;
			}

			if (serverUniquePlayerIds.indexOf(getUniqueId()) == -1 && ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime) {
				objControl.removePlayer(this);
				return;
			}

			for (String serverPlayerData : clientGameObjectMessages[i].hidingPlayerData) {

				String[] playerDataArray = serverPlayerData.split(("-"));
				serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

				if (getInGameName().equals(playerDataArray[4])) {
					if (getHealth() > (int) Float.parseFloat(playerDataArray[2])) {
						setHealth((int) Float.parseFloat(playerDataArray[2]));
					}
				}
			}
		}
	}

	public void hidingPlayerLogic() {
		if (getHealth() <= 0) {
			objControl.removePlayer(this);
			root.getChildren().remove(hidingPlayerCharacter);
		}

		int clock = ObjectSpawner.currentLevelClock;

		if (clock == 1000 || clock == 2000 || clock == 3500 || clock == 5000) {			
			ParallelTransition pt = new ParallelTransition();
			pt = scaleHidingProp(hidingPlayerCharacter.getImageView());
			pt.play();
		}
	}
	
	private ParallelTransition scaleHidingProp(ImageView hidingPlayerImageView) {
		final Duration animationTime = Duration.millis(1000);
		ScaleTransition scaleAnimation = new ScaleTransition(animationTime);
		scaleAnimation.setByX(0.10f);
		scaleAnimation.setByY(0.10f);
		scaleAnimation.setCycleCount(2);
		scaleAnimation.setAutoReverse(true);
		ParallelTransition pt = new ParallelTransition(hidingPlayerImageView, scaleAnimation);
		pt.setCycleCount(1);
		return pt;
		
	}

	public Character spawnAsProp(int x, int y, int width, int height, PropObjectID propID) {

		Character hidingPlayerCharacter = null;

		String filePath = "";

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.propID = propID;

		switch (propID) {
		case BlueFridge:
			filePath = BlueFridge.blueFridgeFilePath;
			break;
		case Microwave:
			filePath = Microwave.microwaveFilePath;
			break;
		case SilverFridge:
			filePath = SilverFridge.silverFridgeFilePath;
			break;
		case OrangeBox:
			filePath = OrangeBox.orangeBoxFilePath;
			break;
		case Wardrobe:
			filePath = Wardrobe.wardrobeFilePath;
			break;
		default:
			filePath = ErrorProp.errorPropFilePath;
			break;
		}

		Image hidingPlayerImage = new Image(filePath, (double) width, (double) height, false, false);
		ImageView imageView = new ImageView(hidingPlayerImage);
		hidingPlayerCharacter = new Character(imageView, x, y, 1, 1, 0, 0, width, height);

		return hidingPlayerCharacter;

	}
	
	private void hidingPlayerAIControls(Character hidingPlayerCharacter2, HidingPlayer hidingPlayer, PlayableID id2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public Character getCharacter() {
		return this.hidingPlayerCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.hidingPlayerCharacter = character;
	}

	public PropObjectID getPropID() {
		return propID;
	}

	public void setPropID(PropObjectID propID) {
		this.propID = propID;
	}
}
