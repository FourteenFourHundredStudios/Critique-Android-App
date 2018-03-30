package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class QueHolderFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public FrameLayout viewFlipper;
    //public ArrayList<QueFragment> posts=new ArrayList<QueFragment>();
    public Animation slide_in_left, slide_out_right;
    public JSONObject currentPost;

    public int remainingPosts=2;
    public int currentPostCount=0;

    public View toRemove;

    public boolean queLoading=true;
    public int untilLoad=0;

    public int buttonsLocked=0;
    public boolean autoNext=false;

    //public boolean firstView


    public static ArrayList<QueView> que = new ArrayList<QueView>();

    public QueHolderFragment() {
        // Required empty public constructor
    }


    public void vote(final int vote){
        buttonsLocked++;
        if (que.size() > 1) {
            continueQue();
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API.castVotes(getActivity(), currentPost.getString("_id"), vote, new Callback() {
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("status").equals("error")) {
                                    Util.showDialog(getActivity(), response.getString("message"));
                                }

                                buttonsLocked--;
                                if(autoNext){
                                    autoNext=false;
                                    vote(vote);
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
        });
    }

    @Override
    public void onClick(View view) {
        try {
            //currentPost=que.get(currentPostCount);



            if(buttonsLocked==0) {
                switch (view.getId()) {
                    case R.id.voteGood:

                        vote(1);
                        return;
                    case R.id.voteBad:
                        //Data.lastVote=0;

                        vote(0);
                        return;
                }
            }else{
                autoNext=true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void continueQue(){


        if(que.size()<2||viewFlipper.getChildCount()>1)return;

        final QueView post= que.get(1);
        post.getSelf().setVisibility(View.INVISIBLE);
        viewFlipper.addView(post.getSelf());

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
                        if(que.size()==3){
                            loadPost(false);
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




        ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);




        loadPost(true);

        return rootView;
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
                                    final QueView post = new QueView(getContext(),postData);
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
                            }else{
                                Util.showDialog(getActivity(),response.getString("message"));
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

