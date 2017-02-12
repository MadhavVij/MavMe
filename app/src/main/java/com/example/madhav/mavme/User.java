package com.example.madhav.mavme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Madhav on 11/1/16.
 */
public class User {


    private String id;
    private String name;
    private String password;
    private String email;
    private boolean isMale;
    private Date dob;
    private String major;
    private String degree;
    private String gradYear;
    private String phoneNumber;
    private boolean onCampus;
    private boolean isPrivate;
    private boolean isAdmin;
    private Inbox inbox;
    private boolean isBlocked = false;


    public User() {
    }

    public User(String id, String name, String password, String email, boolean isMale, Date dob, String major, String degree,
                String gradYear, String phoneNumber, boolean onCampus, boolean isPrivate, boolean isAdmin, Inbox inbox, boolean isBlocked) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.isMale = isMale;
        this.dob = dob;
        this.major = major;
        this.degree = degree;
        this.gradYear = gradYear;
        this.phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
        this.onCampus = onCampus;
        this.isPrivate = isPrivate;
        this.isAdmin = isAdmin;
        this.inbox = inbox;
        this.isBlocked = isBlocked;

    }

    // static functions
    public static User newUser(String name, String password, String email, boolean isMale, Date dob, String major,
                               String degree, String gradYear, String phoneNumber, boolean onCampus, boolean isPrivate) {

        Inbox inbox = new Inbox();
        inbox.addNotification("0");

        // creating a new topic with standard parameters
        User u = new User(DBMgr.generateNewUserID(), name, password, email, isMale, dob, major, degree,
                gradYear, phoneNumber, onCampus, isPrivate, false, inbox, false);

        // saving notification database
        DBMgr.saveUser(u);
        return u;
    }

    // accessor functions
    public String getUserID() {
        return id;
    }

    public String getName() {
        return name;
    }

    // setter functions
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        if (isMale) {
            return "Male";
        } else {
            return "Female";
        }
    }

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    public String getDOB() {
        SimpleDateFormat dFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dFormatter.format(dob);
        return dateString;
    }

    public void setDOB(Date dob) {
        this.dob = dob;
    }

    public Date getRawDOB() {
        return dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getMajorIndex() {
        return DBMgr.majors.indexOf(major);
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGradYear() {
        return gradYear;
    }

    public void setGradYear(String gradYear) {
        this.gradYear = gradYear;
    }

    public String getPhoneNumber() {
        int length = phoneNumber.length();
        String start = phoneNumber.substring(0, Math.min(3, length));
        String middle = phoneNumber.substring(Math.min(3, length), Math.min(6, length));
        String end = phoneNumber.substring(Math.min(6, length));

        String phone = start;
        if (middle.length() > 0) phone = phone + "-" + middle;
        if (end.length() > 0) phone = phone + "-" + end;

        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
    }

    public String getRawPhoneNumber() {
        return phoneNumber;
    }

    public String getResidence() {
        if (onCampus) {
            return "On Campus";
        } else {
            return "Off Campus";
        }
    }

    public boolean getOnCampus() {
        return onCampus;
    }

    public void setOnCampus(boolean onCampus) {
        this.onCampus = onCampus;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void toggleIsBlocked() {
        isBlocked = !isBlocked;
    }


}
