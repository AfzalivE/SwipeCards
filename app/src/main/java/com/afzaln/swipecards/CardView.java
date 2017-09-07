package com.afzaln.swipecards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by afzal on 2017-09-06.
 */

public class CardView extends View {
    public static final int MAX_ROTATION = 30;

    private float dx;
    private float dy;
    private float initialX;
    private float initialY;

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initialX = getX();
        initialY = getY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                dx = getX() - event.getRawX();
                dy = getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + dx;
                float newY = event.getRawY() + dy;

                int screenWidth = getResources().getDisplayMetrics().widthPixels;

                float dragDistance = newX - initialX;
                float progress = Math.min(Math.max(dragDistance / screenWidth, -1), 1);

                animate()
                        .x(newX)
                        .y(newY)
                        .rotation(MAX_ROTATION * progress)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }

        return true;

    }
}
