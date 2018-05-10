package com.fourteenfourhundred.critique.Framework.Models;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.UI.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

public class EmptyPost extends Post{

    SwipeRefreshLayout swipeRefreshLayout;
    QueueFragment queue;

    public EmptyPost(QueueFragment queue) {
        super("none","Oh no!","none","empty",new JSONObject(),"Your queue is empty!");
        this.queue=queue;
    }

    public void stopLoad(){
        swipeRefreshLayout.setRefreshing(false);
    }

    public void inflateView(API api, Context context){
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
