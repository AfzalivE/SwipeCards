package com.afzaln.swipecards;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import timber.log.Timber;

/**
 * Created by afzal on 2017-09-20.
 */

public class SwipeCardsLayout extends LinearLayout {
    private static final int NUM_STACKED_VIEWS = 5;
    private static final int DEFAULT_ROTATION = 8;
    private static final double DEFAULT_SCALE_FACTOR = 1f;
    private static final long DEFAULT_ANIMATION_DURATION = 300;

    private Adapter adapter;
    private DataSetObserver dataObserver;
    private int currentViewIndex;
    private SwipeCardsTouchListener touchListener;
    private View topView;
    private Random mRandom;
    private boolean isFirstLayout = true;

    public SwipeCardsLayout(Context context) {
        this(context, null);
    }

    public SwipeCardsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeCardsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dataObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                invalidate();
                requestLayout();
            }
        };

        touchListener = new SwipeCardsTouchListener(this);
        mRandom = new Random();
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataObserver);
        }
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(dataObserver);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // don't display anything if adapter isn't set or is empty
        if (adapter == null || adapter.isEmpty()) {
            removeAllViewsInLayout();
            return;
        }

        for (int i = getChildCount(); i < NUM_STACKED_VIEWS && currentViewIndex < adapter.getCount(); i++) {
            addNextView(currentViewIndex);
            currentViewIndex++;
        }

        layoutChildren();

        isFirstLayout = false;
    }

    private void addNextView(int currentViewIndex) {
        View bottomView = adapter.getView(currentViewIndex, null, this);
        bottomView.setTag(R.id.new_view);

        int width = bottomView.getWidth() - (bottomView.getPaddingLeft() + bottomView.getPaddingRight());
        int height = bottomView.getHeight() - (bottomView.getPaddingTop() + bottomView.getPaddingBottom());

        if (DEFAULT_ROTATION > 0) {
            bottomView.setRotation(mRandom.nextInt(DEFAULT_ROTATION) - (DEFAULT_ROTATION / 2));
        }

        LinearLayout.LayoutParams params = (LayoutParams) bottomView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        int measureSpecWidth = MeasureSpec.AT_MOST;
        int measureSpecHeight = MeasureSpec.AT_MOST;

        if (params.width == LayoutParams.MATCH_PARENT) {
            measureSpecWidth = MeasureSpec.EXACTLY;
        }

        if (params.height == LayoutParams.MATCH_PARENT) {
            measureSpecHeight = MeasureSpec.EXACTLY;
        }

        bottomView.measure(measureSpecWidth | width, measureSpecHeight | height);

        final int childWidthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        final int childHeightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);

        bottomView.measure(childWidthSpec, childHeightSpec);
        addViewInLayout(bottomView, 0, params, true);


    }

    private void layoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int topViewIndex = getChildCount() - 1;
            Timber.d("Topview index" + topViewIndex);
            Timber.d("view" + ((TextView) childView.findViewById(R.id.name)).getText().toString());

            int distanceToViewAbove = (topViewIndex) - (i);
            int newPositionX = (getWidth() - childView.getMeasuredWidth()) / 2;
            int newPositionY = distanceToViewAbove + getPaddingTop();

            Rect tmpChildRectOut = new Rect(newPositionX,
                    newPositionY,
                    newPositionX + childView.getMeasuredWidth(),
                    newPositionY + childView.getMeasuredHeight());

            Rect tmpParentRectIn = new Rect(getLeft(), getTop(), getRight(), getBottom());

            Gravity.apply(Gravity.CENTER, childView.getMeasuredWidth(), childView.getMeasuredHeight(), tmpParentRectIn, tmpChildRectOut);
            childView.layout(tmpChildRectOut.left, tmpChildRectOut.top, tmpChildRectOut.right, tmpChildRectOut.bottom);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                childView.setTranslationZ(i);
            }

            boolean isNewView = childView.getTag(R.id.new_view) == null;
            float scaleFactor = (float) Math.pow(DEFAULT_SCALE_FACTOR, getChildCount() - 1);

            if (i == topViewIndex) {
                touchListener.unregisterObservedView();
                childView.animate()
                        .rotation(0)
                        .setDuration(300);
                topView = childView;
                touchListener.registerObservedView(topView, tmpChildRectOut.left, tmpChildRectOut.top);
            }

            if (!isFirstLayout) {
                if (isNewView) {
                    childView.setTag(R.id.new_view, false);
                    childView.setAlpha(0);
                    childView.setY(tmpChildRectOut.top);
                    childView.setScaleY(scaleFactor);
                    childView.setScaleX(scaleFactor);
                }

                childView.animate()
                        .y(tmpChildRectOut.top)
                        .scaleX(scaleFactor)
                        .scaleY(scaleFactor)
                        .alpha(1)
                        .setDuration(DEFAULT_ANIMATION_DURATION);
            } else {
                childView.setTag(R.id.new_view, false);
                childView.setY(tmpChildRectOut.top);
                childView.setScaleY(scaleFactor);
                childView.setScaleX(scaleFactor);
            }
        }
    }

    public void onViewSwipedToRight() {
        removeTopView();
    }

    public void onViewSwipedToLeft() {
        removeTopView();
    }

    private void removeTopView() {
        if (topView != null) {
            removeView(topView);
            topView = null;
        }

        // TODO put almost empty notification here
//        if (getChildCount() == 0) {
//            if (mListener != null) mListener.onStackEmpty();
//        }
    }
}
