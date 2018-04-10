package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.activities.HomeScreen.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.views.PostItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllProfileFragment extends Fragment {

    View rootView;
    LinearLayout content;
    API api;
    JSONArray archive;
    SwipeRefreshLayout swipeRefreshLayout;


    public AllProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);
        content = rootView.findViewById(R.id.allFragmentContent);

        api=new API(getActivity());

        archive = ((HomeActivity)getActivity()).archive;

        render();

        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        API.getArchive(getActivity(),0,new Util.Callback(){
                            @Override
                            public void onResponse(final JSONObject response) {
                                try {


                                    ((HomeActivity)getActivity()).archive = response.getJSONArray("archive");
                                    archive = response.getJSONArray("archive");
                                    content.removeAllViews();
                                    render();
                                    swipeRefreshLayout.setRefreshing(false);


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });



                    }
                }
        );



        return rootView;
    }


    public boolean shouldRender(JSONObject e) throws JSONException {
        return true;
    }



    public void render(){
        try {


                for(int i=0;i<archive.length();i++){

                    JSONObject post = new JSONObject(archive.getString(i));

                    if(shouldRender(post)) {
                        PostItemView item = new PostItemView(getContext(), api, post.toString());
                        content.addView(item.getSelf());
                    }

                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
