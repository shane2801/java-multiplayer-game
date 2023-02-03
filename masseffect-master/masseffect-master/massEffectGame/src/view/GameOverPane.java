package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * GameOverPane extends @OverlayPane to display a transparent-like pane over when the game is over.
 * This will contain information on who won the game.
 * @author Owain Edwards
 *
 */
public class GameOverPane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();
	/**
	 * Constructor to set Alignment, spacing, visibility and UI items.
	 */
	public GameOverPane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(40);
		setVisible(false);
		
		setUI();
		
	}
	/**
	 * this creates the game title image along with animations and the Label to display the information on which team won.
	 */
	@Override
	void setUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		Image teamWonLblImage = new Image(getClass().getResourceAsStream("resources/text/Team-Won.png"));
		Label teamWonLbl = new Label();
		teamWonLbl.setGraphic(new ImageView(teamWonLblImage));
		getChildren().addAll(teamWonLbl);
		
	}

}
