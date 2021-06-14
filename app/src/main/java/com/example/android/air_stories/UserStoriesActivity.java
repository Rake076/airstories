package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserStoriesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ViewPager viewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    String username;
    int userID;
    Intent intent;
    TextView username_textview;
    ListView listView;
    Call<List<ShortStories>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    final ArrayList<ShortStories> shortStoryObject = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stories);

        intent = getIntent();
        username = intent.getStringExtra("username");
        userID = intent.getIntExtra("userID", 0);
        networkCall();

        listView = (ListView)findViewById(R.id.short_story_list_view);
//        username_textview = findViewById(R.id.username_textview);
//        username_textview.setText(username);






        AdapterShort adapter = new AdapterShort(getApplicationContext(), shortStoryObject);
//
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ShortStories shortStory = shortStoryObject.get(position);

                Intent intent = new Intent(getApplicationContext(), StoryInfoEditingActivity.class);

                intent.putExtra("Story", shortStory);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);

//              To retrieve object in second Activity
//              getIntent().getSerializableExtra("MyClass");
                startActivity(intent);

            }
        });

    }



    public void networkCall() {
        listCall = jsonPlaceHolderApi.getUserShortStories(userID);
        listCall.enqueue(new Callback<List<ShortStories>>() {
            @Override
            public void onResponse(Call<List<ShortStories>> call, Response<List<ShortStories>> response) {

                if (!response.isSuccessful()) {
                    Log.d("Code", "Code: " + response.code());
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ShortStories> stories = response.body();
                shortStoryObject.clear();

                for (ShortStories Stories : stories) {


                    shortStoryObject.add(new ShortStories(Stories.getshortID(), Stories.getShortTitle(),
                            Stories.getShortStory(), Stories.getShortGenre(), Stories.getAppCount(),
                            Stories.getShortDescription(), Stories.getUsername(), Stories.getCoverImage()));
                    Log.v("shortID", ""+Stories.getshortID());
//                    textView.append(data);
                }
            }

            @Override
            public void onFailure(Call<List<ShortStories>> call, Throwable t) {


            }

        });
    }

}
