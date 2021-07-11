package com.example.android.air_stories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

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

public class FragmentHome extends Fragment implements Serializable {


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
//    TextInputEditText search;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    ListView listView;
    SwitchMaterial switchMaterial;
    HomeActivity homeActivity = new HomeActivity();



    String data = "";
    User user;
    ArrayList<ShortStories> shortStoryObject = new ArrayList<>();
    ArrayList<Stories> StoryObject = new ArrayList<>();

    public FragmentHome(){}

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                listView.setOnItemClickListener(null);

                String[] options = {"Add to Reading List"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if("Add to Reading List".equals(options[which])){


                            if (switchMaterial.isChecked()){
                                homeActivity.addShortReadingList(user.getUserID(), shortStoryObject.get(i).getshortID());

                            }
                            else{
                                homeActivity.addStoryReadingList(user.getUserID(), StoryObject.get(i).getStory_id());
                            }


                        }

                    }
                });
                builder.show();

                return false;
            }
        });

//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String title = search.getText().toString();
//
//                if (switchMaterial.isChecked()){
//                    switchMaterial.setText("Short Stories");
//                    homeActivity.shortTitleSearch(title);
////                    shortStoryObject = homeActivity.getShortStoriesData();
////                    AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
////                    listView.setAdapter(adapter);
//                }
//                else{
//                    switchMaterial.setText("Stories");
//                    homeActivity.storyTitleSearch(title);
////                    StoryObject = homeActivity.getStoriesData();
////                    AdapterStory storyAdapter = new AdapterStory(getActivity(), StoryObject);
////                    listView.setAdapter(storyAdapter);
//                }
////                refreshListViewWithoutNetworkCall();
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshListViewWithoutNetworkCall();
//                    }
//                }, 500);
//
//
//
//
//
//
//
//            }
//        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
//                            search.setText("");
//                            refreshListView();
                            refreshNetworkCall();
                            refreshListViewWithoutNetworkCall();
                        }
                    }
                }, 1000);




            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        search = rootView.findViewById(R.id.search_edit_text);
        //search.setOnKeyListener((View.OnKeyListener) this);
        setHomeActivity();
        user = homeActivity.getUserdata();

        listView = (ListView) rootView.findViewById(R.id.short_story_list_view);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_stories);

        switchMaterial = rootView.findViewById(R.id.switch1);
        switchMaterial.setText("Short Stories");

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchMaterial.isChecked()){
                    switchMaterial.setText("Short Stories");
                    homeActivity.shortNetworkCall();
                    shortStoryObject = homeActivity.getShortStoriesData();
                    AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
                    listView.setAdapter(adapter);
                }
                else{
                    switchMaterial.setText("Stories");
                    homeActivity.storyNetworkCall();
                    StoryObject = homeActivity.getStoriesData();
                    AdapterStory storyAdapter = new AdapterStory(getActivity(), StoryObject);
                    listView.setAdapter(storyAdapter);
                }
            }
        });


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        refreshListView();
//            }
//        }, 500);


        return rootView;
    }



    private void refreshListView(){
        if(switchMaterial.isChecked()){
            homeActivity.shortNetworkCall();
            shortStoryObject = homeActivity.getShortStoriesData();
            AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
            listView.setAdapter(adapter);
        }
        else{
            homeActivity.storyNetworkCall();
            StoryObject = homeActivity.getStoriesData();
            AdapterStory adapter = new AdapterStory(getActivity(), StoryObject);
            listView.setAdapter(adapter);
        }
    }

    private void refreshListViewWithoutNetworkCall(){
        if(switchMaterial.isChecked()){
            shortStoryObject = homeActivity.getShortStoriesData();
            AdapterShort adapter = new AdapterShort(getActivity(), shortStoryObject);
            listView.setAdapter(adapter);
        }
        else{
            StoryObject = homeActivity.getStoriesData();
            AdapterStory adapter = new AdapterStory(getActivity(), StoryObject);
            listView.setAdapter(adapter);
        }
    }

    private void refreshNetworkCall(){
        if(switchMaterial.isChecked()){
            homeActivity.shortNetworkCall();
        }
        else{
            homeActivity.storyNetworkCall();
        }
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
