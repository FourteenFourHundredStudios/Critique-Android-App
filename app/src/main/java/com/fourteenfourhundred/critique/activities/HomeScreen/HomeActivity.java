package com.fourteenfourhundred.critique.activities.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.activities.ComposeActivity;
import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.FriendsFragment;
import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.ProfileFragment;
import com.fourteenfourhundred.critique.activities.HomeScreen.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONObject;

import java.io.IOException;


public class HomeActivity extends AppCompatActivity {


    ViewPager viewPager;
    ImageView syncIcon;
    Menu menu;
    Animation loadAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.pager);


        viewPager.setOffscreenPageLimit(5);


        // Create an adapter that knows which fragment should be shown on each page
        HomePageAdapter adapter = new HomePageAdapter(this, getSupportFragmentManager());

        loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        /*
        viewPager.setFocusableInTouchMode(true);
        viewPager.setFocusable(true);
        viewPager.setEnabled(true);
        viewPager.setClickable(true);
        viewPager.setFocusableInTouchMode(true);*/

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);


        //tabLayout.setSelectedTabIndicatorHeight(0);

        //setProgressBarIndeterminateVisibility(Boolean.TRUE);


    }


    public void stopLoadAnimation() {
        if(syncIcon==null)return;
        syncIcon.setVisibility(View.INVISIBLE);
        loadAnimation.cancel();


    }

    public void startLoadAnimation() {
        if(menu!=null || syncIcon!=null) {

            syncIcon.setVisibility(View.VISIBLE);


            loadAnimation.setRepeatCount(Animation.INFINITE);
            syncIcon.startAnimation(loadAnimation);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.home_menu, menu);
        this.menu=menu;

        syncIcon = (ImageView) menu.findItem(R.id.action_syncing).getActionView();
        syncIcon.setImageResource(R.drawable.loading_symbol);

        //startLoadAnimation();

        //LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*
        syncIcon = (ImageView) menu.findItem(R.id.action_syncing).getActionView();
        syncIcon.setImageResource(R.drawable.loading_symbol);
        syncIcon.setVisibility(View.VISIBLE);


        Animation f = AnimationUtils.loadAnimation(this, R.anim.rotate);
        f.setRepeatCount(Animation.INFINITE);
        syncIcon.startAnimation(f);*/
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openCompose:

                Log.e("ok","fff");
                Intent intent = new Intent(getApplicationContext(), ComposeActivity.class);
                startActivity(intent);
                //viewPager.setCurrentItem(1, true);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectPatch(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (resultCode != RESULT_OK) return;

        switch (requestCode) {

            case 1:
                if (data != null) {


                    Uri path = data.getData();

                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                        API.changePatch(HomeActivity.this,bitmap,new Util.Callback(){
                            public void onResponse(JSONObject response){
                                try {
                                    if (response.get("status").equals("ok")) {
                                        setProfilePatch(bitmap);
                                    }else{
                                        Util.showDialog(HomeActivity.this,response.getString("message"));
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

    }


    public void setProfilePatch(Bitmap img){

        ImageView f = (ImageView) findViewById(R.id.userPatch);
        f.setImageBitmap(img);
    }




    public class HomePageAdapter extends FragmentStatePagerAdapter {
        private Context mContext;



        public HomePageAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            //  ProfileFragment tab1 = new ProfileFragment();

            switch (position) {
                case 0:
                    return new QueueFragment();
                case 1:
                    return new FriendsFragment();
                case 2:
                    return new ProfileFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return 3;
        }


    }




}


