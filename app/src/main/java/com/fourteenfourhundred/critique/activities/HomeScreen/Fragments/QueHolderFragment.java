package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.activities.HomeScreen.HomeActivity;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.views.PostView;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class QueHolderFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public FrameLayout viewFlipper;
    //public ArrayList<QueFragment> posts=new ArrayList<QueFragment>();
    public Animation slide_in_left, slide_out_right;
    public JSONObject currentPost;

    public ImageButton voteGood, voteBad;

    public int remainingPosts=2;
    public int currentPostCount=0;

    public View toRemove;

    public boolean queLoading=true;
    public int untilLoad=0;

    public boolean buttonsLocked=false;
    public boolean autoNext=false;

    //public boolean firstView

    ImageView syncIcon;

    public JSONArray votes=new JSONArray();
    public boolean voteLock=false;
    public static ArrayList<PostView> que = new ArrayList<PostView>();

    public QueHolderFragment() {
        // Required empty public constructor
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.clear();

        //editor.commit();
    }


    public void vote(final int voteVal){
        //buttonsLocked++;
       // if (que.size() > 1) {


        try {
            JSONObject vote=new JSONObject().put("id",currentPost.getString("_id")).put("vote",voteVal);

            votes.put(vote);

            continueQue();

                }catch (Exception e){
                    e.printStackTrace();
                }
          //  }
        //});
    }

    @Override
    public void onClick(View view) {
        try {
            //currentPost=que.get(currentPostCount);



         //   if(!buttonsLocked) {

                switch (view.getId()) {
                    case R.id.voteGood:

                        vote(1);
                        return;
                    case R.id.voteBad:
                        //Data.lastVote=0;

                        vote(0);
                        return;
                }
          //  }
           // }else{
             //   autoNext=true;
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void continueQue(){
        Log.e("sliding next post","sliding next post "+que.size());

        /*
        if(voteLock){
            Log.e("VOTE LOCKED","VOTE LOCKED");
            return;
        }*/

        final PostView post= que.get(1);
        post.getSelf().setVisibility(View.INVISIBLE);
        viewFlipper.addView(post.getSelf());



        if(voteLock){
            Log.e("VOTE LOCKED","VOTE LOCKED");
            return;
        }

        post.getSelf().post(new Runnable() {
            @Override
            public void run() {
                slideUp(post.getSelf(), new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fadeOut(que.get(0).getSelf());
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewFlipper.removeViewAt(0);
                        que.remove(0);
                        currentPost = que.get(0).getPost();
                        saveQue();
                        if(que.size()==3){
                            voteLock=true;
                            voteGood.setEnabled(false);
                            voteBad.setEnabled(false);
                            ((HomeActivity)getActivity()).startLoadAnimation();
                            Log.e("voting","loading...");


                                API.castVotes(getActivity(), votes, new Util.Callback() {
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("voting","done!");
                                        if (!response.getString("status").equals("error")) {
                                            votes=new JSONArray();
                                            loadPost(false);
                                            //saveQue();
                                        }else{
                                            Util.showDialog(getActivity(), "error voting: "+response.getString("message"));
                                        }

                                    }catch (Exception e){

                                    }
                                }
                            });


                        }



                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_que_holder, container, false);

        viewFlipper = (FrameLayout) rootView.findViewById(R.id.contentHolder);




        voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);





        //setHasOptionsMenu(true);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().g
        String q = sharedPref.getString("que", "[]");

        try{
            JSONArray j=new JSONArray(q);
            if(j.length()==0){
                loadPost(true);
            }else{
                for(int i=0;i<j.length();i++){
                    final PostView post = new PostView(getContext(),j.get(i).toString());
                    que.add(post);
                    votes=new JSONArray(sharedPref.getString("votes", "[]"));
                    if(i==0) {
                        currentPost = post.getPost();
                        viewFlipper.addView(post.getSelf());
                        post.getSelf().post(new Runnable() {
                            @Override
                            public void run() {
                                slideUp(post.getSelf(), null);
                            }
                        });
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }






        return rootView;
    }

    public void saveQue(){
        JSONArray save=new JSONArray();
        for (PostView post: que){
            save.put(post.getPost());
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString("que", save.toString());
        editor.putString("votes", votes.toString());
        editor.apply();
    }

    public void slideUp(View view, Animation.AnimationListener al){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
        animate.setDuration(200);
        animate.setFillAfter(true);
        animate.setAnimationListener(al);
        view.startAnimation(animate);
    }

    public void fadeOut(final View view){


        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(150);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //alphaAnimation.setRepeatCount(1);
        //alphaAnimation.setRepeatMode(Animation.REVERSE);

        view.startAnimation(alphaAnimation);
    }


    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

        public void loadPost(final boolean start) {
            Log.e("getting posts","loading...");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                API.getQue(getActivity(),new Callback(){
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("ok")) {

                                JSONArray posts = new JSONArray(response.getString("message"));
                                for (int i = 0; i < posts.length(); i++) {
                                    String postData = posts.get(i).toString();
                                    final PostView post = new PostView(getContext(),postData);
                                    que.add(post);

                                    post.getSelf().setVisibility(View.INVISIBLE);

                                    if(i==0) {
                                        //
                                        if (start) {
                                            currentPost = post.getPost();
                                            viewFlipper.addView(post.getSelf());
                                            post.getSelf().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    slideUp(post.getSelf(), null);
                                                }
                                            });
                                        }
                                    }
                                }
                                voteLock=false;
                                voteGood.setEnabled(true);
                                voteBad.setEnabled(true);
                                saveQue();
                                ((HomeActivity)getActivity()).stopLoadAnimation();
                                Log.e("VOTE LOCK","VOTES UNLOCKED");
                                Log.e("getting posts","done!");
                            }else{
                                Util.showDialog(getActivity(),""+response.getString("message"));
                            }
                            //viewFlipper.showNext();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
                }
            });

        }



}

