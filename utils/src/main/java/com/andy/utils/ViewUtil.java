/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.utils;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * Created by lixn on 2017/6/13.
 */

public class ViewUtil {

    /**
     * 将一个AbsListView平滑滚动到指定位置（修复系统AbsListView#smoothScrollToPositionFromTop()方法的bug）
     * @param view
     * @param position
     */
    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // // There's no need to scroll if child is already at top or view is already scrolled to its end
        if (child != null && ((child.getTop() == 0))) {
            return;
        }
        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);
                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    /**
     * 获取ListView等适配器视图中某个位置的View
     * @param view
     * @param position
     * @return
     */
    public static View getChildAtPosition(final AdapterView view, final int position) {
        if (view == null) {
            return null;
        }
        final int index = position - view.getFirstVisiblePosition();
        if (index >= 0 && index < view.getChildCount()) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    /**
     * 设置View的触摸事件代理（如扩大按钮的点击范围）
     * @param view 需要代理的View
     * @param leftOffset 左偏移量（）
     * @param topOffset
     * @param rightOffset
     * @param bottomOffset
     */
    public static void enableTouchDelegate(final View view, final int leftOffset, final int topOffset, final int rightOffset, final int bottomOffset) {
        final View parentView = (View) view.getParent();
        if (parentView == null) {
            return;
        }
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.getHitRect(bounds);
                bounds.left += leftOffset;
                bounds.top += topOffset;
                bounds.right += rightOffset;
                bounds.bottom += bottomOffset;
                parentView.setTouchDelegate(new TouchDelegate(bounds, view));
            }
        });
    }

    public static void enableTouchDelegate(final View view, final Rect bounds) {
        final View parentView = (View) view.getParent();
        if (parentView == null) {
            return;
        }
        parentView.post(new Runnable() {
            @Override
            public void run() {
                parentView.setTouchDelegate(new TouchDelegate(bounds, view));
            }
        });
    }

}
