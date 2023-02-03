package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * BlindSeekerPane extends @OverlayPane. This pane will be used to display a black pane for the @Seeker in order for the @Hider's to hide
 * This will be displayed for a given length of time.
 * @author Owain Edwards
 *
 */
public class BlindSeekerPane extends OverlayPane{

	/**
	 * Constructor to set the style to black
	 * This also sets Alignment, spacing and visibility along with the UI items.
	 */
	public BlindSeekerPane() {
		
		final String menuStyle = "-fx-background-color:rgba(1,1,1,1);";
		setStyle(menuStyle);
		
		setAlignment(Pos.CENTER);
		setVisible(false);
			
		setUI();
	}
	/**
	 * Creates the Image information that's displayed on the @Seeker's screen
	 */
	@Override
	void setUI() {
	
		Image seekerInfoImage = new Image(getClass().getResourceAsStream("resources/text/SeekersInfo.png"));
		Label seekerInfoLbl = new Label();
		seekerInfoLbl.setGraphic(new ImageView(seekerInfoImage));
		
		getChildren().add(seekerInfoLbl);
		
	}

}
