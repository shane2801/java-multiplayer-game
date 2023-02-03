package playable;

import java.awt.Rectangle;
import java.util.Random;

import application.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import networking.Client;
import networking.Server;
import view.LobbyScene;
import view.WindowManager;

public class Seeker extends Player {

	public static final double WIDTH = 77, HEIGHT = 103;
	public static int HEALTH = 100;
	boolean seekerMovingLeft = false, seekerMovingRight = false, chasing = false;

	public int x, y;
	protected PlayableID id;
	private String inGameName;
	private int randomDirection = randomInt(1, 2);
	boolean reachedLeft = false;
	boolean reachedRight = false;

	Image seekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/baseSeekerSpritesheet.png"));
	ImageView seekerImageView = new ImageView(seekerImage);
	Character seekerCharacter = null;

	public Seeker(int x, int y, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, (int) WIDTH, (int) HEIGHT, HEALTH, id, scene, root, objControl, inGameName);

		this.x = (int) (x - HEIGHT);
		this.y = (int) (y - HEIGHT);
		this.id = id;
		this.inGameName = inGameName;

		seekerCharacter = new Character(seekerImageView, x, y, 8, 8, 0, 0, (int) WIDTH, (int) HEIGHT);
		root.getChildren().addAll(seekerCharacter);

	}

	public void update() {
		this.x = seekerCharacter.getX();
		this.y = seekerCharacter.getY();

		if (id.equals(PlayableID.Seeker) && inGameName.equals(WindowManager.inGameName)) {
			requestFocus();
			playerInput(seekerCharacter, this, id);
		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {
			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {
			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AISeeker)) {
			seekerAIControls(seekerCharacter, this, id);
		}
		baseSeekerLogic();
	}

	public void seekerAIControls(Character character, Player player, PlayableID id) {
		
		playerPhysics(seekerCharacter);

		int startHealth = getHealth();

		if (!reachedLeft && !reachedRight) {
			if (randomDirection == 1) {
				rightMovement(character, 2, id);
				seekerMovingRight = true;
				stepsTaken++;
			} else if (randomDirection == 2) {
				leftMovement(character, 2, id);
				seekerMovingLeft = true;
				stepsTaken++;
			}
		}

		if (x >= 1800) {
			reachedRight = true;
			reachedLeft = false;
		}
		if (x <= 30) {
			reachedLeft = true;
			reachedRight = false;
		}
		if (reachedRight) {
			leftMovement(character, 2, id);
		}
		if (reachedLeft) {
			rightMovement(character, 2, id);
		}
		
		int randInt = randomInt(50, 300);

		if (randInt <= stepsTaken) {
			kick(id, player, character);
			stepsTaken = 0;
		}

		for (Player tempPlayer : objControl.players) {

			if (isHider(tempPlayer) && tempPlayer.getBounds().intersects(getBounds())) {
				autoKick(id, player, character);
			}

		}

	}

	public void baseSeekerLogic() {
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
		return this.seekerCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.seekerCharacter = character;
	}

}
