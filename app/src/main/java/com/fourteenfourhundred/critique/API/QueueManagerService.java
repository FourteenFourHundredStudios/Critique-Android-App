package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.content.Context;
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

        queueFragment.setVoteLock(true);
        loadPostsIntoQue(new Util.Callback(){
            @Override
            public void onFinished() {
                queueFragment.renderNextPost();
                queueFragment.setVoteLock(false);
            }
        });
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


    public void vote(int voteValue){




        try {

            if(currentView!=null) {
                JSONObject vote = new JSONObject().put("id", currentView.getPost().getString("_id")).put("vote", voteValue);
                votes.put(vote);
            }

            if ((queue.size()==3 && !isVoting)) {
                isVoting=true;
                Log.e("votes",votes.toString());

                JSONArray v=new JSONArray(votes.toString());
                votes = new JSONArray();

                API.castVotes(activity, v, new Util.Callback() {
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("voting", "done!");
                            if (!response.getString("status").equals("error")) {

                                loadPostsIntoQue(new Util.Callback(){
                                    @Override
                                    public void onFinished() {
                                        queueFragment.setVoteLock(false);
                                        isVoting=false;
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PostView getNextPost(){
        PostView view=new EmptyView(context);
        currentView=null;



        if(queue.size()==2 && isVoting){

            queueFragment.setVoteLock(true);
        }

        if(queue.size()>0){
            view = queue.remove(0);
        }

        if(!(view instanceof  EmptyView)){
            currentView=view;
        }else{
            //loadPostsIntoQue(null);
        }




        return view;
    }

}
