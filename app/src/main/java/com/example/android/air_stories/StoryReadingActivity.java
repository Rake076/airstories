package com.example.android.air_stories;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.android.air_stories.Model.Chapters;
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

public class StoryReadingActivity extends AppCompatActivity {


    TextView title_textview;
    AreTextView story_textview;

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
    ImageView like_btn, comment_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_reading);

        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        Chapters chapter = (Chapters) intent.getSerializableExtra("chapter");
        User user = (User) intent.getSerializableExtra("user");
        Stories story = (Stories) intent.getSerializableExtra("story");
        isLiked(user.getUserID(), story.getStory_id());


//        isLiked(user.getUserID(), story.getStory_id());

        like_btn = findViewById(R.id.like_btn);
        comment_btn = findViewById(R.id.comment_btn);


        story_textview = (AreTextView) findViewById(R.id.short_story_textview);
        title_textview = findViewById(R.id.short_title_textview);

        title_textview.setText("" + chapter.getChapter_name());
        story_textview.fromHtml("" + chapter.getChapter_text());


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable drawable = like_btn.getDrawable();
                if(!isFilled){
                    like(user.getUserID(), story.getStory_id());
                    like_btn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    isFilled = true;
                } else {
                    unlike(user.getUserID(), story.getStory_id());
                    like_btn.setImageResource(R.drawable.ic_outline_thumb_up_24);
                    isFilled = false;
                }
            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoryCommentsActivity.class);

                intent.putExtra("Story", story);
                intent.putExtra("user", user);

                startActivity(intent);
            }
        });

    }

    public void isLiked(int userID, int storyID){
        compositeDisposable.add(myAPI.isStoryLiked(userID, storyID)
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

    public void like(int userID, int storyID){
        compositeDisposable.add(myAPI.likeStory(userID, storyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (s.contains("liked")) {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   } else {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                ));
    }

    public void unlike(int userID, int storyID){
        compositeDisposable.add(myAPI.unlikeStory(userID, storyID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (s.contains("unliked")) {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   } else {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                ));
    }

}
