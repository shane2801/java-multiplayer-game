package view;

import application.*;
import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.image.Image;

/**
 * PausePane extends @OverlayPane
 * This class will be displayed when the user pauses the game. this will allow them leave the game, to quit the game,
 * and pause the game.
 * @author Owain
 *
 */
public class PausePane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();
	
	public PausePane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(40);
		setVisible(false);
		
		setUI();
	}

	@Override
	void setUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		final String settingsButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Settings.png'); -fx-background-size: 286px 78px;";
		GameButtonText settingsButton = new GameButtonText(settingsButtonStyle, 286, 78);
		settingsButton.setOnAction(e -> { });
		
		final String leaveButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Leave-Game.png'); -fx-background-size: 412px 62px;";
		GameButtonText leaveButton = new GameButtonText(leaveButtonStyle, 412, 62);
		leaveButton.setOnAction(e -> {
			WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
			ObjectSpawner.currentLevelClock = 0;
			Main.propsAndSceneryRendered = false;
			Main.hidersRendered = false;
			Main.seekersRendered = false;
			this.setVisible(false);
			Main.objControl.clearObjects();
			ObjectSpawner.currentLevelClock = 0;});
		
		final String quitButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/Quit.png'); -fx-background-size: 152px 71px;";
		GameButtonText quitButton = new GameButtonText(quitButtonStyle, 152, 71);
		quitButton.setOnAction(e -> System.exit(0));
		
		getChildren().addAll(settingsButton, leaveButton, quitButton);
		//pausePane.getChildren().removeAll(quitButton); // upon clicking settings remove nodes then add new nodes. - possible could do that for normal menu too.
		
		
	}

}
