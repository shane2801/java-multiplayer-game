package view;

import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
/**
 *  SignupSuccessPane extends @OverlayPane
 *  This class can be created to display a pane to inform the user that their sign-up was successful.
 * @author Owain Edwards
 *
 */
public class SignupSuccessPane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();

	public SignupSuccessPane() {
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(15);
		setVisible(false);
		
		setUI();
	}

	@Override
	void setUI() {
	
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		Image successLblImage = new Image(getClass().getResourceAsStream("resources/text/Account-Created.png"));
		Label successLbl = new Label();
		successLbl.setGraphic(new ImageView(successLblImage));
		
		final String okButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/OK.png'); -fx-background-size: 119px 65px;";
		GameButtonText okButton = new GameButtonText(okButtonStyle, 119, 65);
		okButton.setOnAction(e -> setVisible(false));
		
		getChildren().addAll(successLbl, okButton);
		
		
	}

}
