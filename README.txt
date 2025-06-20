MTG Card Archive

This tool is designed to archive and search/filter a private collection of Magic: The Gathering cards.

Features
- Retrieve cards via Scryfall API (https://scryfall.com/docs/api)
- Save found cards in local database as a private collection
- Search cards in private collection
- Filter search using the cards name, type, text, set, color identity, language, or whether its holo or not
- Quick add duplicate copies of cards 
- Quick delete cards in private collection
- Current supported languages: German, English; all filtering is still done in english only, but cards can be marked as 'English' or 'German' to distinguish between german and english cards in collection 
  (only ~50% of all cards are available in german language via Scryfall API, so for better overview all cards are saved in english)

To set up fill out db.properties.template.txt and save it as db.properties.txt

The following columns are required to set up the database:
	//cardId is a unique identifier; each combination of all other attributes below is unique and all cards with the same combination get the same id; cardId is generated automatically
	int cardId
	//english name of the card
	String cardName
	//creature / artifact / etc.
	String cardType
	//all of the card text that is in the text box
	String cardText
	//the set that the card was released in represented by a 3 letter combination
	String cardSet
	//Url of the card image
	String cardImageUrl
	//how many of the card with this id is in posession
	String cardQuantity
	//the color identity of the card, represented by an array of letters that represent the colors
	String cardColorIdentity
	//determines whether the card is holo or not
	boolean cardHolo
	//the language of the card in the collecion
	String cardLanguage

Technologies used
- Java 24
- JDBC
- Scryfall API
- JavaFX
- Maven