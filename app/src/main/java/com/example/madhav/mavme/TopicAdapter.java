package com.example.madhav.mavme;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Madhav on 10/7/16.
 */
public class TopicAdapter extends ArrayAdapter<String> {

    private User activeUser;
    private ArrayList<String> topicIDs;
    private LayoutInflater inflater;
    private Context context;
    private int resource;

    public TopicAdapter(Context context, int resource, String activeUserID, ArrayList<String> topicIDs) {
        super(context, resource, topicIDs);
        this.context = context;
        this.activeUser = DBMgr.getUserByID(activeUserID);
        this.topicIDs = topicIDs;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }


        Topic t = DBMgr.getTopicByID(topicIDs.get(position));


        loadTextViews(convertView, t);
        addListenerOnButtons(convertView, t);
        configureButtons(convertView, t);

        return convertView;

    }

    public void loadTextViews(View convertView, Topic t) {
        // loading TextViews
        TextView topicTitle = (TextView) convertView.findViewById(R.id.topicTitle);
        TextView topicPoster = (TextView) convertView.findViewById(R.id.topicPoster);
        TextView topicDate = (TextView) convertView.findViewById(R.id.topicDate);
        TextView topicLoveCount = (TextView) convertView.findViewById(R.id.topicLoveCount);
        TextView groupPath = (TextView) convertView.findViewById(R.id.groupPath);

        topicTitle.setText(t.getTopicName());
        topicPoster.setText(DBMgr.getUserByID(t.getPostUserID()).getName());
        topicDate.setText(t.getPostDate());
        topicLoveCount.setText("" + t.getMavLoveCount());
        groupPath.setText(t.getGroupPath());
    }

    public void configureButtons(View convertView, Topic t) {
        ImageButton mavLove = (ImageButton) convertView.findViewById(R.id.butLove);
        TextView loveCount = (TextView) convertView.findViewById(R.id.topicLoveCount);
        TextView flag = (TextView) convertView.findViewById(R.id.flagTopic);
        TextView pin = (TextView) convertView.findViewById(R.id.pinTopic);
        TextView delete = (TextView) convertView.findViewById(R.id.deleteTopic);

        // configuring mavLove button and count
        if (t.isLovedBy(activeUser.getUserID())) {
            mavLove.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.MULTIPLY));
            loveCount.setTextColor(Color.parseColor("#ff0000"));
        } else {
            mavLove.getBackground().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY));
            loveCount.setTextColor(Color.parseColor("#aaaaaa"));
        }


        // configuring flag option
        if (activeUser.getIsAdmin()) {
            flag.setText("flag (" + t.getFlagCount() + ")");
        }
        if (t.isFlaggedBy(activeUser.getUserID())) {
            flag.setTextColor(Color.RED);
            flag.setTypeface(null, Typeface.BOLD);
        } else {
            flag.setTextColor(Color.GRAY);
            flag.setTypeface(null, Typeface.NORMAL);
        }


        // configuring delete & pin topics based on active user
        if (activeUser.getIsAdmin()) {
            delete.setVisibility(View.VISIBLE);
            pin.setVisibility(View.VISIBLE);

            // configuring pin option
            if (t.isPinned()) {
                pin.setTextColor(Color.RED);
                pin.setTypeface(null, Typeface.BOLD);
            } else {
                pin.setTextColor(Color.GRAY);
                pin.setTypeface(null, Typeface.NORMAL);
            }

        } else if (activeUser.getUserID().equals(t.getPostUserID())) {
            delete.setVisibility(View.VISIBLE);
            pin.setVisibility(View.GONE);
        } else {
            delete.setVisibility(View.GONE);
            pin.setVisibility(View.GONE);
        }

    }

    public void addListenerOnButtons(final View convertView, final Topic t) {

        final ImageButton mavLove = (ImageButton) convertView.findViewById(R.id.butLove);
        final TextView mavLoveCount = (TextView) convertView.findViewById(R.id.topicLoveCount);
        final TextView topicTitle = (TextView) convertView.findViewById(R.id.topicTitle);
        final TextView groupPath = (TextView) convertView.findViewById(R.id.groupPath);
        final TextView username = (TextView) convertView.findViewById(R.id.topicPoster);
        final TextView flag = (TextView) convertView.findViewById(R.id.flagTopic);
        final TextView pin = (TextView) convertView.findViewById(R.id.pinTopic);
        final TextView delete = (TextView) convertView.findViewById(R.id.deleteTopic);


        mavLove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                t.toggleLove(activeUser.getUserID());
                mavLoveCount.setText("" + t.getMavLoveCount());
                configureButtons(convertView, t);


            }
        });

        mavLoveCount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Date today = new Date();
                String days = "" + (double) (today.getTime() - t.getTimeStamp()) / 24 / 3600;
                Toast.makeText(context, "" + t.getTrendIndex(), Toast.LENGTH_SHORT).show();
            }
        });

        topicTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(context, "T" + t.getTopicID());

            }
        });

        groupPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(context, "G" + t.getGroupID());

            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(context, "U" + t.getPostUserID());
            }
        });


        pin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                t.pinTopic();
                configureButtons(convertView, t);
                notifyDataSetChanged();
                if (context instanceof GroupDisplay)
                    ((GroupDisplay) context).onResume();
            }
        });

        flag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // notifying admin for first instance of flag
                if (t.getFlagCount() == 0) {
                    String linkTo = "T" + t.getTopicID();
                    Notification n = Notification.newNotification("0", "The topic: " + t.getTopicName() +
                            " (posted by " + username.getText().toString() + ") was flagged", linkTo);
                    n.sendNotificationTo(DBMgr.getAdminIDs());
                }

                // flagging topic
                t.toggleFlag(activeUser.getUserID());
                configureButtons(convertView, t);
                notifyDataSetChanged();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Utils.confirmationDialog(context, "Delete '" + t.getTopicName() + "'?", new Utils.dialogHandler() {
                    @Override
                    public void onButtonClick(boolean click) {
                        if (click) {
                            topicIDs.remove(t.getTopicID());
                            t.delete();
                            notifyDataSetChanged();
                        }
                    }
                });

            }
        });
    }

}
