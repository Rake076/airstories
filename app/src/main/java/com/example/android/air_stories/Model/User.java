package com.example.android.air_stories.Model;

import java.io.Serializable;

public class User implements Serializable {

    int user_id;

    String username;

    String u_email;

    String u_password;

    String u_profile_image;

    String about;

    int story_count;

    public User(int userID, String username, String email, String password, int story_count, String profile_image, String about){
        this.user_id = userID;
        this.story_count = story_count;
        this.username = username;
        this.u_email = email;
        this.u_password = password;
        this.u_profile_image = profile_image;
        this.about = about;
    }

    public User(int userID, String username, String profile_image, String about){
        this.user_id = userID;
        this.username = username;
        this.u_profile_image = profile_image;
        this.about = about;
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
        return u_email;
    }

    public String getPassword(){
        return u_password;
    }

    public String getAbout() {
        return about;
    }

    public String getProfile_image() {
        return u_profile_image;
    }
}
