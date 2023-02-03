package networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import guiItems.LobbyPlayer;
import view.LobbyScene;
/**
 * LobbyManager deals with all requests and operation that a user may execute in the Lobby.
 * @author Owain Edwards
 *
 */
public class LobbyManager {
	 /**
	  * 
	  * @param message takes in a @Message . this is then read and converted to @LobbyPlayer
	  * @return a @LobbyPlayer based on the message received from the client
	  */
	 public LobbyPlayer clientMessageToLobbyPlayer(Message message) {
     	LobbyPlayer lp = new LobbyPlayer(message.getClientMsg()[0], message.getClientMsg()[1], message.getClientMsg()[2]);
     	return lp;
     }
	 /**
	  * 
	  * @param player takes a String[] array to be converted to a @LobbyPlayer
	  * @return a @LobbyPlayer based on a string input.
	  */
	 public LobbyPlayer stringArrayToLobbyPlayer(String[] player) {
		 LobbyPlayer lp = new LobbyPlayer(player[0], player[1], player[2]);
		 return lp;
	 }
     /**
      * 
      * @param lp takes a @LobbyPlayer and checks to see if it's unique within the lobby.
      * @return true or false depending on result 
      */
     public boolean isPlayerUnique(LobbyPlayer lp) {
     	
     	boolean unique = true;
     	
     	for(LobbyPlayer player: Server.lobbyPlayers) {
     		if(lp.getName().equals(player.getName())) {
     			unique = false;
     			break;
     		}
     	}
     	
     	return unique;
     }
     /**
      * Checks if the player has changed sides on the UI. The @Server will then deal with updating other clients.
      * @param lp takes @LobbyPlayer
      */
     public void isChangedSide(LobbyPlayer lp) {
    	 
    		for(int i = 0; i < Server.lobbyPlayers.size(); i++) {
	       		if(lp.getName().equals(Server.lobbyPlayers.get(i).getName())) {
	       			Server.lobbyPlayers.remove(i);
	       			break;
	       		}
	       	}
     }
     /**
      * Computation to check GUI elements to decide whenther the @LobbyPlayer needs to be added or not.
      * This can be added to seekerPane or hiderPane.
      * @param lp takes @LobbyPlayer to display it in lobby.
      */
     public void displayLobbyPlayer(LobbyPlayer lp) {
    	 
    	 boolean inSeeker = false;
    	 boolean inHider = false;
    	 if(!lp.getName().equals(LobbyScene.playableYou.getName())) {
    		 
    		 if(lp.getSide().equals("Seeker")) {
        		 
        		 for(int i = 1; i < LobbyScene.seekerPane.getChildren().size(); i++) {
            		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.seekerPane.getChildren().get(i);
            		 
            		 if(testPlayer.getName().equals(lp.getName())) {
            			 inSeeker = true;
            		 }
        			 
            	 }
            	 for(int i = 1; i < LobbyScene.hiderPane.getChildren().size(); i++) {
            		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.hiderPane.getChildren().get(i);

            		 if(testPlayer.getName().equals(lp.getName())) {
            			 inHider = true;
            		 }
            	 }
            	 
            	 if(!inSeeker && !inHider) {
            		 LobbyScene.seekerPane.getChildren().add(lp);
            		 
            	 }
            	 inSeeker = false;
            	 inHider = false;
        		 
        	 }
        	 else if(lp.getSide().equals("Hider")) {
        		 
        		 for(int i = 1; i < LobbyScene.seekerPane.getChildren().size(); i++) {
            		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.seekerPane.getChildren().get(i);
            		 
            		 if(testPlayer.getName().equals(lp.getName())) {
            			 inSeeker = true;
            		 }
        			 
            	 }
            	 for(int i = 1; i < LobbyScene.hiderPane.getChildren().size(); i++) {
            		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.hiderPane.getChildren().get(i);

            		 if(testPlayer.getName().equals(lp.getName())) {
            			 inHider = true;
            		 }
            	 }
            	 
            	 if(!inSeeker && !inHider) {
            		 LobbyScene.hiderPane.getChildren().add(lp);
            		 
            	 }
            	 inSeeker = false;
            	 inHider = false;
        		 
        	 }
        	 
        	 for(int i = 1; i < LobbyScene.seekerPane.getChildren().size(); i++) {
        		 
        		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.seekerPane.getChildren().get(i);
        		 if(lp.getSide().equals("Hider")) { // want to change to hider.
        			 
        			 if(lp.getName().equals(testPlayer.getName())) {
        				 LobbyScene.seekerPane.getChildren().remove(i);
        				 LobbyScene.hiderPane.getChildren().add(lp);
        				 break;
        			 }
        		 }
        		 else if(lp.getSide().equals("Seeker")) {
        			 // do nothing because already seeker. 
        		 }
        	 }
        	 for(int i = 1; i < LobbyScene.hiderPane.getChildren().size(); i++) {
        		 
        		 LobbyPlayer testPlayer = (LobbyPlayer) LobbyScene.hiderPane.getChildren().get(i);
        		 if(lp.getSide().equals("Seeker")) { // want to change to seeker.
        			 
        			 if(lp.getName().equals(testPlayer.getName())) {
        				 LobbyScene.hiderPane.getChildren().remove(i);
        				 LobbyScene.seekerPane.getChildren().add(lp);
        				 break;
        			 }
        			 
        		 }
        		 else if(lp.getSide().equals("Hider")) {
        			 // do nothing because already hider. 
        		 }     		 
        	 }        	 
    	 } 
     }
     /**
      * 
      * @param m takes @Message to determine which connection this client is in respect of the index of the messages array in @Server
      * @return the index of that client within server.
      */
     int checkNumOfConnections(Message m) {
    	 
    	 String name = m.getClientMsg()[0];
    	 
    	 int index = -1;
    	 
    	 for(int i = 0; i < Server.messages.length; i++) {
    		 
    		 if(name.equals(Server.messages[i][0])) {
    			 index = i;
    			 break;
    		 }
      		 
    	 }
    	 return index;
    	 
     }
     /**
      * send data to @Server to ensure that all Client's lobbies are updated according to the side change requested by @Client
      */
     public void serverRequestSideChange() {
    	 
    	// If seeker change data to Hider then re-send for it to update.
    	if(Server.serverMessage.getServerMsg()[0][2].equals("Seeker")) {
    		Server.serverMessage.getServerMsg()[0][2] = "Hider";
    	}
    	else if(Server.serverMessage.getServerMsg()[0][2].equals("Hider")) {
    		Server.serverMessage.getServerMsg()[0][2] = "Seeker";

    	}
    	
    	for(Socket sc : Server.socketList) {
    		//re-sending to each client. 
    		send(sc, Server.serverMessage);
    	}
    	
    	 
     }
     /**
      * 
      * @param socket takes the socket of the client / or the server socket to send a message to.
      * @param message takes the @Message to be sent.
      */
     public void send(Socket socket, Message message){
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
