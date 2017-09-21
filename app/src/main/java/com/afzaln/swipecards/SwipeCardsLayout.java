package com.afzaln.swipecards;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

/**
 * Created by afzal on 2017-09-20.
 */

public class SwipeCardsLayout extends ViewGroup {
    private static final int NUM_STACKED_VIEWS = 5;

    private Adapter adapter;
    private DataSetObserver dataObserver;
    private int currentViewIndex;
    private SwipeCardsTouchListener touchListener;
    private View mTopView;

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

        // draw all views in reverse order so that the first one is in the front
        for (int x = getChildCount(); x < NUM_STACKED_VIEWS && currentViewIndex < adapter.getCount(); x++) {
            addNextView(currentViewIndex);
            currentViewIndex++;
        }

        layoutChildren();
    }

    private void addNextView(int currentViewIndex) {
        View bottomView = adapter.getView(currentViewIndex, null, this);
        bottomView.setTag(R.id.new_view);

        int width = bottomView.getWidth() - (bottomView.getPaddingLeft() + bottomView.getPaddingRight());
        int height = bottomView.getHeight() - (bottomView.getPaddingTop() + bottomView.getPaddingBottom());

        LayoutParams params = bottomView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(params.width, params.height);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        int gravity = layoutParams.gravity;

        lParams.gravity = Gravity.CENTER;

        int measureSpecWidth = MeasureSpec.AT_MOST;
        int measureSpecHeight = MeasureSpec.AT_MOST;

        if (lParams.width == LayoutParams.MATCH_PARENT) {
            measureSpecWidth = MeasureSpec.EXACTLY;
        }

        if (lParams.height == LayoutParams.MATCH_PARENT) {
            measureSpecHeight = MeasureSpec.EXACTLY;
        }

        bottomView.measure(measureSpecWidth | width, measureSpecHeight | height);

        final int childWidthSpec = MeasureSpec.makeMeasureSpec(lParams.width, MeasureSpec.EXACTLY);
        final int childHeightSpec = MeasureSpec.makeMeasureSpec(lParams.height, MeasureSpec.EXACTLY);

        bottomView.measure(childWidthSpec, childHeightSpec);
        addViewInLayout(bottomView, 0, lParams, true);
    }

    private void layoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int topViewIndex = getChildCount() - 1;

            int distanceToViewAbove = (topViewIndex) - (i);
            int newPositionX = (getWidth() - childView.getMeasuredWidth()) / 2;
            int newPositionY = distanceToViewAbove + getPaddingTop();

            Rect tmpChildRectOut = new Rect(newPositionX,
                    newPositionY,
                    newPositionX + childView.getMeasuredWidth(),
                    newPositionY + childView.getMeasuredHeight());

            Rect tmpParentRectIn = new Rect(getLeft(), getTop(), getRight(), getBottom());

            La

            Gravity.apply(Gravity.CENTER, childView.getMeasuredWidth(), childView.getMeasuredHeight(), tmpParentRectIn, tmpChildRectOut);
            childView.layout(tmpChildRectOut.left, tmpChildRectOut.top, tmpChildRectOut.right, tmpChildRectOut.bottom);

            if (i == topViewIndex) {
                touchListener.unregisterObservedView();
                mTopView = childView;
                touchListener.registerObservedView(mTopView, tmpChildRectOut.left, tmpChildRectOut.top);
            }

            childView.animate()
                    .y(tmpChildRectOut.top)
                    .alpha(1)
                    .setDuration(300);
        }
    }

    public void onViewSwipedToRight() {
        removeTopView();
    }

    public void onViewSwipedToLeft() {
        removeTopView();
    }

    private void removeTopView() {
        if (mTopView != null) {
            removeView(mTopView);
            mTopView = null;
        }

        // TODO put almost empty notification here
//        if (getChildCount() == 0) {
//            if (mListener != null) mListener.onStackEmpty();
//        }
    }
}
