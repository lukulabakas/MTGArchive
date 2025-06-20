package controller;

import java.sql.SQLException;

import api.ScryfallApiClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Card;
import persistence.CardDAO;

public class TabAddSingleCardsController {

	//user input for the search
	@FXML
	private TextField cardNameText;
	@FXML
	private TextField cardSetText;
	//displays the card that was found
	@FXML
	private ImageView cardImageView;
	//main app reference
	private MainMTGArchive main;
	//saves the saerched card
	private Card fetchedCard;
	//info prompt for additional info
	@FXML
	private Label infoPrompt;
	//button to add the card to collection; needs to be activated/deactivated
	@FXML
	private Button addButton;
	//choice of language of the card to add
	@FXML
	private ComboBox<String> languageComboBox;
	//checkbox to check if the card is holo
	@FXML
	private CheckBox cardHoloCheckBox;
	//Tooltips
	private final Tooltip cardNameTooltip = new Tooltip("Card Name cannot be empty");
	private final Tooltip cardSetTooltip = new Tooltip("Card Set cannot be empty");
	private final Tooltip languageTooltip = new Tooltip("Please select a language");
	
	
	@FXML
	public void initialize() {
		languageComboBox.setItems(FXCollections.observableArrayList("English", "German"));
		resetCardImageView();
	}
	
	//save the main app reference
	public void setMainApp(MainMTGArchive main) {
		this.main = main;
	}
	
	//handle the search for a card via cardname & cardset
	@FXML
	private void handleSearch() {
	//cleanup
		//reset Tooltips
		cardNameText.setTooltip(null);
		cardSetText.setTooltip(null);
		//disable the add button for validation
		addButton.setDisable(true);
		//reset card image
		resetCardImageView();
		//track if all inputs were correct and search was valid
		boolean isSearchValid = true;
		//remove previous error borders
		removeErrorBorder(cardNameText);
		removeErrorBorder(cardSetText);
		//reset infoPrompt Text
		infoPrompt.setText("Enter Card Name and Set");
	//validation & api call
		//check if input fields are correct
		isSearchValid = validateUserSearchInputs();
		//When the search was not valid (wrong inputs) the user gets info to hover his inputs for more info
		if(!isSearchValid) {
			infoPrompt.setText("Hover inputs for info!");
		}
		//when the search was valid api call is performed
		else {
			try {
				//api call
				ScryfallApiClient apiClient = new ScryfallApiClient();
				//trim() to delete all spaces after the cardname
				fetchedCard = apiClient.fetchCardFromApi(cardNameText.getText().trim(), cardSetText.getText().trim());
				//if the call is successfull set the card image
				cardImageView.setImage(new Image(fetchedCard.getCardImageUrl()));
				//give info to user that the card was found
				infoPrompt.setText("Card found");
				//activate the addCardButton
				addButton.setDisable(false);
			}catch(Exception e) {
				//info to user that his card was not found
				infoPrompt.setText("Card was not found");
				//reset the fetchedCard
				fetchedCard = null;
				e.printStackTrace();
			}
		}	
	}
	
	//helper method that applies the error-border to parent if it is not already
	private void addErrorBorder(Parent parent) {
		if(!parent.getStyleClass().contains("error-border")) {
			parent.getStyleClass().add("error-border");
		}
	}
	
	//helper method to remove the error border from parent if there is one
	private void removeErrorBorder(Parent parent) {
		if(parent.getStyleClass().contains("error-border")) {
			parent.getStyleClass().remove("error-border");
		}
	}
	
	//Helper method to validate user inputs for the card search
	private boolean validateUserSearchInputs() {
		boolean isSearchValid = true;
		//check if the cardname textfield is empty
		if(cardNameText.getText().trim().isEmpty()) {
			addErrorBorder(cardNameText);
			cardNameText.setTooltip(cardNameTooltip);
			isSearchValid = false;
		}
		//check if the cardset textfield is empty
		if(cardSetText.getText().trim().isEmpty()) {
			addErrorBorder(cardSetText);
			cardSetText.setTooltip(cardSetTooltip);
			isSearchValid = false;
		}
		return isSearchValid;
	}
	
	@FXML
	private void handleAddToCollection() {
		//reset error border
		removeErrorBorder(languageComboBox);
		//reset error tooltip
		languageComboBox.setTooltip(null);
		//validate user input
		boolean isAddValid = validateUserAddToCollectionInputs();
		//if input is not valid, stop
		if(!isAddValid) {
			return;
		}
		//when input was valid proceed
		//assemble card
		fetchedCard.setCardHolo(cardHoloCheckBox.isSelected());
		fetchedCard.setCardLanguage(languageComboBox.getValue());
		try {
			//add a new card or update the existing card in db
			CardDAO cardDao = new CardDAO();
			cardDao.addOrUpdateCard(fetchedCard);
		}catch(SQLException f) {
			f.printStackTrace();
			infoPrompt.setText(f.getMessage());
			return;
		}catch(Exception e) {
			e.printStackTrace();
			infoPrompt.setText("Could not add Card");
		}
		//deactivate the addButton after the current card was added
		addButton.setDisable(true);
		//update info prompt to let user know
		infoPrompt.setText("Card added to Collection");
		//reset imageView + cardNameText + cardSetText
		resetCardImageView();
		cardNameText.setText("");
		cardSetText.setText("");
	}
	
	//helper method to validate user inputs when adding a card to the collection
	//card info is taken from fetchedCard (already validated)
	//this method validates the languageComboBox
	private boolean validateUserAddToCollectionInputs() {
		boolean isAddValid = true;
		//if nothing is selected the validation fails
		if(languageComboBox.getValue() == null) {
			addErrorBorder(languageComboBox);
			languageComboBox.setTooltip(languageTooltip);
			isAddValid = false;
			infoPrompt.setText("Select a language");
		}
		return isAddValid;
	}
	
	//helper method that resets the imageview to card back image
	private void resetCardImageView() {
		cardImageView.setImage(new Image(getClass().getResource("/images/magicCardBack.jpeg").toExternalForm()));
	}
}
