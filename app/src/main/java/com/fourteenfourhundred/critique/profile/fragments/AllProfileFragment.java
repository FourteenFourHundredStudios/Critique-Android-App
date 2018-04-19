package com.fourteenfourhundred.critique.profile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.activities.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
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
    boolean moreContent=true;
    boolean isLoading=false;
    ProgressBar more;
    API api;

    TextView nothingHere ;


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

                if (diff <650 && moreContent && !swipeRefreshLayout.isRefreshing() && !isLoading) {
                    isLoading=true;
                    loadNewContent();
                }
            }
        });


        api= Data.backgroundApi;
        more = new ProgressBar(getContext());

        refresh();

        return rootView;
    }



    public void refresh(){

        content.removeAllViews();
        archive=new JSONArray();
        new ApiRequest.GetArchiveRequest(api,0,page+1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    archive = response.getJSONArray("archive");
                    moreContent = (archive.length()!=0);
                    finishedLoading();
                    swipeRefreshLayout.setRefreshing(false);
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
        });
    }


    public void startLoading(){

        if(content.indexOfChild(more)==-1)content.addView(more);
    }

    public void finishedLoading(){
        if(more!=null && content.getChildCount()>0){
            int moreIndex=(content.indexOfChild(more));
            if(moreIndex!=-1)content.removeViewAt(moreIndex);
        }
        if(!moreContent && content.indexOfChild(nothingHere)==-1){
            nothingHere=new TextView(getContext());

            nothingHere.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            nothingHere.setPadding(15,35,15,35);

            nothingHere.setText("There's nothing here :(");
            content.addView(nothingHere);
        }
    }


    public void loadNewContent(){

        startLoading();

        page++;

        new ApiRequest.GetArchiveRequest(api,page,1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    JSONArray newContent = response.getJSONArray("archive");

                    moreContent = (newContent.length()!=0);
                    finishedLoading();

                    for(int i=0;i<newContent.length();i++){
                        PostItemView item = new PostItemView(getContext(), api, newContent.getJSONObject(i).toString());
                        content.addView(item.getSelf());
                    }
                    isLoading=false;

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public boolean shouldRender(JSONObject e) throws JSONException {
        return true;
    }



}
