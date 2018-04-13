package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.API.ApiRequest;
import com.fourteenfourhundred.critique.profile.fragments.BestProfileFragment;
import com.fourteenfourhundred.critique.profile.fragments.MeProfileFragment;
import com.fourteenfourhundred.critique.profile.fragments.AllProfileFragment;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;


public class ProfileFragment extends LoadOnViewFragment {

    API api;
    public int page=0;
    public static int loadingStages=0;


    public void setupFragment() {


        api = new API(getActivity());

        new ApiRequest.GetPatchRequest(api,"self").execute(new Util.Callback() {
            public void onResponse(Bitmap img){
                ImageView userPatch = (ImageView)rootView.findViewById(R.id.userPatch);
                userPatch.setImageBitmap(img);



                final ViewPager viewPager = rootView.findViewById(R.id.profilePager);
                final TabLayout tablayout = rootView.findViewById(R.id.tab_layout);

                viewPager.setAdapter(new SectionPagerAdapter(getFragmentManager()));
                tablayout.setupWithViewPager(viewPager);

                //ProfileFragment.updateLoadingStages();

                viewPager.setOffscreenPageLimit(5);
                onFinishedRendering();

            }
        });




    }



    public int getLayout(){
        return R.layout.fragment_profile;
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
