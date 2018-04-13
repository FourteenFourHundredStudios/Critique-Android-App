package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fourteenfourhundred.critique.critique.R;

import static com.fourteenfourhundred.critique.critique.R.layout.fragment_friends;

public class LoadOnViewFragment extends Fragment {

    /*
        LoadOnViewFragment Requires the body of your layout XML to wrapped in
        2 RelativeLayouts with the innermost RelativeLayout having an Id of home_fragment_container

     */

    public View rootView;
    public boolean isFirstRender=true;
    public RelativeLayout content;
    public ProgressBar loading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(getLayout(), container, false);

        content = rootView.findViewById(R.id.home_fragment_container);
        content.setVisibility(View.GONE);

        loading = new ProgressBar(getContext());

        ((RelativeLayout)content.getParent()).addView(loading);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)loading.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        loading.setLayoutParams(layoutParams);

        return rootView;
    }

    public int getLayout(){
        return -1;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isFirstRender){
            setupFragment();
        }
        if(isVisibleToUser){
            isFirstRender=false;
        }
    }


    public void setupFragment(){

    }

    public void onFinishedRendering(){
        loading.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }



}
