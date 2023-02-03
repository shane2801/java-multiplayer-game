package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Main;
import application.ObjectController;
import application.ObjectSpawner;
import guiItems.LobbyPlayer;
import javafx.application.Platform;
import networking.Server;
import view.ConnectionLostPane;
import view.LobbyScene;
import view.WindowManager;

/**
 * Client extends thread This will be created every time someone tries to join a
 * game. This will then connect to a server.
 * 
 * @author Owain Edwards
 *
 */
public class Client extends Thread {

	// define a Socket
	public static Socket socket = null;
	private String cname;
	public sendMessThread messageThread = new sendMessThread();

	public static Message serverMessageRecieved = null;
	public static ObjectController clientObjControl = Main.clientObjControl;
	public static GameObjectMessage clientGameObjMsg = null;

	private static String[] perminantPlayer = new String[3];

	public static Message clientMessage = new Message((WindowManager.inGameName + ": "), perminantPlayer, null, clientGameObjMsg, null);

	/**
	 * 
	 * @param host             takes the IP of the host in which the client will
	 *                         join
	 * @param port             takes the port number
	 * @param name             takes the name of the player
	 * @param clientObjControl takes the @ObjectController that the client will use
	 *                         for the game.
	 */
	public Client(String host, int port, String name, ObjectController clientObjControl) {

		this.clientObjControl = clientObjControl;

		clientGameObjMsg = new GameObjectMessage(clientObjControl);

		try {

			socket = new Socket(host, port);
			this.cname = name;
		} catch (UnknownHostException e) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
					WindowManager.gameSelectScene.connectionLostPane.setVisible(true);

				}
			});
		} catch (IOException e) {

		}

	}

	/**
	 * here the message thread starts. this message thread allows the client to send
	 * data. This is also allowing the client to receive data - the data is the
	 * processed to ensure that all LobbyPlayer UI elements sent from the server are
	 * displayed in the lobby of the client.
	 */
	@Override
	public void run() {

		messageThread.start();
		InputStream s = null;
		try {

			s = socket.getInputStream();
			byte[] buf = new byte[999999];
			int len = 0;
			LobbyManager lm = new LobbyManager();

			while ((len = s.read(buf)) != -1) {

				Message message = (Message) ObjectAndByte.toObject(buf);
				if (message != null && message.getClientName().equals("STARTGAME")) {
					perminantPlayer[0] = LobbyScene.playableYou.getName();
					perminantPlayer[1] = LobbyScene.playableYou.getClassRole();
					perminantPlayer[2] = LobbyScene.playableYou.getSide();

					LobbyScene.multiPlayersArray[0] = (LobbyScene.hiderPane.getChildren().size() - 1) + (LobbyScene.seekerPane.getChildren().size() - 1);
					Main.clientObjSpawn = new ObjectSpawner(Main.clientObjControl, "Multiplayer", LobbyScene.multiPlayersArray);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							WindowManager.mainStage.setScene(WindowManager.multiplayerScene);

						}
					});

				} else if (message != null) {
					for (String[] player : message.getServerMsg()) {

						if (player[0] != null) {
							LobbyPlayer playerAdd = lm.stringArrayToLobbyPlayer(player);
							lm.isChangedSide(playerAdd);

							if (lm.isPlayerUnique(playerAdd)) {
								Server.lobbyPlayers.add(playerAdd);

								// if unique then we can add it to the GUI.
								Platform.runLater(new Runnable() {
									@Override
									public void run() {

										lm.displayLobbyPlayer(playerAdd);

									}
								});
							}
						}
					}
				}
				serverMessageRecieved = message;
			}
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
					WindowManager.gameSelectScene.connectionLostPane.setVisible(true);

				}
			});

		} catch (NullPointerException e) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					WindowManager.mainStage.setScene(WindowManager.gameSelectScene);

				}
			});
		}
	}

	/**
	 * Embedded class which allows the client to send messages - this thread is
	 * started for each client upon creation.
	 * 
	 * @author Owain Edwards
	 *
	 */
	public class sendMessThread extends Thread {

		// when player disconnects the player needs to be removed.
		private String[] playerSent;

		private GameObjectMessage clientGameObjMsg = null;

		public void getInitialData(String[] playerSent, GameObjectMessage clientGameObjMsg) {

			this.playerSent = playerSent;
			this.clientGameObjMsg = clientGameObjMsg;

		}

		/**
		 * updateServer writed data to the server for hte server to process game
		 * information.
		 * 
		 * @param clientObjControl takes the clients @ObjectController to send to the
		 *                         server.
		 */
		public void updateServer(ObjectController clientObjControl) {

			clientGameObjMsg = new GameObjectMessage(clientObjControl);
			clientMessage.setClientGameObjMsg(clientGameObjMsg);
			clientMessage.setClientName("UPDATING SERVER WITH CLIENT DATA: ");

			for (String hidingPlayerDataString : clientGameObjMsg.getHidingPlayerData()) {
			}

			try {
				OutputStream os = socket.getOutputStream();

				os.write(ObjectAndByte.toByteArray(clientMessage));

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		/**
		 * requestSideChange used by the client to ask the server to change side
		 * ("Hider" or "Seeker")
		 * 
		 * @param lp takes in the GUI element @LobbyPlayer
		 */
		public void requestSideChange(LobbyPlayer lp) {

			String playerName = lp.getName();

			for (int i = 0; i < Server.lobbyPlayers.size(); i++) {
				if (playerName.equals(Server.lobbyPlayers.get(i).getName())) {
					Server.lobbyPlayers.remove(i);
					break;
				}
			}

			String[] lpArray = new String[3];
			lpArray[0] = lp.getName();
			lpArray[1] = lp.getClassRole();
			lpArray[2] = lp.getSide();

			// playerSent = lpArray;
			try {
				OutputStream os = socket.getOutputStream();

				os.write(ObjectAndByte.toByteArray(new Message("SIDE CHANGE", lpArray, null, null, null)));

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			OutputStream os = null;
			try {

				os = socket.getOutputStream();

				do {

					if (playerSent != null) {

						os.write(ObjectAndByte.toByteArray(new Message(cname, playerSent, null, clientGameObjMsg, null)));
					}
					playerSent = null;
					os.flush();
				} while (playerSent == null);
			} catch (IOException e) {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
						WindowManager.gameSelectScene.connectionLostPane.setVisible(true);

					}
				});
			} catch (NullPointerException e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						WindowManager.mainStage.setScene(WindowManager.gameSelectScene);

					}
				});
			}
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
		}
	}
}