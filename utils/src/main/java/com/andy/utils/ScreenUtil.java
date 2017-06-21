/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {
    
    private static DisplayMetrics getDisplayMetrics(Context ctx) {
        return ctx.getApplicationContext().getResources().getDisplayMetrics();
    }

    /**
     * obtain the dpi of screen
     */
    public static float getScreenDpi(Context ctx) {
        return getDisplayMetrics(ctx).densityDpi;
    }

    /**
     * obtain the density of screen
     */
    public static float getScreenDensity(Context ctx) {
        return getDisplayMetrics(ctx).density;
    }
    
    /**
     * obtain the scaled density of screen
     */
    public static float getScreenScaledDensity(Context ctx) {
        return getDisplayMetrics(ctx).scaledDensity;
    }

    /**
     * obtain the width of screen
     */
    public static int getScreenWidth(Context ctx) {
        return getDisplayMetrics(ctx).widthPixels;
    }

    /**
     * obtain the height of screen
     */
    public static int getScreenHeight(Context ctx) {
        return getDisplayMetrics(ctx).heightPixels;
    }

    /**
     * According to the resolution of the phone from the dp unit will become a px (pixels)
     */
    public static int dip2px(Context ctx, int dip) {
        float density = getScreenDensity(ctx);
        return (int) (dip * density + 0.5f);
    }

    /**
     * Turn from the units of px (pixels) become dp according to phone resolution
     */
    public static int px2dip(Context ctx, float px) {
        float density = getScreenDensity(ctx);
        return (int) (px / density + 0.5f);
    }

    /**
     * Turn from the units of px (pixels) become sp according to phone scaledDensity
     * @param ctx
     * @param px
     * @return
     */
    public static int px2sp(Context ctx, float px) {
        float scale = getScreenScaledDensity(ctx);
        return (int) (px / scale + 0.5f);
    }

    /**
     * According to the scaledDensity of the phone from the sp unit will become a px (pixels)
     * @param ctx
     * @param sp
     * @return
     */
    public static int sp2px(Context ctx, int sp){
        float scale = getScreenScaledDensity(ctx);
        return (int) (sp * scale + 0.5f);
    }
}

