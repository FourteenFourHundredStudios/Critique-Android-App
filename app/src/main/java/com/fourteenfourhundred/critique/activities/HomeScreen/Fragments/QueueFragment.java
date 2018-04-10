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
import com.fourteenfourhundred.critique.API.QueueManagerService;
import com.fourteenfourhundred.critique.activities.HomeScreen.HomeActivity;
import com.fourteenfourhundred.critique.util.AnimationUtil;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.views.EmptyView;
import com.fourteenfourhundred.critique.views.PostView;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class QueueFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public ImageButton voteGood, voteBad;
    public ImageView syncIcon;
    public FrameLayout frame;
    public PostView activePost;
    public boolean voteLock=false;

    public QueueManagerService queue;


    public QueueFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_que_holder, container, false);

        frame = (FrameLayout) rootView.findViewById(R.id.contentHolder);

        voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);

        queue=new QueueManagerService(this);

       // Util.showDialog(getActivity(),String.valueOf(queue==null));

        ((HomeActivity)getActivity()).addViewpagerListener();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        getActivity().invalidateOptionsMenu();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voteGood:

                vote(1);
                return;

            case R.id.voteBad:
                vote(0);
                return;
        }
    }


    public void onPause(){

        super.onPause();
        saveState();

    }

    public void saveState(){
        if(queue==null)return;
        queue.castVotes(false);
        queue.saveAll();
    }

    public void vote(int vote){

         queue.vote(vote);
        renderNextPost();
    }

    public void forcePostRender(final PostView post){
        forcePostRender(post,null);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }


    public void forcePostRender(final PostView post,final Callback callback){
        post.getSelf().setVisibility(View.INVISIBLE);
        frame.addView(post.getSelf());

        post.getSelf().post(new Runnable() {
            @Override
            public void run() {
                AnimationUtil.slideUp(post.getSelf(), new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(activePost!=null){
                            AnimationUtil.fadeOut(activePost.getSelf(),null);
                        }
                        activePost=post;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(callback!=null)callback.onFinished();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public void renderNextPost(){

        final PostView nextPost = queue.getNextPost();

        if(nextPost instanceof EmptyView){
            setVoteLock(true);
            return;
        }

        nextPost.getSelf().setVisibility(View.INVISIBLE);
        frame.addView(nextPost.getSelf());

        nextPost.getSelf().post(new Runnable() {
            @Override
            public void run() {
                AnimationUtil.slideUp(nextPost.getSelf(), new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(activePost!=null){
                            AnimationUtil.fadeOut(activePost.getSelf(),null);
                        }
                        activePost=nextPost;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


    }

    public void setVoteLock(boolean locked) {
        if (locked == voteLock ) return;
        voteLock = locked;
        voteGood.setEnabled(!locked);
        voteBad.setEnabled(!locked);
        if (locked) {
            ((HomeActivity) getActivity()).startLoadAnimation();

        } else {
            ((HomeActivity) getActivity()).stopLoadAnimation();
        }


    }




}

