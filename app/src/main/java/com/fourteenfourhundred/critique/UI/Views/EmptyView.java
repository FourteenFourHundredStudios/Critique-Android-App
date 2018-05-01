package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;

import com.fourteenfourhundred.critique.UI.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

public class EmptyView extends PostView {

    SwipeRefreshLayout swipeRefreshLayout;
    QueueFragment queue;
    public EmptyView(Context context, QueueFragment queue) {
        super(context,null ,"{\"title\":\"Whoops!\"}");
        this.queue=queue;
    }

    @Override
    public JSONObject getPost() {
        return new JSONObject();
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
