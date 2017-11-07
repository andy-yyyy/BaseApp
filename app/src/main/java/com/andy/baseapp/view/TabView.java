/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy.utils.ScreenUtil;

/**
 * Created by andy on 2016/12/14.
 */
public class TabView extends BaseTabView<TextView> {

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void updateSelectedItem(TextView itemView, int pos) {
        super.updateSelectedItem(itemView, pos);
        mItemViewList.get(mLastPos).setTextColor(Color.parseColor("#999999"));
        mItemViewList.get(mCurrentPos).setTextColor(Color.parseColor("#ffac38"));
    }

    public void addItem(String title) {
        TextView item = new TextView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        item.setLayoutParams(lp);
        item.setTextColor(Color.parseColor("#999999"));
        item.setBackgroundColor(Color.parseColor("#eeeeee"));
        item.setGravity(Gravity.CENTER);
        int padding = ScreenUtil.dip2px(getContext(), 12);
        item.setPadding(padding, padding, padding, padding);
        if (!TextUtils.isEmpty(title)) {
            item.setText(title);
        }
        addItemView(item);
    }
}
