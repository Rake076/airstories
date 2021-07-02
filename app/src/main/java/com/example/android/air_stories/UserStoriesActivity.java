package com.example.android.air_stories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

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

public class UserStoriesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ViewPager viewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SwitchMaterial switchMaterial;

    SwipeRefreshLayout mSwipeRefreshLayout;

    TextInputEditText search_edit_text;
    String username;
    int userID;
    Intent intent;
    TextView username_textview, top_textview;
    ListView listView;
    Call<List<ShortStories>> listCall;
    Call<List<Stories>> storyListCall;

    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    Retrofit retrofit = RetrofitClient.getInstance();
    INodeJS myAPI = retrofit.create(INodeJS.class);

//    private SwipeRefreshLayout mSwipeRefreshLayout;

    final ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    final ArrayList<Stories> StoryObject = new ArrayList<>();





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_stories_activity);

        switchMaterial = findViewById(R.id.switch1);
        switchMaterial.setText("Short Stories");

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_stories);

//        switchMaterial.findViewById(R.id.switch1);
        intent = getIntent();
        username = intent.getStringExtra("username");
        userID = intent.getIntExtra("userID", 0);
        networkCall();


        listView = (ListView)findViewById(R.id.short_story_list_view);





        AdapterShort adapter = new AdapterShort(getApplicationContext(), shortStoryObject);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                listView.setAdapter(adapter);
                refreshListView();
            }
        }, 100);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            refreshListView();
                        }
                    }
                }, 300);


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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                String[] options = {"Yes", "No"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UserStoriesActivity.this);

                if(switchMaterial.isChecked()){
                    // Short Stories
                    builder.setTitle("Delete short story?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if("Yes".equals(options[which])){
                                deleteShortStories(shortStoryObject.get(i).getshortID());
                                shortStoryObject.remove(i);
                                AdapterShort adapter = new AdapterShort(getApplicationContext(), shortStoryObject);
                                listView.setAdapter(adapter);
                            }
                            else if ("No".equals(options[which])){
                            }
                        }
                    });
                    builder.show();

                }
                else {
                    // Stories
                    String[] storyOptions = {"Delete Story", "Unpublish"};

                    int status = StoryObject.get(i).getStatus();
                    if(status == 1)
                    {
                        storyOptions[1] = "Unpublish";
                    }
                    else
                    {
                        storyOptions[1] = "Publish";
                    }
                    builder.setTitle("Options");
                    builder.setItems(storyOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if( storyOptions[0].equals(storyOptions[which]) ){
                                deleteStories(StoryObject.get(i).getStory_id());
                                StoryObject.remove(i);
                                AdapterStory adapter = new AdapterStory(getApplicationContext(), StoryObject);
                                listView.setAdapter(adapter);
                            }
                            else if ("Publish".equals(storyOptions[which])){
                               publishStories(StoryObject.get(i).getStory_id());
                            }
                            else if ("Unpublish".equals(storyOptions[which])){
                                unpublishStories(StoryObject.get(i).getStory_id());
                            }
                        }
                    });
                    builder.show();

                }



                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(switchMaterial.isChecked()){
                    ShortStories shortStory = shortStoryObject.get(position);
                    Intent intent = new Intent(getApplicationContext(), ShortStoryInfoEditingActivity.class);
                    intent.putExtra("Story", shortStory);
                    intent.putExtra("username", username);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
                else {
                    Stories story = StoryObject.get(position);
                    Intent intent = new Intent(getApplicationContext(), StoryInfoEditingActivity.class);
                    intent.putExtra("Story", story);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }
            }
        });




        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMaterial.isChecked()){

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switchMaterial.setText("Short Stories");
                            networkCall();
                            AdapterShort adapter = new AdapterShort(getApplicationContext(), shortStoryObject);
                            listView.setAdapter(adapter);
                        }
                    }, 100);
                }
                else{
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switchMaterial.setText("Stories");
                            storyNetworkCall();
                            AdapterStory storyAdapter = new AdapterStory(getApplicationContext(), StoryObject);
                            listView.setAdapter(storyAdapter);                        }
                    }, 100);
                }
            }
        });


    }





    public void deleteShortStories(int shortID){
        compositeDisposable.add(myAPI.deleteShortStories(SaveSharedPreference.getUserID(getApplication()), shortID)
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
                                   Toast.makeText(getApplicationContext(), "Short Story successfully deleted", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    public void deleteStories(int story_ID){
        compositeDisposable.add(myAPI.deleteStories(SaveSharedPreference.getUserID(getApplication()), story_ID)
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
                                   Toast.makeText(getApplicationContext(), "Story successfully deleted", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    public void publishStories(int story_id){
        compositeDisposable.add(myAPI.publishStories(SaveSharedPreference.getUserID(getApplication()), story_id)
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
                                   Toast.makeText(getApplicationContext(), "Short Story successfully published", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    public void unpublishStories(int story_id){
        compositeDisposable.add(myAPI.unpublishStories(SaveSharedPreference.getUserID(getApplication()), story_id)
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
                                   Toast.makeText(getApplicationContext(), "Short Story successfully unpublished!", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    public void storyNetworkCall() {
        storyListCall = jsonPlaceHolderApi.getUserStories(userID);
        storyListCall.enqueue(new Callback<List<Stories>>() {
            @Override
            public void onResponse(Call<List<Stories>> call, Response<List<Stories>> response) {

                if (!response.isSuccessful()) {
//                    textView.setText("Code " + response.code());
                    return;
                }

                List<Stories> stories = response.body();

                StoryObject.clear();
                for (Stories Stories : stories) {

//                    int story_id, String story_title, String story_description, String story_genre, int status,
//                    int likes, int readings, int chapters, String username, String coverImage

                    StoryObject.add(new Stories(Stories.getStory_id(), Stories.getStory_title(),
                            Stories.getStory_description(), Stories.getStory_genre(), Stories.getStatus(),
                            Stories.getLikes(), Stories.getReadings(), Stories.getChapters(), Stories.getUsername(), Stories.getCoverImage()));
//                    textView.append(data);
                }
            }

            @Override
            public void onFailure(Call<List<Stories>> call, Throwable t) {

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


    private void refreshListView(){

        if(switchMaterial.isChecked()){
            networkCall();
            AdapterShort adapter = new AdapterShort(getApplicationContext(), shortStoryObject);
            listView.setAdapter(adapter);
        }
        else{
            storyNetworkCall();
            AdapterStory adapter = new AdapterStory(getApplicationContext(), StoryObject);
            listView.setAdapter(adapter);
        }
    }

}
