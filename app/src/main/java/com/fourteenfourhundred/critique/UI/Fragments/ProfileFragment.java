package com.fourteenfourhundred.critique.UI.Fragments;


import android.graphics.Bitmap;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.Framework.API.API;
import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Callback;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;


public class ProfileFragment extends HomeFragment {

    API api;
    public int page=0;
    public static int loadingStages=0;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        super.onCreateView(inflater, container, savedInstanceState);



        api = new API(getActivity());


        final ViewPager viewPager = rootView.findViewById(R.id.profilePager);
        final TabLayout tablayout = rootView.findViewById(R.id.tab_layout);

        SectionPagerAdapter adapter = new SectionPagerAdapter(getFragmentManager());


        //viewPager.setAdapter(adapter);
        //tablayout.setupWithViewPager(viewPager);
        // tablayout.setupWithViewPager(viewPager);

//        adapter.addFragment(new FragmentOne(), "FRAG1");
        //      adapter.addFragment(new FragmentTwo(), "FRAG2");
        //     adapter.addFragment(new FragmentThree(), "FRAG3");
        // viewPager.setAdapter(adapter);


        //viewPager.setAdapter(new SectionPagerAdapter(getFragmentManager()));
        // tablayout.setupWithViewPager(viewPager);

        ((TextView) getCritiqueBar().findViewById(R.id.username)).setText(Data.getUsername()+"'s profile");

        //    ((TextView)rootView.findViewById(R.id.scoreText)).setText("Score of "+Data.getScore());

        // ((NestedScrollView)rootView.findViewById(R.id.scroll)).setFillViewport(true);


        viewPager.setAdapter(new SectionPagerAdapter(getFragmentManager()));
        tablayout.setupWithViewPager(viewPager);

        //ImageView userPatch;



        new ApiRequest.GetPatchRequest(api, Data.getUsername()).execute(  img -> {



            ImageView userPatch = rootView.findViewById(R.id.userPatch);
            userPatch.setImageBitmap((Bitmap) img);



            viewPager.setOffscreenPageLimit(5);


        }) ;


        return rootView;
    }

/*
        View.OnTouchListener mSuppressInterceptListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch




    }*/



    public int getToolbar(){
        return R.layout.action_bar_profile;
    }



    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AllProfileFragment();
                case 1:
                    return new BestProfileFragment();
                case 2:
                    return new MeProfileFragment();
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Best";
                case 2:
                    return "Me";
                default:
                    return null;
            }
        }


    }



    @Override
    public void onResume() {
        super.onResume();

    }
}
