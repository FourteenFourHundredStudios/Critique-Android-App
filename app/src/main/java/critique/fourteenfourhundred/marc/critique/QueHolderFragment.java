package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class QueHolderFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public ViewFlipper viewFlipper;
    //public ArrayList<QueFragment> posts=new ArrayList<QueFragment>();
    public Animation slide_in_left, slide_out_right;
    public JSONObject currentPost;

    public int remainingPosts=2;
    public int currentPostCount=0;

    public View toRemove;

    public boolean queLoading=true;

    public static ArrayList<JSONObject> que = new ArrayList<JSONObject>();

    public QueHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        try {
            currentPost=que.get(currentPostCount);
            switch (view.getId()) {
                case R.id.voteGood:

                    API.castVotes(getActivity(), currentPost.getString("_id"),1,new Callback(){
                        @Override
                        public void onResponse(JSONObject response) {
                            //loadPost(false);
                            //toRemove=layout.getChildAt(layout.getDisplayedChild());

                            //Log.e("hmmm",viewFlipper.getChildCount()+"");

                            toRemove=viewFlipper.getCurrentView();

                            viewFlipper.showPrevious();




                            /*
                            viewFlipper.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadPost(false);
                                }
                            });*/

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    viewFlipper.removeView(toRemove);
                                    loadPost(false);
                                    currentPostCount++;
                                }
                            });





                        }
                    });

                    return;
                case R.id.voteBad:
                    //Data.lastVote=0;

                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_que_holder, container, false);

        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.contentHolder);


        slide_in_left = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);

        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);



        ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);

        loadPost(true);


        return rootView;
    }


        public void loadPost(final boolean start) {
            Log.e("COUNT",viewFlipper.getChildCount()+"");
            if(start || viewFlipper.getChildCount()<3){
                API.getQue(getActivity(),new Callback(){
                    public void onResponse(JSONObject response) {
                        try {
                            currentPostCount=0;
                            que.clear();
                            Log.e("HERE","GOT RESPONSE");
                            JSONArray posts = new JSONArray(response.getString("message"));
                            for(int i=0;i<posts.length();i++){
                                Log.e("ffff","ADDING POST");
                                String postData=posts.get(i).toString();
                                final QueView post = new QueView(getContext(),postData);
                                que.add(new JSONObject(postData));


                                        viewFlipper.addView(post.getSelf());


                            }
                            //Util.showDialog(getActivity(),viewFlipper.getChildCount()+"");
                            viewFlipper.setDisplayedChild(posts.length()-1);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }




}

