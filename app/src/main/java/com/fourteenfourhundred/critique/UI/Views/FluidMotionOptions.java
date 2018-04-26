package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.widget.LinearLayout;

import com.fourteenfourhundred.critique.critique.R;

public class FluidMotionOptions extends LinearLayout {


    public int selected=1;
    Vibrator v;
    public FluidMotionSelctionListener callback;


    public FluidMotionOptions(Context context) {
        super(context);
    }

    public FluidMotionOptions(Context context, AttributeSet attrs) {
        super(context, attrs);


    }



    public void selectLast(){
       // v.vibrate(VibrationEffect.createOneShot(10, 255));

        selected--;

        if(selected>-1)performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

    }

    public void selectNext(){

        selected++;
        if(selected<getChildCount())performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    public void setFluidMotionSelctionListener(FluidMotionSelctionListener callback){
        this.callback=callback;
    }

    public void castVote(){
        if(callback!=null){
            callback.onSelection(selected);
        }
        selected=1;
    }


    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

    public void changeButtonSelection(){

        if(selected>getChildCount()-1){
            selected=getChildCount()-1;
        }else if(selected < 0){
            selected=0;
        }


        for(int i=0;i<getChildCount();i++){
            if(i==selected){
                getChildAt(i).setBackgroundColor(manipulateColor(getResources().getColor(R.color.colorAccent), 0.7f));
            }else{
                getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }


    }

    public static class FluidMotionSelctionListener{

        public FluidMotionSelctionListener(){

        }

        public void onSelection(int value){}
    }

}


