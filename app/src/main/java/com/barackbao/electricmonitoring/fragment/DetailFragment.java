package com.barackbao.electricmonitoring.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.adapter.DetailRecyclerAdapter;
import com.barackbao.electricmonitoring.bean.DetailItem;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Wangtianrui on 2018/6/2.
 */

public class DetailFragment extends Fragment {

    private int days;
    private int voltage;
    private int current;
    private int temp;
    private int capacity;
    private int state;

    public static DetailFragment newInstance(int days,int voltage,int current,int temp,int capacity,int state) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString( "Day",String.valueOf(days));
        bundle.putString( "Voltage",String.valueOf(voltage));
        bundle.putString( "Current",String.valueOf(current));
        bundle.putString( "Temperature",String.valueOf(temp) );
        bundle.putString( "Capacity",String.valueOf(capacity));
        bundle.putString( "State",String.valueOf(state));
        fragment.setArguments( bundle );
        return fragment;
    }


    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private boolean isLoad = false;

    private DetailRecyclerAdapter detailRecyclerAdapter;
    private ArrayList<DetailItem> datas = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        days = Integer.parseInt(bundle.getString( "Day" ));
        voltage = Integer.parseInt(bundle.getString( "Voltage" ));
        current = Integer.parseInt(bundle.getString( "Current" ));
        temp = Integer.parseInt( bundle.getString( "Temperature"));
        capacity = Integer.parseInt(bundle.getString( "Capacity" ));
        state = Integer.parseInt(bundle.getString( "State" ));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detial_information_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (isLoad) {
            loadData();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("设备未连接")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(true)
                    .show();
        }
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Test", "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Test", "onpause: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void loadData() {
        datas.add(makeData("瞬时电压", String.valueOf(voltage),220));
        datas.add(makeData("瞬时电流", String.valueOf(current),20));
        datas.add(makeData("瞬时温度", String.valueOf( temp ),60));
        datas.add(makeData("瞬时功率", String.valueOf( capacity),2978));
        datas.add(makeData("瞬时状态", String.valueOf( state ),19));
        datas.add( makeData( "电晕", "0",100) );
        datas.add( makeData( "弧垂", "0",5) );
    }

    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        detailRecyclerAdapter = new DetailRecyclerAdapter(getContext(), datas);
        recyclerview.setAdapter(detailRecyclerAdapter);
        recyclerview.setNestedScrollingEnabled(true);
    }

    private DetailItem makeData(String title, String number,int peek) {
        DetailItem test = new DetailItem();
        ArrayList<Entry> pointValues = new ArrayList<>();
        int stable = 0,safe = 0,danger = 0;
        for (int i = 1; i < days; i++) {
            int y = (int) (Math.random() * peek);
            pointValues.add(new Entry(i, y));
            if(y < 0.6*peek)
                safe ++;
            else if (y < 0.9*peek)
                stable ++;
            else
                danger ++;
            number = String.valueOf( y );
        }
        test.setLineList(pointValues);


        //饼图数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(stable, "稳定"));
        entries.add(new PieEntry(safe, "安全"));
        entries.add(new PieEntry(danger, "危险"));
        test.setPieList(entries);

        test.setTitle(title);
        test.setValue(number);
        return test;
    }

    public void changePage() {
        datas.clear();
        detailRecyclerAdapter.notifyDataSetChanged();
        loadData();
        detailRecyclerAdapter.notifyDataSetChanged();
    }

    public void setLoad(boolean b) {
        isLoad = b;
    }

}
