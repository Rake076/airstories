package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class DescriptionShortActivity extends AppCompatActivity implements Serializable {

    ImageView imageView;
    TextView title, username, appCount, description;

    MaterialButton readbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airstories_short_intro);

        Intent intent = getIntent();

            ShortStories shortStory = (ShortStories) intent.getSerializableExtra("shortStory");
            User user = (User) intent.getSerializableExtra("user");

            title = findViewById(R.id.short_title_textview);
            title.setText("" + shortStory.getShortTitle());

            appCount = findViewById(R.id.app_count_textview);
            appCount.setText("" + shortStory.getAppCount());

            description = findViewById(R.id.short_description_textview);
            description.setText("" + shortStory.getShortDescription());

            username = findViewById(R.id.username_textview);
            username.setText("" + shortStory.getUsername());

            imageView = findViewById(R.id.short_image);
            Picasso.with(getApplicationContext())
                    .load(shortStory.getCoverImage())
                    .into(imageView);

            readbtn = findViewById(R.id.read_btn);

            readbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ShortReadingActivity.class);
                    intent.putExtra("ShortStory", shortStory);
                    intent.putExtra("user", user);
                    startActivity(intent);

                }
            });

    }
}
