package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TabAddSingleCardsController {

	@FXML
	private ImageView cardImageView;
	//main app reference
	private Main main;
	
	
	@FXML
	public void initialize() {
		cardImageView.setImage(new Image(getClass().getResource("/images/magicCardBack.jpeg").toExternalForm()));
	}
	
	//save the main app reference
	public void setMainApp(Main main) {
		this.main = main;
	}
}
