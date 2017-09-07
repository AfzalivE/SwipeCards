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

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

                animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }

        return true;

    }
}
