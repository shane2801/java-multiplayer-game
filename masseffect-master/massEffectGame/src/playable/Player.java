package playable;

import propObjects.*;
import usables.*;
import view.WindowManager;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import application.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.util.Duration;
import networking.GameObjectMessage;
import networking.Server;

public abstract class Player extends PlayerPane {

	public static final int TERMINAL_VELOCITY = 3;

	public float uniqueId = 0.0f;

	public int health;
	public Random r = new Random();
	int stepsTaken = 0;
	int reloadTime = 5;
	int triggerHeld = 0;

	boolean hasJumped = false;
	boolean hasDropped = false;
	public static boolean moving = false;
	boolean chasing = false;
	boolean triggerPulled = false;
	boolean hasShot = false;
	boolean gunInHand = false;

	boolean left = false;
	boolean right = false;
	boolean shiftPressed = false;
	boolean tabPressed = false;
	boolean space = false;

	boolean escapePressed = false;

	boolean ePressed = false;

	ObjectController objControl;
	PlayableID id;
	public String inGameName;

	public Player(int x, int y, int width, int height, int health, PlayableID id, Scene scene, Pane root, ObjectController objControl, String inGameName) {
		super(x, y, scene);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.health = health;
		this.id = id;
		this.inGameName = inGameName;
		this.scene = scene;
		Player.root = root;
		this.objControl = objControl;

		this.uniqueId = x * y * playableIdToPrime(id);
	}

