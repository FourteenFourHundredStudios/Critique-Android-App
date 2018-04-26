package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.fourteenfourhundred.critique.UI.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.UI.Views.EmptyView;
import com.fourteenfourhundred.critique.UI.Views.LinkPostView;
import com.fourteenfourhundred.critique.UI.Views.PostView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueueManagerService {

    public QueueFragment queueFragment;
    public Context context;
    public Activity activity;

    public ArrayList<PostView> queue = new ArrayList<>();
    public JSONArray votes= new JSONArray();

    public EmptyView emptyView;
    public PostView currentView;

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

        emptyView = new EmptyView(context,fragment);



        api=new API(activity);

        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        queueInit();




    }

    public void addToQueue(String postData){

        try {
            JSONObject postJson = new JSONObject(postData);
            PostView post=null;

            switch (postJson.getString("type")){
                case "text":
                    post = new PostView(activity,api,postJson.toString());
                    break;

                case "link":
                    post = new LinkPostView(activity,api,postJson.toString());
                    break;
            }


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
            if(j.length()==0){
                loadPostsIntoQue(new Util.Callback(){
                    @Override
                    public void onFinished() {
                        queueFragment.renderNextPost();
                        queueFragment.setVoteLock(false);
                    }
                });
            }else{
                currentView = new PostView(activity,api,c);

                votes=new JSONArray(sharedPref.getString("votes", "[]"));
                for(int i=0;i<j.length();i++){
                    //final PostView post = new PostView(activity,api,j.get(i).toString());

                    //queue.add(post);
                    addToQueue(j.get(i).toString());

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
        //System.out.println((t/t));
        //t--;
        Log.e("from here","from here");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                new ApiRequest.GetQueRequest(api).execute(new Util.Callback(){
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")) {
                                JSONArray posts = new JSONArray(response.getString("message"));
                                queEmpty = (posts.length() == 0);
                                for (int i = 0; i < posts.length(); i++) {
                                    String postData = posts.get(i).toString();
                                    //final PostView post = new PostView(activity, api,postData);
                                    //queueFragment..add(post.getSelf());
                                    //queue.add(post);
                                    addToQueue(postData);
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

    public void castVotes(final boolean loadingQue) {
        if(votes.length()==0 || isVoting)return;
        Log.e("votes","casting votes!");
        try {
            isVoting=true;
            JSONArray v = new JSONArray(votes.toString());
            saveVotes();
            votes = new JSONArray();

            new ApiRequest.CastVotesRequest(api,v).execute(new Util.Callback(){
                public void onResponse(JSONObject response) {
                    try {
                        if (!response.getString("status").equals("error")) {

                            if(loadingQue) {
                                //Log.e("HERE","HERE");
                                loadPostsIntoQue(new Util.Callback() {
                                    @Override
                                    public void onFinished() {
                                        queueFragment.setVoteLock(false);
                                        isVoting = false;
                                        saveVotes();
                                    }
                                });
                            }else{
                                queueFragment.setVoteLock(false);
                                isVoting = false;
                            }

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
            loadPostsIntoQue(new Util.Callback() {
                @Override
                public void onFinished() {
                    queueFragment.setVoteLock(false);
                    isVoting = false;

                    if (queEmpty) {
                        queueFragment.forcePostRender(new EmptyView(context, queueFragment));

                    } else {
                        PostView view = queue.remove(0);
                        currentView = view;
                        queueFragment.forcePostRender(view);
                    }

                }
            });

    }

    public void saveVotes(){
        editor.putString("votes", votes.toString());
        editor.commit();

    }


    public void saveQue(){
        JSONArray save=new JSONArray();
        for (PostView post: queue){
            save.put(post.getPost());
        }

        editor.putString("que", save.toString());
        if(currentView!=null){
            editor.putString("currentPost", currentView.getPost().toString());
        }else {
            editor.putString("currentPost", null);
        }
        editor.commit();

    }

    public void saveAll(){
        saveQue();
        saveVotes();
    }


    public PostView getNextPost(){
        PostView view=new EmptyView(context,queueFragment);
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

        if(!(view instanceof  EmptyView)){
            currentView=view;
        }else{





                if(votes.length()>0) {

                    //AsyncTask.execute(new Runnable() {
                        //@Override
                     //   public void run() {
                            try {

                                JSONArray v = new JSONArray(votes.toString());
                                votes = new JSONArray();
                                new ApiRequest.CastVotesRequest(api, v).execute(new Util.Callback() {
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

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        //}
                    //});

                }else{
                    checkForNewPosts();
                }



        }


        saveAll();

        return view;
    }

}
