/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by lixn on 2017/10/18.
 */

public class BannerView extends RelativeLayout {

    protected RelativeLayout mRootView;
    protected ViewPager mViewPager;
    protected BannerAdapter mAdapter;

    public BannerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_banner_view, this, true);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.vp);
        initViewPager();
    }

    protected void initViewPager() {
        mAdapter = new BannerAdapter(getContext(), new int[]{});
        mViewPager.setAdapter(mAdapter);
    }

    public void setBannerData(int[] bannerData) {
        mAdapter.setData(bannerData);
    }
}
