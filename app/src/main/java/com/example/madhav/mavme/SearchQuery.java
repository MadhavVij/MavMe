package com.example.madhav.mavme;

import java.util.ArrayList;

/**
 * Created by Madhav on 11/17/16.
 */
public class SearchQuery {

    private String query;
    private ArrayList<String> userIDs;

    public SearchQuery(String query) {
        this.query = query.toLowerCase();
        this.userIDs = new ArrayList<String>();
    }

    /* fuzzy searching using cosing similarity
    */
    public ArrayList<String> search() {

        ArrayList<Double> scores = new ArrayList<Double>();

        // for now just return first 10 users in system unlesss query is empty
        if (!query.isEmpty()) {
            String[] qNames = query.split(" ");
            int qSize = qNames.length;

            for (int i = 0; i < Integer.parseInt(DBMgr.generateNewUserID()); i++) {
                User u = DBMgr.getUserByID("" + i);

                String[] uNames = u.getName().toLowerCase().split(" ");
                int uSize = uNames.length;
                double sumScore = 0, avgScore = 0;

                for (int j = 0; j < Math.min(qSize, uSize); j++) {
                    sumScore += Utils.cosineSimilarity(qNames[j], uNames[j]);
                }

                avgScore = sumScore / Math.max(qSize, uSize);
                if (avgScore >= .9) {
                    userIDs.add("" + i);
                    scores.add(-avgScore);
                }

            }
        } else {
            userIDs.clear();
        }

        ArrayList<String> sortedUserIDs = Utils.pairSort(userIDs, scores);
        return sortedUserIDs;
    }
}
