package com.example.android.air_stories;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class StoriesFragment extends Fragment implements Serializable {

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
    TextView textView, username_textview;
    TextInputEditText search;

    ListView listView;

    HomeActivity homeActivity = new HomeActivity();

    String data = "";
    User user;
    ArrayList<ShortStories> shortStoryObject = new ArrayList<>();

    public StoriesFragment(){}

    public static StoriesFragment newInstance(String param1, String param2) {
        StoriesFragment fragment = new StoriesFragment();
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
            ShortStories shortStory = shortStoryObject.get(position);

            Intent intent = new Intent(getActivity(), DescriptionShortActivity.class);

            intent.putExtra("Story", shortStory);

//              To retrieve object in second Activity
//              getIntent().getSerializableExtra("MyClass");
            startActivity(intent);

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
//        textView = (TextView) rootView.findViewById(R.id.short_data_textview);
        search = rootView.findViewById(R.id.search_edit_text);
        //search.setOnKeyListener((View.OnKeyListener) this);

        listView = (ListView) rootView.findViewById(R.id.short_story_list_view);
        username_textview = rootView.findViewById(R.id.username_textview);



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

        username_textview.setText(user.getUsername());
    }

    private void setHomeActivity(){
        homeActivity = (HomeActivity) getActivity();
    }

    private void setTheTextView(){

        shortStoryObject = homeActivity.getShortStoriesData();

        data="";

        for (ShortStories Stories : shortStoryObject){

            data += "shortID: " + Stories.getshortID() + "\n";
            data += "shortTitle: " + Stories.getShortTitle() + "\n";
            data += "shortGenre: " + Stories.getShortGenre() + "\n";
            data += "shortStory: " + Stories.getShortStory() + "\n";
            data += "appCount: " + Stories.getAppCount() + "\n";
            data += "shortDescription: " + Stories.getShortDescription()+ "\n";
        }

        textView.setText(data);
    }


}