package com.barackbao.electricmonitoring.bean;

import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by Wangtianrui on 2018/6/2.
 */

public class DetailItem {
    private String value;
    private String title;
    private ArrayList<Entry> lineList = new ArrayList<>();
    private ArrayList<PieEntry> pieList = new ArrayList<>();

    public ArrayList<Entry> getLineList() {
        return lineList;
    }

    public void setLineList(ArrayList<Entry> lineList) {
        this.lineList = lineList;
    }

    public ArrayList<PieEntry> getPieList() {
        return pieList;
    }

    public void setPieList(ArrayList<PieEntry> pieList) {
        this.pieList = pieList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
