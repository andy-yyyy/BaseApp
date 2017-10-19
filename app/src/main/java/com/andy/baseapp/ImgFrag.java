/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by lixn on 2017/10/13.
 */

public class ImgFrag extends Fragment {

    public static final String KEY_IMG_RES = "image_res";
    private int mImgRes;

    public static ImgFrag newInstance(int img) {
            ImgFrag frag = new ImgFrag();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_IMG_RES, img);
            frag.setArguments(bundle);
            return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
        return inflater.inflate(R.layout.frag_img, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgView = (ImageView) view.findViewById(R.id.img);
        imgView.setImageResource(mImgRes);
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mImgRes = savedInstanceState.getInt(KEY_IMG_RES);
        } else if (getArguments() != null) {
            mImgRes = getArguments().getInt(KEY_IMG_RES);
        }
    }
}
