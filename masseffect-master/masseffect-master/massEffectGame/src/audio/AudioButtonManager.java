package audio;

import java.net.URL;

import javafx.scene.media.AudioClip;

/**
 * AudioButtonManager can be used to play sounds effects.
 * @author Owain
 *
 */
public class AudioButtonManager {
	
	URL hoverResource = getClass().getResource("/audio/files/hover.mp3");
	AudioClip hoverSound = new AudioClip(hoverResource.toString());
	
	URL clickResource = getClass().getResource("/audio/files/clickButton.mp3");
	AudioClip clickSound = new AudioClip(clickResource.toString());
	/**
	 * plays the hoverSound
	 */
	public void playHover() {
		hoverSound.play();
	}
	/**
	 * plays the clickSound
	 */
	public void playClick() {
		clickSound.play();
	}

	
}
