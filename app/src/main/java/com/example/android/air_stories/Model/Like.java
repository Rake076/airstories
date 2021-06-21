package com.example.android.air_stories.Model;

import android.widget.LinearLayout;

import java.io.Serializable;

public class Like implements Serializable {
    int like_id;

    int user_id;

    int status;

    int story_id;

    int story_type;

    public Like(int like_id, int user_id, int status, int story_id, int story_type){
        this.like_id = like_id;
        this.user_id = user_id;
        this.status = status;
        this.story_id = story_id;
        this.story_type = story_type;
    }

    public int getLike_id(){return like_id;}

    public int getUser_id(){return user_id;}

    public int getStatus(){return status; }

    public int getStory_id(){return story_id;}

    public int getStory_type(){return story_type;}
}
