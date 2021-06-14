package com.example.android.air_stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.ShortStories;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;


public class AdapterStory extends ArrayAdapter<Stories> {


    //      private int mColorResourceId;
//    private ArrayList<Stories> stories;

    //        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
    public AdapterStory(Context mcontext, ArrayList<Stories> stories){
        super(mcontext, 0, stories);
//        this.stories = stories;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

//            View listItemView = convertView;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }



        TextView titleTextView, descriptionTextView, genreTextView, appCountTextView, usernameTextView;
        ImageView imageView;

        Stories stories = getItem(position);

        titleTextView = convertView.findViewById(R.id.short_title_textview);
        titleTextView.setText(stories.getStory_title());

        descriptionTextView = convertView.findViewById(R.id.short_description_textview);
        descriptionTextView.setText(stories.getStory_description());

        genreTextView = convertView.findViewById(R.id.short_genre_textview);
        genreTextView.setText(stories.getStory_genre());

        usernameTextView = convertView.findViewById(R.id.username_textview_short);
        usernameTextView.setText(stories.getUsername());

        appCountTextView = convertView.findViewById(R.id.app_count_textview);
        appCountTextView.setText("" + stories.getLikes());

        Log.e("data", "Data: "+stories.getStory_genre());
        imageView = convertView.findViewById(R.id.image);


        Picasso.with(getContext())
                .load(stories.getCoverImage())
                .into(imageView);


        return convertView;
    }

}
