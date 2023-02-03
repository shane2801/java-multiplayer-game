package audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * MusicManager allows music to be played in the game.
 * @author Owain
 * @author Heh Shyang Lee
 */
public class MusicManager{
	
	String menuMusicFilePath = "massEffectGame/src/audio/files/music/menuSong.mp3";
	
	boolean musicOn = true;

	Media menuMusicFile = new Media(new File(menuMusicFilePath).toURI().toString());
	MediaPlayer menuMusicPlayer = new MediaPlayer(menuMusicFile);
	/**
	 * Method to play the menu song.
	 */
	public void playMenuSong() {
		
		menuMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		menuMusicPlayer.play();
	}
	/**
	 * Method to turn on or off the background music
	 */
	public void musicOnOff() {
		if (musicOn) {
			musicOn = false;
			menuMusicPlayer.stop();
		} else if (!musicOn) {
			musicOn = true;
			playMenuSong();
		}
	}
	/**
	 * 
	 * @return volume of music
	 */
	public double getMusicVolume() {
		return menuMusicPlayer.getVolume();
	}
	/**
	 * 
	 * @param volume takes double to set the volume of music
	 */
	public void setMusicVolume(double volume) {
		menuMusicPlayer.setVolume(volume);
	}
}
