package com.example.madhav.mavme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Madhav on 11/8/16.
 */
public class NewTopicDisplay extends BaseActivity {

    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_topic_display);
        loadMenu(-1);

        Intent i = getIntent();
        String groupID = i.getStringExtra("groupID");
        activeUser = DBMgr.getUserByID(HomeDisplay.userID);

        addListenerOnButtons(groupID);

    }

    public void addListenerOnButtons(String groupID) {

        final Group g = DBMgr.getGroupByID(groupID);

        final EditText topicTitle = (EditText) findViewById(R.id.topicTitle);
        final EditText topicContent = (EditText) findViewById(R.id.topicContent);
        final TextView submit = (TextView) findViewById(R.id.submit);
        final TextView cancel = (TextView) findViewById(R.id.cancel);


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                g.addTopic(activeUser.getUserID(), topicTitle.getText().toString(), topicContent.getText().toString());
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

}
