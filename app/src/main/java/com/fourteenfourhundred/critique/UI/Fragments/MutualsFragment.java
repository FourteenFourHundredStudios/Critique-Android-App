package com.fourteenfourhundred.critique.UI.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.UI.Activities.MutualFinderActivity;
import com.fourteenfourhundred.critique.UI.Views.RecycleViewManager;
import com.fourteenfourhundred.critique.UI.Views.User;
import com.fourteenfourhundred.critique.UI.Views.UserAdapter;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.storage.Storage;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MutualsFragment extends HomeFragment {

    JSONObject mutuals[];
    ArrayAdapter<JSONObject> adapter;
    ListView listView;

    HomeActivity home;

    RecyclerView mutualsList;

    boolean isEmpty=true;
    public API api;


    RelativeLayout content;

    SwipeRefreshLayout swipe;

    RecycleViewManager view;

    Intent searchIntent;

    public SwipeRefreshLayout.OnRefreshListener swipeListener;

    public MutualsFragment(){

    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        content = rootView.findViewById(R.id.home_fragment_container);

        swipe=rootView.findViewById(R.id.swiperefresh);

        api=Data.backgroundApi;


        setupRecyclerView();



        setupViews();
        searchIntent = new Intent(getContext().getApplicationContext(), MutualFinderActivity.class);


        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public int getToolbar(){
        return R.layout.action_bar_search;
    }

    public void onResume(){
        super.onResume();

        updateData();

    }


    public void updateData(){
        new ApiRequest.GetMutualsRequest(api).execute(new Util.Callback(){
            public void onResponse(JSONObject response) {
                try {
                    Data.setMutuals(response.getJSONArray("mutuals"));

                    //Util.showDialog(getActivity(),Data.getMutuals().toString());

                    Storage.saveData(home.getApplicationContext());


                    view.update((ArrayList) User.jsonToUserList(Data.getMutuals()));

                    swipe.setRefreshing(false);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setupRecyclerView(){

        try {


            mutualsList=(RecyclerView)rootView.findViewById(R.id.mutualsList);

            List<User> m=User.jsonToUserList(Data.getMutuals());

            view = new RecycleViewManager(mutualsList, this.getActivity(),new UserAdapter(m),m);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setupViews(){

        home=(HomeActivity)getActivity();


        swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //view.update();
                updateData();
            }
        };

        swipe.setOnRefreshListener(swipeListener);

    }

    @Override
    public void onActionBarClicked(){
        getActivity().startActivity(searchIntent);
        getActivity().overridePendingTransition(0,0);
    }



}
