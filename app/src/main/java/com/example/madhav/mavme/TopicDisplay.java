package com.example.madhav.mavme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class TopicDisplay extends BaseActivity {

    private User activeUser;
    private Topic t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_display);
        loadMenu(-1);

        Intent i = getIntent();
        String topicID = i.getStringExtra("topicID");
        activeUser = DBMgr.getUserByID(HomeDisplay.userID);

        if (topicID != null) {
            t = DBMgr.getTopicByID(topicID);
            loadTopic();
            loadComments();
            addListenerOnButtons();
        }


    }


    public void loadTopic() {

        TextView topicTitle = (TextView) findViewById(R.id.topicTitle);
        TextView topicContent = (TextView) findViewById(R.id.topicContent);
        TextView topicUser = (TextView) findViewById(R.id.topicPoster);
        TextView topicDate = (TextView) findViewById(R.id.topicDate);
        TextView mavLoveCount = (TextView) findViewById(R.id.loveCount);
        ImageButton mavLove = (ImageButton) findViewById(R.id.butLove);
        ImageButton flag = (ImageButton) findViewById(R.id.butFlag);
        TextView groupPath = (TextView) findViewById(R.id.groupPath);


        // showing topic title, subtitle, and content
        User u = DBMgr.getUserByID(t.getPostUserID());
        topicTitle.setText(t.getTopicName());
        topicContent.setText(t.getPostContent());
        topicUser.setText(u.getName());
        topicDate.setText(t.getPostDate());
        groupPath.setText(t.getGroupPath());


        // configuring mavLove button and count
        configureButtons();
    }


    public void loadComments() {

        ListView commentList = (ListView) findViewById(R.id.commentList);
        CommentAdapter commentAdapter = new CommentAdapter(this, R.layout.comment_list, activeUser.getUserID(), t.getCommentIDs());
        commentList.setAdapter(commentAdapter);


    }

    public void configureButtons() {

        TextView mavLoveCount = (TextView) findViewById(R.id.loveCount);
        ImageButton mavLove = (ImageButton) findViewById(R.id.butLove);
        ImageButton flag = (ImageButton) findViewById(R.id.butFlag);
        ImageButton delete = (ImageButton) findViewById(R.id.butDelete);

        mavLoveCount.setText("" + t.getMavLoveCount());
        if (t.isLovedBy(activeUser.getUserID())) {
            mavLove.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.MULTIPLY));
            mavLoveCount.setTextColor(Color.parseColor("#ff0000"));
        } else {
            mavLove.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY));
            mavLoveCount.setTextColor(Color.parseColor("#aaaaaa"));
        }

        if (t.isFlaggedBy(activeUser.getUserID())) {
            flag.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.MULTIPLY));
        } else {
            flag.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY));
        }

        if (t.getPostUserID().equals(activeUser.getUserID()) || activeUser.getIsAdmin()) {
            delete.setVisibility(View.VISIBLE);
            if (!t.getIsActive())
                delete.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.MULTIPLY));
        } else {
            delete.setVisibility(View.GONE);
        }

    }

    public void addListenerOnButtons() {

        final ImageButton mavLove = (ImageButton) findViewById(R.id.butLove);
        final TextView userName = (TextView) findViewById(R.id.topicPoster);
        final ImageButton flag = (ImageButton) findViewById(R.id.butFlag);
        final ImageButton reply = (ImageButton) findViewById(R.id.butReply);
        final ImageButton delete = (ImageButton) findViewById(R.id.butDelete);
        final EditText commentField = (EditText) findViewById(R.id.commentField);
        final TextView submitComment = (TextView) findViewById(R.id.submit);
        final TextView groupPath = (TextView) findViewById(R.id.groupPath);


        groupPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(getApplicationContext(), "G" + t.getGroupID());

            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(getApplicationContext(), "U" + t.getPostUserID());
            }
        });


        mavLove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                t.toggleLove(activeUser.getUserID());
                configureButtons();

            }
        });


        flag.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // notifying admin for first instance of flag
                if (t.getFlagCount() == 0) {
                    String linkTo = "T" + t.getTopicID();
                    Notification n = Notification.newNotification("0", "The topic: " + t.getTopicName() +
                            " (posted by " + userName.getText().toString() + ") was flagged", linkTo);
                    n.sendNotificationTo(DBMgr.getAdminIDs());
                }

                t.toggleFlag(activeUser.getUserID());
                configureButtons();
            }
        });

        delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (t.getIsActive())
                    Utils.confirmationDialog(TopicDisplay.this, "Delete '" + t.getTopicName() + "'?",
                            new Utils.dialogHandler() {
                                @Override
                                public void onButtonClick(boolean click) {
                                    if (click) {
                                        t.delete();
                                        finish();
                                    }
                                }

                            });
            }
        });

        reply.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (commentField.getVisibility() == View.GONE) {
                    commentField.setVisibility(View.VISIBLE);
                    submitComment.setVisibility(View.VISIBLE);
                    reply.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.MULTIPLY));
                } else {
                    commentField.setVisibility(View.GONE);
                    submitComment.setVisibility(View.GONE);
                    reply.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY));

                }

                loadComments();

            }
        });


        // when a comment is submitted
        submitComment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // adding calling the add comment method
                t.addComment(activeUser.getUserID(), commentField.getText().toString());

                // hiding commenting widgets
                commentField.setVisibility(View.GONE);
                submitComment.setVisibility(View.GONE);
                reply.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY));

                // reloading topic display
                loadComments();
            }
        });

    }
}
