/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixn on 2017/10/20.
 */

public class DataUtils {

    public static List<String> mockStringList(int size) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i ++) {
            String s = "item "+i;
            data.add(s);
        }
        return data;
    }
}
