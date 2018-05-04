package com.fourteenfourhundred.critique.UI.Fragments;


import android.graphics.Bitmap;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.UI.Fragments.BestProfileFragment;
import com.fourteenfourhundred.critique.UI.Fragments.LoadOnViewFragment;
import com.fourteenfourhundred.critique.UI.Fragments.MeProfileFragment;
import com.fourteenfourhundred.critique.UI.Fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;


public class ProfileFragment extends HomeFragment {

    API api;
    public int page=0;
    public static int loadingStages=0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        super.onCreateView(inflater,container,savedInstanceState);


        api = new API(getActivity());




        ((TextView)getCritiqueBar().findViewById(R.id.username)).setText(Data.getUsername());

        ((TextView)rootView.findViewById(R.id.scoreText)).setText("Score of "+Data.getScore());

       // ((NestedScrollView)rootView.findViewById(R.id.scroll)).setFillViewport(true);

        new ApiRequest.GetPatchRequest(api,"self").execute(new Util.Callback() {
            public void onResponse(Bitmap img){
                ImageView userPatch = (ImageView)rootView.findViewById(R.id.userPatch);
                userPatch.setImageBitmap(img);



                final ViewPager viewPager = rootView.findViewById(R.id.profilePager);
                final TabLayout tablayout = rootView.findViewById(R.id.tab_layout);

                //viewPager.setAdapter(new SectionPagerAdapter(getFragmentManager()));
               // tablayout.setupWithViewPager(viewPager);



                viewPager.setOffscreenPageLimit(5);
                //onFinishedRendering();

            }
        });


        return rootView;

    }



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
