package com.example.android.air_stories.Model;

import java.io.Serializable;

public class Recommendation implements Serializable {

    int rec_id;

    String story_type;

    int story_id;

    String coverImage;

    String username;

    String story_title;

    public Recommendation(int rec_id, String story_type, int story_id, String coverImage, String username, String story_title){
        this.rec_id = rec_id;
        this.story_type = story_type;
        this.story_id = story_id;
        this.coverImage = coverImage;
        this.username = username;
        this.story_title = story_title;
    }

    public int getRec_id() {
        return rec_id;
    }

    public String getStory_type() {
        return story_type;
    }

    public int getStory_id() {
        return story_id;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return story_title;
    }

}
