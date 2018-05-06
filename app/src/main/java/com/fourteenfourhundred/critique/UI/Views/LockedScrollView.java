package com.fourteenfourhundred.critique.UI.Views;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class LockedScrollView extends ScrollView{
    public LockedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
