package com.afzaln.swipecards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

/**
 * Created by afzal on 2017-09-06.
 */

public class CardView extends View {
    public static final int MAX_ROTATION = 30;
    public static final double PROGRESS_THRESHOLD = 0.3;

    private float dx;
    private float dy;
    private float initialX;
    private float initialY;

    private float lastProgress;
    private boolean isSwipingAway;

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
                lastProgress = 0;
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getRawX() + dx;
                float dragDistance = newX - initialX;
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                float progress = Math.min(Math.max(dragDistance / screenWidth, -1), 1);

                if (isSwipingAway) {
                    if (progress > PROGRESS_THRESHOLD) {
                        swipeRight();
                        Toast.makeText(getContext(), "Swipe right", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (progress < -PROGRESS_THRESHOLD) {
                        swipeLeft();
                        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                resetPosition();

                break;
            case MotionEvent.ACTION_MOVE:
                newX = event.getRawX() + dx;
                float newY = event.getRawY() + dy;

                screenWidth = getResources().getDisplayMetrics().widthPixels;

                dragDistance = newX - initialX;
                progress = Math.min(Math.max(dragDistance / screenWidth, -1), 1);
                isSwipingAway = Math.abs(progress) > Math.abs(lastProgress);
                lastProgress = progress;

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

    private void resetPosition() {
        animate()
                .x(initialX)
                .y(initialY)
                .rotation(0)
                .setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private void swipeLeft() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        animate()
                .x(-screenWidth)
                .rotation(MAX_ROTATION)
                .alpha(0f)
                .setDuration(150);
    }

    private void swipeRight() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        animate()
                .x(screenWidth)
                .rotation(MAX_ROTATION)
                .alpha(0f)
                .setDuration(150);
    }
}
