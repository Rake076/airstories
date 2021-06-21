package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
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

    final ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    final ArrayList<Stories> StoryObject = new ArrayList<>();


    public ArrayList<ShortStories> getShortStoriesData(){
        return shortStoryObject;
    }


    public ArrayList<Stories> getStoriesData(){
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



//        stringuserdata = intent.getStringExtra("stringuserdata");

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
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        int story_count = jsonObject.getInt("story_count");

        userData = new User(userID, username, email, password, story_count);
    }

}
