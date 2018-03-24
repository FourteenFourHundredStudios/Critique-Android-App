package critique.fourteenfourhundred.marc.critique;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Marc on 23/3/18.
 */

public class DeactivatedViewPager extends ViewPager {

    @Override
    public boolean executeKeyEvent(KeyEvent event)
    {
        return isPagingEnabled ? super.executeKeyEvent(event) : false;
    }



    private boolean isPagingEnabled = true;



    public DeactivatedViewPager(Context context) {
        super(context);
    }

    public DeactivatedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}