package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import model.Card;

//Data Access Object
//manages all operations and methods that require db access to retrieve cards

public class CardDAO {

	//save the connection;
	private Connection connection;
	
	//given the parameters stored in the card object performs the search
	public List<Card> getCardsFromDB(Card card) throws SQLException, IOException {
		//ensure that connection is not null and not closed
		ensureConnection();
		//used to build the query based on given parameters in the Card object
		StringBuilder queryBuilder = new StringBuilder("SELECT * FROM cards");
		//stores the cards
		List<Card> cards = new ArrayList<>();
		//storing the conditions and parameters in single Lists is necessary to stop sql injection
		//stores the condition if it was selected in the search, e.g. cardName = ?
		List<String> conditions = new ArrayList<>();
		//stores the corresponding parameter, e.g. "name of the card"
		List<Object> parameters = new ArrayList<>();
		if(!card.getCardName().isEmpty()) {
			conditions.add("LOWER(cardname) LIKE LOWER(?)");
			parameters.add("%" + card.getCardName() + "%");
		}
		if(!card.getCardType().isEmpty()) {
			conditions.add("LOWER(cardtype) LIKE LOWER(?)");
			parameters.add("%" + card.getCardType() + "%");
		}
		if(!card.getCardSet().isEmpty()) {
			conditions.add("LOWER(cardset) LIKE LOWER(?)");
			parameters.add(card.getCardSet());
		}
		if(!card.getCardText().isEmpty()) {
			conditions.add("LOWER(cardtext) LIKE LOWER(?)");
			parameters.add("%" + card.getCardText() + "%");
		}
		if(card.getCardHolo()) {
			conditions.add("cardHolo = ?");
			parameters.add(card.getCardHolo());
		}
		//if the language was not selected or "Any" was selected there is no need to filter
		if(card.getCardLanguage() == null || card.getCardLanguage().equals("Any")) {
		}
		//if any language option was selected this option is handed over as the parameter
		else {
			conditions.add("cardLanguage = ?");
			parameters.add(card.getCardLanguage());
		}
		if(!card.getCardColorIdentity().isEmpty()) {
			//the colors given through the card object that are searched for
			ObservableList<String> includedColors = card.getCardColorIdentity();
			//the other colors that are not being searched for
			List<String> excludedColors = new ArrayList<>(List.of("W", "U", "B", "R", "G"));
			excludedColors.removeAll(includedColors);
			//helper List to store the string necessary to search for included colors as often as necessary
			List<String> includeConditions = new ArrayList<>();
			//all included colors are now saved to be added to the query string
			for(String color : includedColors) {
				includeConditions.add("cardColorIdentity LIKE ?");
				parameters.add("%" + color + "%");
			}
			//add all excluded colors to the search so they are ignored (NOT LIKE)
			conditions.add("(" + String.join(" OR ",  includeConditions) + ")");
			for(String excludedColor : excludedColors) {
				conditions.add("cardColorIdentity NOT LIKE ?");
				parameters.add("%"+ excludedColor + "%");
			}
		}
		//check if any conditions were chosen by checking the list
		if(!conditions.isEmpty()) {
			//taking the query builder which already got "SELECT * FROM cards" and append "WHERE"
			//then append all conditions
			//finally append optional "ORDER BY LOWER(cardName);" to order alphabetically
			queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions)).append(" AND cardquantity > 0").append(" ORDER BY LOWER(cardName);");
		}
		//else clause to append " WHERER cardquantity > 0" also if there are no conditions
		else {
			queryBuilder.append(" WHERE cardquantity > 0");
		}
		//using prepared statement to safely input the parameters
		try(PreparedStatement pS = connection.prepareStatement(queryBuilder.toString())){
			//iterate through parameters
			for(int i = 0; i < parameters.size(); i++) {
				//insert the parameter at i
				pS.setObject(i+1, parameters.get(i));
			}
			//ResultSet will contain all the results given by executing the query
			ResultSet rS = pS.executeQuery();
			//"while the resultset still has another result"
			while(rS.next()) {
				//for each result create a card and add it to our cards List
				cards.add(new Card(
						rS.getInt("cardId"),
						rS.getString("cardName"),
						rS.getString("cardType"),
						rS.getString("cardText"),
						rS.getString("cardSet"),
						rS.getString("cardImageUrl"),
						rS.getInt("cardQuantity"),
						//stored as string seperated by a comma, now joined to an array which can be put into the constructor
						rS.getString("cardColorIdentity").split(","),
						rS.getBoolean("cardHolo"),
						rS.getString("cardLanguage")
						));
			}
		}
		//return cards list with all cards matching the parameters of the given conditions
		return cards;
	}
	
	//check if a card is already in db and then add new card or update quantity of existing card
	public void addOrUpdateCard(Card fetchedCard) throws SQLException {
		//check if card is already in collection
		Card existingCard = isCardInDB(fetchedCard);
		//if card is already in db, the card is returned
		if(existingCard != null) {
			//to add the card we ++ the cardQuantity attribute
			existingCard.setCardQuantity(existingCard.getCardQuantity() + 1);
			//then update the card in the db with the new quantity
			updateCardInDB(existingCard);
		}
		//else if the card is not yet in the DB, create a new card in the db with all info from fetchedCard
		else if(existingCard == null) {
			//getNewCardId returns the current highest id+1
			fetchedCard.setCardId(getNewCardId());
			if(fetchedCard.getCardId() == 0) {
				throw new SQLException("Error setting ID");
			}else {
			//the new card with highest id+1 is saved to db as a new card
			saveCardToDB(fetchedCard);
			}
		}
	}
	
	//check if card is already in the DB, if yes returns the card, if no returns null
	public Card isCardInDB(Card card) throws SQLException {
		//ensure that connection is not null and not closed
		ensureConnection();
		String query = "SELECT * FROM cards WHERE cardname = ? AND cardset = ? AND cardtype = ? AND cardtext = ? "
				+ "AND cardimageurl = ? AND cardcoloridentity = ? AND cardholo = ? AND cardlanguage = ?;";
		PreparedStatement pS = connection.prepareStatement(query);
		pS.setString(1, card.getCardName());
		pS.setString(2, card.getCardSet());
		pS.setString(3, card.getCardType());
		pS.setString(4, card.getCardText());
		pS.setString(5, card.getCardImageUrl());
		pS.setString(6, String.join(",", card.getCardColorIdentity()));
		pS.setBoolean(7, card.getCardHolo());
		pS.setString(8, card.getCardLanguage());
		ResultSet rS = pS.executeQuery();
		if(rS.next()) {
			card.setCardQuantity(rS.getInt("cardquantity"));
			card.setCardId(rS.getInt("cardid"));
			return card;
		}
		return null;
	}
	
	//getNewCardId returns the current highest id+1
	public int getNewCardId() throws SQLException {
		//ensure that connection is not null and not closed
		ensureConnection();
		//get the current highest cardid
		String query = "SELECT MAX(cardid) as maxcardid FROM cards";
		PreparedStatement pS = connection.prepareStatement(query);
		ResultSet rS = pS.executeQuery();
		//pre set to 0; error checks will check if this method returns 0
		int maxId = 0;
		if(rS.next()) {
			//get the highest id and increment
			maxId = rS.getInt("maxcardid") + 1;
			//if the db request returned null (no entry yet) id is set to 1 for the first card
			if(rS.wasNull()) {
				maxId = 1;
			}
		}
		return maxId;
	}
	
	//save a new card to the db
	public void saveCardToDB(Card card) throws SQLException {
		//ensure that connection is not null and not closed
		ensureConnection();
		//insert a new card with data from given Card object
		String query = "INSERT INTO cards(cardid, cardname, cardtype, cardtext, cardset, cardimageurl, "
				+ "cardquantity, cardcoloridentity, cardholo, cardlanguage) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement pS = connection.prepareStatement(query);
		pS.setInt(1, card.getCardId());
		pS.setString(2, card.getCardName());
		pS.setString(3, card.getCardType());
		pS.setString(4, card.getCardText());
		pS.setString(5, card.getCardSet());
		pS.setString(6, card.getCardImageUrl());
		pS.setInt(7, card.getCardQuantity());
		pS.setString(8, String.join(",", card.getCardColorIdentity()));
		pS.setBoolean(9, card.getCardHolo());
		pS.setString(10, card.getCardLanguage());
		pS.executeUpdate();
	}
	
	//update existing card in DB by cardid
	public void updateCardInDB(Card card) throws SQLException {
		//ensure that connection is not null and not closed
		ensureConnection();
		String query = "UPDATE cards SET cardname = ?, cardtype = ?, cardtext = ?, cardset = ?, cardimageurl = ?, "
				+ "cardquantity = ?, cardcoloridentity = ?, cardholo = ?, cardlanguage = ? WHERE cardid = ?;";
		PreparedStatement pS = connection.prepareStatement(query);
		pS.setString(1, card.getCardName());
		pS.setString(2, card.getCardType());
		pS.setString(3, card.getCardText());
		pS.setString(4, card.getCardSet());
		pS.setString(5, card.getCardImageUrl());
		pS.setInt(6, card.getCardQuantity());
		pS.setString(7, String.join(",", card.getCardColorIdentity()));
		pS.setBoolean(8, card.getCardHolo());
		pS.setString(9, card.getCardLanguage());
		pS.setInt(10, card.getCardId());
		pS.executeUpdate();
	}
	
	//return total number of card copies currently in db
	public int totalNumberOfCards() throws SQLException {
		//ensure that connection is not null and not closed
		ensureConnection();
		String query = "SELECT SUM(cardquantity) as cardcount FROM cards;";
		PreparedStatement pS = connection.prepareStatement(query);
		ResultSet rS = pS.executeQuery();
		int cardCount = 0;
		while(rS.next()) {
			cardCount = rS.getInt("cardcount"); 
		}
		return cardCount;
	}
	
	//ensure that the class has a connection != null
	private void ensureConnection() {
		try {
			//check if there already is a connection, or if the connection was closed
			if(connection == null || connection.isClosed()) {
				connection = DBConnection.getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
