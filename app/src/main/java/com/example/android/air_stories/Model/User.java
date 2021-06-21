package com.example.android.air_stories.Model;

import java.io.Serializable;

public class User implements Serializable {

    int user_id;

    String username;

    String email;

    String password;

    int story_count;

    public User(int userID, String username, String email, String password, int story_count){
        this.user_id = userID;
        this.story_count = story_count;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getUserID(){
        return user_id;
    }

    public int getStory_count(){
        return story_count;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

}
