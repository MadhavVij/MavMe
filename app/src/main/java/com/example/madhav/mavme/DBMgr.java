package com.example.madhav.mavme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by madhav on 11/3/16.
 */
public class DBMgr {

    public static ArrayList<String> majors = new ArrayList<String>(
            Arrays.asList("Accounting",
                    "Aerospace Engineering",
                    "Anthropology",
                    "Architecture",
                    "Art",
                    "Athletic Training",
                    "Biochemistry",
                    "Biology",
                    "BusinessAdministration",
                    "Chemistry",
                    "Child/Bilingual Studies",
                    "Civil Engineering",
                    "Communication",
                    "Computer Science",
                    "Computer Science and Engineering",
                    "Criminology and Criminal Justice",
                    "Economics",
                    "Electrical Engineering",
                    "English",
                    "Exercise Science",
                    "Geology",
                    "History",
                    "Industrial Engineering",
                    "Information Systems",
                    "Interdisciplinary Studies",
                    "Interdisciplinary Studies",
                    "Interior Design",
                    "Kinesiology",
                    "Mathematics",
                    "Mechanical Engineering",
                    "Medical Technology",
                    "Microbiology",
                    "Modern Languages",
                    "Music",
                    "Nursing",
                    "Philosophy",
                    "Physics",
                    "Political Science",
                    "Psychology",
                    "Social Work",
                    "Sociology",
                    "Software Engineering",
                    "Theatre Arts",
                    "University Studies",
                    "Undecided/Other"));
    //dummy DB in memory
    private static boolean initiated = false;
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<String> adminIDs = new ArrayList<String>();
    private static ArrayList<Group> groups = new ArrayList<Group>();
    private static ArrayList<Topic> topics = new ArrayList<Topic>();
    private static ArrayList<Comment> comments = new ArrayList<Comment>();
    private static ArrayList<Notification> notifications = new ArrayList<Notification>();

    //initiates the dummy DB... no need to recreate it
    public static void initiateDB() {

        if (!initiated) {
            initiated = true;
            // Creating system user
            User Sys = new User("0", "System", "abc123", "system@mavs.uta.edu", true, new Date(0, 0, 0),
                    "Undecided/Other", "Faculty", "Faculty", "0000000000", true, true, true, new Inbox(), false);
            DBMgr.saveUser(Sys);

            // creating dummy users
            User.newUser("Madhav Vij", "abc12345", "madhav.vij@mavs.uta.edu", true, new Date(91, 6, 6),
                    "Computer Science", "Masters", "2018", "4696014388", false, false);
            User.newUser("Rohit Sarwate", "abc12345", "rohit.sarwate@mavs.uta.edu", true, new Date(90, 0, 1),
                    "Computer Science", "Masters", "2018", "4699541150", false, false);
            User.newUser("Rohini Gund", "abc12345", "rohini.gund@mavs.uta.edu", false, new Date(90, 0, 1),
                    "Computer Science", "Masters", "2018", "4695438667", false, false);
            User.newUser("Brad Simpson", "abc12345", "brad.simpson@mavs.uta.edu", true, new Date(90, 0, 1),
                    "Undecided/Other", "Undergraduate", "2020", "0000000000", true, false);


            for (User u : users) {
                if (u.getIsAdmin()) adminIDs.add(u.getUserID());
            }


            // creating default notifications
            Notification.newNotification("0", "Welcome to MavMe, the social network " +
                    "designed by and for UTA Mavericks!", "");


            // creating default groups
            Group g = Group.newGroup("Academics", "");
            g.addSubgroup("Architecture, Planning & Public Affairs");
            g.addSubgroup("Business");
            g.addSubgroup("Education");
            g.addSubgroup("Engineering");
            g.addSubgroup("Health & Nursing");
            g.addSubgroup("Liberal Arts");
            g.addSubgroup("Natural Sciences");
            g.addSubgroup("Social Work");
            g = Group.newGroup("Athletics", "");
            g.addSubgroup("NCAA");
            g.addSubgroup("Club Sports");
            Group.newGroup("Clubs", "");
            g = Group.newGroup("Dating", "");
            g.addSubgroup("Singles");
            g.addSubgroup("Couples");
            Group.newGroup("Events", "");
            Group.newGroup("Explore Arlington", "");
            Group.newGroup("Food", "");
            Group.newGroup("Faculty", "");
            Group.newGroup("Gaming", "");
            Group.newGroup("Graduate Students", "");
            Group.newGroup("Greek Life", "");
            Group.newGroup("Housing", "");
            Group.newGroup("Incoming Mavericks", "");
            Group.newGroup("Off Campus Life", "");
            g = Group.newGroup("Miscellaneous", "");


            // creating dummy topics & dummy comments
            g.addTopic("1", "Hi, this is a test topic!", "Just testing the new MavMe system! :)");

            Random r = new Random();
            Date today = new Date();
            Date postDate;

            /*
            for (int i = 0; i < 100; i++) {
                ArrayList<String> flagUserIDs = new ArrayList<String>(0);
                ArrayList<String> mavLoveUserIDs = new ArrayList<String>();
                ArrayList<String> commentIDs = new ArrayList<String>();

                String userID = "" + r.nextInt(users.size());
                String groupID = "" + r.nextInt(groups.size());

                g = DBMgr.getGroupByID(groupID);

                double start = new Date(116, 0, 1).getTime();
                double end = today.getTime();
                double range = end-start;
                postDate = new Date((long) (start+range*r.nextDouble()));

                Topic t = new Topic("" + i, postDate, userID, "Topic #" + i, groupID, "Test topic content",
                        mavLoveUserIDs, flagUserIDs, commentIDs);

                double popularity = Math.pow(r.nextDouble(), 3);

                for (int j = 1; j < 100; j++) {
                    if (r.nextDouble() < popularity)
                        t.toggleLove("" + j);
                }

                topics.add(t);
                g.addTopicID(t.getTopicID());
            }*/
        }
    }

