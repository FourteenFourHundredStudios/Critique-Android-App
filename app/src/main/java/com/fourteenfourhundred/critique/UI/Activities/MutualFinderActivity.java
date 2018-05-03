package com.fourteenfourhundred.critique.UI.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Views.RecycleViewManager;
import com.fourteenfourhundred.critique.UI.Views.User;
import com.fourteenfourhundred.critique.UI.Views.UserAdapter;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.SerializableJSONArray;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MutualFinderActivity extends AppCompatActivity {

    public String currentText="";
    public RecyclerView mutualsList;
    public UserAdapter searchAdapter;
    public API searchApi;

    RecycleViewManager view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchApi = new API(this);

        setContentView(R.layout.activity_mutual_finder);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_mutual_finder);

        final View actionbar=getSupportActionBar().getCustomView();

        actionbar.post(new Runnable() {
            @Override
            public void run() {
                SearchView searchView=actionbar.findViewById(R.id.mutual_finder_search);
                searchView.requestFocusFromTouch();
                searchView.setQueryHint("Find new mutuals here!");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        submit(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        queryTyped(newText);
                        return true;
                    }

                });
            }
        });

        mutualsList=(RecyclerView)findViewById(R.id.mutualsList);


        List<User> mutuals=User.jsonToUserList(Data.getMutuals());
        searchAdapter = new UserAdapter(mutuals);
        view = new RecycleViewManager(mutualsList, this,searchAdapter,mutuals);

    }


    public void submit(String text){

    }

    public void queryTyped(String text) {
        // Util.showDialog(this,"one");

        text = text.trim();
        currentText=text;


        if (text.isEmpty()) {
            ArrayList<User> mutuals= (ArrayList<User>) User.jsonToUserList(Data.getMutuals());

            view.update(mutuals);

        } else {

            final ApiRequest.DoSearchRequest request = new ApiRequest.DoSearchRequest(searchApi, text);
            request.execute(new Util.Callback() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if(!request.search.equals(currentText))return;


                        ArrayList<User> newResults = (ArrayList<User>) User.jsonToUserList(response.getJSONArray("results"));

                        view.update(newResults);

                        //searchResults=newResults;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


}
