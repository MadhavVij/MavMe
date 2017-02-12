package com.example.madhav.mavme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by madhav on 11/24/16.
 */

public class Utils {


    /************CO-SINE SIMILARITY*****************/
    /**
     * author: https://blog.nishtahir.com/2015/09/20/fuzzy-string-matching-using-cosine-similarity/
     *
     * @param terms values to analyze
     * @return a map containing unique
     * terms and their frequency
     */
    public static Map<String, Integer> getTermFrequencyMap(String[] terms) {
        Map<String, Integer> termFrequencyMap = new HashMap<>();
        for (String term : terms) {
            Integer n = termFrequencyMap.get(term);
            n = (n == null) ? 1 : ++n;
            termFrequencyMap.put(term, n);
        }
        return termFrequencyMap;
    }

    /**
     * @param text1
     * @param text2
     * @return cosine similarity of text1 and text2
     */


    public static double cosineSimilarity(String text1, String text2) {
        //Get vectors
        Map<String, Integer> a = getTermFrequencyMap(text1.split(""));
        Map<String, Integer> b = getTermFrequencyMap(text2.split(""));

        //Get unique words from both sequences
        HashSet<String> intersection = new HashSet<>(a.keySet());
        intersection.retainAll(b.keySet());

        double dotProduct = 0, magnitudeA = 0, magnitudeB = 0;

        //Calculate dot product
        for (String item : intersection) {
            dotProduct += a.get(item) * b.get(item);
        }

        //Calculate magnitude a
        for (String k : a.keySet()) {
            magnitudeA += Math.pow(a.get(k), 2);
        }

        //Calculate magnitude b
        for (String k : b.keySet()) {
            magnitudeB += Math.pow(b.get(k), 2);
        }

        //return cosine similarity
        return dotProduct / Math.sqrt(magnitudeA * magnitudeB);
    }


    /************
     * SORTING
     **************/
    public static <T, V extends Object & Comparable<V>> ArrayList<T> pairSort(ArrayList<T> l1, ArrayList<V> l2) {
        int size = l1.size();
        ArrayList<V> sortedL2 = new ArrayList<V>();
        ArrayList<T> sortedL1 = new ArrayList<T>();

        // sorting by List2
        for (int i = 0; i < size; i++) {
            sortedL2.add(l2.get(i));
        }
        Collections.sort(sortedL2);

        // Using SortedList2 to map to List1
        for (int i = 0; i < size; i++) {
            V sortedValue = sortedL2.get(i);
            sortedL1.add(l1.get(l2.indexOf(sortedValue)));
        }

        return sortedL1;

    }


    /************
     * NAVIGATION
     ***********/
    public static void goToURL(Context context, String url) {

        User activeUser = DBMgr.getUserByID(HomeDisplay.userID);
        String errorObj = "";

        //going to link
        if (url.length() != 0) {
            String id = url.substring(1);

            // linking to topic
            if (url.charAt(0) == 'T') {
                Topic t = DBMgr.getTopicByID(id);
                if (t == null && !activeUser.getIsAdmin()) {
                    errorObj = "Topic";
                } else if (!t.getIsActive() && !activeUser.getIsAdmin()) {
                    errorObj = "Topic";
                } else {
                    Intent i = new Intent("com.example.madhav.com.example.madhav.mavme.TopicDisplay");
                    i.putExtra("topicID", id);
                    context.startActivity(i);
                }
            }

            // linking to user
            if (url.charAt(0) == 'U') {
                User u = DBMgr.getUserByID(id);

                if (u == null && !activeUser.getIsAdmin()) {
                    errorObj = "User";
                } else if (u.getIsBlocked() && !activeUser.getIsAdmin()) {
                    errorObj = "User";
                } else {
                    Intent i = new Intent("com.example.madhav.com.example.madhav.mavme.UserDisplay");
                    i.putExtra("userID", id);
                    context.startActivity(i);
                }

            }


            // linking to group
            if (url.charAt(0) == 'G') {
                Group g = DBMgr.getGroupByID(id);

                if (g == null && !activeUser.getIsAdmin()) {
                    errorObj = "Group";
                } else if (!g.getIsActive() && !activeUser.getIsAdmin()) {
                    errorObj = "Group";
                } else {
                    Intent i = new Intent("com.example.madhav.com.example.madhav.mavme.GroupDisplay");
                    i.putExtra("groupID", id);
                    context.startActivity(i);
                }
            }

            if (errorObj.length() > 0)
                alertDialog(context, errorObj + " is no longer active", new dialogHandler() {
                    public void onButtonClick(boolean click) {
                    }
                });
        }
    }

    public static void confirmationDialog(Context context, String message, final dialogHandler d) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                d.onButtonClick(true);
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d.onButtonClick(false);
                    }
                });

        // Create the AlertDialog object and return it
        builder.create().show();
    }

    public static void alertDialog(Context context, String message, final dialogHandler d) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                d.onButtonClick(true);
            }
        });

        // Create the AlertDialog object and return it
        builder.create().show();

    }

    /************
     * DIALOGS
     *************/
    public interface dialogHandler {
        void onButtonClick(boolean click);
    }

}
