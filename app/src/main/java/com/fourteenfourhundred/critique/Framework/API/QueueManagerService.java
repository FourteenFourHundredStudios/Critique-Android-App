package com.fourteenfourhundred.critique.Framework.API;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.fourteenfourhundred.critique.Framework.Models.EmptyPost;
import com.fourteenfourhundred.critique.Framework.Models.Post;
import com.fourteenfourhundred.critique.Framework.Models.WebPost;
import com.fourteenfourhundred.critique.UI.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.util.Callback;
import com.fourteenfourhundred.critique.util.Util;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueueManagerService {

    public QueueFragment queueFragment;
    public Context context;
    public Activity activity;

    public ArrayList<Post> queue = new ArrayList<>();
    public JSONArray votes= new JSONArray();

    public EmptyPost emptyView;
    public Post currentView;

    public boolean queEmpty=false;

    public boolean isVoting=false;



    int t=1;

    public API api;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public QueueManagerService(QueueFragment fragment){
        this.queueFragment=fragment;
        this.activity=queueFragment.getActivity();
        this.context=activity.getApplicationContext();

        emptyView = new EmptyPost(queueFragment);
        emptyView.inflateView(api,context);


        api=new API(activity);

        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        queueInit();




    }

    public Post createPostFromJSON(JSONObject postJson){
        Post post = null;
        try {


            switch (postJson.getString("type")) {
                case "text":
                    post = new Post(postJson);
                    break;

                case "link":
                    post = new WebPost(postJson);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return post;
    }


    public void addToQueue(String postData){

        try {
            JSONObject postJson = new JSONObject(postData);
            Post post = createPostFromJSON(postJson);
            post.inflateView(api,activity);
            queue.add(post);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void queueInit(){

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String q = sharedPref.getString("que", "[]");
        String c = sharedPref.getString("currentPost", null);
        queueFragment.setVoteLock(true);
        try{
            JSONArray j=new JSONArray(q);
            if(j.length()==0 && c==null) {
                loadPostsIntoQue( response -> {
                    queueFragment.renderNextPost();
                    queueFragment.setVoteLock(false);
                });
            } else{
                //currentView = new Post(new JSONObject(c));
                //currentView.inflateView(api,activity);

                currentView=createPostFromJSON(new JSONObject(c));
                currentView.inflateView(api,activity);


                votes=new JSONArray(sharedPref.getString("votes", "[]"));
                for(int i=0;i<j.length();i++){
                    //final PostView post = new PostView(activity,api,j.get(i).toString());

                    //queue.add(post);
                    addToQueue(j.get(i).toString());

                }
                queueFragment.forcePostRender(currentView,response -> {

                        queueFragment.setVoteLock(false);

                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void loadPostsIntoQue(final Callback.Response callback){

        AsyncTask.execute(() -> new ApiRequest.GetQueRequest(api).execute(response -> {
            try {
                    JSONArray posts = (JSONArray) response;
                    queEmpty = (posts.length() == 0);
                    for (int i = 0; i < posts.length(); i++) {
                        String postData = posts.get(i).toString();
                        addToQueue(postData);
                    }
                    saveQue();

                    if (callback != null) callback.onResponse(null);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public void castVotes(final boolean loadingQue) {


        if(votes.length()==0 || isVoting)return;
        Log.e("votes","casting votes!");
        try {
            isVoting=true;
            JSONArray v = new JSONArray(votes.toString());
            saveVotes();
            votes = new JSONArray();

            new ApiRequest.CastVotesRequest(api,v).execute(response -> {
                try {
                    if(loadingQue) {
                        loadPostsIntoQue( resp -> {
                            queueFragment.setVoteLock(false);
                            isVoting = false;
                            saveVotes();
                        });
                    }else{
                        queueFragment.setVoteLock(false);
                        isVoting = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void vote(int voteValue){
        try {
            if(currentView!=null) {
                JSONObject vote = new JSONObject().put("id", currentView.getId()).put("vote", voteValue);
                votes.put(vote);
            }

            if ((queue.size()==3)) {


                castVotes(true);
            }
            saveVotes();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkForNewPosts(){
            loadPostsIntoQue(resp -> {
                    queueFragment.setVoteLock(false);
                    isVoting = false;

                    if (queEmpty) {
                        emptyView=new EmptyPost(queueFragment);
                        emptyView.inflateView(api,context);
                        queueFragment.forcePostRender(emptyView);

                    } else {
                        Post view = queue.remove(0);
                        currentView = view;
                        queueFragment.forcePostRender(view);
                    }


            });

    }

    public void saveVotes(){
        editor.putString("votes", votes.toString());
        editor.commit();

    }


    public void saveQue(){
        JSONArray save=new JSONArray();
        for (Post post: queue){
            save.put(post.getPostData());
        }

        //
        // if(currentView!=null)save.put(currentView.getPost());

        editor.putString("que", save.toString());
        if(currentView!=null){
            editor.putString("currentPost", currentView.getPostData().toString());
        }else {
            editor.putString("currentPost", null);
        }
        editor.commit();

    }

    public void saveAll(){
        saveQue();
        saveVotes();
    }


    public Post getNextPost(){
        Post view=new EmptyPost(queueFragment);
        currentView=null;

        if(queue.size()==0){
            queueFragment.setVoteLock(true);
        }


        if(queue.size()==2 && isVoting){

            queueFragment.setVoteLock(true);
        }

        if(queue.size()>0){

            view = queue.remove(0);

        }

        if(!(view instanceof  EmptyPost)){
            currentView=view;
        }else{





                if(votes.length()>0) {

                            try {

                                JSONArray v = new JSONArray(votes.toString());
                                votes = new JSONArray();
                                new ApiRequest.CastVotesRequest(api, v).execute( (Callback.Response<JSONObject>) response -> {

                                    checkForNewPosts();




                                });

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                }else{
                    checkForNewPosts();
                }



        }


        saveAll();

        return view;
    }

}
