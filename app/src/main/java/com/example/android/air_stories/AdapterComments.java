package com.example.android.air_stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.Comments;
import com.example.android.air_stories.Model.Journals;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AdapterComments extends ArrayAdapter<Comments> {



        //      private int mColorResourceId;
        private ArrayList<Comments> comments;

        //        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
        public AdapterComments(Context mcontext, ArrayList<Comments> comments){
            super(mcontext, 0, comments);
            this.comments = comments;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_listitem, parent, false);
            }


            TextView comment_textview, comment_user_textview;
            MaterialButton post_comment_btn;
//            AreTextView descriptionTextView;

            Comments comments = getItem(position);


            comment_textview = convertView.findViewById(R.id.comment_textview);
            comment_user_textview = convertView.findViewById(R.id.comment_user_textview);


            comment_textview.setText(comments.getComment());
            comment_user_textview.setText(comments.getUsername());


            return convertView;
        }



}
