package view;

import guiItems.GameButtonText;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConnectionLostPane extends OverlayPane{
	
	private SceneManager sm = new SceneManager();

	public ConnectionLostPane(){
		
		setAlignment(Pos.TOP_CENTER);
		setSpacing(40);
		setVisible(false);
		
		setUI();
	}

	@Override
	void setUI() {
		
		Image gameTitleImage = new Image(getClass().getResourceAsStream("resources/text/Lost-And-Found.png"));
		sm.scaleGameLogo(this, gameTitleImage).play();
		
		Image connectionLostLblImage = new Image(getClass().getResourceAsStream("resources/text/Connection-Lost.png"));
		Label connectionLostLbl = new Label();
		connectionLostLbl.setGraphic(new ImageView(connectionLostLblImage));
		
		final String okButtonStyle = "-fx-background-color: transparent; -fx-background-image: url('/guiItems/guiResources/OK.png'); -fx-background-size: 119px 65px;";
		GameButtonText okButton = new GameButtonText(okButtonStyle, 119, 65);
		okButton.setOnAction(e -> {
			
			System.exit(0);
//			WindowManager.mainStage.setScene(WindowManager.gameSelectScene);
		});
		
		getChildren().addAll(connectionLostLbl, okButton );
	}

}
