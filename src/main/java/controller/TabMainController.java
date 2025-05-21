package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabMainController {

	//TabCardCollection tab button
	@FXML
	private Tab cardCollectionTab;
	//TabAddSingleCards tab button
	@FXML
	private Tab addSingleCardsTab;
	//TabPane object
	@FXML
	private TabPane tabMain;

	//reference to the main app
	private Main main;
	
	//intilizes the tabs by loading the corresponding fxmls
	@FXML
	public void initialize() {
		try {
			//load cardCollectionTab
			FXMLLoader cardCollectionLoader = new FXMLLoader(getClass().getResource("/view/TabCardCollection.fxml"));
			Parent cardCollectionTabContent = cardCollectionLoader.load();
			TabCardCollectionController tabCardCollectionController = cardCollectionLoader.getController();
			tabCardCollectionController.setMainApp(main); 
			cardCollectionTab.setContent(cardCollectionTabContent);
			//load addSingleCardsTab
			FXMLLoader addSingleCardsLoader = new FXMLLoader(getClass().getResource("/view/TabAddSingleCards.fxml"));
			Parent addSingleCardsTabContent = addSingleCardsLoader.load();
			TabAddSingleCardsController tabAddSingleCardsController = addSingleCardsLoader.getController();
			tabAddSingleCardsController.setMainApp(main);
			addSingleCardsTab.setContent(addSingleCardsTabContent);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//save the main app reference
	public void setMainApp(Main main) {
		this.main = main;
	}
	
	

}
