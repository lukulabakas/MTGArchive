package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import api.ScryfallApiClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
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
			
	        //set the application icon
	        this.primaryStage.getIcons().add(new Image("/images/mtgIcon.png"));
			initRootLayout();
			showTabMain();
	}
	
	//to execute upon application termination
	@Override
	public void stop() {
		//close db connection
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
			
            //give the controller access to the main app
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

			//set the stage visible
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//initialize the main gui contents
	public void showTabMain() {
		try {
			//load the TabPane that will hold the Tabs
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/TabMain.fxml"));
			TabPane tabMain = loader.load();
			
			//set the TabPane into the center of the root layout
			rootLayout.setCenter(tabMain);
			
			//give the controller access to the main app
			TabMainController controller = loader.getController();
			controller.setMainApp(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
