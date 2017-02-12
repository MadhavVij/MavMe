package com.example.madhav.mavme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Madhav on 10/5/16.
 */
public class Post {

    long timeStamp;
    private String postUserID, postContent;
    private ArrayList<String> flagUserIDs;


    public Post(long timeStamp,
                String postUserID,
                String postContent,
                ArrayList<String> flagUserIDs) {
        this.timeStamp = timeStamp;
        this.postUserID = postUserID;
        this.postContent = postContent;
        this.flagUserIDs = flagUserIDs;

    }


    public String getPostContent() {
        return postContent;
    }

    public String getPostUserID() {
        return postUserID;
    }

    public String getPostDate() {
        Date postDate = new Date(timeStamp);
        SimpleDateFormat dFormatter = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
        String dateString = dFormatter.format(postDate);
        return dateString;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<String> getFlagUsers() {
        return flagUserIDs;
    }

    public int getFlagCount() {
        return flagUserIDs.size();
    }

    public boolean isFlaggedBy(String userID) {
        boolean isFlagged = false;
        for (int i = 0; i < flagUserIDs.size(); i++) {
            if (flagUserIDs.get(i) == userID) {
                isFlagged = true;
            }
        }
        return isFlagged;
    }

    public void toggleFlag(String userID) {

        boolean isFlagged = isFlaggedBy(userID);

        if (isFlagged == false) {
            flagUserIDs.add(userID);
        } else {
            flagUserIDs.remove(userID);
        }
    }

}
