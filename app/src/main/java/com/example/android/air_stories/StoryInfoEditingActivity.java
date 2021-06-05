package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StoryInfoEditingActivity extends AppCompatActivity {
    String title, genre, type, description, username;
    int userID;
    MaterialButton next_btn, edit_btn;
    String[] genres = new String[]{"Action", "Comedy", "Thriller", "Horror", "Adventure", "Crime and Mystery", "Fantasy", "Historical", "Romance", "Satire", "Science Fiction", "Cyberpunk", "Speculative", "Thriller", "Western"};
//    String[] types = new String[]{"Short", "Complete"};
    Spinner genre_dropdown, type_dropdown;
    HomeActivity homeActivity;
    User user;
    TextInputEditText title_edit_text, description_edit_text;
    TextView textview, topTextView;

    ShortStories shortStory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_writing);

        Intent intent = getIntent();
        shortStory = (ShortStories) intent.getSerializableExtra("Story");

        username = intent.getStringExtra("username");
        userID = intent.getIntExtra("userID", 0);


        topTextView = findViewById(R.id.topTextView);
        topTextView.setText("Edit Story");
        title_edit_text = findViewById(R.id.title_edit_text);
        title_edit_text.setText(shortStory.getShortStory());

        description_edit_text = findViewById(R.id.description_edittext);
        description_edit_text.setText(shortStory.getShortDescription());
        next_btn = findViewById(R.id.next_btn);
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setText("Back");


//        type_dropdown = findViewById(R.id.type_spinner);
//        genre_dropdown = findViewById(R.id.genre_spinner);
//        genre_dropdown.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

//        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, types);
//        ArrayAdapter<String> genre_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genres);
//
//        genre_dropdown.setAdapter(genre_adapter);
//        type_dropdown.setAdapter(type_adapter);



        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title_edit_text.getText().toString().equals("")){
                    title_edit_text.setError("Title cannot be empty");
                    return;
                }

                if(description_edit_text.getText().toString().equals("")){
                    description = description_edit_text.getText().toString();
                    description_edit_text.setError("Description cannot be empty");
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), StoryEditingActivity.class);
                title = title_edit_text.getText().toString();
                description = description_edit_text.getText().toString();


//                intent.putExtra("genre", genre);
//                intent.putExtra("type", type);

                intent.putExtra("title", title);
                intent.putExtra("description", description);
                if (shortStory.getShortStory() != null){
                    intent.putExtra("story", shortStory.getShortStory());
                } else {
                    intent.putExtra("story", "Nice");
                }

                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), UserStoriesActivity.class);
//                intent.putExtra("username", user.getUsername());
//                intent.putExtra("userID", user.getUserID());
                finish();

//                Bundle bundle = new Bundle();
//                bundle.putInt("userID", user.getUserID());
//                intent.putExtras(bundle);

//                startActivity(intent);
            }
        });

        //get the spinner from the xml.


    }
}
