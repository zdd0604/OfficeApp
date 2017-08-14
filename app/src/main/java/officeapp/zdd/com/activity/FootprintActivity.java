package officeapp.zdd.com.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sdyy.utils.XPermissionListener;
import com.sdyy.utils.XPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import officeapp.zdd.com.R;
import officeapp.zdd.com.db.SqliteStatement;
import officeapp.zdd.com.entity.MapInfo;
import officeapp.zdd.com.widget.CustomDialog;
import officeapp.zdd.com.widget.SuperActivity;

public class FootprintActivity extends SuperActivity {
    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    private double myLatitude = 0.00000;//
    private double myLongitude = 0.00000;//
    private String myAddress;
    //自定义定位图标
    private BitmapDescriptor bitmapDescriptor;

    private MyLocationConfiguration.LocationMode locationMode;
    //覆盖物相关
    private BitmapDescriptor mMark;
    private InfoWindow mInfoWindow;

    private List<MapInfo> mapList = new ArrayList<>();

    private View infoWindowView;
    private TextView infowindow_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_footprint);
        showActionBar(R.string.footprintActivity_name);

        initView();

        //初始化定位
        initLocation();

        initMark();
        XPermissions.getPermissions(new String[]{
                        XPermissions.CAMERA,
                        XPermissions.WRITE_EXTERNAL_STORAGE,
                        XPermissions.ACCESS_COARSE_LOCATION,
                        XPermissions.ACCESS_FINE_LOCATION,
                        XPermissions.READ_PHONE_STATE},
                this, new XPermissionListener() {
                    @Override
                    public void onAcceptAccredit() {
                        // 用户授权成功(API>=23时)

                    }

                    @Override
                    public void onRefuseAccredit(String[] results) {
                        // 用户拒绝授权(API>=23时)
                        toastLONG("定位授权失败");
                    }
                });
    }

    /**
     * 控件加载
     */
    private void initView() {
        mMapView = (MapView) findViewById(R.id.footView);
        baiduMap = mMapView.getMap();

        //地图的放大比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);// 地图放大级别
        baiduMap.setMapStatus(msu);

        infoWindowView = LayoutInflater.from(context).inflate(R.layout.infowindow_item, null);
        infowindow_text = (TextView) infoWindowView.findViewById(R.id.infowindow_text);
    }


    private void initLocation() {
        //设置模式
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;

        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        //自定义定位图标
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_quicksearch);

    }

    private void initMark() {
        mMark = BitmapDescriptorFactory.fromResource(R.mipmap.icon_mylocation);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Bundle mapinfo = marker.getExtraInfo();
                MapInfo info = (MapInfo) mapinfo.getSerializable("mapinfo");

//                TextView tx = new TextView(context);
//                tx.setText(info.getMapMemoryTiltle());
//                tx.setBackgroundResource(R.drawable.location_tips);
//                tx.setPadding(30, 20, 30, 20);
//                tx.setTextColor(Color.WHITE);
//                tx.setGravity(Gravity.CENTER);
                infowindow_text.setText(info.getMapMemoryTiltle());
                LatLng latlng = marker.getPosition();
                android.graphics.Point p = baiduMap.getProjection().toScreenLocation(latlng);
//                p.y -= 47;
                LatLng ll = baiduMap.getProjection().fromScreenLocation(p);
                // 为弹出的InfoWindow添加点击事件
                mInfoWindow = new InfoWindow(infoWindowView, ll, -45);
                // 显示InfoWindow
                baiduMap.showInfoWindow(mInfoWindow);

                showMyDialog(info);

                return true;
            }
        });
    }

    private void showMyDialog(MapInfo mapInfo)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.view_share_dialog, null);
        Dialog dialog = new CustomDialog(this, view, R.style.CustomButtonDialog);
        dialog.show();
        TextView dialog_ID = (TextView) view.findViewById(R.id.dialog_ID);
        dialog_ID.setText("" + mapInfo.getMapID());
        TextView dialog_name = (TextView) view.findViewById(R.id.dialog_name);
        dialog_name.setText(mapInfo.getMapMemoryTiltle());
        TextView dialog_time = (TextView) view.findViewById(R.id.dialog_time);
        dialog_time.setText(mapInfo.getMapTime());
        TextView dialog_address = (TextView) view.findViewById(R.id.dialog_address);
        dialog_address.setText(mapInfo.getMapAddress());
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        dialog_content.setText(mapInfo.getMapMemoryContent());
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }

        });

    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            myAddress = bdLocation.getAddrStr();
            myLatitude = bdLocation.getLatitude();
            myLongitude = bdLocation.getLongitude();
            logOut(myLongitude + ",," + myLatitude);
            //转化数据
            MyLocationData data = new MyLocationData.Builder()//
