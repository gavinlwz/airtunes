package com.mycompany.airtunes;

import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.content.Context;
import android.view.View;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * Created by jessicadoheekim on 4/21/16.
 */
public class OnTouchListenerSwipeTap implements OnTouchListener {

    private GestureDetector gestureDetector;

    public OnTouchListenerSwipeTap(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            OnTouchListenerSwipeTap.this.onDoubleTap(e);
            return super.onDoubleTap(e);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onDoubleTap(MotionEvent e) {
        // To be overridden when implementing listener
    }

    public void onSwipeRight() {
        // overridden in PlaylistActivity
    }

    public void onSwipeLeft() {
        // overridden in PlaylistActivity
    }

    public void onSwipeUp() {
        // overridden in PlaylistActivity
    }

    public void onSwipeDown() {
        // overridden in PlaylistActivity
    }


}
