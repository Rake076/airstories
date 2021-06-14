package com.example.android.air_stories.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;

public class ShortStories implements Serializable {
//    @SerializedName("shortID")
    int shortID;

//    @SerializedName("shortTitle")
    String shortTitle;

//    @SerializedName("shortGenre")
    String shortGenre;

//    @SerializedName("shortStory")
    String shortStory;

//    @SerializedName("appCount")
    int appCount;

    String shortDescription;

    String username;

    String coverImage;


    public ShortStories (int short_id, String shortTitle, String shortStory, String shortGenre, int appCount, String shortDescription, String username, String coverImage){

        this.shortID = short_id;
        this.shortTitle = shortTitle;
        this.shortStory = shortStory;
        this.shortGenre = shortGenre;
        this.appCount = appCount;
        this.shortDescription = shortDescription;
        this.username = username;
        this.coverImage = coverImage;

    }


    public int getshortID(){ return shortID; }

    public String getShortTitle() { return shortTitle; }

    public String getShortGenre(){ return shortGenre; }

    public String getShortStory(){ return shortStory; }

    public int getAppCount(){ return appCount; }

    public String getShortDescription(){ return shortDescription; }

    public String getUsername(){ return username; }

    public String getCoverImage(){ return coverImage; }
}
