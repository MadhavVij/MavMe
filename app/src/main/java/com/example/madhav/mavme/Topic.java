package com.example.madhav.mavme;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Madhav on 10/5/16.
 */
public class Topic extends Post {

    private String topicName;
    private String topicID;
    private String groupID;
    private ArrayList<String> commentIDs;
    private ArrayList<String> mavLoveUserIDs;
    private boolean isActive = true;


    public Topic(String topicID,
                 long timeStamp,
                 String postUserID,
                 String topicName,
                 String groupID,
                 String postContent,
                 ArrayList<String> mavLoveUserIDs,
                 ArrayList<String> flagUserIDs,
                 ArrayList<String> commentIDs) {

        super(timeStamp, postUserID, postContent, flagUserIDs);
        this.topicID = topicID;
        this.topicName = topicName;
        this.groupID = groupID;
        this.mavLoveUserIDs = mavLoveUserIDs;
        this.commentIDs = commentIDs;
    }

    // static functions
    public static Topic newTopic(String activeUserID, String topicName, String content, String groupID) {

        // creating a new topic with standard parameters
        Date today = new Date();
        Topic t = new Topic(DBMgr.generateNewTopicID(), today.getTime(), activeUserID, topicName, groupID,
                content, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());

        // saving notification database
        DBMgr.saveTopic(t);

        return t;
    }

    // accessor functions
    public String getTopicID() {
        return topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return DBMgr.getGroupByID(groupID).getGroupName();
    }

    public String getGroupPath() {
        Group g = DBMgr.getGroupByID(groupID);
        return g.getPath();
    }

    public ArrayList<String> getCommentIDs() {
        return commentIDs;
    }

    public int getMavLoveCount() {
        return mavLoveUserIDs.size();
    }

    public boolean isLovedBy(String userID) {
        return mavLoveUserIDs.contains(userID);
    }

    public boolean isPinned() {
        Group g = DBMgr.getGroupByID(groupID);
        return topicID.equals(g.getPinnedTopicID());
    }

    public double getTrendIndex() {
        long now = new Date().getTime();
        double age = (double) (now - timeStamp) / 24 / 3600;
        return -(getMavLoveCount() + 1) / age;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String toString() {
        String topicObject;

        topicObject = getTopicID() + ", " + getPostDate() + ", " + getPostUserID() + ", " + topicName + ", " +
                getPostContent() + ", {" + TextUtils.join(", ", mavLoveUserIDs) + "}, {" +
                TextUtils.join(", ", getFlagUsers()) + "}, {" + TextUtils.join(", ", commentIDs) + "}";


        return topicObject;
    }

    // computing functions
    public void addComment(String cUserID, String commentMsg) {

        String notificationMsg;
        ArrayList<String> sendToArray = new ArrayList<String>();
        sendToArray.add(getPostUserID());

        // creating new comment and storing to DB
        Comment newComment = Comment.newComment(cUserID, commentMsg, topicID);

        // adding commentID to array in topic
        commentIDs.add(newComment.getCommentID());

        // creating a new notification and sending to owner of topic
        User cUser = DBMgr.getUserByID(cUserID);
        notificationMsg = commentMsg.substring(0, Math.min(70, commentMsg.length()));
        Notification n = Notification.newNotification(cUser.getUserID(), "RE: " + getTopicName() + "\n" +
                notificationMsg, "T" + getTopicID());
        n.sendNotificationTo(sendToArray);

    }

    public void removeComment(String id) {
        commentIDs.remove(id);
    }

    public void toggleLove(String userID) {
        boolean isLoved = isLovedBy(userID);
        if (!isLoved) {
            mavLoveUserIDs.add(userID);
        } else {
            mavLoveUserIDs.remove(userID);
        }
    }

    public void delete() {
        Group g = DBMgr.getGroupByID(groupID);
        g.removeTopic(topicID);
        isActive = false;
    }

    public void pinTopic() {
        Group g = DBMgr.getGroupByID(groupID);

        if (topicID.equals(g.getPinnedTopicID())) {
            g.setPinnedTopicID("");
        } else {
            g.setPinnedTopicID(topicID);
        }
    }

}
