package com.example.android.air_stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterUser extends ArrayAdapter<User> {

    //        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
    public AdapterUser(Context mcontext, ArrayList<User> users){
        super(mcontext, 0, users);
//            this.shortStories = shortStories;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

//            View listItemView = convertView;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_listitem, parent, false);
        }


        User user = getItem(position);

        TextView usernameTextView, descriptionTextView;
        ImageView imageView;


        usernameTextView = convertView.findViewById(R.id.username_textview);
        usernameTextView.setText(user.getUsername());

        descriptionTextView = convertView.findViewById(R.id.user_description_textview);
        descriptionTextView.setText(user.getAbout());

        imageView = convertView.findViewById(R.id.user_image);

        Picasso.with(getContext())
                .load(user.getProfile_image())
                .into(imageView);


        return convertView;
    }
}
