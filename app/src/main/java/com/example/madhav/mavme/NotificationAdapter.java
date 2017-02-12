package com.example.madhav.mavme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class NotificationAdapter extends ArrayAdapter<String> {

    private ArrayList<String> notificationIDs;
    private TextView inboxTitle;
    private User activeUser;
    private LayoutInflater inflater;
    private int resource;
    private Context context;

    public NotificationAdapter(Context context, int resource, String activeUserID, TextView inboxTitle, ArrayList<String> notificationIDs) {
        super(context, resource, notificationIDs);
        this.context = context;
        this.activeUser = DBMgr.getUserByID(activeUserID);
        this.inboxTitle = inboxTitle;
        this.notificationIDs = notificationIDs;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }

        String nID = notificationIDs.get(position);
        Notification n = DBMgr.getNotificationByID(nID);
        Inbox inbox = activeUser.getInbox();

        // loading TextViews and buttons
        RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
        TextView message = (TextView) convertView.findViewById(R.id.notificationMessage);
        TextView subtitle = (TextView) convertView.findViewById(R.id.notificationSubtitle);
        CheckBox isNew = (CheckBox) convertView.findViewById(R.id.isNew);
        ImageButton link = (ImageButton) convertView.findViewById(R.id.link);


        message.setText(n.getMessage());
        subtitle.setText(n.getSenderName() + " - " + n.getSendDate());

        // if the notification contains a link
        if (n.getLinkTo().length() != 0) {
            link.setVisibility(View.VISIBLE);
        } else {
            link.setVisibility(View.GONE);
        }

        inboxTitle.setText("NOTIFICATIONS (" + inbox.unseenCount() + " unread)");

        // if user has seen notification...
        if (!inbox.wasSeen(nID)) {
            background.setBackgroundColor(Color.parseColor("#c3e8ff"));
            message.setTextColor(Color.BLACK);
            subtitle.setTextColor(Color.BLACK);
            isNew.setChecked(true);
        } else {
            background.setBackgroundColor(Color.WHITE);
            message.setTextColor(Color.GRAY);
            subtitle.setTextColor(Color.GRAY);
            isNew.setChecked(false);
        }

        addListenerOnButtons(convertView, nID, position);

        return convertView;

    }

    public void addListenerOnButtons(View convertView, final String nID, final int position) {

        final CheckBox isNew = (CheckBox) convertView.findViewById(R.id.isNew);
        final ImageButton link = (ImageButton) convertView.findViewById(R.id.link);

        isNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activeUser.getInbox().toggleSeen(nID);
                notifyDataSetChanged();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String linkTo = DBMgr.getNotificationByID(nID).getLinkTo();
                Utils.goToURL(context, linkTo);

            }
        });


    }


}
