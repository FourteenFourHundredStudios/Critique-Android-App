package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

public class ActionBarView extends View {

    View[] rootView=new View[3];
    LayoutInflater inflater;


    public ActionBarView(Context context) {
        super(context);

        inflater = LayoutInflater.from(context);
        rootView[0] = inflater.inflate(R.layout.action_bar_post, null, false);

    }



    public View getView(int position){


        if(rootView[position]==null){
            //there's no case for rootView[0] because rootView[0] is inflated when the class is created
            int viewId=0;
            switch (position){
                case 1:
                    viewId=R.layout.action_bar_search;
                    break;
                case 2:
                    viewId=R.layout.action_bar_search;
                    break;
            }
            rootView[position] = inflater.inflate(viewId, null, false);
        }



        return rootView[position];
    }




    public boolean onTouchEvent(MotionEvent e){
        Util.showDialog(getContext(),"Test");
        return true;
    }


}
