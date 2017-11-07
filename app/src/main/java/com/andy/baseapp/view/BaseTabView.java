/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andy.baseapp.R;
import com.andy.utils.ScreenUtil;
import com.andy.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/12/12.
 */
public class BaseTabView<T extends View> extends LinearLayout {

    protected LinearLayout mRootView;
    protected LinearLayout mTabContainer;
    private ImageView mCursorView;
    protected List<T> mItemViewList = new ArrayList<>();
    protected int mCurrentPos;
    protected int mLastPos;

    private OnItemSelectedListener mListener;

    public BaseTabView(Context context) {
        super(context);
        initView(context);
    }

    public BaseTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void commit() {
        updateCursorView();
    }

    public void setSelection(int pos) {
        if (pos < 0 || pos > mItemViewList.size() - 1) {
            return;
        }
        this.mLastPos = mCurrentPos;
        this.mCurrentPos = pos;
        T itemView = mItemViewList.get(pos);
        updateSelectedItem(itemView, pos);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mListener = listener;
    }

    protected void updateSelectedItem(T itemView, int pos) {
        animateCursor(pos);
    }


    public void addItemView(final T view) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mTabContainer.addView(view, params);
        mItemViewList.add(view);
        initListener();
    }

    private void initView(Context context) {
        mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_base_tab, this, true);
        mTabContainer = (LinearLayout) mRootView.findViewById(R.id.tab_container);
        initListener();
    }

    private void initListener() {
        for (int i = 0; i < mItemViewList.size(); i ++) {
            final int position = i;
            T v = mItemViewList.get(position);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelection(position);
                    if (mListener != null) {
                        mListener.onItemSelected(v, position);
                        ToastUtil.show("position: "+position);
                    }
                }
            });
        }
    }

    private void updateCursorView() {
        mCursorView = (ImageView) findViewById(R.id.cursor_view);
        int tabSize = mItemViewList.size();
        if (tabSize > 0) {
            int width = ScreenUtil.getScreenWidth(getContext()) / tabSize;
            LayoutParams params = (LayoutParams) mCursorView.getLayoutParams();
            params.width = width;
            mCursorView.setLayoutParams(params);
        }
    }

    private void animateCursor(int pos) {
        int width = ScreenUtil.getScreenWidth(getContext()) / mItemViewList.size();
        int current = width*mLastPos;
        Animation anim = new TranslateAnimation(current, width*pos, 0, 0);
        anim.setDuration(200);
        anim.setFillAfter(true);
        mCursorView.startAnimation(anim);
    }

    interface OnItemSelectedListener {
        void onItemSelected(View itemView, int pos);
    }
}
