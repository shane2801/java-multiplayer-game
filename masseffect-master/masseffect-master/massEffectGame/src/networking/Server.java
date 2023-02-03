package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import application.Main;
import application.ObjectController;
import guiItems.LobbyPlayer;
import javafx.application.Platform;
import view.*;
/**
 * Server will be created when someone tried to "host" a game in @GameSelectScene 
 * The server will deal with all client requests and update the message to be sent to all clients to correctly update their game
 * and their lobby.
 * 
 * @author Owain Edwards
 *
 */
public class Server extends Thread {

	public static ArrayList<LobbyPlayer> lobbyPlayers = new ArrayList<LobbyPlayer>();
    public static ServerSocket server = null;
    public final static String[][] messages = new String[6][3]; // max 6 players. 5 connections.
    private static int numOfConnections = 0; // max 5
    public final static List<Socket> socketList = new ArrayList<>();
    static ObjectController objControl = Main.objControl;
    public static GameObjectMessage[] serverGameObjMsg = new GameObjectMessage[6];
    public Socket socket;
    public static Message serverMessage = new Message("SERVER: ", null, messages, null, serverGameObjMsg);
    /**
     * 
     * @param port takes the port number that the @Client will connect to the server with.
     * @param objControl takes @ObjectController that contains the data for the game to be controled with.
     */
    public Server(int port, ObjectController objControl) {
    	
    	// Server Side LobbyPLayer needs to be added too.
    	messages[0][0] = LobbyScene.playableYou.getName();
    	messages[0][1] = LobbyScene.playableYou.getClassRole();
    	messages[0][2] = LobbyScene.playableYou.getSide();
    	// server therefore will always occupy index 0;

        serverGameObjMsg[0] = new GameObjectMessage(objControl);
        
    	numOfConnections += 1;
    	
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * receives a connection.
     */
    @Override
    public void run() {
        while (true) {
        	
            try {
                socket = server.accept();
                socketList.add(socket);
                new RecieveMsgThread(socket,msg->new Message(
                		msg.getClientName(),
                		msg.getClientMsg(),
                		msg.getServerMsg(),
                		msg.getClientGameObjMsg(),
                		msg.getServerGameObjMsg())).start();
               
            } catch (IOException e) {
                
               break;
            }
        }

    }
     /**
      * RecieveMsgThread is and embedded class which allows the server to receive messages from @client
      * @author Owain Edwards
      *
      */
    static class RecieveMsgThread extends Thread {
    	 
    	private Socket socket;
        private Function<Message,Message> function;

        public RecieveMsgThread(Socket socket,Function<Message,Message> function) {
            this.socket = socket;
            this.function = function;
        }

        /**
         * This will perform the server operation for dealing with any data sent by the client.
         * This will update the @Messaage to be sent according to the data sent by the cleints.
         * 
         */
        @Override
        public void run() {
        	
            InputStream in = null;
            try {
                in = socket.getInputStream();
                int len = 0;
                byte[] buf = new byte[999999];
                while ((len = in.read(buf)) != -1) {
                	
                    Message result = function.apply((Message) ObjectAndByte.toObject(buf));
                   
                    LobbyManager lm = new LobbyManager();
                    LobbyPlayer playerAdd = lm.clientMessageToLobbyPlayer(result);
                    lm.isChangedSide(playerAdd);
                    
                    if(lm.isPlayerUnique(playerAdd)) {
                    	lobbyPlayers.add(playerAdd);
                    	
                    	 if(numOfConnections < 6) {//max6 including server
                    		 if(lm.checkNumOfConnections(result) != -1) { // The user already exists so update no add new.
                                serverGameObjMsg[lm.checkNumOfConnections(result)] = result.getClientGameObjMsg();

                    			messages[lm.checkNumOfConnections(result)][0] = result.getClientMsg()[0];
           	                    messages[lm.checkNumOfConnections(result)][1] = result.getClientMsg()[1];
           	                    messages[lm.checkNumOfConnections(result)][2] = result.getClientMsg()[2];
                    		 }else { // otherwise add new.
                    			 serverGameObjMsg[numOfConnections] = result.getClientGameObjMsg();

	                         	messages[numOfConnections][0] = result.getClientMsg()[0];
	      	                    messages[numOfConnections][1] = result.getClientMsg()[1];
	      	                    messages[numOfConnections][2] = result.getClientMsg()[2];
	      	                    numOfConnections += 1;
                    		 }
      	                    serverMessage.setServerMsg(messages);
      	                    serverMessage.setServerGameObjMsg(serverGameObjMsg);// changing results server message to contain all previous client messages to be sent out to new clients.
                    	 }
                    	 
                    	
                    	// if unique then we can add it to the GUI.
                    	 Platform.runLater(new Runnable() {
         				    @Override
         				    public void run() {
         				    	
         				    	
         				    	lm.displayLobbyPlayer(playerAdd);
         				    
         				    }
                     	});
                    }

                    for (Socket sc : socketList) {

                        send(sc, serverMessage);

                    }
                }
            } catch (IOException e) {
            	try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
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
            }
        }
    }
    /**
     * This allows the server to send data back to the Clients.
     * @param socket takes the socket of the client
     * @param message take @Message to be sent to the clients.
     */
    public static void send(Socket socket,Message message){
            if(socket != null){
                OutputStream out = null;
                try {
                    out = socket.getOutputStream();
                    out.write(ObjectAndByte.toByteArray(message));
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    
                    
                }
            }
    }   
}
