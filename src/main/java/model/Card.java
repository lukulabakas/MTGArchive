package model;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

//class that describes a card

public class Card {
	
	//attributes of a card
	//all card data is retrieved in english even if the card has another value for the attribute cardLanguage
	//cardId is a unique identifier; each combination of all other attributes below is unique and all cards with the same combination get the same id
	private IntegerProperty cardId;
	//english name of the card
	private StringProperty cardName;
	//creature / artifact / etc.
	private StringProperty cardType;
	//all of the card text that is in the text box
	private StringProperty cardText;
	//the set that the card was released in represented by a 3 letter combination
	private StringProperty cardSet;
	//Url of the card image
	private StringProperty cardImageUrl;
	//how many of the card with this id is in posession
	private IntegerProperty cardQuantity;
	//the color identity of the card, represented by an array of letters that represent the colors
	private ListProperty<String> cardColorIdentity;
	//determines whether the card is holo or not
	private BooleanProperty cardHolo;
	
	//default constructor
	public Card() {
		this(0, null, null, null, null, null, 0, null, false);
	}
	
	//complete constructor
	public Card(int cardId, String cardName, String cardType, String cardText, String cardSet, String cardImageUrl, int cardQuantity, ArrayList<String> cardColorIdentity, boolean cardHolo) {
		this.cardId = new SimpleIntegerProperty(cardId);
		this.cardName = new SimpleStringProperty(cardName);
		this.cardType = new SimpleStringProperty(cardType);
		this.cardText = new SimpleStringProperty(cardText);
		this.cardSet = new SimpleStringProperty(cardSet);
		this.cardImageUrl = new SimpleStringProperty(cardImageUrl);
		this.cardQuantity = new SimpleIntegerProperty(cardQuantity);
		this.cardColorIdentity = new SimpleListProperty<String>(FXCollections.observableArrayList(cardColorIdentity));
		this.cardHolo = new SimpleBooleanProperty(cardHolo);
	}
	
	//Getter & Setter
	
}
