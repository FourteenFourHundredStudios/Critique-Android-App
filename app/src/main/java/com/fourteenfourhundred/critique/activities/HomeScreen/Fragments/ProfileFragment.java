package com.fourteenfourhundred.critique.activities.HomeScreen.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.util.Util.Callback;
import com.fourteenfourhundred.critique.critique.R;


public class ProfileFragment extends Fragment {

    View rootView;
    API api;


    public ProfileFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        api = new API(getActivity());

        api.getPatch(getActivity(),"self",new Callback(){
            public void onResponse(Bitmap img){
                ImageView userPatch = (ImageView)rootView.findViewById(R.id.userPatch);
                userPatch.setImageBitmap(img);
            }
        });

        ViewPager viewPager = rootView.findViewById(R.id.profilePager);
        TabLayout tablayout = rootView.findViewById(R.id.tab_layout);

        viewPager.setAdapter(new SectionPagerAdapter(getFragmentManager()));
        tablayout.setupWithViewPager(viewPager);


        return rootView;
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
                    return new AllProfileFragment();
                case 2:
                    return new AllProfileFragment();
                default:
                    return new AllProfileFragment();

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

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("here","fffff");

    }
}
