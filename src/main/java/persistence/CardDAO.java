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
		if(connection == null) {
			connection = DBConnection.getConnection();
		}
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
			conditions.add("LOWER(cardName) LIKE LOWER(?)");
			parameters.add("%" + card.getCardName() + "%");
		}
		if(!card.getCardType().isEmpty()) {
			conditions.add("LOWER(cardType) LIKE LOWER(?)");
			parameters.add("%" + card.getCardText() + "%");
		}
		if(!card.getCardSet().isEmpty()) {
			conditions.add("LOWER(cardSet) LIKE LOWER(?)");
			parameters.add(card.getCardSet());
		}
		if(!card.getCardText().isEmpty()) {
			conditions.add("LOWER(cardText) LIKE LOWER(?)");
			parameters.add("%" + card.getCardText() + "%");
		}
		if(card.getCardHolo()) {
			conditions.add("cardHolo = ?");
			parameters.add(card.getCardHolo());
		}
		//if the language was not selected or "Any" was selected there is no need to filter
		if(card.getCardLanguage() == null || card.getCardLanguage() == "Any") {
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
			queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions)).append(" ORDER BY LOWER(cardName);");
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
	
	//call the scryfall api to retrieve the information about a card based on its name and set
	public Card fetchCardFromAPI(String cardName, String cardSet) {
		return null;
	}
	
	
}
