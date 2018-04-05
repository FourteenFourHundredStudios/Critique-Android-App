package com.fourteenfourhundred.critique.API;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.views.EmptyView;
import com.fourteenfourhundred.critique.views.PostView;

public class QueueManagerService {

    public QueueFragment queueFragment;
    public Context context;


    public QueueManagerService(QueueFragment fragment){
        this.queueFragment=fragment;
        this.context=queueFragment.getActivity().getApplicationContext();


    }


    public void vote(int vote){

    }

    public PostView getNextPost(){
        queueFragment.setVoteLock(true);
        return new EmptyView(context);
    }

}
