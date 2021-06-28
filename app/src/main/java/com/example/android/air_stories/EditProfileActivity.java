package com.example.android.air_stories;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.air_stories.Model.User;
import com.example.android.air_stories.Retrofit.INodeJS;
import com.example.android.air_stories.Retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {


    Retrofit retrofit;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    TextInputEditText username_edittext, description_edittext;
    ImageButton picture_btn;
    MaterialButton save_btn;
    Bitmap pictureBitmap;

    MultipartBody.Part fileBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        // Init API
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        getUser(SaveSharedPreference.getUserID(getApplicationContext()));


        username_edittext = findViewById(R.id.username_edit_text);
        description_edittext = findViewById(R.id.description_edit_text);

        picture_btn = findViewById(R.id.user_image);
        picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });




        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_id = SaveSharedPreference.getUserID(getApplicationContext());
                String username = username_edittext.getText().toString();
                String description = description_edittext.getText().toString();

                File coverImage = persistImage(pictureBitmap, "cover");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);
                fileBody = MultipartBody.Part.createFormData("cover", coverImage.getName(), requestFile);

                updateUserData(user_id, username, description, fileBody);
            }
        });

    }


    public void getUser (int userID){
        compositeDisposable.add(myAPI.getUserData(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   JSONObject jsonObject = new JSONObject(s);
                                   String username = jsonObject.getString("username");
                                   String profile_image = jsonObject.getString("u_profile_image");
                                   String about = jsonObject.getString("about");
                                   username_edittext.setText(username);
                                   description_edittext.setText(about);
                                   Picasso.with(getApplicationContext())
                                           .load(profile_image)
                                           .into(new Target() {
                                               @Override
                                               public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                   picture_btn.setImageBitmap(bitmap);
                                                   pictureBitmap = bitmap;
                                               }

                                               @Override
                                               public void onBitmapFailed(Drawable errorDrawable) {

                                               }

                                               @Override
                                               public void onPrepareLoad(Drawable placeHolderDrawable) {

                                               }
                                           });
                               }
                           }
                ));
    }


    private void updateUserData(int user_id, String username, String about, MultipartBody.Part fileBody) {
        compositeDisposable.add(myAPI.updateUserData(user_id, username, about, fileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if(responseBody.toString().contains("successfully")){
                            Toast.makeText(getApplicationContext(), "Success: " + responseBody.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "User data has been updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
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
                pictureBitmap = bundle.getParcelable("data");
                picture_btn.setImageBitmap(pictureBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
