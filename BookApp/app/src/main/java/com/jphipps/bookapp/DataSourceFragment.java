package com.jphipps.bookapp;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//Fragment to display the data source choices and passes any choice back to the main activity
public class DataSourceFragment extends Fragment {

    //Key for keeping the state of the fragment between instances
    private final String DATA_SOURCE_KEY = "DATA_SOURCE";

    //Connection to the data source label
    private TextView txtDataSource;

    //Instance of the interaction listener interface to pass any choice made back to the main activity
    private OnDataSourceFragmentInteractionListener mListener;

    public DataSourceFragment() {
        // Required empty public constructor
    }

    //Function to create a new instance of a fragment and takes a bundle to restore the state of a previous instance if one existed
    public static DataSourceFragment newInstance(Bundle savedState) {
        DataSourceFragment dataSourceFragment = new DataSourceFragment();
        dataSourceFragment.setArguments(savedState);
        return dataSourceFragment;
    }

    //Function to create and set up the fragment view and button listeners as well as restoring the previous instance state if one existed
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_source, container, false);

        txtDataSource = view.findViewById(R.id.txtDataSource);

        Button btnXML = view.findViewById(R.id.btnXML);
        btnXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onXMLButtonPressed();
            }
        });

        Button btnJSON= view.findViewById(R.id.btnJSON);
        btnJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJSONButtonPressed();
            }
        });

        if(getArguments() != null){
            Bundle savedState = getArguments();
            txtDataSource.setText(savedState.getString(DATA_SOURCE_KEY));
        }
        return view;
    }

    //Function to bundle up the state of the instance for saving before the fragment gets destroy
    public Bundle preserveState() {
        Bundle state = new Bundle();
        state.putString(DATA_SOURCE_KEY, txtDataSource.getText().toString());
        return state;
    }

    //Function to save the state of the instance before occasions such as rotating the screen
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DATA_SOURCE_KEY, txtDataSource.getText().toString());
        super.onSaveInstanceState(outState);
    }

    //Function to restore the state of the instance after occasions such as rotating the screen, if an instance exists
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            txtDataSource.setText(savedInstanceState.getString(DATA_SOURCE_KEY));
        }
    }

    //On click function for the XML button to select the XML data source and passes the choice through the interaction interface back to the main activity
    public void onXMLButtonPressed() {
        if (mListener != null) {
            txtDataSource.setText(R.string.data_source_xml_label);
            mListener.onDataSourceFragmentInteraction("XML");
        }
    }

    //On click function for the JSON button to select the JSON data source and passes the choice through the interaction interface back to the main activity
    public void onJSONButtonPressed() {
        if (mListener != null) {
            txtDataSource.setText(R.string.data_source_json_label);
            mListener.onDataSourceFragmentInteraction("JSON");
        }
    }

    //Sets a listener to the fragment when the fragment is attached to an activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataSourceFragmentInteractionListener) {
            mListener = (OnDataSourceFragmentInteractionListener) context;
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
    public interface OnDataSourceFragmentInteractionListener {
        void onDataSourceFragmentInteraction(String option);
    }
}
