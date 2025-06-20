package controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.Card;
import persistence.CardDAO;

public class CardPanelController {

	@FXML
	private Label cardNameLabel;
	@FXML
	private Label cardCountLabel;
	@FXML
	private ImageView cardImageView;
	@FXML
	private ImageView cardHoloImageView;
	@FXML
	private ImageView cardLanguageImageView;
	@FXML
	private Button addButton;
	@FXML
	private Button removeButton;
	@FXML
	private BorderPane borderPane;
	//save Card object
	private Card card;
	//reference to the parent FlowPane
	private FlowPane flowPane;
	//reference to the main application
	private MainMTGArchive main;
	
	//contructor
	public CardPanelController() {
		
	}
	
	//intiliazie the controller class; automatically called after the fxml file has been loaded
	@FXML
	private void initialize() {
		
	}
	
	public void setMainApp(MainMTGArchive main) {
		this.main = main;
	}
	
	public void setParentFlowPane(FlowPane parent) {
		this.flowPane = parent;
	}
	
	//receives a Card object and sets all label etc. to represent the given card
	public void setCardData(Card card) {
		//save card object for further use
		this.card = card;
		//display cardname
		cardNameLabel.setText("[" + card.getCardSet().toUpperCase() + "] " + card.getCardName());
		cardNameLabel.setTooltip(new Tooltip(card.getCardName()));
		//display the quantity of the card in collection
		cardCountLabel.setText(Integer.toString(card.getCardQuantity()));
		//the url to the cards image is stored in cardImageUrl
		cardImageView.setImage(new Image(card.getCardImageUrl()));
		//card gets an icon if its holo
		if(card.getCardHolo()) {
			cardHoloImageView.setImage(new Image(getClass().getResource("/images/cardHolo.png").toExternalForm()));
		}
		//language icon is decided by the cards attribute cardLanguage (can either be "eng" or "ger")
		switch(card.getCardLanguage().trim())
		{
			case "English": {
				cardLanguageImageView.setImage(new Image(getClass().getResource("/images/englandFlag.png").toExternalForm()));
				break;
			}
			case "German": {
				cardLanguageImageView.setImage(new Image(getClass().getResource("/images/germanyFlag.png").toExternalForm()));
				break;
			}
			default: {
				break;
			}
		}
	}
	
	//"+" Button to add one copy of the card
	@FXML
	private void handleAddCard() {
		//add one copy of the card and save to cardQuantity attribute
		card.setCardQuantity(card.getCardQuantity() + 1);
		//update the label
		cardCountLabel.setText(Integer.toString(card.getCardQuantity()));
		//save changes to db
		CardDAO cardDao = new CardDAO();
		try {
			cardDao.updateCardInDB(card);
		} catch (SQLException e) {
			//if card update in DB fails, reset card attribute and label
			card.setCardQuantity(card.getCardQuantity() - 1);
			cardCountLabel.setText(null);
			e.printStackTrace();
		}
	}
	
	//"-" Button to remove one copy of the card
	@FXML
	private void handleRemoveCard() {
		//remove one copy of the card and save to cardQuantity attribute
		card.setCardQuantity(card.getCardQuantity() - 1);
		//update Label
		cardCountLabel.setText(Integer.toString(card.getCardQuantity()));
		//if there is 0 copies of a card (or by any error less than 0)
		if(card.getCardQuantity() < 1) {
			//disable buttons to make sure there cannot be -1 copy of a card
			addButton.setDisable(true);
			removeButton.setDisable(true);
			//remove CardPanel object from the flowPane
			removeSelf();
		}
		//update card quantity in db
		CardDAO cardDao = new CardDAO();
		try {
			cardDao.updateCardInDB(card);
		} catch (SQLException e) {
			//if card update in DB fails, reset card attribute and label
			card.setCardQuantity(card.getCardQuantity() + 1);
			cardCountLabel.setText(Integer.toString(card.getCardQuantity()));
			//enable buttons again
			addButton.setDisable(false);
			removeButton.setDisable(false);
			e.printStackTrace();
		}
	}
	
	//removes the current CardPanel from its parent flowPane
	private void removeSelf() {
		if(flowPane != null && borderPane != null) {
			flowPane.getChildren().remove(borderPane);
		}
	}
}
