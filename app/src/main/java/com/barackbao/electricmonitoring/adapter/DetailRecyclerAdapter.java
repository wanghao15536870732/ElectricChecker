package com.barackbao.electricmonitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.bean.DetailItem;

import java.util.ArrayList;

/**
 * Created by Wangtianrui on 2018/6/2.
 */

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailHolder> {
    private Context mContext;
    private ArrayList<DetailItem> list;

    public DetailRecyclerAdapter(Context mContext, ArrayList<DetailItem> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.detail_item_view, parent, false);
        return new DetailHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