	public void playerInput(Character character, Player player, PlayableID id) {
		triggerHeld++;

		playerPhysics(character);

		if (isPlayerControlled(this)) {
			if (left) {
				leftMovement(character, 1, id);
			}
			if (right) {
				rightMovement(character, 1, id);
			}
			if (left && shiftPressed) {
				leftMovement(character, 2, id);
			}
			if (right && shiftPressed) {
				rightMovement(character, 2, id);
			}
			if (space && !hasJumped) {
				jump(character, id);
				hasJumped = true;
			}
			if (tabPressed && !hasDropped) {
				drop(character, id);
			}
			if (left || right || space || (tabPressed && hasDropped)) {
				moving = true;
			} else {
				moving = false;
			}
		}

		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case A:
				left = true;
				break;
			case D:
				right = true;
				break;
			case E:
				becomeProp(id, character);
				break;
			case ESCAPE:
				if (ObjectSpawner.getGameMode().contentEquals("Singleplayer")) {
					if (!WindowManager.singleplayerPausePane.isVisible()) {
						WindowManager.singleplayerPausePane.setVisible(true);
					} else if (WindowManager.singleplayerPausePane.isVisible()) {
						WindowManager.singleplayerPausePane.setVisible(false);
					}
				} else if (ObjectSpawner.getGameMode().contentEquals("Multiplayer")) {
					if (!WindowManager.multiplayerPausePane.isVisible()) {
						WindowManager.multiplayerPausePane.setVisible(true);
					} else if (WindowManager.multiplayerPausePane.isVisible()) {
						WindowManager.multiplayerPausePane.setVisible(false);
					}
				}
				break;
			case F:
				kick(id, player, character);
				break;
			case K:
				if (!moving) {
					shoot(id, player, character);
				}
				break;
			case SPACE:
				space = true;
				break;
			case SHIFT:
				shiftPressed = true;
				break;
			case TAB:
				tabPressed = true;
				break;
			default:
				break;
			}
		});

		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case A:
				left = false;
				character.animation.stop();
				break;
			case D:
				right = false;
				character.animation.stop();
				break;
			case F:
				character.animation.stop();
				break;
			case K:
				character.animation.stop();
				break;
			case SPACE:
				setAccY(1);
				space = false;
				hasJumped = false;
				character.animation.stop();
				break;
			case SHIFT:
				shiftPressed = false;
				break;
			case TAB:
				tabPressed = false;
				hasDropped = false;
				character.animation.stop();
				break;
			default:
				break;
			}
		});

	}

	public void updateFromServer(GameObjectMessage serverGameObjectMessage) {

		LinkedList<Float> serverUniqueIds = new LinkedList<Float>();
		for (String serverPlayerData : serverGameObjectMessage.playerData) {
			String[] playerDataArray = serverPlayerData.split(("-"));
			serverUniqueIds.add(Float.parseFloat(playerDataArray[7]));

		}
		if (serverGameObjectMessage.getPlayerData().size() == 0) {
			return;
		}
		if (serverUniqueIds.indexOf(getUniqueId()) == -1) {
			if (isSeeker(this) && ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime + 10) {
				// this condiditon is necessary if the a client is a seeker, the server wont
				// update fron a previous ticks client message and immediately remove the seeker
				// after being added.
				objControl.removePlayer(this);
				return;
			} else {
				objControl.removePlayer(this);
				return;
			}
		}

		for (String serverPlayerData : serverGameObjectMessage.playerData) {

			String[] playerDataArray = serverPlayerData.split(("-"));
//			serverUniqueIds.add(Float.parseFloat(playerDataArray[7]));

			if (getInGameName().equals(playerDataArray[4])) {

				if (getCharacter().animation.getOffSetY() != Integer.parseInt(playerDataArray[5])) {
					getCharacter().animation.play();
				} else {
					getCharacter().animation.stop();
				}
				if (getCharacter().getX() != Integer.parseInt(playerDataArray[0]) || getCharacter().getY() != Integer.parseInt(playerDataArray[1])) {
					getCharacter().setX(Integer.parseInt(playerDataArray[0]));
					getCharacter().setY(Integer.parseInt(playerDataArray[1]));
					getCharacter().animation.setOffsetY(Integer.parseInt(playerDataArray[5]));
				}

				if (getHealth() != (int) Float.parseFloat(playerDataArray[2])) {
					setHealth((int) Float.parseFloat(playerDataArray[2]));
				} // also need else statements for shooting, and other animations.
			}
		}
	}

	public void updateFromClients(GameObjectMessage[] clientGameObjectMessages) {

		for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {
			if (clientGameObjectMessages[i].getPlayerData().size() == 0) {
				return;
			}
			LinkedList<Float> clientUniqueIds = new LinkedList<Float>();
			for (String serverPlayerData : clientGameObjectMessages[i].getPlayerData()) {
				String[] playerDataArray = serverPlayerData.split(("-"));
				clientUniqueIds.add(Float.parseFloat(playerDataArray[7]));
			}
			LinkedList<Float> clientHidPlayerUniqueIds = new LinkedList<Float>();
			for (String serverHidPlayerData : clientGameObjectMessages[i].getHidingPlayerData()) {
				String[] hidPlayerDataArray = serverHidPlayerData.split("-");
				clientHidPlayerUniqueIds.add(Float.parseFloat(hidPlayerDataArray[7]));
			}

			if (clientUniqueIds.indexOf(this.getUniqueId()) == (-1)) {
				if (isSeeker(this)) {// this condiditon is necessary if the a client is a seeker, the server wont
										// update fron a previous ticks client message and immediately remove the seeker
										// after being added.
					if (ObjectSpawner.currentLevelClock > ObjectSpawner.seekersSpawnTime + 10) {
						objControl.removePlayer(this);
						return;
					}
				} else if (this.getPlayableID().equals(PlayableID.HidingPlayer)) {
					if (clientHidPlayerUniqueIds.indexOf(this.getUniqueId()) == -1) {
						objControl.removePlayer(this);
						return;
					}

				} else {
					objControl.removePlayer(this);
					return;
				}
			}

			for (String clientPlayerData : clientGameObjectMessages[i].playerData) {
				String[] playerDataArray = clientPlayerData.split("-");

				if (playerDataArray[4].equals(inGameName)) {

					if (getCharacter().animation.getOffSetY() != Integer.parseInt(playerDataArray[5])) {
						getCharacter().animation.play();
					} else {
						getCharacter().animation.stop();
					}
					if (getCharacter().getX() != Integer.parseInt(playerDataArray[0]) || getCharacter().getY() != Integer.parseInt(playerDataArray[1])) {
						getCharacter().setX(Integer.parseInt(playerDataArray[0]));
						getCharacter().setY(Integer.parseInt(playerDataArray[1]));
						getCharacter().animation.setOffsetY(Integer.parseInt(playerDataArray[5]));
					}
				}
			}
		}
	}

	public void becomeProp(PlayableID id, Character character) {

		System.out.println("becomeProp run.");

		if (id.equals(PlayableID.Hider) || id.equals(PlayableID.AIHider)) {
			for (int i = 0; i < objControl.props.size(); i++) {
				Prop tempProp = objControl.props.get(i);
				if (getBounds(character).intersects(tempProp.getBounds())) {
					if (tempProp.getBounds().contains(character.getX() + ((int) width) / 2, character.getY() + ((int) height - 5))) {
						/*
						 * checks if middle point of player model exists within any rectangle, this will
						 * determine which rectangle, if multiple, has the biggest overlap.
						 */

						objControl.removeProp(tempProp);
						objControl.removePlayer(this);

						Media becomePropSound = new Media(getClass().getResource("/audio/files/becomePropSucceed.wav").toString());
						MediaPlayer mediaPlayer = new MediaPlayer(becomePropSound);
						mediaPlayer.setAutoPlay(true);
						mediaPlayer.play();

						getCharacter().animation.setOffsetY(0);

						if (id.equals(PlayableID.Hider)) {

							objControl.addPlayer(new HidingPlayer(tempProp.getX(), tempProp.getY() - 20, tempProp.getWidth(), tempProp.getHeight(), tempProp.getHealth(), PlayableID.HidingPlayer, tempProp.getId(), scene, root, objControl, inGameName));
							// - 20 is because the props sprite has a 20 pixel gap between the top of the
							// bounds and the top of the icons picture.

							System.out.println("Hider has become a hiding player.");

						} else if (id.equals(PlayableID.AIHider)) {
							objControl.addPlayer(new HidingPlayer(tempProp.getX(), tempProp.getY() - 20, tempProp.getWidth(), tempProp.getHeight(), tempProp.getHealth(), PlayableID.AIHidingPlayer, tempProp.getId(), scene, root, objControl, inGameName));
						}
					}
				}
			}
		}
	}

	public void kick(PlayableID id, Player player, Character character) {

		boolean hasKicked = false;
		// this boolean is used to check if the seeker has kicked, if true, the method
		// is exited without allowing the seeker to kick multiple objects with one key
		// press.

		if (isSeeker(player)) {
			kickAnimation(character);

			Media becomePropSound = new Media(getClass().getResource("/audio/files/kick.wav").toString());
			MediaPlayer mediaPlayer = new MediaPlayer(becomePropSound);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.play();

			// for checking if the seeker is hittng any player object.
			for (int i = 0; i < objControl.players.size(); i++) {
				Player tempPlayer = objControl.players.get(i);
				if (getBounds(character).intersects(tempPlayer.getBounds(tempPlayer.getCharacter()))) {
					if (tempPlayer.getBounds().contains(character.getX() + ((int) width) / 2, character.getY() + ((int) height - 2))) {
						if (isHider(tempPlayer)) {
							tempPlayer.setHealth(tempPlayer.getHealth() - 20);
							hasKicked = true;
							System.out.println("The seeker has now kicked a hider <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
							if (tempPlayer.getHealth() <= 0) {
//								objControl.removePlayer(tempPlayer);
								// maybe have a method here that is called that runs more code like sound
								// effects, points, that sort of stuff.
							}
							if (hasKicked)
								return;
						}
					}
				}
			}
			for (int i = 0; i < objControl.props.size(); i++) {// for checking if the seeker is hitting any prop
																// object.
				Prop tempProp = objControl.props.get(i);
				if (getBounds(character).intersects(tempProp.getBounds())) {
					if (tempProp.getBounds().contains(character.getX() + ((int) width) / 2, character.getY() + ((int) height - 2))) {
						player.setHealth(player.getHealth() - 5);
						hasKicked = true;
						System.out.println("The seeker has now kicked a prop <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

						if (player.getHealth() <= 0) {
							objControl.removePlayer(this);
						}
						if (hasKicked)
							return;
					}
				}
			}
		}
	}

	public void autoKick(PlayableID id, Player player, Character character) {

		boolean hasKicked = false; // this boolean is used to check if the seeker has kicked, if true, the method
									// is exited without allowing the seeker to kick multiple objects with one key
									// press.

		if (isSeeker(player)) {
			kickAnimation(character);
			// for checking if the seeker is hittng any player object.
			for (int i = 0; i < objControl.players.size(); i++) {
				Player tempPlayer = objControl.players.get(i);
				if (getBounds(character).intersects(tempPlayer.getBounds(tempPlayer.getCharacter()))) {
					if (tempPlayer.getBounds().contains(character.getX() + ((int) width) / 2, character.getY() + ((int) height - 2))) {
						if (isHider(tempPlayer)) {
							tempPlayer.setHealth(tempPlayer.getHealth() - 20);
							hasKicked = true;
							if (tempPlayer.getHealth() <= 0) {
								objControl.removePlayer(tempPlayer);
								// maybe have a method here that is called that runs more code like sound
								// effects, points, that sort of stuff.
							}
							if (hasKicked)
								return;
						}
					}
				}
			}
		}
	}

	public void shoot(PlayableID id, Player player, Character character) {

		if (isSeeker(this)) {

			if (character.animation.getOffSetY() == 412 || character.animation.getOffSetY() == 618) {// shooting left
				character.animation.setOffsetY(206);
				character.animation.play();
			}
			if (character.animation.getOffSetY() == 0 || character.animation.getOffSetY() == 309 || character.animation.getOffSetY() == 515) {// shooting right
				character.animation.setOffsetY(103);
				character.animation.play();
			}

			Media becomePropSound = new Media(getClass().getResource("/audio/files/shoot.wav").toString());
			MediaPlayer mediaPlayer = new MediaPlayer(becomePropSound);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.play();

			Pane paneToAddTo = null;
			if (WindowManager.mainStage.getScene().equals(WindowManager.singleplayerScene)) {
				paneToAddTo = WindowManager.singleplayerWeaponPane;
			} else if (WindowManager.mainStage.getScene().equals(WindowManager.multiplayerScene)) {
				paneToAddTo = WindowManager.multiplayerWeaponPane;
			}
			if (character.animation.getOffSetY() == 206) {// gun in hand pointing left
				objControl.addBullet(new Bullet(character.getX(), character.getY(), 270, scene, paneToAddTo, objControl, this));

			}
			if (character.animation.getOffSetY() == 103) {// gun in hand pointing right
				objControl.addBullet(new Bullet(character.getX(), character.getY(), 90, scene, paneToAddTo, objControl, this));
			}
		}
	}

	public static boolean isPlayerControlled(Player player) {
		boolean playerIsPC = false;

		PlayableID id = player.getPlayableID();

		if (!(id.equals(PlayableID.AIHealthSeeker) || id.equals(PlayableID.AIHider) || id.equals(PlayableID.AIHidingPlayer) || id.equals(PlayableID.AISpeedSeeker) || id.equals(PlayableID.AIGunSeeker) || id.equals(PlayableID.AISeeker))) {
			playerIsPC = true;
		}
		return playerIsPC;
	}

	public static boolean isSeeker(Player player) {
		boolean playerCheckedIsSeeker = false;

		PlayableID id = player.getPlayableID();

		if (id.equals(PlayableID.AISeeker) || id.equals(PlayableID.Seeker) || id.equals(PlayableID.SpeedSeeker) || id.equals(PlayableID.HealthSeeker) || id.equals(PlayableID.GunSeeker)) {
			playerCheckedIsSeeker = true;
		}
		return playerCheckedIsSeeker;
	}

	public static boolean isHider(Player player) {
		boolean playerCheckedIsHider = false;

		PlayableID id = player.getPlayableID();

		if (id.equals(PlayableID.AIHider) || id.equals(PlayableID.Hider) || id.equals(PlayableID.HidingPlayer) || id.equals(PlayableID.AIHidingPlayer)) {
			playerCheckedIsHider = true;
		}
		return playerCheckedIsHider;
	}

	public void playerPhysics(Character character) {
		velY += accY;
		if (velY > TERMINAL_VELOCITY) {
			velY = TERMINAL_VELOCITY;
		}

		character.moveY(velY);
		if (velY > 0) {// controls vertical movement when jumping and falling.
			character.moveY(velY * 1.75);
		}

		if (y <= 0) {// stops the player getting stuck at top of screen when holding down space bar.
			velY = 0;
		}
		x = character.getX();
		x = (int) Main.clampVariable(0, 1841, (float) x);
		character.setX(x);
		if (character.getY() <= 980 && character.getY() > (429 - height)) {
			y = character.getY();
			y = (int) Main.clampVariable(0, (960 - height), (float) y);
			character.setY(y);
		} else if (character.getY() <= (429 - height)) {
			y = character.getY();
			y = (int) Main.clampVariable(0, (420 - height), (float) y);
			character.setY(y);
		}

	}

	public void kickAnimation(Character character) {
		character.animation.setOffsetY(927);
		character.animation.setDelay(Duration.millis(10));
		character.animation.play();

	}

	public void jump(Character character, PlayableID id) {

		if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
			character.animation.setOffsetY(721);
			character.animation.play();

			Media becomePropSound = new Media(getClass().getResource("/audio/files/jump.wav").toString());
			MediaPlayer mediaPlayer = new MediaPlayer(becomePropSound);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.play();

		}
		setAccY(-1);
		hasJumped = false;
	}

	public void drop(Character character, PlayableID id) {

		if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
			character.animation.setOffsetY(824);
			character.animation.play();
		}

		character.moveY(1);
		hasDropped = true;

	}

	public void leftMovement(Character character, int speed, PlayableID id) {
		gunInHand = false;
		if (speed == 1) { // walk speed
			if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
				character.animation.setOffsetY(412);
				character.animation.play();
			}
			if (id.equals(PlayableID.SpeedSeeker) || id.equals(PlayableID.AISpeedSeeker)) {
				character.moveX(-3);
			} else {
				character.moveX(-2);

			}

		} else if (speed == 2) { // run speed
			if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
				character.animation.play();
				character.animation.setOffsetY(618);
			}
			if (id.equals(PlayableID.SpeedSeeker) || id.equals(PlayableID.AISpeedSeeker)) {
				character.moveX(-6);
			} else {
				character.moveX(-5);

			}
		}
	}

	public void rightMovement(Character character, int speed, PlayableID id) {
		gunInHand = false;
		if (speed == 1) { // walk speed
			if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
				character.animation.setOffsetY(309);
				character.animation.play();
			}
			if (id.equals(PlayableID.SpeedSeeker) || id.equals(PlayableID.AISpeedSeeker)) {
				character.moveX(3);
			} else {
				character.moveX(2);

			}

			character.moveX(2);
		} else if (speed == 2) { // run speed
			if (!id.equals(PlayableID.HidingPlayer) && !id.equals(PlayableID.AIHidingPlayer)) {
				character.animation.setOffsetY(515);
				character.animation.play();
			}
			if (id.equals(PlayableID.SpeedSeeker) || id.equals(PlayableID.AISpeedSeeker)) {
				character.moveX(6);
			} else {
				character.moveX(5);

			}
		}
	}

	public Rectangle getBounds(Character character) {
		return new Rectangle(character.getX(), character.getY(), (int) width, (int) height);
	}

	public Rectangle getBounds(int x, int y) {
		return new Rectangle(x, y, (int) width, (int) height);
	}

	public void unrender(Character character) {
		root.getChildren().remove(character);
	}

	public int randomInt(int min, int max) {
		int randomNumber = r.nextInt((max - min) + 1) + min;
		return randomNumber;
	}

	public static float playableIdToPrime(PlayableID id) {
		float prime = 0.0f;

		switch (id) {
		case Hider:
			prime = 3;
			break;
		case AIHider:
			prime = 5;
			break;
		case HidingPlayer:
			prime = 7;
			break;
		case AIHidingPlayer:
			prime = 11;
			break;
		case Seeker:
			prime = 13;
			break;
		case AISeeker:
			prime = 17;
			break;
		case HealthSeeker:
			prime = 19;
			break;
		case AIHealthSeeker:
			prime = 23;
			break;
		case SpeedSeeker:
			prime = 29;
			break;
		case AISpeedSeeker:
			prime = 31;
			break;
		case GunSeeker:
			prime = 37;
			break;
		case AIGunSeeker:
			prime = 39;
			break;
		default:
			break;
		}

		return prime;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public PlayableID getPlayableID() {
		return this.id;
	}

	public void setPlayableID(PlayableID id) {
		this.id = id;
	}

	public abstract Character getCharacter();

	public abstract void setCharacter(Character character);

	public String getInGameName() {
		return inGameName;
	}

	public void setInGameName(String inGameName) {
		this.inGameName = inGameName;
	}

	public float getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(float uniqueId) {
		this.uniqueId = uniqueId;
	}

}
