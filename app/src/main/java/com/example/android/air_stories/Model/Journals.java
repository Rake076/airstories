package com.example.android.air_stories.Model;

public class Journals {

    int user_id;

    int journal_id;

    String journal_title;

    String journal_date;

    String journal;

    public Journals(int userID, int journal_id, String journal_title, String journal_date, String journal){
        this.user_id = userID;
        this.journal_id = journal_id;
        this.journal_title = journal_title;
        this.journal_date = journal_date;
        this.journal = journal;

    }

    int getUser_id(){
        return user_id;
    }

    int getJournal_id(){
        return journal_id;
    }

    String getJournal_title(){
        return journal_title;
    }

    String getJournal_date(){
        return journal_date;
    }

    String getJournal(){
        return journal;
    }
}