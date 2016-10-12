package com.cyrus.zhihudaily.models;

import java.util.ArrayList;

/**
 * Created by weics on 2016/4/25.
 */
public class NewsData {
    private String date;
    private ArrayList<Story> stories;
    private ArrayList<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public ArrayList<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<TopStory> top_stories) {
        this.top_stories = top_stories;
    }

}
