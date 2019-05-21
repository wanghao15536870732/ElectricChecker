package com.barackbao.electricmonitoring.app;



import android.app.Application;

import com.fengmap.android.FMMapSDK;

/**
 * Created by Baoqianyue on 2018/5/26.
 * <p>
 * 初始化各类SDK
 */

public class BaseApplication extends Application {

    public static String[] PROVINCE = {"安徽", "北京", "重庆", "福建", "甘肃", "广东",
            "广西", "贵州", "海南", "河北", "黑龙江", "河南", "香港", "湖北",
            "湖南", "江苏", "江西", "吉林", "辽宁", "澳门", "内蒙古", "宁夏",
            "青海", "陕西", "上海", "山东", "山西", "四川", "台湾", "天津",
            "新疆", "西藏", "云南", "浙江"};

    @Override
    public void onCreate() {
        FMMapSDK.init(this);
        super.onCreate();
    }
}
