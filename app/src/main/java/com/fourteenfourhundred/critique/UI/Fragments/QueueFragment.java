package com.fourteenfourhundred.critique.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.QueueManagerService;
import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.UI.Views.FluidMotionOptions;
import com.fourteenfourhundred.critique.UI.Views.OneFluidMotionView;
import com.fourteenfourhundred.critique.util.AnimationUtil;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.UI.Views.EmptyView;
import com.fourteenfourhundred.critique.UI.Views.PostView;
import com.fourteenfourhundred.critique.critique.R;


public class QueueFragment extends HomeFragment{


    public ImageButton voteGood, voteBad;
    public ImageView syncIcon;
    public FrameLayout frame;
    public PostView activePost;
    public boolean voteLock=false;
    public OneFluidMotionView motionView;

    public QueueManagerService queue;


    public QueueFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.queue_fragment, container, false);

        frame = (FrameLayout) rootView.findViewById(R.id.post_container);

        motionView = ((OneFluidMotionView) rootView.findViewById(R.id.motion_view));

        motionView.setFluidMotionSelectionListener(new FluidMotionOptions.FluidMotionSelctionListener(){
            public void onSelection(int selection){
                switch (selection){
                    case 2:
                        vote(1);
                        break;
                    case 0:
                        vote(0);
                        break;
                }

            }
        });




        queue=new QueueManagerService(this);


        ((HomeActivity)getActivity()).addViewpagerListener();

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        getActivity().invalidateOptionsMenu();

    }



    public int getToolbar(){
        return R.layout.action_bar_post;
    }

    public void onPause(){

        super.onPause();
        saveState();

    }

    public void saveState(){
        if(queue==null)return;
        Log.e("SAVED","SUCSESS");
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
                        //((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title)).setText(activePost.getPostAttribute("title"));
                        //((TextView)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.action_bar_caption)).setText(" post by "+activePost.getPostAttribute("username"));
                        ((TextView)getCritiqueBar().findViewById(R.id.action_bar_title)).setText(activePost.getPostAttribute("title"));
                        ((TextView)getCritiqueBar().findViewById(R.id.action_bar_caption)).setText(" post by "+activePost.getPostAttribute("username"));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(callback!=null)callback.onFinished();

                        if(activePost instanceof EmptyView){
                            ((EmptyView)activePost).stopLoad();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public void renderNextPost(){
        renderNextPost(true);
    }

    public void renderNextPost(boolean showAnimation){

        final PostView nextPost = queue.getNextPost();

        if(nextPost instanceof EmptyView){
            setVoteLock(true,showAnimation);
            return;
        }
        forcePostRender(nextPost);

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && motionView!=null)motionView.reset();

    }


    public void setVoteLock(boolean locked){
        setVoteLock(locked,true);
    }

    public void setVoteLock(boolean locked,boolean showAnimation) {
        if (locked == voteLock ) return;
        voteLock = locked;

        if (locked) {
            motionView.setLocked(true);
            if(showAnimation) ((HomeActivity) getActivity()).startLoadAnimation();
        } else {
            ((HomeActivity) getActivity()).stopLoadAnimation();
            motionView.setLocked(false);
        }



    }







}

