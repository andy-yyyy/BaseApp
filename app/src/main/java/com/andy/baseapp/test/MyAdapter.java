/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.baseapp.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.andy.baseapp.R;
import com.andy.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixn on 2017/8/8.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;

    public MyAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    public void updateData(List<String> data) {
        if (data != null) {
            this.mData = data;
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
            holder = createHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(Holder holder, int position) {
        if (holder == null || position > mData.size()) {
            return;
        }
        final String s = mData.get(position);
        holder.tv.setText(s);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(s);
            }
        });
    }

    private Holder createHolder(View convertView) {
        Holder holder = new Holder();
        holder.tv = (TextView) convertView.findViewById(R.id.tv);
        holder.btn = (Button) convertView.findViewById(R.id.btn);
        return holder;
    }

    class Holder {
        TextView tv;
        Button btn;
    }

}
