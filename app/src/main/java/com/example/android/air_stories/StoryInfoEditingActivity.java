package com.example.android.air_stories;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class StoryInfoEditingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
    Stories story;

    Bitmap bitmap;

    MultipartBody.Part fileBody;

    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_info_editing_activity);

        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        story = (Stories) intent.getSerializableExtra("Story");
//        userID = intent.getIntExtra("userID", 0);

        genre_dropdown = findViewById(R.id.genre_spinner);
        genre_dropdown.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        addCover_btn = findViewById(R.id.addCoverButton);
        imageView = findViewById(R.id.write_image);

        topTextView = findViewById(R.id.topTextView);
        topTextView.setText("Edit Story");
        title_edit_text = findViewById(R.id.title_edit_text);
        title_edit_text.setText(story.getStory_title());

        description_edit_text = findViewById(R.id.description_edittext);
        description_edit_text.setText(story.getStory_description());

        next_btn = findViewById(R.id.next_btn);
        next_btn.setText("Save Changes");
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setText("Back");

//        Picasso.with(getApplicationContext())
//                .load(shortStory.getCoverImage())
//                .into(imageView);



        Picasso.with(getApplicationContext())
                .load(story.getCoverImage())
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

//        File coverImage = persistImage(bitmap, "cover");
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);
//        fileBody = MultipartBody.Part.createFormData("cover", coverImage.getName(), requestFile);

        ArrayAdapter<String> genre_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, genres);
        genre_dropdown.setAdapter(genre_adapter);

        RelativeLayout chapters_layout = findViewById(R.id.chapter_view);
        chapters_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StoryChaptersActivity.class);
                intent.putExtra("story", story);
                startActivity(intent);
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

                title = title_edit_text.getText().toString();
                description = description_edit_text.getText().toString();

                File coverImage = persistImage(bitmap, "cover");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);
                fileBody = MultipartBody.Part.createFormData("cover", coverImage.getName(), requestFile);

                editStory(story.getStory_id(), title, genre, description, fileBody);
                finish();
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


    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
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


    private void editStory(int story_id, String story_title, String story_genre, String story_description, MultipartBody.Part fileBody) {
        compositeDisposable.add(myAPI.editStory(story_id, story_title, story_genre, story_description, fileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if(responseBody.toString().contains("successfully")){
                            Toast.makeText(getApplicationContext(), "Success: " + responseBody.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Story has been edited successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }





    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            genre = genres[genre_dropdown.getSelectedItemPosition()];
        genre = (String) genre_dropdown.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        genre = (String) genre_dropdown.getSelectedItem();
    }

}
