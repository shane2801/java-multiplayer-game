package view;

import audio.MusicManager;
import guiItems.GameButtonText;
import javafx.animation.ParallelTransition;
import javafx.beans.Observable;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * SettingsScene extends @GameScene
 * This class will display a scene in which shows all settings available in the game.
 * This includes audio options.
 * @author Owain Edwards
 * @author Heh Shyang Lee
 */
public class SettingsScene extends GameScene{
	
	private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	private static StackPane settingsStack = new StackPane();
	private VBox settingsPane;
	private SceneManager sm = new SceneManager();
	private MusicManager mm = new MusicManager();
	
	ParallelTransition settingsSceneAnimations = new ParallelTransition();
	ParallelTransition settingsSceneBackgroundAnimation = new ParallelTransition();

	public SettingsScene() {
		super(settingsStack);
		
		settingsPane = new VBox();
		settingsPane.setAlignment(Pos.TOP_CENTER);
		settingsPane.setSpacing(40);
		
		createUI();
		createBackground();
		mm.playMenuSong();
		
	}

	@Override
	void createUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(settingsPane, gameTitleImage).play();
		
		Image settingsLblImage = new Image(getClass().getResourceAsStream("resources/text/Settings.png"));
		Label settingsLbl = new Label();
		settingsLbl.setGraphic(new ImageView(settingsLblImage));
		
		Image resolutionLblImage = new Image(getClass().getResourceAsStream("resources/text/Resolution.png"));
		Label resolutionLbl = new Label();
		resolutionLbl.setGraphic(new ImageView(resolutionLblImage));
		
		Image audioLblImage = new Image(getClass().getResourceAsStream("resources/text/Audio.png"));
		Label audioLbl = new Label();
		audioLbl.setGraphic(new ImageView(audioLblImage));

		Image bgmLblImage = new Image(getClass().getResourceAsStream("resources/text/BGM.png"));
		Label bgmLbl = new Label();
		bgmLbl.setGraphic(new ImageView(bgmLblImage));

		ObservableList<String> resolutionOptions = 
			    FXCollections.observableArrayList(
			        "Option 1",
			        "1920 x 1080",
			        "Option 3"
			    );
		
		final ComboBox<String> resolutionComboBox = new ComboBox<String>(resolutionOptions);
		
		final String backButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Back.png'); -fx-background-size: 177px 65px;";
		GameButtonText backButton = new GameButtonText(backButtonStyle, 177, 65);
		backButton.setOnAction(e -> {WindowManager.mainStage.setScene(WindowManager.startScene); WindowManager.startScene.getUserInterfaceAnimation().play(); settingsSceneBackgroundAnimation.stop();});
		
		final String muteButtonStyle = "-fx-background-colour: transparent; -fx-background-image: url('/guiItems/guiResources/mute.png'); -fx-background-size: 40px 40px;";
		GameButtonText bgmMuteButton = new GameButtonText(muteButtonStyle, 40, 40);
		bgmMuteButton.setOnAction(e -> {mm.musicOnOff();});
		
		Slider bgmSlider = new Slider();
		bgmSlider.setValue(mm.getMusicVolume() * 100);
		bgmSlider.setMaxWidth(120);
		bgmSlider.valueProperty().addListener(new InvalidationListener() {
            
            @Override
            public void invalidated(Observable observable) {
                mm.setMusicVolume(bgmSlider.getValue() / 100);
                
            }
        });

		settingsPane.getChildren().addAll(settingsLbl, resolutionLbl, resolutionComboBox, audioLbl, bgmLbl, bgmSlider, bgmMuteButton, backButton);
	}
		

	@Override
	void createBackground() {
		
		Image backgroundImage = new Image("view/resources/backgroundEmpty.png", screenSize.getWidth()*2, screenSize.getHeight(), false, true);
		settingsSceneBackgroundAnimation = sm.loopBackground( settingsStack, backgroundImage )[0];
		settingsStack.getChildren().add(settingsPane);
		
	}

	@Override
	ParallelTransition getUserInterfaceAnimation() {
		return settingsSceneAnimations;
	}

	@Override
	ParallelTransition getBackgroundAnimation() {
		return settingsSceneBackgroundAnimation;
	}

}
