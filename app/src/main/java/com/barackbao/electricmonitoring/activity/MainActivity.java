package com.barackbao.electricmonitoring.activity;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.barackbao.electricmonitoring.R;
import com.barackbao.electricmonitoring.utils.FileUtils;
import com.barackbao.electricmonitoring.utils.RequestPermission;
import com.barackbao.electricmonitoring.widget.FabTagLayout;
import com.barackbao.electricmonitoring.widget.FloatingActionButtonPlus;
import com.fengmap.android.FMDevice;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapUpgradeInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMNodeListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.layer.FMModelLayer;
import com.fengmap.android.map.marker.FMModel;
import com.fengmap.android.map.marker.FMNode;
import com.fengmap.android.widget.FM3DControllerButton;
import com.fengmap.android.widget.FMFloorControllerComponent;
import com.fengmap.android.widget.FMMultiFloorControllerButton;
import com.fengmap.android.widget.FMZoomComponent;
import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.picker.LinkagePicker;
import cn.addapp.pickers.util.ConvertUtils;

public class MainActivity extends AppCompatActivity implements OnFMMapInitListener {

    private static final String TAG = "MainActivity";

    private FMModelLayer mModelLayer;
    private FMModel mClickModel;
    private FMMapView mMapView;
    private FMMap mMap;
    private FMZoomComponent mZoomComponent;
    private FloatingActionButtonPlus mFabGroup;
    FM3DControllerButton mTextBtn;
    FMFloorControllerComponent mFloorControllerComponent;
    FMMultiFloorControllerButton mMultiFloorButton;

