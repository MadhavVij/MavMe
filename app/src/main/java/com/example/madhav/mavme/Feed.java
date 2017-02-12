package com.example.madhav.mavme;

import java.util.ArrayList;

/**
 * Created by madhav on 10/25/16.
 */
public class Feed {

    private ArrayList<String> topicIDs;
    private ArrayList<Double> feedValues;

    public Feed() {
    }

    public Feed(ArrayList<String> topicIDs, ArrayList<Double> feedValues) {

        this.topicIDs = topicIDs;
        this.feedValues = feedValues;

    }


    public ArrayList<String> getTopicIDs() {
        ArrayList<String> topicIDs = new ArrayList<String>();
        for (int i = 0; i < this.topicIDs.size(); i++) {
            topicIDs.add(this.topicIDs.get(i));
        }
        return topicIDs;
    }

    public void sortTopicIDs() {
        int feedSize = topicIDs.size();
        ArrayList<String> sortedTopicIDs = new ArrayList<String>();
        //double[] sortedFeedValues = new double[feedSize];

        sortedTopicIDs = Utils.pairSort(topicIDs, feedValues);

        /*
        double countValue=0;

        for(int i=0; i<feedSize; i++) {
            sortedFeedValues[i]=feedValues.get(i);
        }

        Arrays.sort(sortedFeedValues);

        for(int i=0; i<feedSize; i++) {
            countValue=sortedFeedValues[feedSize-1-i];
            sortedTopicIDs.add(topicIDs.get(feedValues.indexOf(countValue)));
        }*/

        // Copying sorted values of temp feed to topicList
        for (int i = 0; i < feedSize; i++) {
            topicIDs.set(i, sortedTopicIDs.get(i));
        }
    }

    public void trim(int size) {
        size = Math.min(size, topicIDs.size());
        for (int i = topicIDs.size() - 1; i >= size; i--) {
            topicIDs.remove(i);
            feedValues.remove(i);
        }
        /*
        topicIDs = topicIDs.subList(0, size);
        feedValues = feedValues.subList(0, size);*/
    }

}
