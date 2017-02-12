package com.example.madhav.mavme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by madhav on 11/30/16.
 */

public class EditGroupDisplay extends BaseActivity {

    private User activeUser;
    private String groupID;
    private Context context;
    private boolean createMode;
    private ArrayList<String> parentNames = new ArrayList<String>();
    private ArrayList<String> parentIDs = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_display);
        loadMenu(-1);

        Intent i = getIntent();
        groupID = i.getStringExtra("groupID");
        createMode = (groupID.length() == 0);
        activeUser = DBMgr.getUserByID(HomeDisplay.userID);
        context = this;

        Button deleteGroup = (Button) findViewById(R.id.deleteGroup);
        EditText editGroupName = (EditText) findViewById(R.id.editGroupName);
        final Spinner parentGroup = (Spinner) findViewById(R.id.parentGroup);

        // loading spinner
        parentNames.add("<NONE>");
        parentIDs.add("");

        ArrayList<String> idsByAlphabet = DBMgr.getSortedGroupIDs();

        for (String gID : idsByAlphabet) {
            Group g = DBMgr.getGroupByID(gID);
            if (g.isParentGroup() && !g.getGroupID().equals(groupID) & g.getIsActive()) {
                parentNames.add(g.getGroupName());
                parentIDs.add(g.getGroupID());
            }
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                parentNames);
        parentGroup.setAdapter(arrayAdapter);


        // creating new group or editing existing group?
        if (createMode) {
            deleteGroup.setVisibility(View.GONE);
        } else {
            Group g = DBMgr.getGroupByID(groupID);
            deleteGroup.setVisibility(View.VISIBLE);
            editGroupName.setText(g.getGroupName());
            parentGroup.setSelection(parentIDs.indexOf(g.getParentGroupID()));

        }

        addListenerOnButtons();
    }


    public void addListenerOnButtons() {
        final Button deleteGroup = (Button) findViewById(R.id.deleteGroup);
        final TextView save = (TextView) findViewById(R.id.saveChanges);
        final TextView cancel = (TextView) findViewById(R.id.cancel);

        deleteGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Group g = DBMgr.getGroupByID(groupID);
                Utils.confirmationDialog(context, "Are you sure you want to delete this group (and its subgroups)?",
                        new Utils.dialogHandler() {
                            public void onButtonClick(boolean click) {
                                if (click) {
                                    g.delete();
                                    finish();
                                }
                            }
                        });
            }
        });


        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String action = "saved";

                if (createMode) action = "created";

                Utils.alertDialog(context, "Group " + action + " successfully", new Utils.dialogHandler() {
                    @Override
                    public void onButtonClick(boolean click) {
                        saveGroup();
                        finish();
                    }
                });
            }
        });


        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void saveGroup() {

        String groupName = ((EditText) findViewById(R.id.editGroupName)).getText().toString();
        int spinnerIndex = ((Spinner) findViewById(R.id.parentGroup)).getSelectedItemPosition();
        String oldParentID;
        String newParentID = parentIDs.get(spinnerIndex);
        Group g;

        if (createMode) {
            oldParentID = "";
            g = Group.newGroup(groupName, newParentID);
        } else {
            g = DBMgr.getGroupByID(groupID);
            oldParentID = g.getParentGroupID();

            if (newParentID.length() > 0 && g.getSubGroupIDs().size() > 0) {
                Utils.alertDialog(context, "Error: Parent cannot be assigned to " + groupName + " since it contains subgroups.",
                        new Utils.dialogHandler() {
                            @Override
                            public void onButtonClick(boolean click) {
                            }
                        });
            }

            g.setGroupName(groupName);
            g.setParentGroupID(newParentID);
        }

        if (newParentID.length() > 0) {
            Group oldParent = DBMgr.getGroupByID(oldParentID);
            Group newParent = DBMgr.getGroupByID(newParentID);
            if (oldParent != null) oldParent.removeSubgroupID(g.getGroupID());
            newParent.addSubgroupID(g.getGroupID());
        }
    }

}
