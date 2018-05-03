package com.fourteenfourhundred.critique.UI.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarContainer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.UI.Activities.MutualFinderActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

public class ActionBarView extends View {

    View[] rootView=new View[3];
    LayoutInflater inflater;
    HomeActivity activity;

    public static final int HOME=0;
    public static final int SEARCH=1;
    public static final int PROFILE=2;

    Intent searchIntent;


    public ActionBarView(Context context,HomeActivity activity) {
        super(context);
        this.activity=activity;

        inflater = LayoutInflater.from(context);
        rootView[HOME] = inflater.inflate(R.layout.action_bar_post, null, false);


        searchIntent = new Intent(getContext().getApplicationContext(), MutualFinderActivity.class);


    }



    public View getView(int position){


        if(rootView[position]==null){
            //there's no case for rootView[0] because rootView[0] is inflated when the class is created
            int viewId=0;
            switch (position){
                case SEARCH:
                    viewId=R.layout.action_bar_search;
                    break;
                case PROFILE:
                    viewId=R.layout.action_bar_search;
                    break;
            }
            rootView[position] = inflater.inflate(viewId, null, false);
            initBar(position);


        }



        return rootView[position];
    }


    public void initBar(final int position){

        rootView[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (position) {
                    case SEARCH:

                        activity.startActivity(searchIntent);
                        activity.overridePendingTransition(0,0);
                        break;
                }




            }
        });

    }


    public boolean onTouchEvent(MotionEvent e){
        //Util.showDialog(getContext(),"Test");
        return true;
    }


}
