package com.example.android.air_stories.Model;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;



public class    Stories implements Serializable {


    //    @SerializedName("shortID")
    int story_id;

    //    @SerializedName("shortTitle")
    String story_title;

    //    @SerializedName("shortGenre")
    String story_description;

    //    @SerializedName("shortStory")
    String story_genre;

    //    @SerializedName("appCount")
    int story_status;

    int likes;

    int readings;

    int chapters;

    String username;

    String coverImage;


    public Stories (int story_id, String story_title, String story_description, String story_genre, int story_status, int likes, int readings, int chapters, String username, String coverImage){

        this.story_id = story_id;
        this.story_title = story_title;
        this.story_description = story_description;
        this.story_genre = story_genre;
        this.story_status = story_status;
        this.likes = likes;
        this.readings = readings;
        this.chapters = chapters;
        this.username = username;
        this.coverImage = coverImage;

    }


    public int getStory_id() { return story_id; }

    public String getStory_title(){ return story_title; }

    public String getStory_description(){ return story_description; }

    public String getStory_genre(){ return story_genre; }

    public int getStatus(){ return story_status; }

    public int getLikes(){ return likes; }

    public int getReadings(){ return readings; }

    public int getChapters(){return chapters;}

    public String getUsername(){ return username; }

    public String getCoverImage(){return coverImage;}

}
