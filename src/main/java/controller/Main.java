package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.DBConnection;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	//main method
    public static void main(String[] args) {
    	//initializes JavaFX and calls start()
        launch(args);
    }
    
    // is called immediately by main method
    //initializes the gui
	@Override
	public void start(Stage primaryStage) {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("MTG Card Archive");
			
			initRootLayout();
			showCardOverview();
	}
	
	//to execute upon application termination
	@Override
	public void stop() {
		DBConnection.closeConnection();
	}
	
	//initialize the rootlayout from fxml file (main frame)
	public void initRootLayout() {
		try {
			//load the root layout from the fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/RootLayout.fxml"));
			rootLayout = loader.load();
			
			//set the loaded rootlayout as the current scene
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			//set the stage visible
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//initialize the main gui contents
	public void showCardOverview() {
		try {
			//load card overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/CardOverview.fxml"));
			TabPane cardOVerview = loader.load();
			
			//set the loaded card overview into the center of the root layout
			rootLayout.setCenter(cardOVerview);
			
			//give the controller access to the main app
			CardOverviewController controller = loader.getController();
			controller.setMainApp(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
