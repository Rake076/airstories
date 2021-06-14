package com.example.android.air_stories;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import static android.app.Activity.RESULT_OK;


@SuppressWarnings("ALL")
public class FragmentWriting extends Fragment implements Serializable, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //     * @param param1 Parameter 1.
     //     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters


    private static final int SELECT_PICTURE = 1;

    String title, genre, type, description;
    MaterialButton next_btn, edit_btn, addCover_btn;
    String[] genres = new String[]{"Action", "Comedy", "Thriller", "Horror", "Adventure", "Crime and Mystery", "Fantasy", "Historical", "Romance", "Satire", "Science Fiction", "Cyberpunk", "Speculative", "Thriller", "Western"};
    String[] types = new String[]{"Short", "Complete"};
    Spinner genre_dropdown, type_dropdown;
    HomeActivity homeActivity;
    User user;
    TextInputEditText title_edit_text, description_edit_text;
    TextView textview;

    Bitmap bitmap;
    ImageView imageView;
    Uri imageUri;


    Intent intent;

//    Intent intent = new Intent(getActivity(), StoryWritingActivity.class);
    public FragmentWriting(){

    }

    public static FragmentWriting newInstance(String param1, String param2) {
        FragmentWriting fragment = new FragmentWriting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textview.setText(title_edit_text.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



        @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_writing, container, false);

            setHomeActivity();
            user = homeActivity.getUserdata();
            //setUserData();
            title_edit_text = rootView.findViewById(R.id.title_edit_text);
            description_edit_text = rootView.findViewById(R.id.description_edittext);
            next_btn = rootView.findViewById(R.id.next_btn);
            edit_btn = rootView.findViewById(R.id.edit_btn);
            addCover_btn = rootView.findViewById(R.id.addCoverButton);
            imageView = rootView.findViewById(R.id.write_image);

            TextView username_textview = rootView.findViewById(R.id.username_textview);
            username_textview.setText(user.getUsername());

            type_dropdown = rootView.findViewById(R.id.type_spinner);
            genre_dropdown = rootView.findViewById(R.id.genre_spinner);
            genre_dropdown.setOnItemSelectedListener(this);




            imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                });

//            user = homeActivity.getUserdata();
            ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
            ArrayAdapter<String> genre_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genres);

            genre_dropdown.setAdapter(genre_adapter);
            type_dropdown.setAdapter(type_adapter);



        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap == null){
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
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


                type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        type = type_dropdown.getSelectedItem().toString();
                        Log.v("Dropdown", type + "222");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                intent = new Intent(getContext(), WritingShortActivity.class);
                intent.putExtra("genre", genre);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("type", type);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("userID", user.getUserID());
                intent.putExtra("shortImage", bitmap);
                startActivity(intent);

            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserStoriesActivity.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("userID", user.getUserID());

                Bundle bundle = new Bundle();
                bundle.putInt("userID", user.getUserID());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        addCover_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return rootView;
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




    //Multipart body thingy
    //pass it like this
//    Image file = new Image();
//    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//    // MultipartBody.Part is used to send also the actual file name
//    MultipartBody.Part body =
//            MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//    // add another part within the multipart request
//    RequestBody fullName =
//            RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name");
//    service.updateProfile(id, fullName, body, other);

    // Try 2.0 ends.


    private void setHomeActivity(){
        homeActivity = (HomeActivity) getActivity();
    }

    private void setUserData(){
            user = homeActivity.getUserdata();
    }








    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            genre = genres[genre_dropdown.getSelectedItemPosition()];
        genre = (String) genre_dropdown.getSelectedItem();
        Log.v("Dropdown", genre + "222");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
            genre = (String) genre_dropdown.getSelectedItem();
    }
}
