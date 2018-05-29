package com.fourteenfourhundred.critique.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Activities.ComposeActivity;
import com.fourteenfourhundred.critique.UI.Activities.LoginActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;

public class HomeFragment extends Fragment {

    public View rootView;
    Toolbar critiqueBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedLayout= inflater.inflate(getToolbar(), null, false);
        critiqueBar=rootView.findViewById(R.id.critique_bar);
        critiqueBar.addView(inflatedLayout);
        critiqueBar.inflateMenu(R.menu.home_menu);

        critiqueBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionBarClicked();
            }
        });


        critiqueBar.setOnMenuItemClickListener(getOnMenuClickListener());


        if(Data.debug) {

            critiqueBar.setOnLongClickListener(view -> {

                Data.nuke(getActivity());

                return true;
            });
        }

        return rootView;
    }



    public Toolbar.OnMenuItemClickListener getOnMenuClickListener(){
        return new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.openCompose:

                        Intent intent = new Intent(getActivity().getApplicationContext(), ComposeActivity.class);
                        startActivity(intent);
                        return true;

                }
                return false;
            }

        };
    }

    public Toolbar getCritiqueBar(){
        return critiqueBar;
    }

    public void onActionBarClicked(){

    }

    public int getToolbar(){
        return -1;
    }

}
