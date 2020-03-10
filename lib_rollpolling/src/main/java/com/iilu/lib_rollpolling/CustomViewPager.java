package com.iilu.lib_rollpolling;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @name CustomViewPager
 */
public class CustomViewPager extends ViewPager {

    private boolean mIsPageEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }
        return this.mIsPageEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mIsPageEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagerEnabled(boolean isPageEnabled) {
        this.mIsPageEnabled = isPageEnabled;
    }
}
