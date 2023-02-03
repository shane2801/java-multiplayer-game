package view;

import application.Main;
import application.ObjectSpawner;
import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * SeekerErrorPane extends @OverlayPane
 * This class is a pane that will be displayed when the the game is attempted to start when there is not 1 seeker. 
 * @author Owain Edwards
 *
 */
public class SeekerErrorPane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();

	public SeekerErrorPane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(40);
		setVisible(false);
			
		setUI();
	}
	
	@Override
	void setUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
	
		Image seekerErrorImage = new Image(getClass().getResourceAsStream("resources/text/SeekerError.png"));
		Label seekerErrorLbl = new Label();
		seekerErrorLbl.setGraphic(new ImageView(seekerErrorImage));
		
		final String okButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/OK.png'); -fx-background-size: 119px 65px;";
		GameButtonText okButton = new GameButtonText(okButtonStyle, 119, 65);
		okButton.setOnAction(e -> {
			
			setVisible(false);
		
		});
		
		getChildren().addAll(seekerErrorLbl, okButton);
		
	}

}
