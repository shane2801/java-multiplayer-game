package networking;

import java.io.Serializable;
/**
 * Message is used to create teh message that we wish to send between @Client and @Server
 * @author Owain Edwards
 *
 */
public class Message implements Serializable {

    private String clientName;
    private String[] clientMsg;
    private String[][] serverMsg;
    private GameObjectMessage[] serverGameObjMsg;
    private GameObjectMessage clientGameObjMsg;
    /**
     * @param clientName takes a String which allows the Programmer to identify how the message is sent.
     * @param clientMsg takes a String array which contains the data that that @LobbyPlayer needs to be created.
     * @param serverMsg takes a 2D array in which the server uses to send all @lobbyPlayer elements to the client so are able to update their @LobbyScene
     * @param clientGameObjMsg takes a @GameObjectMessage to send to the server in order to update the servers version of the game.
     * @param serverGameObjMsg takes a @GameObjectMessage array to send to the Clients to update all other clients games. 
     */
	public Message(String clientName,String clientMsg[], String[][] serverMsg, GameObjectMessage clientGameObjMsg, GameObjectMessage[] serverGameObjMsg) {
        this.clientName = clientName;
        this.clientMsg = clientMsg;
        this.serverMsg = serverMsg;
        this.serverGameObjMsg = serverGameObjMsg;
        this.clientGameObjMsg = clientGameObjMsg;

    }
    /**
     * client name being used as info for debugging.
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }
    /**
     * 
     * @return clientMsg
     */
    public String[] getClientMsg() {
        return clientMsg;
    }
    /**
     * 
     * @return serverMsg
     */
    public String[][] getServerMsg() {
        return serverMsg;
    }
    /**
     * 
     * @param message takes String[][] to set the message of the server.
     */
    public void setServerMsg(String[][] message) {
    	serverMsg = message;
    }
    /**
     * 
     * @return serverGameObjMsg
     */
	public GameObjectMessage[] getServerGameObjMsg() {
		return serverGameObjMsg;
	}
	/**
	 * 
	 * @param serverGameObjMsg takes @GameObjectMessage Array to be set in order to send to all clients.
	 */
	public void setServerGameObjMsg(GameObjectMessage[] serverGameObjMsg) {
		this.serverGameObjMsg = serverGameObjMsg;
	}
	/**
	 * 
	 * @return clientGameObjMsg
	 */
	public GameObjectMessage getClientGameObjMsg() {
		return clientGameObjMsg;
	}
	/**
	 * 
	 * @param clientGameObjMsg takes @GameObjectMessage to be set.
	 */
	public void setClientGameObjMsg(GameObjectMessage clientGameObjMsg) {
		this.clientGameObjMsg = clientGameObjMsg;
	}
	/**
	 * 
	 * @param name takes a String to set the ClientName.
	 */
    public void setClientName(String name){
        clientName = name;
    }
}
