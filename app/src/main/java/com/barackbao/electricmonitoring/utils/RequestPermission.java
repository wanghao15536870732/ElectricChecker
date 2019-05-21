package com.barackbao.electricmonitoring.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baoqianyue on 2018/6/2.
 */

public class RequestPermission {
    public static final int PERMISSION_REQUEST_CODE = 1;

    private static OnPermissionsRequestListener mListener;

    public interface OnPermissionsRequestListener {
        //同意权限时被调用
        public void onGranted();

        //拒绝权限时被调用
        public void onDenied(List<String> deniedList);
    }


    public static void requestPermissions(Activity activity, String[] permissions,
                                          OnPermissionsRequestListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }

        //如果有权限申请
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]),
                    PERMISSION_REQUEST_CODE);
        } else {
            listener.onGranted();
        }
    }
}
