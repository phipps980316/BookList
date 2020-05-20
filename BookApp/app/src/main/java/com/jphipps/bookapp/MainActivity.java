package com.jphipps.bookapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

//Main activity for the book app that implements the fragment's listeners and the network handlers response delegate
public class MainActivity extends AppCompatActivity
        implements
        DataSourceFragment.OnDataSourceFragmentInteractionListener,
        SearchFragment.OnSearchFragmentInteractionListener,
        NetworkHandler.AsyncResponseDelegate {

    //Keys for keeping the state of the view
    private final String BOOK_LIST_KEY = "BOOK_LIST";
    private final String DISPLAY_BOOK_LIST_KEY = "DISPLAY_LIST";
    private final String DATA_SOURCE_FRAGMENT_KEY = "DATA_SOURCE_FRAGMENT";
    private final String DATA_SOURCE_STATE_KEY = "DATA_SOURCE_STATE";
    private final String SEARCH_FRAGMENT_KEY = "SEARCH_FRAGMENT";
    private final String SEARCH_STATE_KEY = "SEARCH_STATE";

    //Book list adapter and 2 array lists for book details
    //One arraylist will contain all of the book details and the other arraylist will only contain the book details currently being displayed
    private BookListAdapter bookListAdapter;
    private ArrayList<BookDetails> bookList = new ArrayList<>();
    private ArrayList<BookDetails> displayBookList = new ArrayList<>();

    //Bundles to preserve the state of the fragments
    private Bundle dataSourceState = null;
    private Bundle searchState = null;

    //Booleans to flag if the the fragments are being displayed
    private boolean isDataSourceFragmentDisplayed = false;
    private boolean isSearchFragmentDisplayed = false;

    //Function to save the state of the activity before occasions such as rotating the screen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BOOK_LIST_KEY, bookList);
        outState.putSerializable(DISPLAY_BOOK_LIST_KEY, displayBookList);
        outState.putBoolean(DATA_SOURCE_FRAGMENT_KEY, isDataSourceFragmentDisplayed);
        outState.putBoolean(SEARCH_FRAGMENT_KEY, isSearchFragmentDisplayed);
        outState.putBundle(DATA_SOURCE_STATE_KEY, dataSourceState);
        outState.putBundle(SEARCH_STATE_KEY, searchState);
    }

    //Function to restore the state of the activity after occasions such as rotating the screen
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bookList = (ArrayList<BookDetails>) savedInstanceState.getSerializable(BOOK_LIST_KEY);
        displayBookList = (ArrayList<BookDetails>) savedInstanceState.getSerializable(DISPLAY_BOOK_LIST_KEY);
        bookListAdapter.updateData(displayBookList);
        isSearchFragmentDisplayed = savedInstanceState.getBoolean(SEARCH_FRAGMENT_KEY);
        isDataSourceFragmentDisplayed = savedInstanceState.getBoolean(DATA_SOURCE_FRAGMENT_KEY);
        dataSourceState = savedInstanceState.getBundle(DATA_SOURCE_STATE_KEY);
        searchState = savedInstanceState.getBundle(SEARCH_STATE_KEY);
    }

    //Function to set up the recycler view and button listeners when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerBookList);

        Button btnDataSource = findViewById(R.id.btnDataSource);
        btnDataSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDataSourceFragmentDisplayed && isSearchFragmentDisplayed) {
                    closeSearchFragment();
                    displayDataSourceFragment();
                }
                else if(!isDataSourceFragmentDisplayed){
                    displayDataSourceFragment();
                }
                else {
                    closeDataSourceFragment();
                }
            }
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearchFragmentDisplayed && isDataSourceFragmentDisplayed) {
                    closeDataSourceFragment();
                    displaySearchFragment();
                }
                else if(!isSearchFragmentDisplayed){
                    displaySearchFragment();
                }
                else {
                    closeSearchFragment();
                }
            }
        });
        bookListAdapter = new BookListAdapter(this, new ArrayList<BookDetails>());
        recyclerView.setAdapter(bookListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Function to dynamically display the data source fragment
    public void displayDataSourceFragment() {
        DataSourceFragment dataSourceFragment = DataSourceFragment.newInstance(dataSourceState);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, dataSourceFragment, DATA_SOURCE_FRAGMENT_KEY).addToBackStack(null).commit();
        isDataSourceFragmentDisplayed = true;
    }

    //Function to get the fragments state and dynamically close the data source fragment
    public void closeDataSourceFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        DataSourceFragment dataSourceFragment = (DataSourceFragment)fragmentManager.findFragmentByTag(DATA_SOURCE_FRAGMENT_KEY);
        if(dataSourceFragment != null){
            dataSourceState = dataSourceFragment.preserveState();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(dataSourceFragment).commit();
        }
        isDataSourceFragmentDisplayed = false;
    }

    //Function to dynamically display the search fragment
    public void displaySearchFragment() {
        SearchFragment searchFragment = SearchFragment.newInstance(searchState);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, searchFragment, SEARCH_FRAGMENT_KEY).addToBackStack(null).commit();
        isSearchFragmentDisplayed = true;
    }

    //Function to get the fragments state and dynamically close the search fragment
    public void closeSearchFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        SearchFragment searchFragment = (SearchFragment)fragmentManager.findFragmentByTag(SEARCH_FRAGMENT_KEY);
        if(searchFragment != null) {
            searchState = searchFragment.preserveState();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(searchFragment).commit();
        }
        isSearchFragmentDisplayed = false;
    }

    //Implements the search fragment's interaction interface and handles the search operation
    public void onSearchFragmentInteraction(String title, String year) {
        //Hide the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            View view = getCurrentFocus();
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        displayBookList.clear();

        if(!title.equals("") && !year.equals("")) {
            //search for both title and year
            for(int index = 0; index < bookList.size(); index++){
                BookDetails currentBook = bookList.get(index);
                if(currentBook.getTitle().toLowerCase().contains(title.toLowerCase()) && currentBook.getYear().equals(year)) {
                    displayBookList.add(currentBook);
                }
            }
        }
        else if(!title.equals("")) {
            //search for just title
            for(int index = 0; index < bookList.size(); index++){
                BookDetails currentBook = bookList.get(index);
                if(currentBook.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    displayBookList.add(currentBook);
                }
            }
        }
        else if(!year.equals("")) {
            //search for just year
            for(int index = 0; index < bookList.size(); index++){
                BookDetails currentBook = bookList.get(index);
                if(currentBook.getYear().equals(year)) {
                    displayBookList.add(currentBook);
                }
            }
        }
        else{
            //no search parameters, display full list
            displayBookList.addAll(bookList);
        }

        bookListAdapter.updateData(displayBookList);
    }

    //Implements the data source fragment's interaction interface and handles loading the book data from the data source
    public void onDataSourceFragmentInteraction(String option){
        final String JSON_DATA_URL = "https://ces-web2.southwales.ac.uk/students/14729831/books.json";
        final String XML_DATA_URL = "https://ces-web2.southwales.ac.uk/students/14729831/books.xml";

        bookList.clear();
        displayBookList.clear();

        //Checks if the device has network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if(connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if(networkInfo != null && networkInfo.isConnected()){
            //If there is a network connect, get the xml data if that is the selected option
            if (option.equals("XML")){
                XMLHandler xmlHandler = new XMLHandler();
                xmlHandler.delegate = this;
                xmlHandler.execute(XML_DATA_URL);

            }
            //Or get the json data if that is the selected option
            else if (option.equals("JSON")){
                JSONHandler jsonHandler = new JSONHandler();
                jsonHandler.delegate = this;
                jsonHandler.execute(JSON_DATA_URL);
            }
        }
        else {
            Toast.makeText(this, "No Network Available", Toast.LENGTH_LONG).show();
        }
    }

    //Implements the network manager's response delegate interface and passed the async task's result to the main activity
    public void asyncTaskFinished(ArrayList<BookDetails> output){
        bookList.addAll(output);
        displayBookList.addAll(output);
        bookListAdapter.updateData(displayBookList);
    }
}