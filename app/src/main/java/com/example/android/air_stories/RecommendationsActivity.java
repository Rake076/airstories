package com.example.android.air_stories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.Recommendation;
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

public class RecommendationsActivity extends AppCompatActivity {


    Call<List<Recommendation>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    ListView listView;

    ArrayList<Recommendation> recommendationsObject = new ArrayList<>();
    int userID;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Retrofit retrofit = RetrofitClient.getInstance();
    INodeJS myAPI = retrofit.create(INodeJS.class);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_activity);


        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        userID = SaveSharedPreference.getUserID(this);

        networkCall();


        listView = findViewById(R.id.recommendation_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Recommendation recommendation = recommendationsObject.get(position);

                String[] options = {"Add to Reading List", "Decline"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationsActivity.this);
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if("Add to Reading List".equals(options[which])){

                            if(recommendation.getStory_type().equals("story")){
                                addStoryReadingList(userID, recommendation.getStory_id());
                                removeRecommendation(recommendation.getRec_id());
                            }
                            else if (recommendation.getStory_type().equals("short_story")){
                                addShortReadingList(userID, recommendation.getStory_id());
                                removeRecommendation(recommendation.getRec_id());
                            }

                        }
                        else if ("Decline".equals(options[which])){
                            removeRecommendation(recommendation.getRec_id());
                            Toast.makeText(RecommendationsActivity.this, "Recommendation has been declined", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();

            }
        });
    }

    public void networkCall() {
        listCall = jsonPlaceHolderApi.getRecommendations(userID);
        listCall.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                recommendationsObject.clear();

                List<Recommendation> recommendations = response.body();

                for (Recommendation recommendation : recommendations) {
//    public Recommendation(int rec_id, String story_type, int story_id, String coverImage, String username, String title){
                    recommendationsObject.add(new Recommendation(recommendation.getRec_id(),recommendation.getStory_type(),
                            recommendation.getStory_id(), recommendation.getCoverImage(), recommendation.getUsername(),
                            recommendation.getTitle()));
                }

                AdapterRecommendation adapter = new AdapterRecommendation(getApplicationContext(), recommendationsObject);
                listView.setAdapter(adapter);
            }



            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable t) {

            }

        });
    }


    public void removeRecommendation(int rec_id){
        compositeDisposable.add(myAPI.removeRecommendation(rec_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                           @Override
                           public void accept(String s) throws Exception {
                               networkCall();
                           }
                       }
            ));
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

}
