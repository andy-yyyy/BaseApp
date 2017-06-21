/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Remember to call the setContext(Context context) method when the app is
 * first created.
 */
public final class ToastUtil {
    static Context ctx = null;

    private ToastUtil() {}

    static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public final static void setContext(Context ctxp) {
        if (!isMainThread()) {
            throw new RuntimeException("ToastUtil::setContext must be in UI thread");
        }
        ctx = ctxp;
        sHander = new Handler();
    }

    public static Context getContext() {
        return ctx;
    }

    private static Thread sThread = null;
    private static Handler sHander = null;

    private final static void show(final CharSequence text, final int duration, final int[] gravity) {
        if (sHander != null) {
            sHander.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(ctx, text, duration);
                    if (gravity != null && gravity.length >= 3)
                        toast.setGravity(gravity[0], gravity[1], gravity[2]);
                    toast.show();
                }
            });
        }
    }

    public final static void show(final CharSequence text, final int duration, final int gravity, final int xOffset, final int yOffset) {
        show(text, duration, new int[]{gravity, xOffset, yOffset});
    }

    public final static void show(final CharSequence text, final int gravity, final int xOffset, final int yOffset) {
        show(text, Toast.LENGTH_LONG, new int[]{gravity, xOffset, yOffset});
    }

    public final static void show(final CharSequence text, final int duration) {
        show(text, duration, null);
    }

    public final static void show(final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

}
