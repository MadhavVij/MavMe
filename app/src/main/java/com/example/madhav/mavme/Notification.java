package com.example.madhav.mavme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Madhav on 11/1/16.
 */
public class Notification {

    private String notificationID;
    private String senderID;
    private Date sendDate;
    private String message;
    private String linkTo;

    public Notification(String id, String senderID, Date sendDate, String message, String linkTo) {
        this.notificationID = id;
        this.senderID = senderID;
        this.sendDate = sendDate;
        this.message = message;
        this.linkTo = linkTo;
    }

    public static Notification newNotification(String senderID, String message, String linkTo) {
        Date today = new Date();
        Notification n = new Notification(DBMgr.generateNewNotificationID(), senderID, today, message, linkTo);

        // saving notification database
        DBMgr.saveNotification(n);
        return n;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderName() {
        return DBMgr.getUserByID(senderID).getName();
    }

    public String getLinkTo() {
        return linkTo;
    }

    public String getSendDate() {
        SimpleDateFormat dFormatter = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
        String dateString = dFormatter.format(sendDate);
        return dateString;
    }

    public String getMessage() {
        return message;
    }

    public void sendNotificationTo(ArrayList<String> userIDs) {
        for (String uID : userIDs) {
            User u = DBMgr.getUserByID(uID);
            u.getInbox().addNotification(notificationID);
        }
    }


}
