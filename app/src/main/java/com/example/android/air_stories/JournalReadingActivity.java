package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chinalwb.are.render.AreTextView;
import com.example.android.air_stories.Model.Journals;

public class JournalReadingActivity extends AppCompatActivity {

    AreTextView journal_textview;
    TextView title_textview;
    ImageView like_btn, comment_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_reading);

        Intent intent = getIntent();
        Journals journal = (Journals) intent.getSerializableExtra("journal");

        journal_textview = (AreTextView) findViewById(R.id.short_story_textview);
        title_textview = findViewById(R.id.short_title_textview);


        title_textview.setText("" + journal.getJournal_title());
        journal_textview.fromHtml("" + journal.getJournal());

        like_btn = findViewById(R.id.like_btn);
        comment_btn = findViewById(R.id.comment_btn);

        like_btn.setImageDrawable(null);
        comment_btn.setImageDrawable(null);


    }
}
