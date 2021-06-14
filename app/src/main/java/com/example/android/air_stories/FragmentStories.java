package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentStories extends Fragment implements Serializable {

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
    MaterialButton button;
    TextView textView;
//            TextView username_textview;
    TextInputEditText search;

    ListView listView;
    SwitchMaterial switchMaterial;
    HomeActivity homeActivity = new HomeActivity();

    String data = "";
    User user;
    ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    ArrayList<Stories> StoryObject = new ArrayList<>();

    public FragmentStories(){}

    public static FragmentStories newInstance(String param1, String param2) {
        FragmentStories fragment = new FragmentStories();
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


    public void onViewCreated(View V, Bundle savedInstanceState){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(switchMaterial.isChecked()){
                    ShortStories shortStory = shortStoryObject.get(position);
                    Intent intent = new Intent(getActivity(), DescriptionShortActivity.class);
//                    intent.putExtra("isShort", 1);
                    intent.putExtra("shortStory", shortStory);
                    startActivity(intent);
                }
                else{
                    Stories story = StoryObject.get(position);
                    Intent intent = new Intent(getActivity(), DescriptionStoryActivity.class);
//                    intent.putExtra("isShort", 0);
                    intent.putExtra("Story", story);
                    startActivity(intent);
                }


            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String s = search.getText().toString();

//                setTheTextView();

            }


            @Override
            public void afterTextChanged(Editable editable) {
                //String s = search.getText().toString();
//                setTheTextView();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_stories, container, false);
        search = rootView.findViewById(R.id.search_edit_text);
        //search.setOnKeyListener((View.OnKeyListener) this);

        listView = (ListView) rootView.findViewById(R.id.short_story_list_view);
//        username_textview = rootView.findViewById(R.id.username_textview);

        switchMaterial = rootView.findViewById(R.id.switch1);
        switchMaterial.setText("Short Stories");

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMaterial.isChecked()){
                    switchMaterial.setText("Short Stories");
                    shortStoryObject = homeActivity.getShortStoriesData();
                    AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
                    listView.setAdapter(adapter);
                }
                else{
                    switchMaterial.setText("Stories");
                    StoryObject = homeActivity.getStoriesData();
                    AdapterStory storyAdapter = new AdapterStory(getActivity(), StoryObject);
                    listView.setAdapter(storyAdapter);
                }
            }
        });

        setHomeActivity();
        try {
            setUsername();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //setTheTextView();




        shortStoryObject = homeActivity.getShortStoriesData();


        AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);

        listView.setAdapter(adapter);


        //setTheTextView();

        return rootView;
    }





    private void setUsername() throws JSONException {
//        String userJson = homeActivity.getStringuserdata();

        user = homeActivity.getUserdata();

//        JSONObject jsonObject = new JSONObject(userJson);
//        String username = jsonObject.getString("username");

//        username_textview.setText(user.getUsername());
    }

    private void setHomeActivity(){
        homeActivity = (HomeActivity) getActivity();
    }



}