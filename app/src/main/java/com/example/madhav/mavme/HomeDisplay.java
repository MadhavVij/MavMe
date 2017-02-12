package com.example.madhav.mavme;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Madhav on 10/25/16.
 */
public class HomeDisplay extends BaseActivity {

    // creating dummer user
    public static String userID = "1";
    private final int inboxPageSize = 3;
    private String defaultBroadcast = "Enter broadcast message here...";
    private User activeUser;
    // inbox variables
    private Inbox inbox;
    private int inboxPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_display);

        loadMenu(0);

        activeUser = DBMgr.getUserByID(userID);
        inbox = activeUser.getInbox();

        onResume();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        loadFeed();
        loadNotifications();
        addListenerOnButtons();
    }

    public void loadFeed() {

        // creating feed data
        ArrayList<String> topicIDs = new ArrayList<String>();
        ArrayList<Double> feedValues = new ArrayList<Double>();
        ArrayList<Topic> topics = DBMgr.getMostRecentTopics(1000);

        Topic t;

        // populating feed
        for (int i = 0; i < topics.size(); i++) {
            t = topics.get(i);
            topicIDs.add(t.getTopicID());
            feedValues.add(t.getTrendIndex());
        }

        Feed f = new Feed(topicIDs, feedValues);
        f.sortTopicIDs();
        f.trim(10);

        ListView feedList = (ListView) findViewById(R.id.feedList);
        TopicAdapter topicAdapter = new TopicAdapter(this, R.layout.topic_list, userID, f.getTopicIDs());
        feedList.setAdapter(topicAdapter);

    }


    public void loadNotifications() {

        ArrayList<String> nIDs = new ArrayList<String>();
        TextView pgLeft = (TextView) findViewById(R.id.pgLeft);
        TextView pgRight = (TextView) findViewById(R.id.pgRight);
        EditText broadcastContent = (EditText) findViewById(R.id.broadcastContent);
        TextView sendBroadcast = (TextView) findViewById(R.id.sendBroadcast);

        // displaying page left and page right buttons based on current page position
        if (inboxPage == 1) {
            pgLeft.setTextColor(Color.parseColor("#888888"));
        } else {
            pgLeft.setTextColor(Color.parseColor("#000000"));
        }

        if (inboxPage * inboxPageSize >= inbox.getNotificationIDs().size()) {
            pgRight.setTextColor(Color.parseColor("#888888"));
        } else {
            pgRight.setTextColor(Color.parseColor("#000000"));
        }

        // prepping notificationID ArrayList
        for (int i = (inboxPage - 1) * inboxPageSize; i < Math.min(inbox.getNotificationIDs().size(), inboxPage * inboxPageSize); i++) {
            nIDs.add(inbox.getNotificationIDs().get(i));
        }

        TextView inboxTitle = (TextView) findViewById(R.id.inboxTitle);
        ListView notificationsList = (ListView) findViewById(R.id.inbox);


        NotificationAdapter notificationAdapter = new NotificationAdapter(this, R.layout.notification_list, userID, inboxTitle, nIDs);
        notificationsList.setAdapter(notificationAdapter);

        broadcastContent.setText(defaultBroadcast);
        broadcastContent.setTextColor(Color.parseColor("#aaaaaa"));
        sendBroadcast.setTextColor(Color.parseColor("#aaaaaa"));


        if (activeUser.getIsAdmin()) {
            broadcastContent.setVisibility(View.VISIBLE);
            sendBroadcast.setVisibility(View.VISIBLE);
        } else {
            broadcastContent.setVisibility(View.GONE);
            sendBroadcast.setVisibility(View.GONE);
        }

    }


    public void addListenerOnButtons() {

        final TextView pgLeft = (TextView) findViewById(R.id.pgLeft);
        final TextView pgRight = (TextView) findViewById(R.id.pgRight);
        final EditText broadcastContent = (EditText) findViewById(R.id.broadcastContent);
        final TextView sendBroadcast = (TextView) findViewById(R.id.sendBroadcast);


        pgLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inboxPage = Math.max(1, inboxPage - 1);
                loadNotifications();
            }
        });


        pgRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inboxPage = Math.min(inbox.getNotificationIDs().size() / inboxPageSize + 1, inboxPage + 1);
                loadNotifications();
            }
        });

        sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = broadcastContent.getText().toString();
                Notification n = Notification.newNotification(HomeDisplay.userID, message, "");

                // adding every user to id-list
                ArrayList<String> userIDs = new ArrayList<String>();
                for (int i = 0; i < Integer.parseInt(DBMgr.generateNewUserID()); i++) {
                    userIDs.add("" + i);
                }

                n.sendNotificationTo(userIDs);
                loadNotifications();

            }
        });

        broadcastContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastContent.setTextColor(Color.parseColor("#000000"));
                sendBroadcast.setTextColor(Color.parseColor("#ffa500"));
            }
        });

        broadcastContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    broadcastContent.setTextColor(Color.parseColor("#000000"));
                    sendBroadcast.setTextColor(Color.parseColor("#ffa500"));
                }
            }
        });


    }
}
