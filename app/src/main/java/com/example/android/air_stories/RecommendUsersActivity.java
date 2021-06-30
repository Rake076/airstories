package com.example.android.air_stories;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class RecommendUsersActivity extends AppCompatActivity{


    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Call<List<User>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    String storyType, recommendee_username;
    ListView listView;
    ArrayList<User> usersObject = new ArrayList<>();
    int recommender_id, storyID;


//    TextView username_textview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);

        Intent intent = getIntent();
        storyType = intent.getStringExtra("storyType");

        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        if(storyType.equals("short_story")){
            ShortStories shortStories = (ShortStories) intent.getSerializableExtra("shortStory");
            storyID = shortStories.getshortID();
        }
        else if (storyType.equals("story")){
            Stories stories = (Stories) intent.getSerializableExtra("story");
            storyID = stories.getStory_id();
        }

        recommender_id = SaveSharedPreference.getUserID(getApplication());

        listView = findViewById(R.id.user_list_view);

        networkCall();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                recommendee_username = usersObject.get(position).getUsername();
                int recommendee_id = usersObject.get(position).getUserID();

                String[] options = {"Yes", "No"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RecommendUsersActivity.this);
                builder.setTitle("Recommend to user " + recommendee_username + "?");
//                builder.setMessage("Recommend to user " + username + "?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if("Yes".equals(options[which])){
                            recommendStory(recommender_id, recommendee_id, storyID, storyType);
                        }
                        else if ("No".equals(options[which])){
                            Toast.makeText(getApplicationContext(), "Nohoho", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();

            }
        });
    }

    public void recommendStory(int recommender_id, int recommendee_id, int story_id, String story_type){
        compositeDisposable.add(myAPI.recommendStories(recommender_id, recommendee_id, story_id, story_type)
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
                                   Toast.makeText(getApplicationContext(), "Story successfully recommended to user "+recommendee_username, Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    public void networkCall() {
        listCall = jsonPlaceHolderApi.getUsersData(SaveSharedPreference.getUserID(getApplication()));
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                usersObject.clear();
                List<User> users = response.body();

                for (User user : users) {
                    usersObject.add(new User(user.getUserID(), user.getUsername(), user.getProfile_image(), user.getAbout()));
//                    Toast.makeText(getApplicationContext(), user.getUserID(), Toast.LENGTH_SHORT).show();
                }

                AdapterUser adapter = new AdapterUser(getApplicationContext(), usersObject);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }

        });
    }



}
