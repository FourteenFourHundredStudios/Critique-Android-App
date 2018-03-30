package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.critique.R;


public class ProfileFragment extends Fragment {

    View rootView;

    public ProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        API.getPatch(getActivity(),"self",new Callback(){
            public void onResponse(Bitmap img){
                ImageView userPatch = (ImageView)rootView.findViewById(R.id.userPatch);
                userPatch.setImageBitmap(img);
            }
        });



        return rootView;
    }




}
