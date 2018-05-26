package com.fourteenfourhundred.critique.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {


      public static void slideUp(View view, android.view.animation.Animation.AnimationListener al){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,((ViewGroup)view.getParent()).getHeight(),0);
        animate.setDuration(200);
        animate.setFillAfter(true);
        animate.setAnimationListener(al);
        view.startAnimation(animate);
    }

    public static void fadeOut(final View view, final Callback.Response callback){


        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                view.setVisibility(View.GONE);
               //
                if(callback!=null)callback.onResponse(null);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        //alphaAnimation.setRepeatCount(1);
        //alphaAnimation.setRepeatMode(Animation.REVERSE);

        view.startAnimation(alphaAnimation);
    }




    public static void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(100);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }



}
