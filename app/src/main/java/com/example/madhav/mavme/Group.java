package com.example.madhav.mavme;

import java.util.ArrayList;

/**
 * Created by madhav on 11/3/16.
 */
public class Group extends Object {

    private String groupID;
    private String groupName;
    private String parentGroupID;
    private String pinnedTopicID;
    private ArrayList<String> topicIDs;
    private ArrayList<String> subGroupIDs;
    private boolean isActive = true;

    public Group(String groupID, String groupName, String parentGroupID, String pinnedTopicID,
                 ArrayList<String> topicIDs, ArrayList<String> subGroupIDs) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.parentGroupID = parentGroupID;
        this.pinnedTopicID = pinnedTopicID;
        this.topicIDs = topicIDs;
        this.subGroupIDs = subGroupIDs;
    }

    // static methods
    public static Group newGroup(String groupName, String parentGroupID) {
        String gID = DBMgr.generateNewGroupID();
        Group g = new Group(gID, groupName, parentGroupID, "", new ArrayList<String>(), new ArrayList<String>());

        DBMgr.saveGroup(g);
        return g;
    }

    // getting methods
    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParentGroupID() {
        return parentGroupID;
    }

    public void setParentGroupID(String parentID) {
        this.parentGroupID = parentID;
    }

    public ArrayList<String> getSubGroupIDs() {
        return subGroupIDs;
    }

    public String getPinnedTopicID() {
        return pinnedTopicID;
    }

    // setting methods
    public void setPinnedTopicID(String topicID) {
        this.pinnedTopicID = topicID;
    }

    public ArrayList<String> getTopicIDs() {
        return topicIDs;
    }

    public boolean isParentGroup() {
        return parentGroupID.equals("");
    }

    public String getPath() {
        String path = "/";
        if (!isParentGroup()) {
            path += DBMgr.getGroupByID(parentGroupID).getGroupName() + "/";
        }
        path += groupName;
        path = path.replace(" ", "-");
        path = path.toLowerCase().replaceAll("[^a-z0-9-/]", "");
        return path;
    }

    public boolean getIsActive() {
        return isActive;
    }

    // methods
    public void addTopic(String userID, String topicTitle, String topicText) {

        Topic t = Topic.newTopic(userID, topicTitle, topicText, groupID);
        addTopicID(t.getTopicID());
    }

    public void addTopicID(String topicID) {

        boolean inserted = false;
        Topic newTopic = DBMgr.getTopicByID(topicID);
        int max = topicIDs.size();

        // adding topic ID in order of date
        for (int i = 0; i < max; i++) {
            if (!inserted) {
                Topic t = DBMgr.getTopicByID(topicIDs.get(i));
                if (newTopic.getTimeStamp() > t.getTimeStamp()) {
                    topicIDs.add(i, topicID);
                    inserted = true;
                }
            }
        }

        if (!inserted) topicIDs.add(topicID);
    }

    public void addSubgroup(String subGroupName) {
        Group subGroup = newGroup(subGroupName, groupID);
        addSubgroupID(subGroup.getGroupID());
    }

    public void addSubgroupID(String id) {
        subGroupIDs.add(id);
    }

    public void removeSubgroupID(String id) {
        subGroupIDs.remove(id);
    }

    public void removeTopic(String topicID) {
        if (topicID.equals(pinnedTopicID)) {
            pinnedTopicID = "";
        }
        topicIDs.remove(topicID);
    }

    public void delete() {

        // deleting all topics within group
        for (String tID : topicIDs) {
            Topic t = DBMgr.getTopicByID(tID);
            t.delete();
        }

        isActive = false;

        // deleting all subgroups if parent group
        if (isParentGroup()) {
            for (String gID : subGroupIDs) {
                Group g = DBMgr.getGroupByID(gID);
                g.delete();
            }
            subGroupIDs = new ArrayList<String>();
        }
    }
}
