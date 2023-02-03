package playable;

import java.awt.Rectangle;

import application.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import networking.Client;
import networking.Server;
import view.LobbyScene;
import view.WindowManager;

public class SpeedSeeker extends Player {

	public static final double WIDTH = 77, HEIGHT = 103;
	public static final int HEALTH = 100;

	int health;
	public int x, y;
	protected PlayableID id;
	String inGameName;

	Image speedSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/speedSeekerSpritesheet.png"));
	ImageView speedSeekerImageView = new ImageView(speedSeekerImage);
	Character speedSeekerCharacter = null;

	public SpeedSeeker(int x, int y, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, (int) WIDTH, (int) HEIGHT, HEALTH, id, scene, root, objControl, inGameName);

		this.x = (int) (x - HEIGHT);
		this.y = (int) (y - HEIGHT);
		this.id = id;
		this.inGameName = inGameName;

		speedSeekerCharacter = new Character(speedSeekerImageView, x, y, 8, 8, 0, 0, (int) WIDTH, (int) HEIGHT);
		root.getChildren().add(speedSeekerCharacter);
	}

	public void update() {

		if (id.equals(PlayableID.SpeedSeeker) && inGameName.equals(WindowManager.inGameName)) {

			requestFocus();
			playerInput(speedSeekerCharacter, this, id);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {
			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AIGunSeeker)) {
			speedSeekerAIControls();
		}
		speedSeekerLogic();
	}

	private void speedSeekerAIControls() {

	}

	public void speedSeekerLogic() {
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
		return this.speedSeekerCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.speedSeekerCharacter = character;
	}

}
