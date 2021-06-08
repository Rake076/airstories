package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chinalwb.are.render.AreTextView;
import com.example.android.air_stories.Model.ShortStories;

public class ShortReadingActivity extends AppCompatActivity {

    TextView title_textview;
    AreTextView short_story_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_reading);




        Intent intent = getIntent();
        ShortStories shortStory = (ShortStories) intent.getSerializableExtra("ShortStory");



        title_textview = findViewById(R.id.short_title_textview);
        title_textview.setText("" + shortStory.getShortTitle());


        short_story_textview = (AreTextView) findViewById(R.id.short_story_textview);
//        short_story_textview.setText("" + shortStory.getShortStory());
        short_story_textview.fromHtml("" + shortStory.getShortStory());



    }
}