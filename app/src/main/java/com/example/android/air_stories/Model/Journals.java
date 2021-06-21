package com.example.android.air_stories.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Journals implements Serializable{

    int journal_id;

    int user_id;

    String journal_title;

    String journal_date;

    String journal;

    public Journals(int journal_id, int userID,  String journal_title, String journal_date, String journal){
        this.user_id = userID;
        this.journal_id = journal_id;
        this.journal_title = journal_title;
        this.journal_date = journal_date;
        this.journal = journal;
    }

    public int getUser_id(){
        return user_id;
    }

    public int getJournal_id(){
        return journal_id;
    }

    public String getJournal_title(){
        return journal_title;
    }

    public String getJournal_date(){
        return journal_date;
    }

    public String getJournal(){
        return journal;
    }
}