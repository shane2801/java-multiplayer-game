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

public class HealthSeeker extends Player {

	public static final double SPRITE_SCALE = 2.5;
	public static final double WIDTH = 77, HEIGHT = 103;
	public static final int HEALTH = 200;

	int health;
	public int x, y;
	protected PlayableID id;
	String inGameName;

	Image healthSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/healthSeekerSpritesheet.png"));
	ImageView healthSeekerImageView = new ImageView(healthSeekerImage);
	Character healthSeekerCharacter = null;

	public HealthSeeker(int x, int y, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, (int) WIDTH, (int) HEIGHT, HEALTH, id, scene, root, objControl, inGameName);

		this.x = (int) (x - HEIGHT);
		this.y = (int) (y - HEIGHT);
		this.id = id;
		this.inGameName = inGameName;

		healthSeekerCharacter = new Character(healthSeekerImageView, x, y, 8, 8, 0, 0, (int) WIDTH, (int) HEIGHT);

		root.getChildren().add(healthSeekerCharacter);
	}

	public void update() {

		if (id.equals(PlayableID.HealthSeeker) && inGameName.equals(WindowManager.inGameName)) {
			requestFocus();
			playerInput(healthSeekerCharacter, this, id);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {
			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {
			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AIHealthSeeker)) {
			healthSeekerAIControls();
		}
		healthSeekerLogic();
	}

	private void healthSeekerAIControls() {

	}

	public void healthSeekerLogic() {
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
		return this.healthSeekerCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.healthSeekerCharacter = character;
	}

}
