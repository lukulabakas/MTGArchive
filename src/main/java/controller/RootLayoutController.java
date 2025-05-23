package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RootLayoutController {

	//reference to the main app
	private Main main;
	
	//ger reference to main app
	public void setMainApp(Main main) {
		this.main = main;
	}
	
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MTG Card Archive");
		alert.setHeaderText("About");
		alert.setContentText("So many cards...");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/mtgIcon.png"));
		
		alert.showAndWait();
	}
}
