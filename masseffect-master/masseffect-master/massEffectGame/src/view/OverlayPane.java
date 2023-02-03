package view;

import javafx.scene.layout.VBox;

/**
 * abstract OverlayPane extends @VBox
 * This also forces any Classes extending it to force setting UI elements
 * @author Owain Edwards
 *
 */
public abstract class OverlayPane extends VBox{
	
	/**
	 * Sets the style of the Overlay pane - making it transparent-like
	 */
	public OverlayPane() {
		
		final String menuStyle = "-fx-background-color:rgba(0,0,0,0.5);";
		setStyle(menuStyle);
		
	}
	/**
	 * Creates GUI element Items: Button and Labels/Images
	 */
	abstract void setUI();

	
	
}
