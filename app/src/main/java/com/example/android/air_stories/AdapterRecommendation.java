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

import com.example.android.air_stories.Model.Recommendation;
import com.example.android.air_stories.Model.ShortStories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRecommendation extends ArrayAdapter<Recommendation> {


    public AdapterRecommendation(Context mcontext, ArrayList<Recommendation> recommendation){
        super(mcontext, 0, recommendation);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recommend_listitem, parent, false);
        }

        TextView titleTextView, typeTextView, recommenderTextView;
        ImageView imageView;

        String type;

        Recommendation recommendation = getItem(position);

        titleTextView = convertView.findViewById(R.id.story_title_textview);
        titleTextView.setText(recommendation.getTitle());

        typeTextView = convertView.findViewById(R.id.type_textview);

        if(recommendation.getStory_type().equals("story"))
            type = "Story";
        else
            type = "Short Story";

        typeTextView.setText("Type: " + type);

        recommenderTextView = convertView.findViewById(R.id.recommendee_textview);
        recommenderTextView.setText("From: " + recommendation.getUsername());

        imageView = convertView.findViewById(R.id.image);

        Picasso.with(getContext())
                .load(recommendation.getCoverImage())
                .into(imageView);

        return convertView;
    }

}
