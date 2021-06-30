package com.example.android.air_stories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

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

public class HomeActivity extends AppCompatActivity/* implements StoriesFragment.OnDataPass */ {

    TextView textView;
    TextInputEditText searchEditText;


    User userData;
    Fragment fragment;

    String content, globalTag, stringuserdata;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Call<List<ShortStories>> listCall;
    Call<List<Stories>> storyListCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    Retrofit retrofit = RetrofitClient.getInstance();
    INodeJS myAPI = retrofit.create(INodeJS.class);



    final ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    final ArrayList<Stories> StoryObject = new ArrayList<>();


    public ArrayList<ShortStories> getShortStoriesData(){
        return shortStoryObject;
    }

    public ArrayList<ShortStories> getShortReadingData(){
        return shortStoryObject;
    }

    public ArrayList<Stories> getStoriesData(){
        return StoryObject;
    }

    public ArrayList<Stories> getStoryReadingData(){
        return StoryObject;
    }


    public User getUserdata(){
        return userData;
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airstories_home);

        Intent intent = getIntent();
        if(SaveSharedPreference.getUserName(HomeActivity.this).length() == 0)
        {
            // call Login Activity
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            // Stay at the current activity.
        }

        searchEditText = findViewById(R.id.search_edit_text);
//        textView = findViewById(R.id.short_data_textview);




        try {
            setUserData(SaveSharedPreference.getJSONString(HomeActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        networkCall();
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }

    public void shortNetworkCall() {
        listCall = jsonPlaceHolderApi.getShortStories();
        listCall.enqueue(new Callback<List<ShortStories>>() {
            @Override
            public void onResponse(Call<List<ShortStories>> call, Response<List<ShortStories>> response) {

                if (!response.isSuccessful()) {
                    textView.setText("Code " + response.code());
                    return;
                }

                List<ShortStories> stories = response.body();

                shortStoryObject.clear();
                for (ShortStories Stories : stories) {

                    shortStoryObject.add(new ShortStories(Stories.getshortID(), Stories.getShortTitle(),
                            Stories.getShortStory(), Stories.getShortGenre(), Stories.getAppCount(), Stories.getShortDescription(), Stories.getUsername(), Stories.getCoverImage()));
//                    textView.append(data);
                }
            }

            @Override
            public void onFailure(Call<List<ShortStories>> call, Throwable t) {


            }

        });
    }


    public void removeShortStoryFromReadingList(int user_id, int story_id){
        compositeDisposable.add(myAPI.removeShortStoryFromReadingList(user_id, story_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                               }
                           }
                ));
    }

