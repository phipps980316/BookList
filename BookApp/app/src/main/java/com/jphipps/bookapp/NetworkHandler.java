package com.jphipps.bookapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//Class to handle network tasks and extends async task so that tasks can occur in the background
public abstract class NetworkHandler extends AsyncTask<String, Void, ArrayList<BookDetails>> {

    //Constants to hold the xml and json keys
    protected static final String NAMESPACE = null;
    protected static final String LIBRARY_TAG_STRING = "library";
    protected static final String BOOK_TAG_STRING = "book";
    protected static final String TITLE_TAG_STRING = "title";
    protected static final String YEAR_TAG_STRING = "year";
    protected static final String AUTHOR_TAG_STRING = "author";
    protected static final String AUTHOR_NAME_TAG_STRING = "name";
    protected static final String PUBLISHER_TAG_STRING = "publisher";
    protected static final String PRICE_TAG_STRING = "price";

    //Instance of the response delegate interface
    public AsyncResponseDelegate delegate = null;

    //Function to load the file from the given url and return the arraylist of book data if successfully
    //If unsuccessful return an empty arraylist
    protected ArrayList<BookDetails> doInBackground(String... urls){
        ArrayList<BookDetails> fetchedBookList;
        try {
            fetchedBookList = loadFileFromNetwork(urls[0]);
        } catch (Exception e) {
            fetchedBookList = new ArrayList<>();
        }
        return fetchedBookList;
    }

    //Function to get the input stream from the given url and parse the file
    //Return the list of book details
    private ArrayList<BookDetails> loadFileFromNetwork(String urlString) throws Exception {
        InputStream stream = null;
        ArrayList<BookDetails> fetchedBookList;

        try{
            stream = downloadUrl(urlString);
            fetchedBookList = parseFile(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return fetchedBookList;
    }

    //Connect to the url and get the input stream
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getInputStream();
    }

    //Abstract method to parse the file that will be implemented in the subclasses
    protected abstract ArrayList<BookDetails> parseFile(InputStream inputStream) throws Exception;

    //Function to call the response interface when the background task has completed
    protected void onPostExecute(ArrayList<BookDetails> fetchedData) {
        delegate.asyncTaskFinished(fetchedData);
    }

    //Interface for the response delegate that will pass back the downloaded data back to the main activity
    public interface AsyncResponseDelegate {
        void asyncTaskFinished(ArrayList<BookDetails> fetchedData);
    }
}
