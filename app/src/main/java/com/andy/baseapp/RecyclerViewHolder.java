/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lixn on 2017/8/30.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tv;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView;
        tv.setPadding(50, 50, 50, 50);
    }
}
