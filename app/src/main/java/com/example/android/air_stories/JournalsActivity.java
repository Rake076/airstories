package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.Journals;
import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JournalsActivity extends AppCompatActivity {

    Call<List<Journals>> listCall;
    Retrofit Bretrofit = RetrofitClient.getClient();
    INodeJS jsonPlaceHolderApi = Bretrofit.create(INodeJS.class);

    ListView listView;
    MaterialButton write_journal_btn;
    ArrayList<Journals> journalsObject = new ArrayList<>();
    int userID;


   TextView username_textview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_activity);

        write_journal_btn = findViewById(R.id.write_journal_btn);
        username_textview = findViewById(R.id.journals_username_textview);
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);
        username_textview.setText(userID+"");

        listView = findViewById(R.id.journal_listview);

        write_journal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JournalWritingActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        networkCall();
        AdapterJournal adapter = new AdapterJournal(getApplicationContext(), journalsObject);
        listView.setAdapter(adapter);

    }


    public void networkCall() {
        listCall = jsonPlaceHolderApi.getUserJournals(userID);
        listCall.enqueue(new Callback<List<Journals>>() {
            @Override
            public void onResponse(Call<List<Journals>> call, Response<List<Journals>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                journalsObject.clear();
                List<Journals> journalsRes = response.body();

                for (Journals journal : journalsRes) {
                    journalsObject.add(new Journals(journal.getJournal_id(), journal.getUser_id(), journal.getJournal_title(), journal.getJournal_date(), journal.getJournal()));
                }

                AdapterJournal adapter = new AdapterJournal(getApplicationContext(), journalsObject);
                listView.setAdapter(adapter);
            }



            @Override
            public void onFailure(Call<List<Journals>> call, Throwable t) {

            }

        });
    }



}
