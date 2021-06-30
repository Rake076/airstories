package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Chapters;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
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

    ListView listView;
    AdapterChapter adapter;

    MaterialButton recommendBtn;
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

        Intent intent = getIntent();
        Stories story = (Stories) intent.getSerializableExtra("Story");
        User user = (User) intent.getSerializableExtra("user");

        recommendBtn = findViewById(R.id.recommend_btn);
        RelativeLayout chapters_layout = findViewById(R.id.chapter_view);
        story_id = story.getStory_id();

        title = findViewById(R.id.short_title_textview);
        title.setText("" + story.getStory_title());
//        listView = findViewById(R.id.chapter_listview);

        appCount = findViewById(R.id.app_count_textview);
        appCount.setText("" + story.getLikes());

        description = findViewById(R.id.short_description_textview);
        description.setText("" + story.getStory_description());

        username = findViewById(R.id.username_textview);
        username.setText("" + story.getUsername());

        imageView = findViewById(R.id.short_image);
        Picasso.with(getApplicationContext()).load(story.getCoverImage()).into(imageView);

        chapters_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChaptersActivity.class);
                intent.putExtra("story", story);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecommendUsersActivity.class);
                intent.putExtra("story", story);
                intent.putExtra("storyType", "story");
                startActivity(intent);
            }
        });
//        networkCall();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(getApplicationContext(), StoryReadingActivity.class);
//                intent.putExtra("story", story);
//                intent.putExtra("chapter", chaptersObject.get(position));
//                intent.putExtra("user", user);
//                startActivity(intent);
//            }
//        });


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


//                adapter = new AdapterChapter(getApplicationContext(), chaptersObject);
//                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Chapters>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Unable to get Chapters at this moment", Toast.LENGTH_SHORT).show();
            }



        });

    }



}
