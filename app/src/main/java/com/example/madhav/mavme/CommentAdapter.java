package com.example.madhav.mavme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Madhav on 10/7/16.
 */
public class CommentAdapter extends ArrayAdapter<String> {

    private ArrayList<String> commentIDs;
    private LayoutInflater inflater;
    private int resource;
    private User activeUser;

    public CommentAdapter(Context context, int resource, String activeUserID, ArrayList<String> commentIDs) {
        super(context, resource, commentIDs);
        this.commentIDs = commentIDs;
        this.resource = resource;
        this.activeUser = DBMgr.getUserByID(activeUserID);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }

        // getting the comment in the ListView
        Comment c = DBMgr.getCommentByID(commentIDs.get(position));

        // getting the user who posted the comment
        User u = DBMgr.getUserByID(c.getPostUserID());

        // loading TextViews and buttons
        TextView commentContent = (TextView) convertView.findViewById(R.id.commentContent);
        TextView commentPoster = (TextView) convertView.findViewById(R.id.commentPoster);
        TextView commentDate = (TextView) convertView.findViewById(R.id.commentDate);
        TextView flag = (TextView) convertView.findViewById(R.id.flagComment);
        TextView delete = (TextView) convertView.findViewById(R.id.delete);


        commentContent.setText(c.getPostContent());
        commentPoster.setText(u.getName());
        commentDate.setText(c.getPostDate());

        // configuring flag based on user
        if (activeUser.getIsAdmin()) {
            flag.setText("flag (" + c.getFlagCount() + ")");
        }
        if (c.isFlaggedBy(activeUser.getUserID())) {
            flag.setTextColor(Color.RED);
            flag.setTypeface(null, Typeface.BOLD);
        } else {
            flag.setTextColor(Color.GRAY);
            flag.setTypeface(null, Typeface.NORMAL);
        }

        // configuring delete button based on user
        if (u.getUserID().equals(activeUser.getUserID()) || activeUser.getIsAdmin()) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.INVISIBLE);
        }

        addListenerOnButtons(convertView, c, position);


        return convertView;

    }

    public void addListenerOnButtons(View convertView, final Comment c, final int position) {

        final TextView flag = (TextView) convertView.findViewById(R.id.flagComment);
        final TextView delete = (TextView) convertView.findViewById(R.id.delete);
        final TextView userName = (TextView) convertView.findViewById(R.id.commentPoster);


        userName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.goToURL(getContext(), "U" + c.getPostUserID());
            }
        });

        flag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // notifying admin for first instance of flag
                if (c.getFlagCount() == 0) {
                    String linkTo = "T" + c.getTopicID();
                    Notification n = Notification.newNotification("0", "A comment by: " +
                            userName.getText().toString() + " was flagged", linkTo);
                    n.sendNotificationTo(DBMgr.getAdminIDs());
                }

                // flagging comment
                c.toggleFlag(activeUser.getUserID());
                if (activeUser.getIsAdmin()) {
                    flag.setText("flag (" + c.getFlagCount() + ")");
                }
                if (c.isFlaggedBy(activeUser.getUserID())) {
                    flag.setTextColor(Color.RED);
                    flag.setTypeface(null, Typeface.BOLD);
                } else {
                    flag.setTextColor(Color.GRAY);
                    flag.setTypeface(null, Typeface.NORMAL);
                }

                notifyDataSetChanged();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c.delete();
                notifyDataSetChanged();
            }
        });

    }

}
