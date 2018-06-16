package com.esioner.oneread.activity;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esioner.oneread.R;
import com.esioner.oneread.fragment.AllPageFragment;
import com.esioner.oneread.fragment.HomePageFragment;
import com.esioner.oneread.utils.ConstantValue;
import com.esioner.oneread.utils.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext = this;
    private MainActivity.MyHandler mHandler = new MainActivity.MyHandler(this);

    public LocationClient mLocationClient = null;
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    private MyLocationListener mLocationListener = new MyLocationListener();

    private String location = "";
    private HomePageFragment homePageFragment;
    private Fragment currentFragment = new Fragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private AllPageFragment allPageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //检查权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        checkCurrentPermission(permissions);

        //检查文件路径
        File file = new File(ConstantValue.FILE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        //获取地理位置
        getCurrentLocation();

        initUi();

        location = SPUtils.getInstance(mContext).getString(ConstantValue.WEATHER_LOCATION, "");

//        switchFragment(homePageFragment);
    }

    private void initUi() {
        homePageFragment = new HomePageFragment();
        allPageFragment = new AllPageFragment();

        BottomNavigationBar navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        navigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                super.onTabSelected(position);
                switch (position) {
                    case 0:
                        Log.d(TAG, "onTabSelected: ");
                        switchFragment(homePageFragment);
                        break;
                    case 1:
                        switchFragment(allPageFragment);
                        break;
                    case 2:

                        break;
                }
            }
        });

        navigationBar.addItem(new BottomNavigationItem(R.drawable.one_fill, "ONE").setActiveColorResource(android.R.color.holo_green_light))
                .addItem(new BottomNavigationItem(R.drawable.all_fill, "ALL").setActiveColorResource(android.R.color.holo_red_light))
                .addItem(new BottomNavigationItem(R.drawable.me_fill, "ME").setActiveColorResource(android.R.color.holo_blue_bright))
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        navigationBar.selectTab(0, true);
    }

    private void switchFragment(android.support.v4.app.Fragment fragment) {
        if (currentFragment != fragment) {
            transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.frame_layout, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    /**
     * 返回当前定位
     *
     * @return
     */

    public String getLocation() {
        if (location == null || "".equals(location)) {
            location = "未知";
        }
        return this.location;
    }


    /**
     * 检查并获取权限
     *
     * @param permissions
     */
    private void checkCurrentPermission(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Toast.makeText(MainActivity.this, "已获取权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "获取权限失败,请授予权限再打开该程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * 获取当前位置
     */
    private void getCurrentLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(mLocationListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        //设置定位模式，默认高精度(LocationMode.Hight_Accuracy：高精度；LocationMode. Battery_Saving：低功耗；LocationMode. Device_Sensors：仅使用设备；)
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setIsNeedAddress(true);
        //定位SDK内部是一个service，并放到了独立进程。(设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true))
        option.setIgnoreKillProcess(false);
        //设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 获取地址成功
     *
     * @param loc
     */
    private void getLocationSuccess(String loc) {
        Log.d(TAG, "getLocationSuccess: loc =  " + loc);
        this.location = loc;
    }

    /**
     * 百度定位Api
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation loc) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取位置描述信息相关的结果
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (location != null) {
                int locationType = loc.getLocType();
                Log.d(TAG, "onReceiveLocation: locationType = " + locationType);
                final String city = loc.getCity();    //获取位置描述信息
                Log.d(TAG, "city: " + city);

                if (city != null) {
                    SPUtils.getInstance(mContext).putString(ConstantValue.WEATHER_LOCATION, city);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getLocationSuccess(city);
                    }
                });
            } else {
                Log.d(TAG, "onReceiveLocation: loc 为空");
            }
        }
    }


    /**
     * 防止内存泄漏的Handler写法
     */
    public static class MyHandler extends Handler {
        public WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
