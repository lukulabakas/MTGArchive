package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.Card;
import persistence.CardDAO;

public class TabCardCollectionController {

	@FXML
	private TextField cardNameField;
	@FXML
	private TextField cardSetField;
	@FXML
	private TextField cardTextField;
	@FXML
	private TextField cardTypeField;
	//chosing for which colors to search
	@FXML
	private CheckBox whiteCB;
	@FXML
	private CheckBox blueCB;
	@FXML
	private CheckBox blackCB;
	@FXML
	private CheckBox greenCB;
	@FXML
	private CheckBox redCB;
	@FXML
	private CheckBox colorlessCB;
	//chosing whether the card is holo or not
	@FXML
	private CheckBox holoCB;
	//choosing card language
	@FXML
	private ComboBox<String> languageComboBox;
	
	@FXML
	private FlowPane flowPane;
	//label that displays the number of cards found out of cards in collection
	@FXML
	private Label numberCardsFound;
	
	//reference to the main application
	private MainMTGArchive main;

	
	//contructor
	public TabCardCollectionController() {
		
	}
	
	//intiliazie the controller class; automatically called after the fxml file has been loaded
	@FXML
	private void initialize() {
		//define the options in the languageComboBox
		languageComboBox.setItems(FXCollections.observableArrayList("Any", "English", "German"));
		//define distance between displayed cards
		flowPane.setHgap(5);
		flowPane.setVgap(5);
	}
	
	public void setMainApp(MainMTGArchive main) {
		this.main = main;
	}
	
	//handle the search
	@FXML
	private void handleSearch() {
		flowPane.getChildren().clear();
		try {
			//create Dummy card that represents all user input criteria
			Card searchedCard = new Card();
			//set the parameters of this card based on the search parameters
			searchedCard.setCardName(cardNameField.getText());
			searchedCard.setCardSet(cardSetField.getText());
			searchedCard.setCardText(cardTextField.getText());
			searchedCard.setCardType(cardTypeField.getText());
			//set the cardColorIdentity
			List<String> cardColorIdentityList = new ArrayList<>();
			if(blackCB.isSelected()) {
				cardColorIdentityList.add("B");
			}
			if(greenCB.isSelected()) {
				cardColorIdentityList.add("G");
			}
			if(redCB.isSelected()) {
				cardColorIdentityList.add("R");
			}
			if(blueCB.isSelected()) {
				cardColorIdentityList.add("U");
			}
			if(whiteCB.isSelected()) {
				cardColorIdentityList.add("W");
			}
			if(colorlessCB.isSelected()) {
				cardColorIdentityList.add("");
			}
			searchedCard.setCardColorIdentity(cardColorIdentityList.toArray(new String[0]));
			//set whether the searched card is holo or not
			if(holoCB.isSelected()) {
				searchedCard.setCardHolo(true);
			}
			//set language choice
			searchedCard.setCardLanguage(languageComboBox.getValue());
			//instantiate CardDAO instance
			CardDAO carddao = new CardDAO();
			//call getCardsFromDB with searchedCard as param
			//retrieves all cards that match all values stored in searchedCard
			List<Card> cards = new ArrayList<>();
			cards = carddao.getCardsFromDB(searchedCard);
			//helper variable to count number of cards found
			int numberOfCardsFound = 0;
			//iterate retrieved cards
			for(Card card : cards) {
				//add the number of copies of current card to total cards found
				numberOfCardsFound += card.getCardQuantity();
				//load for each card, create new cardPanel and add that panel to the flowPane
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/cardPanel.fxml"));
				BorderPane cardPanel = loader.load();
				CardPanelController controller = loader.getController();
				//setCardData sets all labels etc. of the CardPanel according to card attributes
				controller.setCardData(card);
				//give FlowPane to CardPanel as parent
				controller.setParentFlowPane(flowPane);
				flowPane.getChildren().add(cardPanel);
			}
			//update search count results
			numberOfSearchResults(numberOfCardsFound);
		} catch (SQLException e) {
			//info to user that search failed
			numberCardsFound.setText("Retrieving Cards failed");
			e.printStackTrace();
		} catch (IOException e) {
			//info to user that search failed
			numberCardsFound.setText("Retrieving Cards failed");
			e.printStackTrace();
		}
	}
	
	private void numberOfSearchResults(int numberOfCardsFound) {
		CardDAO carddao = new CardDAO();
		try {
			numberCardsFound.setText(numberOfCardsFound + " / " + carddao.totalNumberOfCards() + " Cards found");
		} catch (SQLException e) {
			//dummy statement in case retrieving total card count fails
			numberCardsFound.setText(numberOfCardsFound + " out of many Cards found");
			e.printStackTrace();
		}
	}
}