    public void removeStoryFromReadingList(int user_id, int story_id){
        compositeDisposable.add(myAPI.removeStoryFromReadingList(user_id, story_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                               }
                           }
                ));
    }

    public void storyNetworkCall() {
        storyListCall = jsonPlaceHolderApi.getStories();
        storyListCall.enqueue(new Callback<List<Stories>>() {
            @Override
            public void onResponse(Call<List<Stories>> call, Response<List<Stories>> response) {

                if (!response.isSuccessful()) {
                    textView.setText("Code " + response.code());
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


    public void shortReadingListNetworkCall() {
        listCall = jsonPlaceHolderApi.getShortStoriesReadingList(SaveSharedPreference.getUserID(getApplicationContext()));
//        Toast.makeText(getApplicationContext(), ""+SaveSharedPreference.getUserID(getApplicationContext()), Toast.LENGTH_SHORT).show();
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


    public void storyReadingListNetworkCall() {
        storyListCall = jsonPlaceHolderApi.getStoriesReadingList(SaveSharedPreference.getUserID( getApplicationContext() ));
        storyListCall.enqueue(new Callback<List<Stories>>() {
            @Override
            public void onResponse(Call<List<Stories>> call, Response<List<Stories>> response) {

                if (!response.isSuccessful()) {
                    textView.setText("Code " + response.code());
                    return;
                }

                List<Stories> stories = response.body();

                StoryObject.clear();
                for (Stories Stories : stories) {

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

    public void addShortReadingList(int userID, int story_id){
        compositeDisposable.add(myAPI.addShortReadingList(userID, story_id)
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
                                   Toast.makeText(getApplicationContext(), "Short Story successfully added to reading list", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    public void addStoryReadingList(int userID, int story_id){
        compositeDisposable.add(myAPI.addStoryReadingList(userID, story_id)
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
                                   Toast.makeText(getApplicationContext(), "Story successfully added to reading list", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }


    public void shortTitleSearch(String shortTitle) {
        listCall = jsonPlaceHolderApi.getShortStoriesTitle(shortTitle);
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
//                    textView.append(data);
                }
            }

            @Override
            public void onFailure(Call<List<ShortStories>> call, Throwable t) {


            }

        });
    }


    public void shortTitleGenre(String shortGenre) {
        listCall = jsonPlaceHolderApi.getShortStoriesGenre(shortGenre);
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
//                    textView.append(data);
                }
            }

            @Override
            public void onFailure(Call<List<ShortStories>> call, Throwable t) {


            }

        });
    }

    public void storyTitleSearch(String storyTitle) {
        storyListCall = jsonPlaceHolderApi.getStoriesTitle(storyTitle);
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


    public void storyGenreSearch(String storyGenre) {
        storyListCall = jsonPlaceHolderApi.getStoriesGenre(storyGenre);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@Nullable MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home_btn:
                            return true;
                        case R.id.search_btn:
                            shortNetworkCall();
                            storyNetworkCall();

                            fragment = getSupportFragmentManager().findFragmentByTag("StoryFrag");
                            if (fragment == null || !fragment.isVisible()) {
                                // not exist
                                openFragment(R.id.fragment_layout, FragmentStories.newInstance("", ""), "StoryFrag");
                                globalTag = "StoryFrag";
                            }
                            return true;
                        case R.id.list_btn:
                            shortReadingListNetworkCall();
                            storyReadingListNetworkCall();
                            fragment = getSupportFragmentManager().findFragmentByTag("ListFrag");
                            if (fragment == null || !fragment.isVisible()) {
                                // not exist
                                openFragment(R.id.fragment_layout, FragmentReadingList.newInstance("", ""), "ListFrag");
                                globalTag = "ListFrag";
                            }
                            return true;
                        case R.id.write_btn:
                            fragment = getSupportFragmentManager().findFragmentByTag("writeFrag");
                            if (fragment == null || !fragment.isVisible()) {
                                openFragment(R.id.fragment_layout, FragmentWriting.newInstance("", ""), "writeFrag");
                                globalTag = "writeFrag";
                            }
                            return true;

                        case R.id.user_btn:
                            fragment = getSupportFragmentManager().findFragmentByTag("UserFrag");
                            if(fragment == null  || !fragment.isVisible()) {
                                openFragment(R.id.fragment_layout, FragmentUser.newInstance("", ""), "UserFrag");
                                globalTag = "UserFrag";
                            }
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(int id, Fragment fragment, String tag) {

        Fragment removeFragment = getSupportFragmentManager().findFragmentByTag(globalTag);;
        if (removeFragment != null){
            getSupportFragmentManager().beginTransaction().remove(removeFragment).commit();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment, tag);
        transaction.commit();
    }

    public void setUserData(String Data) throws JSONException {

        JSONObject jsonObject = new JSONObject(Data);
        int userID = jsonObject.getInt("user_id");

        String username = jsonObject.getString("username");
        String email = jsonObject.getString("u_email");
        String password = jsonObject.getString("u_password");
        int story_count = jsonObject.getInt("story_count");
        String profile_image = jsonObject.getString("u_profile_image");
        String about = jsonObject.getString("about");

        userData = new User(userID, username, email, password, story_count, profile_image, about);
    }

    public void getUser (int userID){
        compositeDisposable.add(myAPI.getUserData(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   JSONObject jsonObject = new JSONObject(s);
                                   String username = jsonObject.getString("username");
                                   String profile_image = jsonObject.getString("u_profile_image");
                                   String about = jsonObject.getString("about");
                                   String email = jsonObject.getString("u_email");
                                   String password = jsonObject.getString("u_password");
                                   int story_count = jsonObject.getInt("story_count");
                                   userData = new User(userID, username, email, password, story_count, profile_image, about);
                               }
                           }
                ));
    }

}
