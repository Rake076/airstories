package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ReportActivity extends AppCompatActivity {

    TextView title_textview, reportee_textview, type_textview;
    TextInputEditText report_reason_edittext;
    MaterialButton reportBtn;

    Retrofit retrofit = RetrofitClient.getInstance();
    INodeJS myAPI = retrofit.create(INodeJS.class);
    CompositeDisposable compositeDisposable = new CompositeDisposable();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_story_activity);


        Intent intent = getIntent();
        String title, reportee;
        int story_id, user_id;


        String storyType = intent.getStringExtra("storyType");
        if(storyType.equals("Short Story")){
            ShortStories shortStories = (ShortStories) intent.getSerializableExtra("story");
            title = shortStories.getShortTitle();
            story_id = shortStories.getshortID();
            reportee = shortStories.getUsername();
        }
        else{
            Stories stories = (Stories) intent.getSerializableExtra("story");
            title = stories.getStory_title();
            reportee = stories.getUsername();
            story_id = stories.getStory_id();
        }

        title_textview = findViewById(R.id.story_title_textview);
        title_textview.setText(title);

        reportee_textview = findViewById(R.id.username_textview);
        reportee_textview.setText(reportee);

        type_textview = findViewById(R.id.story_type_textview);
        type_textview.setText(storyType);

        reportBtn = findViewById(R.id.report_btn);

        report_reason_edittext = findViewById(R.id.report_reason_edit_text);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String report_reason = report_reason_edittext.getText().toString();
                String username = SaveSharedPreference.getUserName(getApplicationContext());
                submitReport(story_id, username, report_reason, storyType);
                finish();
            }

        });

    }


    private void submitReport(int story_id, String reporter , String report_reason, String story_type) {
        compositeDisposable.add(myAPI.reportStories(story_id, reporter, report_reason, story_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (s.contains("successfully")) {
                                       Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                   } else {
                                       Toast.makeText(getApplicationContext(), "Report has been submitted!", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                ));
    }
}
