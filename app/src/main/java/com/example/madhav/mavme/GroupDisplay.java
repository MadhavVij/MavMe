package com.example.madhav.mavme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupDisplay extends BaseActivity {

    private final int groupPageSize = 10;
    private User activeUser;
    private int page;
    private Group g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_display);
        loadMenu(-1);

        Intent i = getIntent();
        String groupID = i.getStringExtra("groupID");
        activeUser = DBMgr.getUserByID(HomeDisplay.userID);

        if (groupID != null) {
            g = DBMgr.getGroupByID(groupID);
            page = 1;
            onResume();
            addListenerOnButtons();
        }

    }


    public void loadGroup() {
        TextView groupTitle = (TextView) findViewById(R.id.groupTitle);
        TextView groupPath = (TextView) findViewById(R.id.groupPath);
        TextView pgLeft = (TextView) findViewById(R.id.pgLeft);
        TextView pgRight = (TextView) findViewById(R.id.pgRight);

        groupTitle.setText(g.getGroupName());
        groupPath.setText(g.getPath());


        // displaying page left and page right buttons based on current page position
        if (page == 1) {
            pgLeft.setTextColor(Color.parseColor("#888888"));
        } else {
            pgLeft.setTextColor(Color.parseColor("#000000"));
        }

        if (page * groupPageSize >= g.getTopicIDs().size()) {
            pgRight.setTextColor(Color.parseColor("#888888"));
        } else {
            pgRight.setTextColor(Color.parseColor("#000000"));
        }

    }

    public void loadPinnedTopic() {

        RelativeLayout pinnedContainer = (RelativeLayout) findViewById(R.id.pinnedContainer);

        String pinnedID = g.getPinnedTopicID();

        if (pinnedID.equals("") == false) {


            TextView pinnedTitle = (TextView) findViewById(R.id.pinnedTitle);
            TextView pinnedContent = (TextView) findViewById(R.id.pinnedContent);
            TextView pinnedUser = (TextView) findViewById(R.id.pinnedUser);
            TextView pinnedDate = (TextView) findViewById(R.id.pinnedDate);

            Topic t = DBMgr.getTopicByID(pinnedID);
            pinnedTitle.setText(t.getTopicName());

            pinnedContent.setText(t.getPostContent());
            pinnedUser.setText(DBMgr.getUserByID(t.getPostUserID()).getName());
            pinnedDate.setText(t.getPostDate());


            pinnedContainer.setVisibility(View.VISIBLE);

        } else {
            pinnedContainer.setVisibility(View.GONE);
        }
    }


    public void loadTopics() {
        ArrayList<String> pageTopicIDs = new ArrayList<String>();

        //paginating
        int start = (page - 1) * groupPageSize, end = Math.min(g.getTopicIDs().size(), page * groupPageSize);

        for (int i = start; i < end; i++) {
            pageTopicIDs.add(g.getTopicIDs().get(i));
        }

        ListView topicList = (ListView) findViewById(R.id.topicList);
        TopicAdapter topicAdapter = new TopicAdapter(this, R.layout.topic_list, activeUser.getUserID(), pageTopicIDs);
        topicList.setAdapter(topicAdapter);
    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first
        loadGroup();
        loadPinnedTopic();
        loadTopics();
    }


    public void addListenerOnButtons() {

        final ImageButton newTopic = (ImageButton) findViewById(R.id.newTopic);
        final TextView pgLeft = (TextView) findViewById(R.id.pgLeft);
        final TextView pgRight = (TextView) findViewById(R.id.pgRight);


        newTopic.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent("com.example.madhav.mavme.NewTopicDisplay");
                i.putExtra("groupID", g.getGroupID());
                startActivity(i);

            }
        });

        pgLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (page > 1) {
                    page--;
                    onResume();
                }
            }
        });


        pgRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (page * groupPageSize < g.getTopicIDs().size()) {
                    page++;
                    onResume();
                }
            }
        });


    }
}
