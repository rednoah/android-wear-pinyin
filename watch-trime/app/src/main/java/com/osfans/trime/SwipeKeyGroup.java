package com.osfans.trime;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class SwipeKeyGroup extends CircleLayout {


    public SwipeKeyGroup(Context context) {
        this(context, null);
    }


    public SwipeKeyGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // do not forward events to children
        return true;
    }


    private MotionEvent swipe = null;
    private Rect swipeBounds = new Rect();


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                swipe = MotionEvent.obtain(event); // make sure we get a clone, not an instance that is being reused
                swipeBounds.set(0, 0, getWidth(), getHeight());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (swipe != null && !swipeBounds.contains(Math.round(event.getX()), Math.round(event.getY()))) {
                    onSwipe(swipe, event);
                    swipe = null;
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (swipe != null) {
                    onSwipe(swipe, event);
                    swipe = null;
                }
                return true;
        }

        return true;
    }


    protected void onSwipe(MotionEvent a, MotionEvent b) {
        // abort if mouse has not moved
        if (a.getX() == b.getX() && a.getY() == b.getY()) {
            return;
        }


        float bearing = bearing(a.getX(), -a.getY(), b.getX(), -b.getY());
        float anglePerChild = DEFAULT_ANGLE_RANGE / getChildCount();


        View child = getChildAt(Math.round(bearing / anglePerChild) % getChildCount());
        onSwipe(child);
    }


    protected void onSwipe(View child) {
        Button b = (Button) child;
        b.performClick();

        // play visual feedback
        b.setPressed(true);
        b.postDelayed(() -> b.setPressed(false), 100);
    }


    // Calculate angle between vector from (x1,y1) to (x2,y2) & +Y axis in degrees.
    // Essentially gives a compass reading, where N is 0 degrees and E is 90 degrees.
    private float bearing(float x1, float y1, float x2, float y2) {
        // x and y args to atan2() swapped to rotate resulting angle 90 degrees
        // (Thus angle in respect to +Y axis instead of +X axis)
        float angle = (float) Math.toDegrees(Math.atan2(x1 - x2, y2 - y1));

        // Ensure result is in interval [0, 360)
        // Subtract because positive degree angles go clockwise
        return (DEFAULT_ANGLE_RANGE - angle) % DEFAULT_ANGLE_RANGE;
    }


}