//                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
            baiduMap.setMyLocationData(data);

            MyLocationConfiguration myLocationConfiguration =
                    new MyLocationConfiguration(locationMode, true, bitmapDescriptor);
            baiduMap.setMyLocationConfigeration(myLocationConfiguration);

            if (isFirstIn) {
                LatLng latLng = new LatLng(myLatitude, myLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                toastSHORT(myAddress);
                isFirstIn = false;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_common:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_site:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_traffic:
                if (baiduMap.isTrafficEnabled()) {
                    baiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通地图(OFF)");
                } else {
                    baiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通地图(ON)");
                }
                break;
            case R.id.map_me:
                baiduMap.clear();
                LatLng latLng = new LatLng(myLatitude, myLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                break;
            case R.id.map_com:
                baiduMap.clear();
                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.map_compass:
                baiduMap.clear();
                locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;
            case R.id.map_following:
                baiduMap.clear();
                locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case R.id.map_overlord:
                selectMapInfo();
                break;
            case R.id.map_addlocation:
                addLocationInfo();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加足迹
     */
    private void addLocationInfo() {
        Bundle b = new Bundle();
        b.putString("address", myAddress);
        b.putDouble("myLongitude", myLongitude);
        b.putDouble("myLatitude", myLatitude);
        intentActivity(MemoryActivity.class, 10, b);
//        sqliteUtils.insertMapLocation(myAddress,
//                "fdjfsdj",
//                myLongitude,
//                myLatitude,
//                "没有",
//                "没有",
//                "没有");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults); // 回调函数
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // //界面返回值
        /**
         * @author haijian
         * 收到返回的值判断是否成功，如果同意就将数据移除刷新列表
         */
        if (requestCode == 10 && resultCode == 22) {
            toastLONG("返回成功");
            selectMapInfo();
        } else if (requestCode == 10 && resultCode == 33) {

        }
    }

    private void selectMapInfo() {
        mapList.clear();
        Cursor cursor = sqliteUtils.selectMapInfo();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String MapID = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapID));
                String MapMemoryTitle = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapMemoryTitle));
                String MapAddress = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapAddress));
                String MapLongitude = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapLongitude));
                String MapLatitude = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapLatitude));
                String MapTime = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapTime));
                String MapMemoryContent = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapMemoryContent));
                String MapPhotos = cursor.getString(cursor.getColumnIndex(SqliteStatement.MapPhotos));

                MapInfo mInfo = new MapInfo(Integer.valueOf(MapID), MapMemoryTitle, MapAddress, Double.valueOf(MapLongitude),
                        Double.valueOf(MapLatitude), MapTime, MapMemoryContent, MapPhotos);
                mapList.add(mInfo);
                logOut(mapList.toString());
            }
            addOverlays(mapList);
        } else {
            toastLONG("数据为空");
        }
    }


    /**
     * 添加覆盖物
     *
     * @param list
     */
    private void addOverlays(List<MapInfo> list) {
        baiduMap.clear();
        LatLng latlng = null;
        for (MapInfo mapinfo : list) {
            latlng = new LatLng(mapinfo.getMapLatitude(), mapinfo.getMapLongitude());
            //图标
            OverlayOptions options = new MarkerOptions().position(latlng).icon(mMark).zIndex(5);
            Marker mark = (Marker) baiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("mapinfo", mapinfo);
            mark.setExtraInfo(bundle);
        }

        MapStatusUpdate mupdate = MapStatusUpdateFactory.newLatLng(latlng);
        baiduMap.setMapStatus(mupdate);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        baiduMap.setMyLocationEnabled(true);
        //判断地图是否启动
        if (!mLocationClient.isStarted())
            mLocationClient.start();


//        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        baiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //停止方向传感器
//        myOrientationListener.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
