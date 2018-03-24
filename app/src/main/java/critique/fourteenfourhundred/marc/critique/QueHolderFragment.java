package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.util.ArrayList;


public class QueHolderFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    public DeactivatedViewPager viewPager;
    public ArrayList<QueFragment> posts=new ArrayList<QueFragment>();

    public QueHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.voteGood:
                    viewPager.arrowScroll(View.FOCUS_RIGHT);

                    //Util.showDialog(getActivity(),"GOOD!");
                    //Data.lastVote=1;

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


        viewPager = (DeactivatedViewPager) rootView.findViewById(R.id.pageHolder);





        // Create an adapter that knows which fragment should be shown on each page
        QueHolderAdapter adapter = new QueHolderAdapter(getContext(), getFragmentManager());
        viewPager.setAdapter(adapter);


        viewPager.setFocusableInTouchMode(false);
        viewPager.setFocusable(false);
        viewPager.setEnabled(false);
        viewPager.setClickable(false);
        viewPager.setFocusableInTouchMode(false);


        viewPager.setPagingEnabled(false);

        ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
        voteBad.setOnClickListener(this);

        ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
        voteGood.setOnClickListener(this);

        for(int i=0;i<6;i++){
            QueFragment post = new QueFragment();
            Bundle bundle=new Bundle();
            bundle.putString("post","{\"username\":\"marc\",\"title\":\"Nooo\",\"type\":\"text\",\"seen\":[],\"votes\":0,\"to\":[\"john\"],\"content\":\"NOPE!\"}");
            post.setArguments(bundle);
            posts.add(post);
        }

        return rootView;
    }

    class QueHolderAdapter extends FragmentStatePagerAdapter {
        private Context mContext;



        public QueHolderAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            //  ProfileFragment tab1 = new ProfileFragment();
            return posts.get(position);

        }

        @Override
        public int getCount() {

            return 5;
        }

        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Que";
                default:
                    return null;
            }
        }



    }

}

