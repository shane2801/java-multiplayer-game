package application;

import playable.*;
import propObjects.*;
import static propObjects.PropObjectID.*;
import sceneryObjects.*;
import view.*;
import guiItems.*;
import networking.*;

import java.util.LinkedList;
import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ObjectSpawner {

	boolean isAdded = false;

	PropObjectID[] props = { BlueFridge, SilverFridge, Microwave, OrangeBox, Wardrobe };

	int[] coords = new int[6];
	int[] xSpawnCoords = { Main.PIXELS_WIDTH / 6, 5 * Main.PIXELS_WIDTH / 6, Main.PIXELS_WIDTH / 6, 5 * Main.PIXELS_WIDTH / 6, Main.PIXELS_WIDTH / 2, Main.PIXELS_WIDTH / 2 };
	int[] ySpawnCoords = { 980, 980, 318, 318, 318, 980, 440 };
	// 440 is height for props on first level, 318 is height for players on first
	// level.

	private ObjectController objControl;
	public static String gameMode;
	public int numberOfPCHiders; // PC = player controlled hiders
	public static int[] playersArray = new int[7]; // PCHiders, AIHiders, PCSeekers, AISeekers, SpeedSeekers, GunSeekers,
	// HealthSeekers
	Pane gameOverPane = null;

	private Random r = new Random();
	public static int currentLevelClock = 0;
	public static int propsAndScenerySpawnTime = 1, hidersSpawnTime = 25, seekersSpawnTime = 600, maxLevelClock = 4000;

	public ObjectSpawner(ObjectController objControl, String gameMode, int[] playersArray) {
		this.objControl = objControl;
		ObjectSpawner.gameMode = gameMode;
		ObjectSpawner.playersArray = playersArray;

		if (gameMode.equals("Singleplayer")) {
			gameOverPane = WindowManager.singleplayerGameOverPane;
		} else if (gameMode.contentEquals("Multiplayer")) {
			gameOverPane = WindowManager.multiplayerGameOverPane;
		}

	}

	public void update() {
		System.out.println("The current level clock is: " + currentLevelClock);
		if (gameMode.equals("Singleplayer")) {

			if (!Main.seekersRendered && (playersArray[2] == 1 || playersArray[4] == 1 || playersArray[5] == 1 || playersArray[6] == 1)) {
				WindowManager.singleplayerBlindSeekerPane.setVisible(true);
			} else {
				WindowManager.singleplayerBlindSeekerPane.setVisible(false);
			}

			currentLevelClock++;
			if (!Main.propsAndSceneryRendered && currentLevelClock == propsAndScenerySpawnTime) {
				// at game initialisation scenery and props are drawn onto the game UI.
				objControl.addScenery(new FloatingGrass(0, 270, SceneryID.FloatingGrass, WindowManager.singleplayerAnchorPane));
				objControl.addScenery(new FloatingGrass(960, 270, SceneryID.FloatingGrass, WindowManager.singleplayerAnchorPane));
				objControl.addScenery(new GrassGround(0, 540, SceneryID.GrassGround, WindowManager.singleplayerAnchorPane));
				objControl.addScenery(new GrassGround(960, 540, SceneryID.GrassGround, WindowManager.singleplayerAnchorPane));

				// ground level prop spawn
				int x = 25;// number of pixels to spawn from left of screen.

				while (x < Main.PIXELS_WIDTH - 50) {// 50 pixels is maximum x spawn coordinate to the right side of the
													// screen.
					PropObjectID randomProp = props[randomInt(0, props.length - 1)];
					Prop newProp = addRandomPropAtX(randomProp, ySpawnCoords[0], x);
					x += newProp.getWidth();
					objControl.addProp(newProp);
				}

				// level one prop spawn
				x = 25;

				while (x < Main.PIXELS_WIDTH - 50) {
					PropObjectID randomProp = props[randomInt(0, props.length - 1)];
					Prop newProp = addRandomPropAtX(randomProp, ySpawnCoords[6], x);
					x += newProp.getWidth();
					objControl.addProp(newProp);
				}
				Main.propsAndSceneryRendered = true;
			}

			if (currentLevelClock == hidersSpawnTime) {

				// NEED TO ADD CONDITIONAL DEPENDING ON LOBBY SCENE ROLE AND/OR
				// SIDE<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

				for (int i = 0; i < playersArray.length; i++) {
					if (i == 0) {// if statement required because if playerArray[i] is used as the precondition
						// it will spawn each character that many times, rather than a single character
						// corresponding to player type in that cell in the array.
						// PC Hiders
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new Hider(xSpawnCoords[0], ySpawnCoords[0], PlayableID.Hider, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					}
					if (i == 1) { // AI Hiders
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new Hider(xSpawnCoords[1 + objControl.players.size()], ySpawnCoords[1 + objControl.players.size()], PlayableID.AIHider, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
							// coords are 1 + number of current player objects as players cant spawn on top
							// of each other.
						}
					}
				}
				Main.hidersRendered = true;
			}

			if (currentLevelClock == seekersSpawnTime) {

				for (int i = 0; i < playersArray.length; i++) {
					if (i == 2) {
						// PC Seekers, should only spawn one at most out of of all seeker types, PC or
						// AI.
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new Seeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.Seeker, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					} else if (i == 3) {
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new Seeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.AISeeker, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					} else if (i == 4) {
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new SpeedSeeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.SpeedSeeker, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					} else if (i == 5) {
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new GunSeeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.GunSeeker, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					} else if (i == 6) {
						for (int j = 0; j < playersArray[i]; j++) {
							objControl.addPlayer(new HealthSeeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.HealthSeeker, WindowManager.singleplayerScene, WindowManager.singleplayerPlayerPane, objControl, WindowManager.inGameName));
						}
					}
				}
				Main.seekersRendered = true;
			}
		}

		if (gameMode.equals("Multiplayer")) {
			currentLevelClock++;
			if (LobbyScene.isServer) {

				if(!Main.seekersRendered) {
					if(Server.serverMessage.getServerMsg()[0][2].equals("Seeker")) {
						WindowManager.multiplayerBlindSeekerPane.setVisible(true);
					}
				} else {
					WindowManager.multiplayerBlindSeekerPane.setVisible(false);
				}

				if (!Main.propsAndSceneryRendered && currentLevelClock >= propsAndScenerySpawnTime) {
					// at game initialisation scenery and props are drawn onto the game UI.
					objControl.addScenery(new FloatingGrass(0, 270, SceneryID.FloatingGrass, WindowManager.multiplayerAnchorPane));
					objControl.addScenery(new FloatingGrass(960, 270, SceneryID.FloatingGrass, WindowManager.multiplayerAnchorPane));
					objControl.addScenery(new GrassGround(0, 540, SceneryID.GrassGround, WindowManager.multiplayerAnchorPane));
					objControl.addScenery(new GrassGround(960, 540, SceneryID.GrassGround, WindowManager.multiplayerAnchorPane));

					// ground level prop spawn
					int x = 25;// number of pixels to spawn from left of screen.

					while (x < Main.PIXELS_WIDTH - 50) {
						// 50 pixels is maximum x spawn coordinate to the right side of the screen.
						PropObjectID randomProp = props[randomInt(0, props.length - 1)];
						Prop newProp = addRandomPropAtX(randomProp, ySpawnCoords[0], x);
						x += newProp.getWidth();
						addPropToServerController(newProp, objControl);
					}

					// level one prop spawn
					x = 25;

					while (x < Main.PIXELS_WIDTH - 50) {
						PropObjectID randomProp = props[randomInt(0, props.length - 1)];
						Prop newProp = addRandomPropAtX(randomProp, ySpawnCoords[6], x);
						x += newProp.getWidth();
						addPropToServerController(newProp, objControl);
					}

					Main.propsAndSceneryRendered = true;
				}

				if (!Main.hidersRendered && currentLevelClock >= hidersSpawnTime) {
					for (int i = 0; i < playersArray[0]; i++) {
						if (Server.serverMessage.getServerMsg()[i][2].equals("Hider")) {
							// checks side of player right now, will need it to check role when that is
							// implemented.

							Hider newHider = new Hider(xSpawnCoords[i], ySpawnCoords[i], PlayableID.Hider, WindowManager.multiplayerScene, WindowManager.multiplayerPlayerPane, objControl, Server.serverMessage.getServerMsg()[i][0]);
							objControl.addPlayer(newHider);
							newHider.setUniqueId(xSpawnCoords[i] * ySpawnCoords[i] * Player.playableIdToPrime(PlayableID.Hider));
						}
					}
					Main.hidersRendered = true;
				}

				if (Main.hidersRendered && currentLevelClock > hidersSpawnTime && currentLevelClock < seekersSpawnTime) {

					for (int i = 0; i < ObjectSpawner.playersArray[0]; i++) {

						LinkedList<Float> serverUniquePlayerIds = new LinkedList<Float>();

						if (Server.serverMessage.getServerGameObjMsg()[i].getHidingPlayerData().size() == 0) {
							continue;
						}
						for (Player tempPlayer : objControl.players) {
							serverUniquePlayerIds.add(tempPlayer.getUniqueId());
						}
						LinkedList<String> clientMsgHidPlay = Server.serverMessage.getServerGameObjMsg()[i].getHidingPlayerData();

						for (int j = 0; j < clientMsgHidPlay.size(); j++) {
							String hidPlayerString = clientMsgHidPlay.get(j);
							String[] hidPlayerDataArray = hidPlayerString.split("-");

							if (serverUniquePlayerIds.indexOf(Float.parseFloat(hidPlayerDataArray[7])) == -1) {
								addPlayerToController(hidPlayerDataArray);
							}
						}
					}
				}

				if (!Main.seekersRendered && currentLevelClock >= seekersSpawnTime) {

					for (int i = 0; i < playersArray[0]; i++) {

						String instanceInGameName = Server.serverMessage.getServerMsg()[i][0];
						String instanceInGameSide = Server.serverMessage.getServerMsg()[i][2];

						if (Server.serverMessage.getServerMsg()[i][2].equals("Seeker")) {
							Seeker newSeeker = new Seeker(xSpawnCoords[5], ySpawnCoords[5], PlayableID.Seeker, WindowManager.multiplayerScene, WindowManager.multiplayerPlayerPane, objControl, Server.serverMessage.getServerMsg()[i][0]);
							objControl.addPlayer(newSeeker);
							newSeeker.setUniqueId(xSpawnCoords[5] * ySpawnCoords[5] * Player.playableIdToPrime(PlayableID.Seeker));
						}
					}
					Main.seekersRendered = true;
				}
			} else if (LobbyScene.isClient) {
				String currentLevelClockString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getGameStateData().get(0).split("-")[0];

				currentLevelClock = Integer.parseInt(currentLevelClockString);

				if (!Main.seekersRendered) {
//					System.out.println("Seekers havent been rendered");
//					System.out.println("THe size of the players array is: " + playersArray[0]);
					for (int i = 1; i < playersArray[0]; i++) {
//						System.out.println("The instances name is: " + WindowManager.inGameName);
//						System.out.println("The clients instance name according to the server is: " + Client.serverMessageRecieved.getServerMsg()[i][0]);
						if(WindowManager.inGameName.equals(Client.serverMessageRecieved.getServerMsg()[i][0])) {
//							System.out.println("Names match!");
//							System.out.println("The clients side is: " + Client.serverMessageRecieved.getServerMsg()[i][2]);
							if(Client.serverMessageRecieved.getServerMsg()[i][2].equals("Seeker")) {
								WindowManager.multiplayerBlindSeekerPane.setVisible(true);
							}
						}
					}
				} else {
					WindowManager.multiplayerBlindSeekerPane.setVisible(false);
				}

				if (!Main.propsAndSceneryRendered && currentLevelClock >= propsAndScenerySpawnTime) {
					// checks if the servers currentLevelClock value = 1

					for (int i = 0; i < Client.serverMessageRecieved.getServerGameObjMsg()[0].getSceneryData().size(); i++) {
						String sceneryString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getSceneryData().get(i);
						String sceneryStringX = sceneryString.split("-")[0];
						String sceneryStringY = sceneryString.split("-")[1];
						String sceneryStringId = sceneryString.split("-")[2];
						objControl.addScenery(addSceneryToController(sceneryStringX, sceneryStringY, sceneryStringId, WindowManager.multiplayerAnchorPane));
					}

					for (int i = 0; i < Client.serverMessageRecieved.getServerGameObjMsg()[0].getPropData().size(); i++) {// for loop going through every string in the LinkedList propData.
						String propString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getPropData().get(i);
						String propStringX = propString.split("-")[0];
						String propStringY = propString.split("-")[1];
						String propStringId = propString.split("-")[2];
						objControl.addProp(getPropFromString(propStringX, propStringY, propStringId, WindowManager.multiplayerAnchorPane));
					}
					Main.propsAndSceneryRendered = true;
				}

				if (!Main.hidersRendered && currentLevelClock >= hidersSpawnTime) {

					for (int i = 0; i < Client.serverMessageRecieved.getServerGameObjMsg()[0].getPlayerData().size(); i++) {// this for loop is updating from the servers game object message.
						String playerString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getPlayerData().get(i);

						String[] playerStringArray = playerString.split("-");

						String playerStringX = playerStringArray[0];
						String playerStringY = playerStringArray[1];
						String playerStringHealth = playerStringArray[2];
						String playerStringId = playerStringArray[3];
						String playerStringInGameName = playerStringArray[4];
						String playerStringUniqueId = playerStringArray[7];

						if (playerStringId.equals("Hider")) {
							addPlayerToController(playerStringArray);
						}
					}
					Main.hidersRendered = true;
				}

				if (Main.hidersRendered && currentLevelClock > hidersSpawnTime && currentLevelClock < seekersSpawnTime) {

					LinkedList<Float> clientUniquePlayerIds = new LinkedList<Float>();

					for (int i = 0; i < objControl.players.size(); i++) {
						Player tempPlayer = objControl.players.get(i);
						clientUniquePlayerIds.add(tempPlayer.getUniqueId());
					}

					for (int i = 0; i < Client.serverMessageRecieved.getServerGameObjMsg()[0].getHidingPlayerData().size(); i++) {
						String hidPlayerString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getHidingPlayerData().get(i);
						String[] hidPlayerDataArray = hidPlayerString.split("-");

						if (clientUniquePlayerIds.indexOf(Float.parseFloat(hidPlayerDataArray[7])) == -1) {
							addPlayerToController(hidPlayerDataArray);
						}
					}

				}

				if (!Main.seekersRendered && currentLevelClock >= seekersSpawnTime) {
					for (int i = 0; i < Client.serverMessageRecieved.getServerGameObjMsg()[0].getPlayerData().size(); i++) {
						String playerString = Client.serverMessageRecieved.getServerGameObjMsg()[0].getPlayerData().get(i);

						String[] playerStringArray = playerString.split("-");

						String playerStringX = playerStringArray[0];
						String playerStringY = playerStringArray[1];
						String playerStringHealth = playerStringArray[2];
						String playerStringId = playerStringArray[3];
						String playerStringInGameName = playerStringArray[4];
						String playerStringUniqueId = playerStringArray[7];


						if (playerStringId.equals("Seeker")) {
							addPlayerToController(playerStringArray);
						}
					}
					Main.seekersRendered = true;
				}
			}

		}

		if (currentLevelClock >= maxLevelClock) {
			endGame();
		}
	}
	
	public void endGame() {
		
		String typesAlive = "";

		boolean noMoreHiders = true;
		boolean noMoreSeekers = true;
		
		for (int i = 0; i < objControl.players.size(); i++) {
			if (Player.isHider(objControl.players.get(i))) {
				noMoreHiders = false;
			}
			if (Player.isSeeker(objControl.players.get(i))) {
				noMoreSeekers = false;
			}
		}
		
		if (noMoreSeekers) {
			typesAlive = "Hiders";
		} else if (noMoreHiders) {
			typesAlive = "Seekers";
		} else if (noMoreSeekers && noMoreHiders) {
			typesAlive = "None";
		} else {
			typesAlive = "Both";
		}
		
		Image winnersLblImage = null;
		Label winnersLbl = new Label();

		if ((typesAlive.equals("Hiders") || typesAlive.equals("Both")) &&  !isAdded) {

			winnersLblImage = new Image(getClass().getResourceAsStream("/view/resources/text/HIDERSB.png"));
			winnersLbl.setGraphic(new ImageView(winnersLblImage));

		} else if (typesAlive.equals("Seekers") && !isAdded) {

			winnersLblImage = new Image(getClass().getResourceAsStream("/view/resources/text/SEEKERSB.png"));
			winnersLbl.setGraphic(new ImageView(winnersLblImage));
		}

		gameOverPane.getChildren().add(winnersLbl);

		final String quitButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Quit.png'); -fx-background-size: 152px 71px;";
		GameButtonText quitButton = new GameButtonText(quitButtonStyle, 152, 71);
		quitButton.setOnAction(e -> System.exit(0));


		gameOverPane.getChildren().addAll(quitButton);
		isAdded = true;
		
		objControl.clearObjects();
		
		if ((typesAlive.equals("Hiders") || typesAlive.equals("Seekers") || typesAlive.equals("Both"))) {
			if (gameMode.equals("Singleplayer")) {
				WindowManager.singleplayerGameOverPane.setVisible(true);
			} else if (gameMode.equals("Multiplayer")) {
				WindowManager.multiplayerGameOverPane.setVisible(true);
			}
		}	
	}

	public boolean canSpawnProp(Prop newProp, LinkedList<Prop> props, int level) {
		boolean propSpawnable = true;
		boolean collisionExists = false;

		if (props.size() != 0) {
			while (!collisionExists) {
				for (int i = 0; i < props.size(); i++) {
					if ((newProp.getY() == level) && props.get(i).getY() == level) {
						if ((newProp.getX() + newProp.getWidth() < props.get(i).getX() || newProp.getX() > props.get(i).getX() + props.get(i).getWidth())) {
							collisionExists = false;
						} else {
							return propSpawnable = false;
						}
					}
				}
				return propSpawnable;
			}
		}
		return propSpawnable;
	}

	public void addPropToServerController(Prop newProp, ObjectController objControl) {

		objControl.addProp(newProp);

		newProp.setUniqueId(newProp.getX() * newProp.getY() * newProp.propIdToPrime(newProp.getId()));

	}

	public Prop getPropFromString(String x, String y, String stringId, Pane paneToAddTo) {

		Prop propToAdd = null;

		switch (stringId) {
		case "BlueFridge":
			propToAdd = (new BlueFridge(Integer.parseInt(x), Integer.parseInt(y), PropObjectID.BlueFridge, paneToAddTo, objControl));
			break;
		case "SilverFridge":
			propToAdd = (new SilverFridge(Integer.parseInt(x), Integer.parseInt(y), PropObjectID.SilverFridge, paneToAddTo, objControl));
			break;
		case "Microwave":
			propToAdd = (new Microwave(Integer.parseInt(x), Integer.parseInt(y), PropObjectID.Microwave, paneToAddTo, objControl));
			break;
		case "OrangeBox":
			propToAdd = (new OrangeBox(Integer.parseInt(x), Integer.parseInt(y), PropObjectID.OrangeBox, paneToAddTo, objControl));
			break;
		case "Wardrobe":
			propToAdd = (new Wardrobe(Integer.parseInt(x), Integer.parseInt(y), PropObjectID.Wardrobe, paneToAddTo, objControl));
			break;
		default:
			break;
		}

		return propToAdd;

	}

	public Scenery addSceneryToController(String x, String y, String stringId, Pane paneToAddTo) {

		Scenery sceneryToAdd = null;

		switch (stringId) {
		case "FloatingGrass":
			sceneryToAdd = (new FloatingGrass(Integer.parseInt(x), Integer.parseInt(y), SceneryID.FloatingGrass, paneToAddTo));
			break;
		case "GrassGround":
			sceneryToAdd = (new GrassGround(Integer.parseInt(x), Integer.parseInt(y), SceneryID.GrassGround, paneToAddTo));
			break;
		default:
			break;
		}

		return sceneryToAdd;

	}

	public void addPlayerToController(String[] playerDataArray) {

		String playerStringHealth = playerDataArray[2];
		String playerStringId = playerDataArray[3];
		String playerStringInGameName = playerDataArray[4];
		String playerStringUniqueId = playerDataArray[7];

		Player playerToAdd = null;

		int xCoord = (int) Float.parseFloat(playerDataArray[0]);
		int yCoord = (int) Float.parseFloat(playerDataArray[1]);

		PropObjectID hidPlayerPropId = PropObjectID.ErrorProp;

		switch (playerStringId) {
		case "Hider":
			playerToAdd = (new Hider(xCoord, yCoord, PlayableID.Hider, WindowManager.multiplayerScene, WindowManager.multiplayerPlayerPane, objControl, playerStringInGameName));
			break;
		case "Seeker":
			playerToAdd = (new Seeker(xCoord, yCoord, PlayableID.Seeker, WindowManager.multiplayerScene, WindowManager.multiplayerPlayerPane, objControl, playerStringInGameName));
			break;
		case "HidingPlayer":

			switch (playerDataArray[8]) {
			case "BlueFridge":
				hidPlayerPropId = PropObjectID.BlueFridge;
				break;
			case "SilverFridge":
				hidPlayerPropId = PropObjectID.SilverFridge;
				break;
			case "Microwave":
				hidPlayerPropId = PropObjectID.Microwave;
				break;
			case "OrangeBox":
				hidPlayerPropId = PropObjectID.OrangeBox;
				break;
			case "Wardrobe":
				hidPlayerPropId = PropObjectID.Wardrobe;
				break;
			default:
				break;
			}

			int width = Integer.parseInt(playerDataArray[9]);
			int height = Integer.parseInt(playerDataArray[10]);
			int health = (int) Float.parseFloat(playerStringHealth);
			playerToAdd = (new HidingPlayer(xCoord, yCoord, width, height, health, PlayableID.HidingPlayer, hidPlayerPropId, WindowManager.multiplayerScene, WindowManager.multiplayerPlayerPane, objControl, playerStringInGameName));
			break;
		default:
			break;
		}
		objControl.addPlayer(playerToAdd);

		playerToAdd.setUniqueId(Float.parseFloat(playerStringUniqueId));

	}

	public Prop addRandomPropAtX(PropObjectID id, int level, int x) {

		Prop randomProp = null; // Random prop initliased as null because every time a new prop is created it is
								// added to the single player pane.
//				new ErrorProp(x, level - propObjects.ErrorProp.HEIGHT + 100, id);

		Pane paneToAddTo = null;

		if (gameMode.equals("Singleplayer")) {
			paneToAddTo = WindowManager.singleplayerAnchorPane;
		} else if (gameMode.equals("Multiplayer")) {
			paneToAddTo = WindowManager.multiplayerAnchorPane;
		}

		switch (id) {
		case BlueFridge:
			randomProp = (new BlueFridge(x, level - propObjects.BlueFridge.HEIGHT, id, paneToAddTo, objControl));
			break;
		case SilverFridge:
			randomProp = (new SilverFridge(x, level - propObjects.SilverFridge.HEIGHT, id, paneToAddTo, objControl));
			break;
		case Microwave:
			randomProp = (new Microwave(x, level - propObjects.Microwave.HEIGHT, id, paneToAddTo, objControl));
			break;
		case OrangeBox:
			randomProp = (new OrangeBox(x, level - propObjects.OrangeBox.HEIGHT, id, paneToAddTo, objControl));
			break;
		case Wardrobe:
			randomProp = (new Wardrobe(x, level - propObjects.Wardrobe.HEIGHT, id, paneToAddTo, objControl));
			break;
		default:
			break;
		}
		return randomProp;
	}

	/**
	 * The randomInt method takes two integers as parameters and then returns a
	 * random integer within the range of those two integers.
	 * 
	 * @param min - the minima of the range that the random integer is to be
	 *            generated within parsed as an integer min.
	 * @param max - the maxima of the range that the random integer is to be
	 *            generated within parsed as an integer max.
	 * @return - the randomInt function returns a random integer generated using the
	 *         Random package included with java.
	 */
	public int randomInt(int min, int max) {
		int randomNumber = r.nextInt((max - min) + 1) + min;
		return randomNumber;
	}

	public static String getGameMode() {
		return gameMode;
	}

	public static void setGameMode(String gameMode) {
		ObjectSpawner.gameMode = gameMode;
	}
}
