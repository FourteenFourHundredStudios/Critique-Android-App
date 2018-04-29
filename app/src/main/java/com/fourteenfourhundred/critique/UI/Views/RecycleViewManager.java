package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class RecycleViewManager {


    RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public RecycleViewManager(RecyclerView view, Context context, RecyclerView.Adapter adapter){
        this.view = view ;

        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        view.setLayoutManager(layoutManager);
        this.adapter=adapter;
        view.setAdapter(adapter);
    }


}



