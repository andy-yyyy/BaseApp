/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by lixn on 2017/10/13.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG_ACT = "act";   // 可以通过过滤这个tag快速找到当前的Activity的名字

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_ACT, "[act:] "+getClass().getName());
    }
}
