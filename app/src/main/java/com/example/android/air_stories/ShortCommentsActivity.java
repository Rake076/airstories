package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Comments;
import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.ShortStories;
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

public class ShortCommentsActivity extends AppCompatActivity {


    Call<List<Comments>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    ListView listView;
    EditText comment_edit_text;
    MaterialButton postComment_btn;
    ArrayList<Comments> commentsObject = new ArrayList<>();
    int userID, shortID;


    TextView username_textview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_comments_activity);


        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        postComment_btn = findViewById(R.id.submit_comment_btn);
        comment_edit_text = findViewById(R.id.write_comment_edittext);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        ShortStories shortStories = (ShortStories) intent.getSerializableExtra("ShortStory");

        userID = user.getUserID();
        userID = SaveSharedPreference.getUserID(getApplicationContext());
        shortID = shortStories.getshortID();

        listView = findViewById(R.id.comments_listview);

        postComment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_edit_text.getText().length()<1){
                    comment_edit_text.setError("Comments can not be empty");
                }
                else{
                    submitShortComment(shortID, userID, comment_edit_text.getText().toString());
                    comment_edit_text.setText("");
                    networkCall();
                }

            }
        });

        networkCall();




    }

    public void networkCall() {
        listCall = jsonPlaceHolderApi.getShortComments(shortID);
        listCall.enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                commentsObject.clear();
                List<Comments> comments = response.body();

                for (Comments comment : comments) {

                    commentsObject.add(new Comments(comment.getComment_id(), comment.getStory_id(),
                            comment.getComment(), comment.getUser_id(), comment.getStory_type(), comment.getUsername()));
                }


//        commentsObject.add( new Comments(1, 2, "Ha nice", 4, "Short", "Rake"));
                AdapterComments adapter = new AdapterComments(getApplicationContext(), commentsObject);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {

            }


        });
    }

    private void submitShortComment(int shortID, int userID, String comment) {
        compositeDisposable.add(myAPI.submitShortComment(shortID, userID, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (s.contains("successfully")) {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   } else {
                                       Toast.makeText(getApplicationContext(), "Comment has been uploaded! - " + s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                ));

    }



}
