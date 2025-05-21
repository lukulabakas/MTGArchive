package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Card;

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
	
	
	
	
	
	//reference to the main application
	private Main main;
	
	//contructor
	public CardPanelController() {
		
	}
	
	//intiliazie the controller class; automatically called after the fxml file has been loaded
	@FXML
	private void initialize() {
		
	}
	
	public void setMainApp(Main main) {
		this.main = main;
	}
	
	//receives a Card object and sets all label etc. to represent the given card
	public void setCardData(Card card) {
		cardNameLabel.setText(card.getCardName());
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
}
