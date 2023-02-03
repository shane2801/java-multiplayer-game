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

public class GunSeeker extends Player {

	public static final double WIDTH = 77, HEIGHT = 103;
	public static final int HEALTH = 100;

	public int x, y;
	protected PlayableID id;
	String inGameName;

	Image gunSeekerImage = new Image(getClass().getResourceAsStream("/playable/spritesheets/gunSeekerSpritesheet.png"));
	ImageView gunSeekerImageView = new ImageView(gunSeekerImage);
	Character gunSeekerCharacter = null;

	public GunSeeker(int x, int y, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, (int) WIDTH, (int) HEIGHT, HEALTH, id, scene, root, objControl, inGameName);

		this.x = (int) (x - HEIGHT);
		this.y = (int) (y - HEIGHT);
		this.id = id;
		this.inGameName = inGameName;

		gunSeekerCharacter = new Character(gunSeekerImageView, x, y, 8, 8, 0, 0, (int) WIDTH, (int) HEIGHT);

		root.getChildren().add(gunSeekerCharacter);
	}

	public void update() {

		this.x = gunSeekerCharacter.getX();
		this.y = gunSeekerCharacter.getY();

		if (id.equals(PlayableID.GunSeeker) && inGameName.equals(WindowManager.inGameName)) {

			requestFocus();
			playerInput(gunSeekerCharacter, this, id);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isClient) {

			updateFromServer(Client.serverMessageRecieved.getServerGameObjMsg()[0]);

		} else if (!inGameName.equals(WindowManager.inGameName) && LobbyScene.isServer) {

			updateFromClients(Server.serverMessage.getServerGameObjMsg());

		} else if (id.equals(PlayableID.AIGunSeeker)) {
			gunSeekerAIControls();
		}

		gunSeekerLogic();

	}

	private void gunSeekerAIControls() {

	}

	public void gunSeekerLogic() {
		if (getHealth() <= 0) {
			objControl.removePlayer(this);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
	}

	public Character getCharacter() {
		return this.gunSeekerCharacter;
	}

	@Override
	public void setCharacter(Character character) {
		this.gunSeekerCharacter = character;
	}

}