    private OnFMNodeListener mOnModelClickListener = new OnFMNodeListener() {
        @Override
        public boolean onClick(FMNode fmNode) {
            if (mClickModel != null) {
                mClickModel.setSelected(false);
            }
            FMModel model = (FMModel) fmNode;
            mClickModel = model;
            model.setSelected(true);
            mMap.updateMap();
            Log.i(TAG, "model name is " + model.getName());
            //房间名
            String roomName = model.getName();
            //界面跳转,自己补全
            Intent intent = new Intent(MainActivity.this, DetailInformationActivity.class);
            intent.putExtra("roomName", roomName);
            startActivity(intent);

            FMMapCoord centerMapCoord = model.getCenterMapCoord();

            return true;
        }

        @Override
        public boolean onLongPress(FMNode fmNode) {
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        setWindow();
        //申请权限
        requestPermiss();
        openMapByPath();
        fabGroupEvents();
    }

    private void setWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
            window.setStatusBarColor( getResources().getColor( android.R.color.white) );
            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  );
        }
    }

    private void fabGroupEvents() {
        mFabGroup.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener() {
            @Override
            public void onItemClick(FabTagLayout tagView, int position) {
                switch (String.valueOf(position)){
                    case "0":
                        onFloodPicker(findViewById( R.id.main_view ));
                        break;
                    case "1":
                        Toast.makeText( MainActivity.this, "楼层分析", Toast.LENGTH_SHORT ).show();
                        break;
                    case "2":
                        startActivityForResult( new Intent( MainActivity.this,SelectActivity.class ),1);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void onFloodPicker(View view){
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {
            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                firstList.add("主楼" + "    ");
                firstList.add("11号楼" + "    ");
                firstList.add("逸夫楼" + "    ");
                firstList.add("旧1号楼" + "    ");
                firstList.add("文瀛五" + "    ");
                return firstList;
            }

            @Override
            public List<String> provideSecondData(int i) {
                int flood = 4;
                ArrayList<String> secondList = new ArrayList<>( );
                switch (String.valueOf( i )){
                    case "0":
                        flood = 14;
                        break;
                    case "1":
                        flood = 5;
                        break;
                    case "2":
                        flood = 6;
                        break;
                    case "3":
                        flood = 2;
                        break;
                    case "4":
                        flood = 5;
                        break;
                    default:
                        break;
                }
                for (int j = 1;j <= flood;j ++)
                    secondList.add( String.valueOf( j ));
                return secondList;
            }

            @Override
            public List<String> provideThirdData(int i, int i1) {
                return null;
            }
        };
        LinkagePicker picker = new LinkagePicker( this,provider );
        picker.setCanLoop( false );
        picker.setLabel( "    第    ","    层" );
        picker.setTitleText( "请选择建筑和楼层" );
        picker.setTopLineColor(0xFF33B5E5);//顶部标题栏下划线颜色
        picker.setTopLineHeight(1);//顶部标题栏下划线高度
        picker.setSubmitTextColor(getResources().getColor(android.R.color.holo_blue_light));
        picker.setCancelTextColor(getResources().getColor(android.R.color.holo_blue_light));
        picker.setSelectedItem( "主楼","7" );
        picker.setWeightEnable( true );
        picker.setOffset( 2 );
        LineConfig config = new LineConfig(  );
        config.setThick( ConvertUtils.toPx(this, 2));//线粗
        config.setWidth(ConvertUtils.toDp( this,80 ));
        picker.setLineConfig( config );
        picker.setOnMoreItemPickListener( new OnMoreItemPickListener() {
            @Override
            public void onItemPicked(Object o, Object t1, Object t2) {
                Toast.makeText( MainActivity.this,"已选择：" + o.toString() + t1.toString(), Toast.LENGTH_SHORT ).show();
            }
        } );
        picker.show();
    }
    private void requestPermiss() {
        RequestPermission.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new RequestPermission.OnPermissionsRequestListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> deniedList) {
                        Toast.makeText(MainActivity.this, "拒绝将无法使用本应用", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void openMapByPath() {
        mMapView = findViewById(R.id.map_view);
        mFabGroup = findViewById(R.id.FabPlus);
        mMap = mMapView.getFMMap();

        mMap.setOnFMMapInitListener(this);
        String mapId = "nucmain";
        mMap.openMapById(mapId, true);
        String path = FileUtils.getDefaultMapPath(this);
        mMap.openMapByPath(path);
    }


    @Override
    public void onMapInitSuccess(String s) {
//        mMap.loadThemeByPath(FileUtils.getDefaultThemePath(this));
        mMap.loadThemeById("nucmain");
        if (mZoomComponent == null) {
            initZoomComponent();
        }
        init3DControllerComponent();
        int groupId = mMap.getFocusGroupId();
        //模型图层
        mModelLayer = mMap.getFMLayerProxy().getFMModelLayer(groupId);
        mModelLayer.setOnFMNodeListener(mOnModelClickListener);
        mMap.addLayer(mModelLayer);

    }

    /**
     * 加载2d/3d切换控件
     */
    private void init3DControllerComponent() {
        mTextBtn = new FM3DControllerButton(this);
        //设置初始状态为3D(true),设置为false为2D模式
        mTextBtn.initState(true);
        mTextBtn.measure(0, 0);
        int width = mTextBtn.getMeasuredWidth();
        //设置3D控件位置
        mMapView.addComponent(mTextBtn, FMDevice.getDeviceWidth() - 10 - width, 50);
        //2、3D点击监听
        mTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FM3DControllerButton button = (FM3DControllerButton) v;
                if (button.isSelected()) {
                    button.setSelected(false);
                    mMap.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
                } else {
                    button.setSelected(true);
                    mMap.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
                }
            }
        });
        mMultiFloorButton = new FMMultiFloorControllerButton(this);
        mMultiFloorButton.initState(false);
        mMultiFloorButton.measure(0, 0);
        width = mMultiFloorButton.getMeasuredWidth();
        //设置单/多层楼层切换控件位置
        mMapView.addComponent(mMultiFloorButton, FMDevice.getDeviceWidth() - 10 - width, 160);
        //单、多楼层点击切换监听
        mMultiFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initZoomComponent() {
        mZoomComponent = new FMZoomComponent(this);
        mZoomComponent.measure(0, 0);
        int width = mZoomComponent.getMeasuredWidth();
        int height = mZoomComponent.getMeasuredHeight();
        //缩放控件位置
        int offsetX = FMDevice.getDeviceWidth() - width - 10;
        int offsetY = FMDevice.getDeviceHeight() - 400 - height;
        mMapView.addComponent(mZoomComponent, offsetX, offsetY);

        mZoomComponent.setOnFMZoomComponentListener(new FMZoomComponent.OnFMZoomComponentListener() {
            @Override
            public void onZoomIn(View view) {
                //地图放大
                mMap.zoomIn();
            }

            @Override
            public void onZoomOut(View view) {
                //地图缩小
                mMap.zoomOut();
            }
        });
    }

    @Override
    public void onMapInitFailure(String s, int i) {
        Log.i(TAG, "onMapInitFailure: id : " + i);
    }

    @Override
    public boolean onUpgrade(FMMapUpgradeInfo fmMapUpgradeInfo) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mMap != null) {
            mMap.onDestroy();
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode == 1){
            if(data.getStringExtra( "result" ) != null){
                Toast.makeText( this, data.getStringExtra( "result" ), Toast.LENGTH_SHORT ).show();
            }else {
                Toast.makeText( this, "null", Toast.LENGTH_SHORT ).show();
            }
        }
    }
}