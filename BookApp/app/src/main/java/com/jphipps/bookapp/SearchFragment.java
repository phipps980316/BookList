package com.jphipps.bookapp;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//Fragment to display the search options and passes any choices back to the main activity
public class SearchFragment extends Fragment {

    //Keys for keeping the state of the fragment between instances
    private final String TITLE_VALUE_KEY = "TITLE_VALUE";
    private final String YEAR_VALUE_KEY = "KEY_VALUE";

    //Connections to the text fields on the view
    private EditText txtTitleSearch;
    private EditText txtYearSearch;

    //Instance of the interaction listener interface to pass any choice made back to the main activity
    private OnSearchFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    //Function to create a new instance of a fragment and takes a bundle to restore the state of a previous instance if one existed
    public static SearchFragment newInstance(Bundle savedState) {
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(savedState);
        return searchFragment;
    }

    //Function to create and set up the fragment view and button listeners as well as restoring the previous instance state if one existed
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        txtTitleSearch = view.findViewById(R.id.txtTitleSearchBar);
        txtYearSearch = view.findViewById(R.id.txtYearSearchBar);

        Button searchButton = view.findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonPressed();
            }
        });

        Button clearButton = view.findViewById(R.id.btnClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonPressed();
            }
        });

        if(getArguments() != null){
            Bundle savedState = getArguments();
            txtTitleSearch.setText(savedState.getString(TITLE_VALUE_KEY));
            txtYearSearch.setText(savedState.getString(YEAR_VALUE_KEY));
        }

        return view;
    }

    //Function to bundle up the state of the instance for saving before the fragment gets destroy
    public Bundle preserveState() {
        Bundle state = new Bundle();
        state.putString(TITLE_VALUE_KEY, txtTitleSearch.getText().toString());
        state.putString(YEAR_VALUE_KEY, txtYearSearch.getText().toString());
        return state;
    }

    //Function to save the state of the instance before occasions such as rotating the screen
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TITLE_VALUE_KEY, txtTitleSearch.getText().toString());
        outState.putString(YEAR_VALUE_KEY, txtYearSearch.getText().toString());
        super.onSaveInstanceState(outState);
    }

    //Function to restore the state of the instance after occasions such as rotating the screen, if an instance exists
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            txtTitleSearch.setText(savedInstanceState.getString(TITLE_VALUE_KEY));
            txtYearSearch.setText(savedInstanceState.getString(YEAR_VALUE_KEY));
        }
    }

    //On click function for the search button to filter the data source and passes the search term through the interaction interface back to the main activity
    void onSearchButtonPressed() {
        if (mListener != null) {
            mListener.onSearchFragmentInteraction(txtTitleSearch.getText().toString(), txtYearSearch.getText().toString());
        }
    }

    //On click function for the clear button to remove any filters and passes the request through the interaction interface back to the main activity
    void onClearButtonPressed() {
        if (mListener != null) {
            txtTitleSearch.setText("");
            txtYearSearch.setText("");
            mListener.onSearchFragmentInteraction("","");
        }
    }

    //Sets a listener to the fragment when the fragment is attached to an activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentInteractionListener) {
            mListener = (OnSearchFragmentInteractionListener) context;
        }
    }

    //Removes the listener from the fragment when the fragment is detached from an activity
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Interface for the listener that is called when the fragment is interacted with
    //Used to pass any information back to the main activity
    public interface OnSearchFragmentInteractionListener {
        void onSearchFragmentInteraction(String title, String year);
    }
}
