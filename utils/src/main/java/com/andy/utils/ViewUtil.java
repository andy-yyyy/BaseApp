/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * Created by lixn on 2017/6/13.
 */

public class ViewUtil {

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
