package com.example.madhav.mavme;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by madhav on 10/6/16.
 */
public class Comment extends Post {

    private String commentID;
    private String topicID;

    public Comment(String commentID,
                   long timeStamp,
                   String postUserID,
                   String postContent,
                   String topicID,
                   ArrayList<String> flagUserIDs) {

        super(timeStamp, postUserID, postContent, flagUserIDs);
        this.commentID = commentID;
        this.topicID = topicID;

    }

    public static Comment newComment(String activeUserID, String content, String topicID) {

        // creating a new topic with standard parameters
        Date today = new Date();
        Comment c = new Comment(DBMgr.generateNewCommentID(), today.getTime(), activeUserID,
                content, topicID, new ArrayList<String>());

        // saving notification database
        DBMgr.saveComment(c);
        return c;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getTopicID() {
        return topicID;
    }

    public String toString() {
        String commentObject;

        commentObject = getCommentID() + ", " + getPostDate() + ", " + getPostUserID() + ", " +
                getPostContent() + ", {" + TextUtils.join(", ", getFlagUsers()) + "}, {" + topicID;


        return commentObject;
    }

    public void delete() {
        Topic t = DBMgr.getTopicByID(topicID);
        t.removeComment(commentID);
        topicID = "";
    }


}
