package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import model.Card;
import util.JSONParser;

//class that manages methods that use the scryfall API to retrieve card information

public class ScryfallApiClient {

	//takes a cardname and cardset as strings and returns the corresonding 
	//cardname, cardset, cardtype, cardtext, cardcoloridentity and cardimageurl in a Card object
	public Card fetchCardFromApi(String cardName, String cardSet) throws IOException, URISyntaxException, InterruptedException {
		//assemble our url for the request
		//the given cardname is added, spaces replaced with "+"
		//the given setname is added, in lower case
		//language is set to english
		String apiUrl = "https://api.scryfall.com/cards/named?fuzzy=" + 
				cardName.replace(" ", "+") + "&set=" + cardSet.toLowerCase() + "&lang=en";
		//prepare the request / open connection
		URL url = new URI(apiUrl).toURL();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//define request method (GET/POST/etc)
		conn.setRequestMethod("GET");
		//perform the API request
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		//StringBuilder to assemble our response (json might be in several lines to be readable)
		StringBuilder response = new StringBuilder();
		//used to read the lines and append them to the reponse
		String line;
		//iterate the reader
		while((line = reader.readLine()) != null) {
			//assemble the string
			response.append(line);
		}
		//close the raeder
		reader.close();
		//api rules are to only send one request per certain ms
		//to make sure this is not violated by repeated clicks on search etc. > add wait timer
        Thread.sleep(100);
		//response is json - needs to get parsed into a Card object
		//parseCardFromJson takes a json as string and returns a Card object
		return JSONParser.parseCardFromJson(response.toString());
	}
}
