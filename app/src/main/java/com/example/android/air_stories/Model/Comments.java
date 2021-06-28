package com.example.android.air_stories.Model;

import java.io.Serializable;

public class Comments implements Serializable{

    int comment_id;

    int story_id;

    String comment;

    int user_id;

    String story_type;

    String username;

//    String coverImage;

    public Comments(int comment_id, int story_id, String comment, int user_id, String story_type, String username){
        this.comment_id = comment_id;
        this.story_id = story_id;
        this.comment = comment;
        this.user_id = user_id;
        this.story_type = story_type;
        this.username = username;
    }


    public int getStory_id() {
        return story_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getComment() {
        return comment;
    }

    public String getStory_type(){
        return story_type;
    }

    public String getUsername() {
        return username;
    }

//    public String getCoverImage() {
//        return coverImage;
//    }
}
