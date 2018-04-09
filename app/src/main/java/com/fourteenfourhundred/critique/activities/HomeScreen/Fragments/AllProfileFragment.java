package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.views.PostItemView;

import org.json.JSONArray;
import org.json.JSONObject;

public class AllProfileFragment extends Fragment {

    View rootView;
    LinearLayout content;
    API api;


    public AllProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_all_profile, container, false);
        content = rootView.findViewById(R.id.allFragmentContent);

        api=new API(getActivity());

        API.getArchive(getActivity(),new Util.Callback(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray archive = response.getJSONArray("archive");
                    for(int i=0;i<archive.length();i++){
                        String post = archive.getJSONObject(i).toString();

                        PostItemView item = new PostItemView(getContext(),api,post);
                        //HorizontalDivider hd = new HorizontalDivider();

                        content.addView(item.getSelf());

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
