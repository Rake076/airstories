package com.example.android.air_stories.Model;

import java.io.Serializable;

public class Chapters implements Serializable {

    int chapter_id;

    int story_id;

    String chapter_name;

    String chapter_text;

    int status;

    public Chapters(int chapter_id, int story_id, String chapter_name, String chapter_text, int status){
        this.chapter_id = chapter_id;
        this.story_id = story_id;
        this.chapter_name = chapter_name;
        this.chapter_text = chapter_text;
        this.status = status;
    }

    public int getChapter_id(){return chapter_id;}

    public int getStory_id(){return story_id;}

    public String getChapter_name(){return chapter_name;}

    public  String getChapter_text(){return chapter_text;}

    public int getStatus(){return status;}

}
