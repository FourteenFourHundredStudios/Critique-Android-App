package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import org.json.JSONObject;

import java.util.ArrayList;


public class QueHolderFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public ViewFlipper layout;
    public ArrayList<QueFragment> posts=new ArrayList<QueFragment>();
    public Animation slide_in_left, slide_out_right;


    public QueHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.voteGood:


                    layout.showNext();

                    /*
                    API.castVotes(getActivity(), currentPost.getString("_id"),1,new Callback(){
                        @Override
                        public void onResponse(JSONObject response) {
                            loadPost();
                        }
                    });*/

                    return;
                case R.id.voteBad:
                    //Data.lastVote=0;
                    /*
                    API.castVotes(getActivity(), currentPost.getString("_id"),0,new Callback(){
                        @Override
                        public void onResponse(JSONObject response) {
                            loadPost();
                        }
                    });*/
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_que_holder, container, false);


        //content = (LockableScrollView) rootView.findViewById(R.id.pageHolder);

        layout = (ViewFlipper) rootView.findViewById(R.id.contentHolder);

        slide_in_left = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);

        layout.setInAnimation(slide_in_left);
        layout.setOutAnimation(slide_out_right);


        //content = (LockableScrollView) rootView.findViewById(R.id.pageHolder);
        //content.setScrollingEnabled(true);
        //content


        //content.setOnTouchListener(null);


        //getFragmentManager().beginTransaction().add(layout.getId(), QueFragment.instantiate(getContext(),"que"), "someTag1").commit();





        ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);



//        ((ScrollView)rootView.findViewById(R.id.pageHolder)).requestDisallowInterceptTouchEvent(true);


        for(int i=0;i<6;i++){
            QueFragment post = new QueFragment();
            Bundle bundle=new Bundle();
            bundle.putString("post","{\"username\":\"marc\",\"title\":\"Nooo"+i+"\",\"type\":\"text\",\"seen\":[],\"votes\":0,\"to\":[\"john\"],\"content\":\"orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.!\"}");
            post.setArguments(bundle);
            posts.add(post);
            getFragmentManager().beginTransaction().add(layout.getId(), post, "someTag1").commit();
        }



        return rootView;
    }




}

