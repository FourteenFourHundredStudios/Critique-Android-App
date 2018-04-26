package com.fourteenfourhundred.critique.UI.Views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class OneFluidMotionView extends FrameLayout {


    int mLastY;
    int lastX;

    //public int motionCount=0;
    boolean canChangeSlection=false;

    boolean locked=false;

    public OneFluidMotionView(Context context) {
        super(context);


    }




    public OneFluidMotionView(Context context, AttributeSet attrs) {
        super(context, attrs);


        this.post(new Runnable() {
            @Override
            public void run() {
                getChildAt(0).setZ(-10);
                getSubChild().setVisibility(INVISIBLE);
              //  getSubChild().setFluidMotionSelctionListener(onSelction());

            }
        });



    }


    public void setFluidMotionSelectionListener(FluidMotionOptions.FluidMotionSelctionListener selection){

        getSubChild().setFluidMotionSelctionListener(selection);
    }


    public FluidMotionOptions.FluidMotionSelctionListener onSelction(){

        return null;
    }


    public void setLocked(boolean state){
        //canChangeSlection=state;
        locked=state;
    }





    public FluidMotionOptions getSubChild(){
        return (FluidMotionOptions) getChildAt(0);
    }

    public View getMainChild(){
        return  getChildAt(1);
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {

        if(locked)return true;

        getSubChild().changeButtonSelection();

        int y = (int) event.getY();
        getSubChild().setVisibility(VISIBLE);
        switch (event.getAction()) {



            // the gesture is released
            case MotionEvent.ACTION_UP:
                //lastX=(int)event.getX();
                lastX=(int)event.getX();
                reset();
                break;
            case MotionEvent.ACTION_DOWN:

                mLastY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                //changeHeaderPadding(event);
                int histSize = event.getHistorySize();




                if(canChangeSlection) {


                    int dragThreshold=70;


                    if (lastX - event.getX() > dragThreshold) {
                        getSubChild().selectLast();

                        lastX=(int)event.getX();
                    } else if (lastX - event.getX() < -dragThreshold) {
                        getSubChild().selectNext();

                        lastX=(int)event.getX();

                    }




                }


                for (int i = 0; i < histSize; i++) {
                    int histY = (int) event.getHistoricalY(i);


                        int topPadding = ((histY - mLastY)) / 2;
                        if (((histY - mLastY)) * 2 > 0) {
                            if (topPadding > getSubChild().getHeight()) {
                                topPadding = getSubChild().getHeight();

                                if(!canChangeSlection){

                                        lastX=(int)event.getX();
                                        canChangeSlection=true;

                                }


                            }
                            MarginLayoutParams p = (MarginLayoutParams) getMainChild().getLayoutParams();
                            p.setMargins(0, topPadding, 0, 0);

                            requestLayout();
                        }
                    }


                break;
        }
        return true;
    }



    public void reset(){
        final MarginLayoutParams p = (MarginLayoutParams)getMainChild().getLayoutParams();
        canChangeSlection=false;


        ValueAnimator animator = ValueAnimator.ofInt(getMainChild().getTop(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                p.setMargins(0, (Integer) valueAnimator.getAnimatedValue(), 0, 0);
                requestLayout();

            }
        });
        animator.setDuration(100);
        animator.start();


        getSubChild().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubChild().setVisibility(INVISIBLE);
                getSubChild().castVote();
            }
        },110);



    }



}
