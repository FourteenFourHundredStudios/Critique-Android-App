package critique.fourteenfourhundred.marc.critique;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuView;
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
        public QueFragment(){


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_que, container, false);



            ImageButton voteBad = (ImageButton) rootView.findViewById(R.id.voteBad);
            voteBad.setOnClickListener(this);

            ImageButton voteGood = (ImageButton) rootView.findViewById(R.id.voteGood);
            voteGood.setOnClickListener(this);


            API.getQue(getActivity(),new Response.Listener<JSONObject>(){
                public void onResponse(JSONObject response) {
                    try {

                        if(response.get("status").equals("ok")) {
                            JSONObject post= new JSONObject(new JSONArray(response.get("message").toString()).get(0).toString());

                            Util.showDialog(getActivity(),post.toString());

                            TextView title = (TextView) rootView.findViewById(R.id.postTitle);
                            title.setText(post.get("title").toString());
                            TextView content = (TextView) rootView.findViewById(R.id.postContent);
                            content.setText(post.get("content").toString());
                            TextView sender = (TextView) rootView.findViewById(R.id.postSender);
                            sender.setText(post.get("username").toString());
                        }else{
                            Util.showDialog(getActivity(),response.getString("message"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                };
            });


            return rootView;
        }


        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.voteGood:
                    Util.showDialog(getActivity(),"GOOD!");
                    return;
                case R.id.voteBad:
                    Util.showDialog(getActivity(),"BAD!");
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