    /******************************************************************************************/
    // ID generating methods... These methods generate a sequential ID (starting from 0...# of DB OBJECTS)
    // for users, notifications, groups, topics, and comments
    public static String generateNewUserID() {
        return "" + users.size();
    }

    public static String generateNewNotificationID() {
        return "" + notifications.size();
    }

    public static String generateNewGroupID() {
        return "" + groups.size();
    }

    public static String generateNewTopicID() {
        return "" + topics.size();
    }

    public static String generateNewCommentID() {
        return "" + comments.size();
    }


    /******************************************************************************************/
    // These methods take an object and save it to the DB
    public static void saveUser(User u) {
        users.add(u);
    }

    public static void saveNotification(Notification n) {
        notifications.add(n);
    }

    public static void saveGroup(Group g) {
        groups.add(g);
    }

    public static void saveTopic(Topic t) {
        topics.add(t);
    }

    public static void saveComment(Comment c) {
        comments.add(c);
    }

    /******************************************************************************************/
    // these methods take an ID and return the object from the DB
    public static User getUserByID(String id) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getUserID().equals(id))
                return u;
        }
        return null;
    }


    public static User getUserByEmail(String email) {
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getEmail().equals(email))
                return u;
        }
        return null;
    }

    public static Group getGroupByID(String id) {
        for (int i = 0; i < groups.size(); i++) {
            Group g = groups.get(i);
            if (g.getGroupID().equals(id))
                return g;
        }
        return null;
    }

    public static Topic getTopicByID(String id) {
        for (int i = 0; i < topics.size(); i++) {
            if (id.equals(topics.get(i).getTopicID()))
                return topics.get(i);
        }
        return null;
    }

    public static Comment getCommentByID(String id) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getCommentID().equals(id))
                return comments.get(i);
        }
        return null;
    }

    public static Notification getNotificationByID(String id) {
        for (int i = 0; i < notifications.size(); i++) {
            if (id.equals(notifications.get(i).getNotificationID()))
                return notifications.get(i);
        }
        return null;
    }


    /******************************************************************************************/
    //this method returns an ArrayList containing the user IDs of all the MavMe admins
    public static ArrayList<String> getAdminIDs() {
        return adminIDs;
    }

    public static ArrayList<User> getAllUsers() {
        return users;
    }


    //this method returns an ArrayList of all the group IDs sorted alphabetically
    public static ArrayList<String> getSortedGroupIDs() {
        ArrayList<String> sortedGroupIDs = new ArrayList<String>();
        ArrayList<String> groupIDs = new ArrayList<String>();
        ArrayList<String> groupNames = new ArrayList<String>();

        for (Group g : groups) {
            groupIDs.add(g.getGroupID());
            groupNames.add(g.getGroupName());
        }

        /*
        for(int i=0; i<groups.size(); i++) {
            sortedGroupIDs.add(groups.get(i).getGroupID());
        }
        return sortedGroupIDs;*/

        sortedGroupIDs = Utils.pairSort(groupIDs, groupNames);

        return sortedGroupIDs;
    }


    //this method takes an integer 'n' and returns the 'n' most recent topics by date...
    public static ArrayList<Topic> getMostRecentTopics(int n) {
        ArrayList<Topic> topicList = new ArrayList<Topic>();
        int i = topics.size() - 1, count = 0;

        // preventing index out of bounds error
        if (n < 0 || n > topics.size()) n = topics.size();

        // getting the most recent 'n' topics
        while (i >= topics.size() - n && count < n) {
            if (topics.get(i).getIsActive()) {
                topicList.add(topics.get(i));
                count++;
            }
            i--;
        }

        return topicList;
    }

}
