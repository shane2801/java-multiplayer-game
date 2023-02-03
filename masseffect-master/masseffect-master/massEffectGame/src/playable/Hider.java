package playable;

import java.awt.Rectangle;
import java.net.Socket;
import java.util.LinkedList;

import application.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import networking.Client;
import networking.GameObjectMessage;
import networking.Server;
import view.LobbyScene;
import view.WindowManager;

public class Hider extends Player {

	public static final double WIDTH = 77, HEIGHT = 103;

	public static int HEALTH = 100;
	public static int MAX_HEALTH = 100;

	public int x, y;
	protected PlayableID id;
	private String inGameName;

	Image hiderImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/hiderSpritesheet.png"));

	ImageView hiderImageView = new ImageView(hiderImage);
	Character hiderCharacter = null;

	public Hider(int x, int y, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, (int) WIDTH, (int) HEIGHT, HEALTH, id, scene, root, objControl, inGameName);

		this.x = (int) (x - HEIGHT);
		this.y = (int) (y - HEIGHT);
		this.id = id;
		this.health = HEALTH;
		this.inGameName = inGameName;

		hiderCharacter = new Character(hiderImageView, x, y, 8, 8, 0, 0, (int) WIDTH, (int) HEIGHT);

		root.getChildren().addAll(hiderCharacter);

	}

	@Override
	public void update() {

		this.x = hiderCharacter.getX();
		this.y = hiderCharacter.getY();

		if (id.equals(PlayableID.Hider) && inGameName.equals(WindowManager.inGameName) && ObjectSpawner.gameMode.equals("Singleplayer")) {

			requestFocus();
			playerInput(hiderCharacter, this, id);

		} else if (id.equals(PlayableID.Hider) && inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			requestFocus();
			playerInput(hiderCharacter, this, id);
			updateClientFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (id.equals(PlayableID.Hider) && inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {

			requestFocus();
			playerInput(hiderCharacter, this, id);
			updateHealthFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {

			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AIHidingPlayer)) {
			hiderAIControls(hiderCharacter, this, id);
		}

		hiderLogic();

		System.out.println("The hiders health is" + getHealth());
		
	}

	public void updateClientFromServer(GameObjectMessage serverGameObjectMessage) {

		LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

		for (String serverPlayerData : serverGameObjectMessage.playerData) {
			String[] playerDataArray = serverPlayerData.split(("-"));
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

	public void updateHealthFromClients(GameObjectMessage[] clientGameObjectMessages) {

		System.out.println("The size of the array in the object spawner is: " + ObjectSpawner.playersArray[0]);
		for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {
			System.out.println("Currently going throughthe message number " + i);
			LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

			for (String serverPlayerData : clientGameObjectMessages[i].playerData) {
				String[] playerDataArray = serverPlayerData.split(("-"));
				System.out.println("Now adding unique id to the se");
				serverUniquePlayerIds.add(Float.parseFloat(playerDataArray[7]));

			}

			if (clientGameObjectMessages[i].getPlayerData().size() == 0) {
				return;
			}

			if (serverUniquePlayerIds.indexOf(getUniqueId()) == -1 && ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime) {
				objControl.removePlayer(this);
				return;
			}

			for (String serverPlayerData : clientGameObjectMessages[i].playerData) {

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

	public void hiderAIControls(Character character, Player player, PlayableID id) {

		playerPhysics(character);

		if (character.getX() <= 960) {
			rightMovement(character, 1, id);
			stepsTaken++;
		}
		if (character.getX() > 960) {
			leftMovement(character, 1, id);
			stepsTaken++;
		}

		int randInt = randomInt(50, 300);

		if (randInt <= stepsTaken) {
			becomeProp(id, character);
		}

	}

	public void hiderLogic() {
		
		System.out.println("Hiderlogic() run.");
		
		if(ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime) {
			
			System.out.println("becomeprop run from hiderlogic.");
			becomeProp(id, hiderCharacter);
		}
		
		if (getHealth() <= 0) {
			objControl.removePlayer(this);
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
	}

	@Override
	public Character getCharacter() {
		return this.hiderCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.hiderCharacter = character;
	}

}
