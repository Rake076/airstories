package com.example.android.air_stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.Chapters;
import com.example.android.air_stories.Model.Journals;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.Journals;

import java.util.ArrayList;

public class AdapterChapter extends ArrayAdapter<Chapters>{


        //      private int mColorResourceId;
        private ArrayList<Chapters> chapters;

        //        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
        public AdapterChapter(Context mcontext, ArrayList<Chapters> chapters){
            super(mcontext, 0, chapters);
            this.chapters = chapters;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chapter_listitem, parent, false);
            }



            TextView titleTextView, descriptionTextView;

            Chapters chapters = getItem(position);

            titleTextView = convertView.findViewById(R.id.chapter_title_textview);
            titleTextView.setText(chapters.getChapter_name());

            descriptionTextView = convertView.findViewById(R.id.chapter_description_textview);
            descriptionTextView.setText(chapters.getChapter_text());

            return convertView;
        }



}
