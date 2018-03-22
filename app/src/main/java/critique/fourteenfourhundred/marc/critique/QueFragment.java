package critique.fourteenfourhundred.marc.critique;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;


public class QueFragment extends Fragment implements View.OnClickListener {

        View rootView;
        JSONObject currentPost = new JSONObject();


        public QueFragment(){


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_que, container, false);



            ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
            voteBad.setOnClickListener(this);

            ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
            voteGood.setOnClickListener(this);


            loadPost();


            return rootView;
        }

        public void loadPost(){
            QueHandler.getNextInQue(getActivity(),new Callback(){
                public void onResponse(JSONObject post){
                    try{

                        ((TextView) rootView.findViewById(R.id.postTitle)).setText(post.getString("title"));
                        ((TextView) rootView.findViewById(R.id.postContent)).setText(post.getString("content"));
                        ((TextView) rootView.findViewById(R.id.postSender)).setText(post.getString("username"));
                        ((TextView) rootView.findViewById(R.id.postVoteCount)).setText(post.getString("votes")+" votes");

                        API.getPatch(getActivity(), post.getString("username"), new Callback() {
                            public void onResponse(Bitmap img) {
                                ((ImageView) rootView.findViewById(R.id.queProfilePic)).setImageBitmap(img);
                            }
                        });

                        currentPost=post;

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                public void onError(int code){
                    ((TextView) rootView.findViewById(R.id.postTitle)).setText("Oh no!");
                    ((TextView) rootView.findViewById(R.id.postContent)).setText("Your que is empty :(");
                    ((TextView) rootView.findViewById(R.id.postSender)).setText("");
                    ((TextView) rootView.findViewById(R.id.postVoteCount)).setText("");
                }
            });
        }


        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.voteGood:
                        //Util.showDialog(getActivity(),"GOOD!");
                        //Data.lastVote=1;
                        API.castVotes(getActivity(), currentPost.getString("_id"),1,new Callback(){
                            @Override
                            public void onResponse(JSONObject response) {
                                loadPost();
                            }
                        });

                        return;
                    case R.id.voteBad:
                        //Data.lastVote=0;
                        API.castVotes(getActivity(), currentPost.getString("_id"),0,new Callback(){
                            @Override
                            public void onResponse(JSONObject response) {
                                loadPost();
                            }
                        });
                        return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);


        }

    public void myClickMethod(View v) {

    }



}
