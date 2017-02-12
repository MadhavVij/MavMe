package com.example.madhav.mavme;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Madhav on 11/17/16.
 */
public class SearchDisplay extends BaseActivity {

    private ArrayList<String> userIDs = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_display);
        loadMenu(2);
        loadSearchResults();
        addListenerOnButtons();
        addListenerOnListView(this);
    }


    public void loadSearchResults() {

        ListView searchResults = (ListView) findViewById(R.id.searchResults);
        TextView noResults = (TextView) findViewById(R.id.noSearchResults);

        if (userIDs.isEmpty()) {
            searchResults.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        } else {

            UserAdapter userAdapter = new UserAdapter(this, R.layout.user_list, userIDs);
            searchResults.setAdapter(userAdapter);

            searchResults.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.INVISIBLE);
        }
    }

    public void addListenerOnButtons() {

        final EditText query = (EditText) findViewById(R.id.searchQuery);
        final Button search = (Button) findViewById(R.id.search);


        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userIDs = new SearchQuery(query.getText().toString()).search();
                loadSearchResults();
            }
        });


    }

    public void addListenerOnListView(final Context context) {
        ListView searchResults = (ListView) findViewById(R.id.searchResults);
        searchResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.goToURL(context, "U" + userIDs.get(position));
            }
        });

    }
}
