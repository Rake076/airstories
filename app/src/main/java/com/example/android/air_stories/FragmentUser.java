package com.example.android.air_stories;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

public class FragmentUser extends Fragment {


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
    User user;
    HomeActivity homeActivity;

    RelativeLayout journal_layout, editProfile_layout, logout_layout;

    int userID;
    TextView username;
    MaterialButton journal_btn, editProfile_btn;
    ImageView userImage;

    //    Intent intent = new Intent(getActivity(), StoryWritingActivity.class);
    public FragmentUser(){

    }

    public static FragmentUser newInstance(String param1, String param2) {
        FragmentUser fragment = new FragmentUser();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);


        setHomeActivity();
        user = homeActivity.getUserdata();
        //setUserData();

        username = rootView.findViewById(R.id.username_textview);
        username.setText(user.getUsername());
        userID = user.getUserID();
        userImage = rootView.findViewById(R.id.user_image);

        logout_layout = rootView.findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSharedPreference.clear(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        journal_layout = rootView.findViewById(R.id.write_journal_layout);
        journal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JournalsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        editProfile_layout = rootView.findViewById(R.id.edit_profile_layout);
        editProfile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        homeActivity.getUser(SaveSharedPreference.getUserID(getContext()));
        Picasso.with(getContext())
                .load(user.getProfile_image())
                .into(userImage);



        return rootView;
    }


    private void setHomeActivity(){
        homeActivity = (HomeActivity) getActivity();
    }

    private void setUserData(){
        user = homeActivity.getUserdata();
    }
}
