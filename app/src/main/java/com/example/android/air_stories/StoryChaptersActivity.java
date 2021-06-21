package com.example.android.air_stories;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class StoryChaptersActivity extends AppCompatActivity {

    ListView listView;
    AdapterChapter adapter;

    MaterialButton addChapter_btn;

    int story_id;
    Intent intent;

    Call<List<Chapters>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    final ArrayList<Chapters> chaptersObject = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_edit_list_activity);

        addChapter_btn = findViewById(R.id.write_chapter_btn);
        Intent intent = getIntent();
        Stories story = (Stories) intent.getSerializableExtra("story");
        story_id = story.getStory_id();
        listView = findViewById(R.id.chapter_listview);
        networkCall();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(getApplicationContext(), ChapterEditActivity.class);
//                intent.putExtra("story", story);
////                intent.putExtra("chapter", chaptersObject.get(position));
//                startActivity(intent);
            }
        });

        addChapter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChapterWritingActivity.class);
                intent.putExtra("story", story);
                startActivity(intent);

            }
        });

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

                adapter = new AdapterChapter(getApplicationContext(), chaptersObject);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Chapters>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Unable to get Chapters at this moment", Toast.LENGTH_SHORT).show();
            }
        });
    }








}