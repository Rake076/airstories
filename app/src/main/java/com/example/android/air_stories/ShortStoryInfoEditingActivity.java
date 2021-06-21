package com.example.android.air_stories;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ShortStoryInfoEditingActivity extends AppCompatActivity {
    String title, genre, type, description, username;
    int userID;
    MaterialButton next_btn, edit_btn, addCover_btn;
    String[] genres = new String[]{"Action", "Comedy", "Thriller", "Horror", "Adventure", "Crime and Mystery", "Fantasy", "Historical", "Romance", "Satire", "Science Fiction", "Cyberpunk", "Speculative", "Thriller", "Western"};
//    String[] types = new String[]{"Short", "Complete"};
    Spinner genre_dropdown, type_dropdown;
    HomeActivity homeActivity;
    User user;
    TextInputEditText title_edit_text, description_edit_text;
    TextView textview, topTextView;
    ImageView imageView;
    ShortStories shortStory;

    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_writing);

        Intent intent = getIntent();
        shortStory = (ShortStories) intent.getSerializableExtra("Story");

        username = intent.getStringExtra("username");
        userID = intent.getIntExtra("userID", 0);

        addCover_btn = findViewById(R.id.addCoverButton);
        imageView = findViewById(R.id.write_image);

        topTextView = findViewById(R.id.topTextView);
        topTextView.setText("Edit Story");
        title_edit_text = findViewById(R.id.title_edit_text);
        title_edit_text.setText(shortStory.getShortTitle());

        description_edit_text = findViewById(R.id.description_edittext);
        description_edit_text.setText(shortStory.getShortDescription());
        next_btn = findViewById(R.id.next_btn);
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setText("Back");

//        Picasso.with(getApplicationContext())
//                .load(shortStory.getCoverImage())
//                .into(imageView);


        Picasso.with(getApplicationContext())
                .load(shortStory.getCoverImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap herebitmap, Picasso.LoadedFrom from) {
                        imageView.setImageBitmap(herebitmap);
                        bitmap = herebitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

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

                Intent intent = new Intent(getApplicationContext(), ShortStoryEditingActivity.class);
                title = title_edit_text.getText().toString();
                description = description_edit_text.getText().toString();


//                intent.putExtra("genre", genre);
//                intent.putExtra("type", type);

                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("story", shortStory.getShortStory());
//                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                intent.putExtra("shortImage", bitmap);
                intent.putExtra("shortID", shortStory.getshortID());


                startActivity(intent);

            }
        });


        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addCover_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Getting image from gallery try 2.0
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 2);
        i.putExtra("aspectY", 3);
        i.putExtra("outputX", 200);
        i.putExtra("outputY", 300);
        try {
            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), 0);
        }catch (ActivityNotFoundException ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode == Activity.RESULT_OK){
            try {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
