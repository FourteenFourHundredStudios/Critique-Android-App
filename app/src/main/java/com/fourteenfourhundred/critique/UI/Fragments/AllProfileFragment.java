package com.fourteenfourhundred.critique.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Views.LockedScrollView;
import com.fourteenfourhundred.critique.UI.Views.Post;
import com.fourteenfourhundred.critique.UI.Views.PostAdapter;
import com.fourteenfourhundred.critique.UI.Views.RecycleViewManager;
import com.fourteenfourhundred.critique.UI.Views.User;
import com.fourteenfourhundred.critique.UI.Views.UserAdapter;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.UI.Views.PostItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllProfileFragment extends Fragment {

    View rootView;
    LinearLayout content;

    RecycleViewManager listManager;
    PostAdapter postAdapter;


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


        api= Data.backgroundApi;

        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);

        ArrayList<Post> emptyPosts = new ArrayList<Post>();
        RecyclerView view = rootView.findViewById(R.id.allFragmentContent);
        PostAdapter postAdapter = new PostAdapter(emptyPosts);

        listManager=new RecycleViewManager(view, this.getContext(),postAdapter,emptyPosts);


        new ApiRequest.GetArchiveRequest(api,0,1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    archive = response.getJSONArray("archive");
                    ArrayList<Post> posts = new ArrayList<Post>();
                    for(int i=0;i<archive.length();i++){
                        JSONObject post = new JSONObject(archive.getString(i));
                       // Log.e("dwd",post.toString());
                        if(shouldRender(post)) {
                            posts.add(new Post(post.getString("username"),post.getString("content"),0));

                        }
                    }
                    listManager.append(posts);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        /*
        content = rootView.findViewById(R.id.allFragmentContent);

        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );*/


/*
        final NestedScrollView scrollView = rootView.findViewById(R.id.allScrollview);
        //scrollView.canScrollHorizontally(50);
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
*/

/*
        api= Data.backgroundApi;
        more = new ProgressBar(getContext());

        refresh();*/

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
                    moreContent=false;

                    swipeRefreshLayout.setRefreshing(false);
                    for(int i=0;i<archive.length();i++){
                        JSONObject post = new JSONObject(archive.getString(i));
                        if(shouldRender(post)) {
                            moreContent=true;
                            PostItemView item = new PostItemView(getContext(), api, post.toString());
                            content.addView(item.getSelf());
                        }
                    }

                    finishedLoading();

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
                    moreContent=false;
                    JSONArray newContent = response.getJSONArray("archive");




                    for(int i=0;i<newContent.length();i++){
                        JSONObject post=newContent.getJSONObject(i);
                        if(shouldRender(post)) {
                            moreContent=true;
                            PostItemView item = new PostItemView(getContext(), api, post.toString());
                            content.addView(item.getSelf());
                        }
                    }
                    isLoading=false;
                    finishedLoading();


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
