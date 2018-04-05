package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.content.Context;
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

    public EmptyView emptyView;

    public QueueManagerService(QueueFragment fragment){
        this.queueFragment=fragment;
        this.activity=queueFragment.getActivity();
        this.context=activity.getApplicationContext();

         emptyView = new EmptyView(context);

        //queueFragment.setVoteLock(true);
        loadPostsIntoQue();
    }

    public void loadPostsIntoQue(){
        API.getQue(activity,new Util.Callback() {
            public void onResponse(JSONObject response) {
                boolean newPosts;
                try {
                    if (response.getString("status").equals("ok")) {
                        JSONArray posts = new JSONArray(response.getString("message"));
                        newPosts = (posts.length()==0);
                        for (int i = 0; i < posts.length(); i++) {
                            String postData = posts.get(i).toString();
                            final PostView post = new PostView(activity, postData);
                            queue.add(post);
                        }

                    }else{
                        Util.showDialog(activity,"Error loading queue! "+response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void vote(int vote){
        
    }

    public PostView getNextPost(){
        PostView view=new EmptyView(context);

        if(queue.size()>1){
            view = queue.remove(0);
        }

        return view;
    }

}
