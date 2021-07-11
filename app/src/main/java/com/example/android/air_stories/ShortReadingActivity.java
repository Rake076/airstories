package com.example.android.air_stories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chinalwb.are.render.AreTextView;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShortReadingActivity extends AppCompatActivity {

    TextView title_textview;
    AreTextView short_story_textview;

    Bitmap bmap;
    Drawable myDrawable;
    Bitmap thumb;

    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Call<String> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    Boolean isFilled = false;
    ImageView like_btn, comment_btn, report_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_reading);

        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        ShortStories shortStory = (ShortStories) intent.getSerializableExtra("ShortStory");
        User user = (User) intent.getSerializableExtra("user");
        isLiked(user.getUserID(), shortStory.getshortID());

        like_btn = findViewById(R.id.like_btn);
        comment_btn = findViewById(R.id.comment_btn);
        report_btn = findViewById(R.id.report_btn);

        short_story_textview = (AreTextView) findViewById(R.id.short_story_textview);
        title_textview = findViewById(R.id.short_title_textview);

        title_textview.setText("" + shortStory.getShortTitle());
        short_story_textview.fromHtml("" + shortStory.getShortStory());


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = like_btn.getDrawable();
                if(!isFilled){
                    like(user.getUserID(), shortStory.getshortID());
                    like_btn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    isFilled = true;
                } else {
                    unlike(user.getUserID(), shortStory.getshortID());
                    like_btn.setImageResource(R.drawable.ic_outline_thumb_up_24);
                    isFilled = false;
                }

            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShortCommentsActivity.class);

                intent.putExtra("ShortStory", shortStory);
                intent.putExtra("user", user);

                startActivity(intent);
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                intent.putExtra("story", shortStory);
                intent.putExtra("storyType", "Short Story");

                startActivity(intent);
            }
        });

    }

    public void isLiked(int userID, int shortID){
        compositeDisposable.add(myAPI.isShortLiked(userID, shortID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if(s.contains("hasn't")){
                                       like_btn.setImageResource(R.drawable.ic_outline_thumb_up_24);
                                       isFilled = false;
                                   }
                                   else{
                                       like_btn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                                       isFilled = true;
                                   }

                               }
                           }
                ));
    }



    public void like(int userID, int shortID){
        compositeDisposable.add(myAPI.likeShortStory(userID, shortID)
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
                                       Toast.makeText(getApplicationContext(), "Short Story has been liked", Toast.LENGTH_SHORT).show();
                               }
                           }
        ));
    }

    public void unlike(int userID, int shortID){
        compositeDisposable.add(myAPI.unlikeShortStory(userID, shortID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
//                                   if (s.contains("unliked")) {
//                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                   } else {
//                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                   }
                                   Toast.makeText(getApplicationContext(), "Short Story has been unliked", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }



}
