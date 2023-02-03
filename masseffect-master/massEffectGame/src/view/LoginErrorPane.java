package view;

import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
/**
 * LoginErrorPane extends @OverlayPane
 * This class will set up the pane if there is a login error. 
 * @author Owain Edwards
 *
 */
public class LoginErrorPane extends OverlayPane{

	private SceneManager sm = new SceneManager();
	
	public LoginErrorPane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(15);
		setVisible(false);
		
		setUI();
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	@Override
	void setUI() {
	
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		Image errorLblImage = new Image(getClass().getResourceAsStream("resources/text/Unable-to-Login.png"));
		Label errorLbl = new Label();
		errorLbl.setGraphic(new ImageView(errorLblImage));
		
		final String okButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/OK.png'); -fx-background-size: 119px 65px;";
		GameButtonText okButton = new GameButtonText(okButtonStyle, 119, 65);
		okButton.setOnAction(e -> setVisible(false));
		
		getChildren().addAll(errorLbl, okButton);
		
	}

}
