package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
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

    public JSONArray archive;
    SwipeRefreshLayout swipeRefreshLayout;
    public int page=0;

    API api;


    public AllProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);
        content = rootView.findViewById(R.id.allFragmentContent);






        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );



        final ScrollView scrollView = rootView.findViewById(R.id.allScrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached



                if (diff < 650) {
                    //Util.showDialog(getActivity(),"YUP");
                    Log.e("diff",diff+"");
                    loadNewContent();
                }
            }
        });


        api=((HomeActivity) getActivity()).ProfileApi;

        refresh();

        return rootView;
    }



    public void refresh(){
        new ApiRequest.GetArchiveRequest(api,0,page).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

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

    public void loadNewContent(){
        page++;

        new ApiRequest.GetArchiveRequest(api,page,page+1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    JSONArray newContent = response.getJSONArray("archive");

                    for(int i=0;i<newContent.length();i++){
                        PostItemView item = new PostItemView(getContext(), api, newContent.getJSONObject(i).toString());
                        content.addView(item.getSelf());
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
