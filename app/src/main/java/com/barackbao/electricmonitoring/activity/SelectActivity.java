package com.barackbao.electricmonitoring.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.app.BaseApplication;
import com.sorashiro.chinamapinfoview.ChinaMapInfoView;
import com.sorashiro.chinamapinfoview.CnMap;
import com.sorashiro.chinamapinfoview.CnMapConfig;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectActivity extends AppCompatActivity implements ChinaMapInfoView.ChinaMapViewProvinceListener{

    @BindView( R.id.imgCnMap )
    ChinaMapInfoView imgCnMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select );
        setWindow();
        ButterKnife.bind( this );
        imgCnMap.setChinaMapViewProvinceListener( this );
        // 首先获取总设置 map
        CnMap cnMap = imgCnMap.getCnMap();
        HashMap<String, CnMapConfig> cnConfigMap = cnMap.configMap;
        // 或者 configMap.get("Anhui"); 但是用 cnMap.PROVINCE[0] 更好些
        CnMapConfig configAnhui = cnConfigMap.get(cnMap.PROVINCE[26]);
        // 支持链式调用（方法链）
        configAnhui.setFillColor( Color.parseColor("#ee0000")).setClickColor(Color.parseColor("#99ffff"));
    }

    @Override
    public void onProvinceClick(int i) {
        Toast.makeText( this, BaseApplication.PROVINCE[i], Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProvinceLongClick(int i) {
        Toast.makeText( this, BaseApplication.PROVINCE[i], Toast.LENGTH_SHORT ).show();
        Intent intent = new Intent(  );
        intent.putExtra( "result", BaseApplication.PROVINCE[i]);
        setResult( RESULT_OK,intent);
        finish();
    }

    private void setWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
