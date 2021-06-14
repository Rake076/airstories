package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Chapters;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DescriptionStoryActivity extends AppCompatActivity implements Serializable  {

    ImageView imageView;
    TextView title, username, appCount, description;

    MaterialButton readbtn;

    int userID, story_id;
    Intent intent;
    TextView username_textview;
    Call<List<Chapters>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);


    final ArrayList<Chapters> chaptersObject = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airstories_story_intro);

        boolean isShort = false;
        Intent intent = getIntent();

        Stories story = (Stories) intent.getSerializableExtra("Story");

        story_id = story.getStory_id();

        title = findViewById(R.id.short_title_textview);
        title.setText("" + story.getStory_title());


        appCount = findViewById(R.id.app_count_textview);
        appCount.setText("" + story.getLikes());

        description = findViewById(R.id.short_description_textview);
        description.setText("" + story.getStory_description());

        username = findViewById(R.id.username_textview);
        username.setText("" + story.getUsername());

        imageView = findViewById(R.id.short_image);
        Picasso.with(getApplicationContext()).load(story.getCoverImage()).into(imageView);

        readbtn = findViewById(R.id.read_btn);

//        readbtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ShortReadingActivity.class);
//                intent.putExtra("isShort", 0);
//                intent.putExtra("Story", chaptersObject);
//                startActivity(intent);
//
//            }
//        });

        networkCall();


    }


    public void networkCall() {
        listCall = jsonPlaceHolderApi.getStoryChapters(story_id);
        listCall.enqueue(new Callback<List<Chapters>>() {
            @Override
            public void onResponse(Call<List<Chapters>> call, Response<List<Chapters>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Chapters> chapters= response.body();

                for (Chapters Chapter : chapters) {
                    chaptersObject.add(new Chapters(Chapter.getChapter_id(), Chapter.getStory_id(),
                            Chapter.getChapter_name(), Chapter.getChapter_text(), Chapter.getStatus()));
                }
//                Toast.makeText(getApplicationContext(), chaptersObject.get(1).getChapter_name(), Toast.LENGTH_SHORT).show();

                ListView listView = findViewById(R.id.chapter_listview);
                AdapterChapter adapter = new AdapterChapter(getApplicationContext(), chaptersObject);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Chapters>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Unable to get Chapters at this moment", Toast.LENGTH_SHORT).show();
            }



        });

    }



}
