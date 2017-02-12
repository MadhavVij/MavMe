package com.example.madhav.mavme;

/**
 * Created by Madhav on 11/5/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupListDisplay extends BaseActivity {

    GroupListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> parentGroupIDs;
    HashMap<String, List<String>> subGroupIDs;
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grouplist_display);
        loadMenu(1);
        activeUser = DBMgr.getUserByID(HomeDisplay.userID);

        onResume();

    }

    @Override
    public void onResume() {
        super.onResume();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();


        final TextView addGroup = (TextView) findViewById(R.id.addGroup);

        // adding group option if admin
        if (activeUser.getIsAdmin()) {
            addGroup.setVisibility(View.VISIBLE);

            addGroup.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    Intent i = new Intent("com.example.madhav.mavme.EditGroupDisplay");
                    i.putExtra("groupID", "");
                    startActivity(i);

                }
            });

        } else {
            addGroup.setVisibility(View.GONE);
        }


        listAdapter = new GroupListAdapter(this, parentGroupIDs, subGroupIDs);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        ArrayList<String> groupIDs = DBMgr.getSortedGroupIDs();
        parentGroupIDs = new ArrayList<String>();
        subGroupIDs = new HashMap<String, List<String>>();

        for (int i = 0; i < groupIDs.size(); i++) {
            String gID = groupIDs.get(i);
            Group g = DBMgr.getGroupByID(gID);
            if (g.isParentGroup() && g.getIsActive()) {
                parentGroupIDs.add(g.getGroupID());
                subGroupIDs.put(g.getGroupID(), g.getSubGroupIDs());
            }
        }

    }
}
