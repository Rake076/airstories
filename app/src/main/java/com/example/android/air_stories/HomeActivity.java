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
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    final ArrayList<ShortStories> shortStoryObject = new ArrayList<>();




    public ArrayList<ShortStories> getShortStoriesData(){
        return shortStoryObject;
    }

//    public String getStringuserdata(){
//        return stringuserdata;
//    }

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

        searchEditText = findViewById(R.id.search_edit_text);
//        textView = findViewById(R.id.short_data_textview);

        Intent intent = getIntent();

        stringuserdata = intent.getStringExtra("stringuserdata");

        try {
            setUserData(stringuserdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listCall = jsonPlaceHolderApi.getShortStories();

//        listCall.enqueue(new Callback<List<ShortStories>>() {
//            @Override
//            public void onResponse(Call<List<ShortStories>> call, Response<List<ShortStories>> response) {
//
//                if(!response.isSuccessful()){
//                    textView.setText("Code " + response.code());
//                    return;
//                }
//
//                List<ShortStories> stories = response.body();
//
//                for (ShortStories Stories : stories){
//
//                    shortStoryObject.add(new ShortStories (Stories.getshortID(), Stories.getShortTitle(),
//                            Stories.getShortStory(), Stories.getShortGenre(), Stories.getAppCount(), Stories.getShortDescription(), Stories.getUsername()));
////                    textView.append(data);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ShortStories>> call, Throwable t) {
//
//
//            }
//
//        });


        networkCall();
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }

    public void networkCall() {
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


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@Nullable MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.search_btn:
                            fragment = getSupportFragmentManager().findFragmentByTag("StoryFrag");
                            if (fragment == null || !fragment.isVisible()) {
                                // not exist
//                                openSearchFragment(StoriesFragment.newInstance("", ""));
                                openFragment(R.id.fragment_layout, StoriesFragment.newInstance("", ""), "StoryFrag");
                                globalTag = "StoryFrag";
                            }
                            else if (fragment != null || fragment.isVisible()){
                                networkCall();
                            }
                            return true;
                        case R.id.write_btn:

                            fragment = getSupportFragmentManager().findFragmentByTag("writeFrag");

                            if (fragment == null || !fragment.isVisible()) {
                                // not exist

                                openFragment(R.id.fragment_layout, FragmentWriting.newInstance("", ""), "writeFrag");

                                globalTag = "writeFrag";

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
//                .add(R.id.fragment_stories, new StoriesFragment(), "shortStoryFrag")
//                .addToBackStack(null);
        transaction.replace(id, fragment, tag);
        transaction.commit();
    }

//    public void openWriteFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
////                .add(R.id.fragment_writing, new WritingFragment(), "writeFrag")
//                .addToBackStack(null);
//        transaction.replace(R.id.fragment_layout, fragment, "writeFrag");
//        transaction.commit();
//    }
//
//    public void openSearchFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
//                //.add(R.id.fragment_stories, new StoriesFragment(), "shortStoryFrag")
//                .addToBackStack(null);
//        transaction.replace(R.id.fragment_layout, fragment, "shortStoryFrag");
//        transaction.commit();
//    }

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
