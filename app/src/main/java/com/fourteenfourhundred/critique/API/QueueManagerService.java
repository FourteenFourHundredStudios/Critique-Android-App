package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.views.EmptyView;
import com.fourteenfourhundred.critique.views.PostView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueueManagerService {

    public QueueFragment queueFragment;
    public Context context;
    public Activity activity;

    public ArrayList<PostView> queue = new ArrayList<PostView>();
    public JSONArray votes= new JSONArray();

    public EmptyView emptyView;
    public PostView currentView;

    public boolean queEmpty=false;

    public boolean isVoting=false;

    public API api;


    public QueueManagerService(QueueFragment fragment){
        this.queueFragment=fragment;
        this.activity=queueFragment.getActivity();
        this.context=activity.getApplicationContext();

        emptyView = new EmptyView(context);

        api=new API(activity);

        queueInit();

        /*
        queueFragment.setVoteLock(true);
        loadPostsIntoQue(new Util.Callback(){
            @Override
            public void onFinished() {
                queueFragment.renderNextPost();
                queueFragment.setVoteLock(false);
            }
        });
        */
    }

    public void queueInit(){

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String q = sharedPref.getString("que", "[]");
        queueFragment.setVoteLock(true);
        try{
            JSONArray j=new JSONArray(q);
            if(j.length()==0){
                loadPostsIntoQue(new Util.Callback(){
                    @Override
                    public void onFinished() {
                        queueFragment.renderNextPost();
                        queueFragment.setVoteLock(false);
                    }
                });
            }else{
                votes=new JSONArray(sharedPref.getString("votes", "[]"));
                for(int i=0;i<j.length();i++){
                    final PostView post = new PostView(activity,api,j.get(i).toString());
                    if(i==0){
                        currentView=post;
                    }else {
                        queue.add(post);
                    }
                }
                queueFragment.forcePostRender(currentView,new Util.Callback(){
                    public void onFinished(){
                        queueFragment.setVoteLock(false);
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void loadPostsIntoQue(final Util.Callback callback){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                API.getQue(activity, new Util.Callback() {
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")) {
                                JSONArray posts = new JSONArray(response.getString("message"));
                                queEmpty = (posts.length() == 0);
                                for (int i = 0; i < posts.length(); i++) {
                                    String postData = posts.get(i).toString();
                                    final PostView post = new PostView(activity, api,postData);
                                    //queueFragment..add(post.getSelf());
                                    queue.add(post);

                                }
                                saveQue();
                                if (callback != null) callback.onFinished();
                            } else {
                                Util.showDialog(activity, "Error loading queue! " + response.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void castVotes() {
        try {
            isVoting=true;
            JSONArray v = new JSONArray(votes.toString());
            votes = new JSONArray();

            API.castVotes(activity, v, new Util.Callback() {
                public void onResponse(JSONObject response) {
                    try {
                        if (!response.getString("status").equals("error")) {

                            loadPostsIntoQue(new Util.Callback() {
                                @Override
                                public void onFinished() {
                                    queueFragment.setVoteLock(false);
                                    isVoting = false;
                                }
                            });

                        } else {
                            Util.showDialog(activity, "error voting: " + response.getString("message"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void vote(int voteValue){
        try {
            if(currentView!=null) {
                JSONObject vote = new JSONObject().put("id", currentView.getPost().getString("_id")).put("vote", voteValue);
                votes.put(vote);
                Log.e("voted","VOTED!");
            }

            if ((queue.size()==3 && !isVoting)) {


                castVotes();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkForNewPosts(){
        loadPostsIntoQue(new Util.Callback() {
            @Override
            public void onFinished() {
                queueFragment.setVoteLock(false);
                isVoting = false;

                if(queEmpty){
                    queueFragment.forcePostRender(new EmptyView(context));

                }else{
                    PostView view = queue.remove(0);
                    currentView=view;
                    queueFragment.forcePostRender(view);
                }

            }
        });

    }


    public void saveQue(){
        JSONArray save=new JSONArray();
        for (PostView post: queue){
            save.put(post.getPost());
        }
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString("que", save.toString());
        editor.putString("votes", votes.toString());
        editor.apply();
    }


    public PostView getNextPost(){
        PostView view=new EmptyView(context);
        currentView=null;

        if(queue.size()==0){
            queueFragment.setVoteLock(true);
        }


        if(queue.size()==2 && isVoting){

            queueFragment.setVoteLock(true);
        }

        if(queue.size()>0){
            saveQue();
            view = queue.remove(0);

        }

        if(!(view instanceof  EmptyView)){
            currentView=view;
        }else{
            try {
                if(votes.length()>0) {
                    JSONArray v = new JSONArray(votes.toString());
                    votes = new JSONArray();
                    API.castVotes(activity, v, new Util.Callback() {
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.getString("status").equals("error")) {
                                    checkForNewPosts();
                                } else {
                                    Util.showDialog(activity, "error voting: " + response.getString("message"));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    checkForNewPosts();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }




        return view;
    }

}
