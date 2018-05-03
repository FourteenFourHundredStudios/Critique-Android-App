package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fourteenfourhundred.critique.storage.Data;

import java.util.ArrayList;
import java.util.List;


public class RecycleViewManager {


    RecyclerView view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List data;

    public RecycleViewManager(RecyclerView view, Context context, RecyclerView.Adapter adapter,List data){
        this.view = view ;


        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        view.setLayoutManager(layoutManager);
        this.adapter=adapter;
        view.setAdapter(adapter);

        this.data=data;

        view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }



    public void update(final ArrayList newData){

        final User.UserDiffCallback diffCallback = new User.UserDiffCallback((ArrayList<User>) data, newData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        data.clear();
        data.addAll(newData);
        diffResult.dispatchUpdatesTo(adapter);

    }



}



