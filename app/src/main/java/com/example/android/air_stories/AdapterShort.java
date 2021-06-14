package com.example.android.air_stories;

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

public class AdapterShort extends ArrayAdapter<ShortStories> {

//      private int mColorResourceId;
//        private ArrayList<ShortStories> shortStories;

//        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
        public AdapterShort(Context mcontext, ArrayList<ShortStories> shortStories){
            super(mcontext, 0, shortStories);
//            this.shortStories = shortStories;
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

            ShortStories shortStories = getItem(position);

            titleTextView = convertView.findViewById(R.id.short_title_textview);
            titleTextView.setText(shortStories.getShortTitle());

            descriptionTextView = convertView.findViewById(R.id.short_description_textview);
            descriptionTextView.setText(shortStories.getShortDescription());

            genreTextView = convertView.findViewById(R.id.short_genre_textview);
            genreTextView.setText(shortStories.getShortGenre());

            usernameTextView = convertView.findViewById(R.id.username_textview_short);
            usernameTextView.setText(shortStories.getUsername());

            appCountTextView = convertView.findViewById(R.id.app_count_textview);
            appCountTextView.setText("" + shortStories.getAppCount());

            imageView = convertView.findViewById(R.id.image);


        Picasso.with(getContext())
                .load(shortStories.getCoverImage())
                .into(imageView);


            return convertView;
        }

    }