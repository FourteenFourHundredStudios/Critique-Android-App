package com.fourteenfourhundred.critique.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.Framework.Models.Post;
import com.fourteenfourhundred.critique.UI.RecycleView.PostAdapter;
import com.fourteenfourhundred.critique.UI.RecycleView.RecycleViewManager;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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




    public AllProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        api= Data.backgroundApi;

        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);

        ArrayList<Post> emptyPosts = new ArrayList<>();


        RecyclerView view = rootView.findViewById(R.id.allFragmentContent);
        PostAdapter postAdapter = new PostAdapter(emptyPosts);

        listManager=new RecycleViewManager(view, this.getActivity(),postAdapter,emptyPosts);




        new ApiRequest.GetArchiveRequest(api,0,1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {

                    archive = response.getJSONArray("archive");
                    ArrayList<Post> posts = new ArrayList<Post>();
                    for(int i=0;i<archive.length();i++){
                        JSONObject post = new JSONObject(archive.getString(i));

                        if(shouldRender(post)) {

                            posts.add(new Post(post));

                        }
                    }
                    listManager.append(posts);
                    addScrollView();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );





        return rootView;
    }


    public void addScrollView(){
        final NestedScrollView scrollView = rootView.findViewById(R.id.allScrollview);

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

    }


    public void refresh(){


        archive=new JSONArray();
        new ApiRequest.GetArchiveRequest(api,0,page+1).execute(new Util.Callback(){
            @Override
            public void onResponse(final JSONObject response) {
                try {


                    archive = response.getJSONArray("archive");
                    moreContent=false;


                    ArrayList<Post> posts = new ArrayList<Post>();
                    for(int i=0;i<archive.length();i++){
                        JSONObject post = new JSONObject(archive.getString(i));
                        if(shouldRender(post)) {
                            moreContent=true;
                           // Log.e("post",new Post(post).getId());
                            posts.add(new Post(post));
                        }
                    }
                    listManager.update(posts,1);
                    swipeRefreshLayout.setRefreshing(false);

                    finishedLoading();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public void startLoading(){

//        if(content.indexOfChild(more)==-1)content.addView(more);
    }

    public void finishedLoading(){

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



                    ArrayList<Post> posts = new ArrayList<Post>();
                    for(int i=0;i<newContent.length();i++){
                        JSONObject post=newContent.getJSONObject(i);
                        if(shouldRender(post)) {
                            moreContent=true;
                            posts.add(new Post(post));
                        }
                    }
                    listManager.append(posts);
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
