package com.fourteenfourhundred.critique.views;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.activities.HomeScreen.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.util.Util;

import org.json.JSONObject;

public class EmptyView extends PostView{

    SwipeRefreshLayout swipeRefreshLayout;
    QueueFragment queue;
    public EmptyView(Context context, QueueFragment queue) {
        super(context,null ,"{}");
        this.queue=queue;
    }

    public void stopLoad(){
        swipeRefreshLayout.setRefreshing(false);
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.fragment_empty_post, null, false);

        swipeRefreshLayout=rootView.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {


                        if(queue!=null)queue.renderNextPost(false);



                    }
                }
        );

    }



}
