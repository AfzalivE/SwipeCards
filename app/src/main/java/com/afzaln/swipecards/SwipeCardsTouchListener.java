package com.afzaln.swipecards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import timber.log.Timber;

/**
 * Created by afzal on 2017-09-20.
 */

public class SwipeCardsTouchListener implements View.OnTouchListener {
    private static final int MAX_ROTATION = 30;
    private static final double PROGRESS_THRESHOLD = 0.3;

    private float dx;
    private float dy;
    private float initialX;
    private float initialY;

    private float lastProgress;
    private boolean isSwipingAway;
    private View observedView;
    private boolean animationEnded;

    private SwipeCardsLayout swipeCardsLayout;

    public SwipeCardsTouchListener(SwipeCardsLayout swipeCardsLayout) {

        this.swipeCardsLayout = swipeCardsLayout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                dx = view.getX() - event.getRawX();
                dy = view.getY() - event.getRawY();
                lastProgress = 0;
                break;
            }
            case MotionEvent.ACTION_UP: {
                float newX = event.getRawX() + dx;
                float dragDistance = newX - initialX;
                int screenWidth = view.getResources().getDisplayMetrics().widthPixels;
                float progress = Math.min(Math.max(dragDistance / screenWidth, -1), 1);

                if (isSwipingAway) {
                    if (progress > PROGRESS_THRESHOLD) {
                        Timber.d("Swipe right detected");
                        swipeRight();
                        Toast.makeText(view.getContext(), "Swipe right", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (progress < -PROGRESS_THRESHOLD) {
                        Timber.d("Swipe left detected");
                        swipeLeft();
                        Toast.makeText(view.getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                resetPosition();

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float newX = event.getRawX() + dx;
                float newY = event.getRawY() + dy;
                float dragDistance = newX - initialX;
                int screenWidth = view.getResources().getDisplayMetrics().widthPixels;

                float progress = Math.min(Math.max(dragDistance / screenWidth, -1), 1);
                isSwipingAway = Math.abs(progress) > Math.abs(lastProgress);
                lastProgress = progress;

                view.animate()
                        .x(newX)
                        .y(newY)
                        .rotation(MAX_ROTATION * progress)
                        .setDuration(0)
                        .start();
                break;
            }
            default:
                return false;
        }

        return true;
    }

    private void resetPosition() {
        observedView.animate()
                .x(initialX)
                .y(initialY)
                .rotation(0)
                .setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private AnimatorListenerAdapter swipeLeftAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animator) {
            if (!animationEnded) {
                Timber.d("Animated ended, swiping right");
                animationEnded = true;
                swipeCardsLayout.onViewSwipedToLeft();
            }
        }
    };

    private void swipeLeft() {
        int screenWidth = observedView.getResources().getDisplayMetrics().widthPixels;

        animationEnded = false;
        observedView.animate()
                .x(-screenWidth)
                .rotation(MAX_ROTATION)
                .alpha(0f)
                .setDuration(150)
                .setListener(swipeLeftAnimationListener);
    }

    private AnimatorListenerAdapter swipeRightAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animator) {
            if (!animationEnded) {
                Timber.d("Animated ended, swiping right");
                animationEnded = true;
                swipeCardsLayout.onViewSwipedToRight();
            }
        }
    };

    private void swipeRight() {
        int screenWidth = observedView.getResources().getDisplayMetrics().widthPixels;

        animationEnded = false;
        observedView.animate()
                .x(screenWidth)
                .rotation(MAX_ROTATION)
                .alpha(0f)
                .setDuration(150)
                .setListener(swipeRightAnimationListener);
    }

    public void unregisterObservedView() {
        if (observedView != null) {
            observedView.setOnTouchListener(null);
        }
        observedView = null;
    }

    public void registerObservedView(View view, float initialX, float initialY) {
        if (view == null) {
            return;
        }

        observedView = view;
        observedView.setOnTouchListener(this);
        this.initialX = initialX;
        this.initialY = initialY;
    }
}
