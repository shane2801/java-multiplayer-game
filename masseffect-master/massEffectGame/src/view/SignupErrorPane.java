package view;

import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
/**
 * SignupErrorPane extends @OverlayPane
 * This class will be used to display a pane that informs the user that their sign-up was unsuccessful
 * @author Owain Edwards
 *
 */
public class SignupErrorPane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();
	
	public SignupErrorPane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(15);
		setVisible(false);
		
		setUI();
	
	}

	@Override
	void setUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		Image errorLblImage = new Image(getClass().getResourceAsStream("resources/text/error.png"));
		Label errorLbl = new Label();
		errorLbl.setGraphic(new ImageView(errorLblImage));
		
		final String okButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/OK.png'); -fx-background-size: 119px 65px;";
		GameButtonText okButton = new GameButtonText(okButtonStyle, 119, 65);
		okButton.setOnAction(e -> setVisible(false));
		
		getChildren().addAll(errorLbl, okButton);
		
	}

}
