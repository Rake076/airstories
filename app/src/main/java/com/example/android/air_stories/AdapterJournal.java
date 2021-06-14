package com.example.android.air_stories;

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

public class AdapterJournal extends  ArrayAdapter<Journals>{


        //      private int mColorResourceId;
        private ArrayList<Journals> journals;

        //        public StoryAdapter (Context mcontext, ArrayList<ShortStories> shortStories){
        public AdapterJournal(Context mcontext, ArrayList<Journals> journals){
            super(mcontext, 0, journals);
            this.journals = journals;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.journals_listitem, parent, false);
            }



            TextView titleTextView, descriptionTextView, dateTextView;

            Journals journals = getItem(position);

            titleTextView = convertView.findViewById(R.id.journal_title_textview);
            titleTextView.setText(journals.getJournal_title());

            descriptionTextView = convertView.findViewById(R.id.journal_description_textview);
            descriptionTextView.setText(journals.getJournal());

            dateTextView = convertView.findViewById(R.id.journal_date_textview);
            dateTextView.setText(journals.getJournal_date());
            return convertView;
        }




}
