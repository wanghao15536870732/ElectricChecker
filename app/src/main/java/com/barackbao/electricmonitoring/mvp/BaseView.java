package com.barackbao.electricmonitoring.mvp;

import android.view.View;

/**
 * Created by Baoqianyue on 2018/5/26.
 */

public interface BaseView<T> {
    void setPresenter(T t);

    void initViews(View view);
}
