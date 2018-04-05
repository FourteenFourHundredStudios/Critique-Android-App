package com.fourteenfourhundred.critique.views;

import android.content.Context;
import android.view.LayoutInflater;

import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

public class EmptyView extends PostView{


    public EmptyView(Context context) {
        super(context,null ,"{}");
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.fragment_empty_post, null, false);
    }

}
