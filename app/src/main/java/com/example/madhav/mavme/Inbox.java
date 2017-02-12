package com.example.madhav.mavme;

import java.util.ArrayList;

/**
 * Created by Madhav on 11/3/16.
 */
public class Inbox {

    private ArrayList<String> notificationIDs;
    private ArrayList<Boolean> isSeen;

    public Inbox() {
        notificationIDs = new ArrayList<String>();
        isSeen = new ArrayList<Boolean>();
    }

    public ArrayList<String> getNotificationIDs() {
        return notificationIDs;
    }

    public boolean wasSeen(String notificationID) {
        int inboxIndex = notificationIDs.indexOf(notificationID);
        if (inboxIndex >= 0) {
            return isSeen.get(inboxIndex);
        } else {
            return false;
        }
    }

    public void addNotification(String id) {
        notificationIDs.add(0, id);
        isSeen.add(0, false);
    }

    public void toggleSeen(String nID) {
        int inboxIndex = notificationIDs.indexOf(nID);
        if (inboxIndex >= 0) {
            if (isSeen.get(inboxIndex)) {
                isSeen.set(inboxIndex, false);
            } else {
                isSeen.set(inboxIndex, true);
            }
        }
    }


    public int unseenCount() {
        int count = 0;
        for (int i = 0; i < notificationIDs.size(); i++) {
            if (!isSeen.get(i)) count++;
        }
        return count;
    }
}
