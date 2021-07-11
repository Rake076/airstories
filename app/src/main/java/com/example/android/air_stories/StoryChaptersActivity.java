package com.example.android.air_stories;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Retrofit retrofit = RetrofitClient.getInstance();
    INodeJS myAPI = retrofit.create(INodeJS.class);

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_edit_list_activity);

        addChapter_btn = findViewById(R.id.write_chapter_btn);
        Intent intent = getIntent();
        Stories story = (Stories) intent.getSerializableExtra("story");
        story_id = story.getStory_id();
        listView = findViewById(R.id.chapter_listview);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_chapters);
        networkCall();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String[] options = {"Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(StoryChaptersActivity.this);
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if("Edit".equals(options[which])){
                            Intent intent = new Intent(getApplicationContext(), ChapterEditActivity.class);
                            intent.putExtra("chapter", chaptersObject.get(position));
                            startActivity(intent);
                        }
                        else if ("Delete".equals(options[which])){
                            deleteChapters(chaptersObject.get(position).getChapter_id());
                            chaptersObject.remove(position);
                            adapter = new AdapterChapter(getApplicationContext(), chaptersObject);
                            listView.setAdapter(adapter);
                        }

                    }
                });
                builder.show();
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listView.getChildAt(0) != null) {
                    mSwipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            networkCall();
                        }
                    }
                }, 1000);




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

    public void deleteChapters(int chapter_id){
        compositeDisposable.add(myAPI.deleteChapters(chapter_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
//                                   if (s.contains("liked")) {
//                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                   } else {
//                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                   }
                                   Toast.makeText(getApplicationContext(), "Chapter deleted successfully", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
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

                chaptersObject.clear();
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