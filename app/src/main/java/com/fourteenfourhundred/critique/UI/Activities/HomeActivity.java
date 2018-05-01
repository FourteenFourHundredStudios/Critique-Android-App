package com.fourteenfourhundred.critique.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fourteenfourhundred.critique.API.API;
import com.fourteenfourhundred.critique.UI.Fragments.MutualsFragment;
import com.fourteenfourhundred.critique.UI.Fragments.ProfileFragment;
import com.fourteenfourhundred.critique.UI.Fragments.QueueFragment;
import com.fourteenfourhundred.critique.UI.Views.ActionBarView;
import com.fourteenfourhundred.critique.storage.Data;
import com.fourteenfourhundred.critique.util.Util;
import com.fourteenfourhundred.critique.critique.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class HomeActivity extends AppCompatActivity {

    public JSONArray archive;
    public QueueFragment queue;

    public ViewPager viewPager;
    ImageView syncIcon;
    Menu menu;
    Animation loadAnimation;

    public ActionBarView actionBar;

    public int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.pager);


        viewPager.setOffscreenPageLimit(5);


        HomePageAdapter adapter = new HomePageAdapter(this, getSupportFragmentManager());

        loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        viewPager.setAdapter(adapter);


        Data.backgroundApi=new API(this);







        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar=new ActionBarView(getApplicationContext());



        actionBar.getView(0).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                //Util.showDialog(HomeActivity.this,"NUKING!");

                if(Data.debug) {
                    Data.nuke(HomeActivity.this);

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                    finish();
                }

                return false;
            }
        });

       changeActionBar(0);



    }


    public void changeActionBar(int position){
        getSupportActionBar().setCustomView(actionBar.getView(position));
    }



    public void addViewpagerListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                if(position!=0)queue.saveState();


                changeActionBar(position);

            }
        });
    }


    public void stopLoadAnimation() {
        if(syncIcon==null)return;
        syncIcon.setVisibility(View.GONE);
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
        syncIcon.setVisibility(View.INVISIBLE);
        syncIcon.setImageResource(R.drawable.loading_symbol);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openCompose:

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
            queue=new QueueFragment();
            switch (position) {
                case 0:
                    return queue;
                case 1:
                    return new MutualsFragment();
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


