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
import javafx.collections.ObservableList;

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
	//the language of the card in the collecion
	private StringProperty cardLanguage;
	
	//default constructor
	public Card() {
		this(0, "", "", "", "", "", 1, null, false, "");
	}
	
	//dummy constructor for tests
	public Card(String cardName) {
		this(0, cardName, "CARDTYPE", "CARDTEXT", "CARDSET", "CARDIMAGEURL", 99, new String[]{"G", "R"}, false, "CARDLANGUAGE");
	}
	
	//complete constructor
	public Card(int cardId, String cardName, String cardType, String cardText, String cardSet, String cardImageUrl, int cardQuantity, String[] cardColorIdentity, boolean cardHolo, String cardLanguage) {
		this.cardId = new SimpleIntegerProperty(cardId);
		this.cardName = new SimpleStringProperty(cardName);
		this.cardType = new SimpleStringProperty(cardType);
		this.cardText = new SimpleStringProperty(cardText);
		this.cardSet = new SimpleStringProperty(cardSet);
		this.cardImageUrl = new SimpleStringProperty(cardImageUrl);
		this.cardQuantity = new SimpleIntegerProperty(cardQuantity);
		if(cardColorIdentity != null) {
			this.cardColorIdentity = new SimpleListProperty<>(FXCollections.observableArrayList(cardColorIdentity));
		}else {
			this.cardColorIdentity = new SimpleListProperty<>(FXCollections.observableArrayList());
		}
		this.cardHolo = new SimpleBooleanProperty(cardHolo);
		this.cardLanguage = new SimpleStringProperty(cardLanguage);
	}
	
	//Getter & Setter
	public int getCardId() { return this.cardId.get(); }
	public void setCardId(int cardId) { this.cardId.set(cardId); }
	public IntegerProperty getCardIdProperty() { return this.cardId; }
	
	public String getCardName() { return this.cardName.get(); }
	public void setCardName(String cardName) { this.cardName.set(cardName); }
	public StringProperty getCardNameProperty() { return this.cardName; }
	
	public String getCardType() { return this.cardType.get(); }
	public void setCardType(String cardType) { this.cardType.set(cardType); }
	public StringProperty getCardTypeProperty() { return this.cardType; }
	
	public String getCardText() { return this.cardText.get(); }
	public void setCardText(String cardText) { this.cardText.set(cardText); }
	public StringProperty getCardTextProperty() { return this.cardText; }
	
	public String getCardSet() { return this.cardSet.get(); }
	public void setCardSet(String cardSet) { this.cardSet.set(cardSet); }
	public StringProperty getCardSetProperty() { return this.cardSet; }
	
	public String getCardImageUrl() { return this.cardImageUrl.get(); }
	public void setCardImageUrl(String cardImageUrl) { this.cardImageUrl.set(cardImageUrl); }
	public StringProperty getCardImageUrlProperty() { return this.cardImageUrl; }
	
	public int getCardQuantity() { return this.cardQuantity.get(); }
	public void setCardQuantity(int cardQuantity) { this.cardQuantity.set(cardQuantity); }
	public IntegerProperty getCardQuantityProperty() { return this.cardQuantity; }
	
	public ObservableList<String> getCardColorIdentity() { return this.cardColorIdentity.get(); }
	public void setCardColorIdentity(String[] cardColorIdentity) { this.cardColorIdentity.set(FXCollections.observableArrayList(cardColorIdentity)); }
	public ObservableList<String> getCardColorIdentityProperty() { return this.cardColorIdentity; }
	
	public boolean getCardHolo() { return this.cardHolo.get(); }
	public void setCardHolo(boolean cardHolo) { this.cardHolo.set(cardHolo); }
	public BooleanProperty getCardHoloProperty() { return this.cardHolo; }
	
	public String getCardLanguage() { return this.cardLanguage.get(); }
	public void setCardLanguage(String cardLanguage) { this.cardLanguage.set(cardLanguage); }
	public StringProperty getCardLanguageProperty() { return this.cardLanguage; }
}