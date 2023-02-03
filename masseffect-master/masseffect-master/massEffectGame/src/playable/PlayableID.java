package playable;

/**
 * @author joshua hamilton-brown
 * The PlayableID enum class is used to designate an ID to every player game object that is created in the game.
 * The id assigned to each player type controls what actions the object can carry out when it comes to specific actions within the game, and whether the object is player controlled or AI.
 *
 */
public enum PlayableID {
	
	Player(),
	Hider(),
	AIHider(),
	HidingPlayer(),
	AIHidingPlayer(),
	Seeker(),
	AISeeker(),
	HealthSeeker(),
	AIHealthSeeker(),
	SpeedSeeker(),
	AISpeedSeeker(),
	GunSeeker(),
	AIGunSeeker();

}
