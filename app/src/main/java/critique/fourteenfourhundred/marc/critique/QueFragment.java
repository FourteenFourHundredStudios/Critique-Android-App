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
            API.getQue(getActivity(),new Response.Listener<JSONObject>(){
                public void onResponse(JSONObject response) {
                    try {

                        if(response.get("status").equals("ok")) {

                            if(new JSONArray(response.get("message").toString()).length()!=0) {

                                JSONObject post = new JSONObject(new JSONArray(response.get("message").toString()).get(0).toString());

                                //Util.showDialog(getActivity(),post.toString());
                                Data.lastPost = new JSONArray();
                                Data.lastPost.put(0, post.get("_id"));

                                TextView title = (TextView) rootView.findViewById(R.id.postTitle);
                                title.setText(post.get("title").toString());
                                TextView content = (TextView) rootView.findViewById(R.id.postContent);
                                content.setText(post.get("content").toString());
                                TextView sender = (TextView) rootView.findViewById(R.id.postSender);
                                sender.setText(post.get("username").toString());

                                TextView postVoteCount = (TextView) rootView.findViewById(R.id.postVoteCount);
                                postVoteCount.setText(post.get("votes").toString()+" votes");



                                API.getPatch(getActivity(), post.get("username").toString(), new Callback() {
                                    public void onResponse(Bitmap img) {
                                        ImageView userPatch = (ImageView) rootView.findViewById(R.id.queProfilePic);
                                        userPatch.setImageBitmap(img);
                                    }
                                });
                            }else{

                                TextView title = (TextView) rootView.findViewById(R.id.postTitle);
                                title.setText("Oh no!");
                                TextView content = (TextView) rootView.findViewById(R.id.postContent);
                                content.setText("Your Que is empty :(");
                                TextView sender = (TextView) rootView.findViewById(R.id.postSender);
                                sender.setText("");
                                TextView postVoteCount = (TextView) rootView.findViewById(R.id.postVoteCount);
                                sender.setText("");
                                CardView userPatchHolder = (CardView) rootView.findViewById(R.id.queProfilePicHolder);
                                userPatchHolder.setVisibility(View.GONE);
                                ImageView userPatch = (ImageView) rootView.findViewById(R.id.queProfilePic);
                                userPatch.setVisibility(View.GONE);
                            }


                        }else{
                            Util.showDialog(getActivity(),response.getString("message"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                };
            });
        }


        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.voteGood:
                    //Util.showDialog(getActivity(),"GOOD!");
                    Data.lastVote=1;
                    loadPost();
                    return;
                case R.id.voteBad:
                    Data.lastVote=0;
                    loadPost();
                    return;
            }
        }


        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);


        }

    public void myClickMethod(View v) {

    }



}
