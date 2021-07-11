package com.example.android.air_stories;

import androidx.fragment.app.Fragment;
import java.io.Serializable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.air_stories.Model.ShortStories;
import com.example.android.air_stories.Model.Stories;
import com.example.android.air_stories.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentReadingList extends Fragment implements Serializable {

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

    private SwipeRefreshLayout mSwipeRefreshLayout;


    ListView listView;
    SwitchMaterial switchMaterial;
    HomeActivity homeActivity = new HomeActivity();



    String data = "";
    User user;
    ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    ArrayList<Stories> StoryObject = new ArrayList<>();

    public FragmentReadingList(){}

    public static FragmentReadingList newInstance(String param1, String param2) {
        FragmentReadingList fragment = new FragmentReadingList();
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

    @Override
    public void onStop() {
        super.onStop();
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
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
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                else{
                    Stories story = StoryObject.get(position);
                    Intent intent = new Intent(getActivity(), DescriptionStoryActivity.class);
//                    intent.putExtra("isShort", 0);
                    intent.putExtra("Story", story);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }


            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listView.getChildAt(0) != null) {
                    mSwipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                listView.setOnItemClickListener(null);

                String[] options = {"Remove"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if("Remove".equals(options[which])){
                            if(switchMaterial.isChecked()){
                                ShortStories shortStory = shortStoryObject.get(position);
                                homeActivity.removeShortStoryFromReadingList(SaveSharedPreference.getUserID(getActivity()), shortStory.getshortID());
                                Toast.makeText(homeActivity, "Short Story has been removed from the reading list.", Toast.LENGTH_SHORT).show();

                                shortStoryObject.remove(position);
                                AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
                                listView.setAdapter(adapter);

                            }
                            else{
                                Stories story = StoryObject.get(position);
                                homeActivity.removeStoryFromReadingList(SaveSharedPreference.getUserID(getActivity()), story.getStory_id());
                                Toast.makeText(homeActivity, "Story has been removed from the reading list.", Toast.LENGTH_SHORT).show();

                                StoryObject.remove(position);
                                AdapterStory adapter = new AdapterStory(getActivity(), StoryObject);
                                listView.setAdapter(adapter);

                            }
                            refreshListView();
                        }

                    }
                });
                builder.show();

                return false;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            refreshListView();
                        }
                    }
                }, 500);



            }
        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_reading_list, container, false);

        setHomeActivity();
        user = homeActivity.getUserdata();

        listView = (ListView) rootView.findViewById(R.id.reading_list_story_list_view);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_stories);

        switchMaterial = rootView.findViewById(R.id.switch1);
        switchMaterial.setText("Short Stories");

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMaterial.isChecked()){
                    switchMaterial.setText("Short Stories");
                    homeActivity.shortReadingListNetworkCall();
                    shortStoryObject = homeActivity.getShortReadingData();
                    if(shortStoryObject.size() < 1){
                        shortStoryObject.clear();
                        Toast.makeText(homeActivity, "Ayo, dis empty!", Toast.LENGTH_SHORT).show();
                        listView.setAdapter(null);
                    }
                    else{
                        AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
                        listView.setAdapter(adapter);
                    }


                }
                else{
                    switchMaterial.setText("Stories");
                    homeActivity.storyReadingListNetworkCall();
                    StoryObject = homeActivity.getStoryReadingData();
                    if(StoryObject.size() < 1){
                        StoryObject.clear();
                        Toast.makeText(homeActivity, "Ayo, dis empty!", Toast.LENGTH_SHORT).show();
                        listView.setAdapter(null);
                    }
                    else{
                        AdapterStory storyAdapter = new AdapterStory(getActivity(), StoryObject);
                        listView.setAdapter(storyAdapter);
                    }


                }
            }
        });


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                refreshListView();
//            }
//        }, 100);




        return rootView;
    }



    private void refreshListView(){

        if(switchMaterial.isChecked()){
            homeActivity.shortReadingListNetworkCall();
            shortStoryObject = homeActivity.getShortReadingData();
            if(shortStoryObject.size() < 1){
                shortStoryObject.clear();
                Log.v("Ayo", "Works");
                Toast.makeText(homeActivity, "Ayo, dis empty!", Toast.LENGTH_SHORT).show();
                listView.setAdapter(null);
            }
            else{
                AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
                listView.setAdapter(adapter);
            }

        }
        else{
            Log.v("Ayo", "Works");
            homeActivity.storyReadingListNetworkCall();
            StoryObject = homeActivity.getStoryReadingData();
            if(StoryObject.size() < 1){
                StoryObject.clear();
                Toast.makeText(homeActivity, "Ayo, dis empty!", Toast.LENGTH_SHORT).show();
                listView.setAdapter(null);
            }
            else{
                AdapterStory storyAdapter = new AdapterStory(getActivity(), StoryObject);
                listView.setAdapter(storyAdapter);
            }

        }

    }


    private void setUsername() throws JSONException {

        user = homeActivity.getUserdata();

    }

    private void setHomeActivity(){
        homeActivity = (HomeActivity) getActivity();
    }


}
