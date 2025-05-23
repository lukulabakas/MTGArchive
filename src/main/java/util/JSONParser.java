package util;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Card;

//util class that manages methods to parse JSON files

public class JSONParser {

	//parse Card from Json received from Scryfall API
	public static Card parseCardFromJson(String string) {
		//card object to return later
		Card card = new Card();
		//create json object with the received json line as string
		JSONObject json = new JSONObject(string);
		//retrieve the information required to create our Card object
		String cardName = json.optString("name", "Card name not available");
		String cardType = json.optString("type_line", "Card type not available");
		String cardText = json.optString("oracle_text", "Card text not available");
		String cardSet = json.optString("set", "Card set not available");
		String cardImageUrl = json.getJSONObject("image_uris").optString("normal", "Card Image not available");
		String[] cardColorIdentity = convertJsonArray(json.optJSONArray("color_identity"));
		//sort the Array to make it comparable later when searching the db
		Arrays.sort(cardColorIdentity);
		//save the information in our Card object
		card.setCardName(cardName);
		card.setCardType(cardType);
		card.setCardText(cardText);
		card.setCardSet(cardSet);
		card.setCardImageUrl(cardImageUrl);
		card.setCardColorIdentity(cardColorIdentity);
		return card;
	}
	
	//helper method to convert a JSONArray to a String Array
	public static String[] convertJsonArray(JSONArray jsonArray) {
		String[] stringArray = new String[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}
		return stringArray;
	}
}
