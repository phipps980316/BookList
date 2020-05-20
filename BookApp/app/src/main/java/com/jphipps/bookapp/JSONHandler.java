package com.jphipps.bookapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Class to parse json files from a url
//Class is a subclass of NetworkHandler
public class JSONHandler extends NetworkHandler {

    //Implement the parseFile function from the superclass to begin reading the json
    protected ArrayList<BookDetails> parseFile(InputStream inputStream) throws Exception {
        String jsonString = getJSON(inputStream);
        return decodeJSON(jsonString);
    }

    //Function to fetch the json string from the url and return the string
    private String getJSON(InputStream stream) throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    //Function to decode the json string into book detail objects
    //Return the arraylist of book detail objects
    private ArrayList<BookDetails> decodeJSON(String jsonString) throws Exception {
        ArrayList<BookDetails> bookDetails = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(LIBRARY_TAG_STRING);
        for (int index = 0; index < jsonArray.length(); index++){
            BookDetails currentBookDetails = new BookDetails();
            JSONObject currentBookJSON = jsonArray.getJSONObject(index);

            if(currentBookJSON.has(TITLE_TAG_STRING)) {
                currentBookDetails.setTitle(currentBookJSON.getString(TITLE_TAG_STRING));
            }
            if(currentBookJSON.has(YEAR_TAG_STRING)) {
                currentBookDetails.setYear(currentBookJSON.getString(YEAR_TAG_STRING));
            }
            if(currentBookJSON.has(PUBLISHER_TAG_STRING)) {
                currentBookDetails.setPublisher(currentBookJSON.getString(PUBLISHER_TAG_STRING));
            }
            if(currentBookJSON.has(PRICE_TAG_STRING)) {
                currentBookDetails.setPrice(currentBookJSON.getString(PRICE_TAG_STRING));
            }
            if(currentBookJSON.has(AUTHOR_TAG_STRING)){
                JSONArray authorsJSON = currentBookJSON.getJSONArray(AUTHOR_TAG_STRING);
                for (int authorIndex = 0; authorIndex < authorsJSON.length(); authorIndex++) {
                    JSONObject currentAuthorJSON = authorsJSON.getJSONObject(authorIndex);
                    if(currentAuthorJSON.has(AUTHOR_NAME_TAG_STRING)) {
                        currentBookDetails.addAuthor(currentAuthorJSON.getString(AUTHOR_NAME_TAG_STRING));
                    }
                }
            }
            bookDetails.add(currentBookDetails);
        }
        return bookDetails;
    }
}
