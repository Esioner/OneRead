package com.esioner.oneread.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esioner.oneread.R;
import com.esioner.oneread.listener.LocationCallback;
import com.esioner.oneread.utils.ConstantValue;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import io.reactivex.functions.Consumer;

/**
 * Created by Esioner on 2018/6/17.
 */

public class SplashActivity extends BaseActivity implements LocationCallback {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Context mContext = this;
    private ImageView ivCurrentImage;
    private TextView tvCurrentDate;

    public LocationClient mLocationClient = null;
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    private MyLocationListener mLocationListener = new MyLocationListener(this);
    /**
     * 当前地址
     */
    private String mLocation;
    /**
     * 倒計時是否結束
     */
    private boolean countdownTimeIsFinish;
    /**
     * 獲取地址是否完成
     */
    private boolean locationIsFinish;
    private TextView tvCountDown;
    private CountDownTimer mCountDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_activity);

        Log.d(TAG, "onCreate: ");

        locationIsFinish = false;
        countdownTimeIsFinish = false;

        initUi();

        initDate();

        final int countdownTime = 3000;
        tvCountDown.setText((countdownTime / 1000) + "");
        mCountDownTimer = new CountDownTimer(countdownTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millisUntilFinished);
                tvCountDown.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                countdownTimeIsFinish = true;
                jumpToMainActivity(mLocation);
            }
        }.start();

        initData();


    }


    /**
     * 初始化 Ui
     */
    private void initUi() {
        ivCurrentImage = findViewById(R.id.iv_splash_image);
        tvCurrentDate = findViewById(R.id.tv_splash_current_date);
        tvCountDown = findViewById(R.id.tv_splash_countdown);
    }

    /**
     * 初始化显示的图片以及日期
     */
    private void initDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String date = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        int weekInt = c.get(Calendar.DAY_OF_WEEK);
        String week;
        switch (weekInt) {
            case 1:
                week = "星期日";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_sunday);
                break;
            case 2:
                week = "星期一";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_monday);
                break;
            case 3:
                week = "星期二";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_tuesday);
                break;
            case 4:
                week = "星期三";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_wednesday);
                break;
            case 5:
                week = "星期四";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_thursday);
                break;
            case 6:
                week = "星期五";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_friday);
                break;
            case 7:
                week = "星期六";
                ivCurrentImage.setImageResource(R.drawable.splash_date_image_saturday);
                break;
            default:
                week = "";
        }

        Log.d(TAG, "onCreate: year = " + year);
        Log.d(TAG, "onCreate: month = " + month);
        Log.d(TAG, "onCreate: date = " + date);
        Log.d(TAG, "onCreate: week = " + week);

        tvCurrentDate.setText(year + "年 " + month + "月 " + date + "日 " + week);
    }

    @Override
    protected void onStop() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (mLocationListener != null) {
            mLocationListener = null;
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    /**
     * 初始化一些必要的权限以及其他数据
     */
    private void initData() {
        //检查权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        checkCurrentPermission(permissions);

        //检查文件路径
        File file = new File(ConstantValue.FILE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        //获取当前位置
        getCurrentLocation();
//        //获取位置的时间
//        String getLocTime = SPUtils.getInstance(mContext).getString(ConstantValue.GET_LOCATION_TIME, "");
//        //如果当前从本地取出的地址及保存地址的时间，如果两个都为空，则获取地址
//        if (!"".equals(getLocTime)) {
//            //从本地获取位置信息
//            mLocation = SPUtils.getInstance(mContext).getString(ConstantValue.WEATHER_LOCATION, "");
//            long oneDayTimeMill = 24 * 60 * 1000 * 1000;
//            long timeDifference = System.currentTimeMillis() - Long.valueOf(getLocTime);
//            Log.d(TAG, "onCreate: 时间差 = " + (timeDifference - oneDayTimeMill));
//            Log.d(TAG, "onCreate: oneDayTimeMill = " + oneDayTimeMill);
//            //判断当前时间和上一次获取地址的时间差是否大于一天
//            if ("".equals(mLocation) || timeDifference >= oneDayTimeMill) {
//                //获取地理位置
//                getCurrentLocation();
//            }
//        } else {
//            getCurrentLocation();
//        }
    }

    /**
     * 跳转到 MainActivity
     */
    public void jumpToMainActivity(String location) {

        if (countdownTimeIsFinish && locationIsFinish) {
            Log.d(TAG, "jumpToMainActivity: countdownTimeIsFinish = " + countdownTimeIsFinish);
            Log.d(TAG, "jumpToMainActivity: locationIsFinish = " + locationIsFinish);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(ConstantValue.WEATHER_LOCATION, location);
            startActivity(intent);
            finish();
        } else {
            return;
        }
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
                    Toast.makeText(mContext, "已获取权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "获取权限失败,请授予权限再打开该程序", Toast.LENGTH_SHORT).show();
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
     * 获取当前位置成功
     *
     * @param location
     */
    @Override
    public void onSuccess(String location) {
        Log.d(TAG, "onSuccess: getLocationSuccess = " + location);
        this.mLocation = location;
        locationIsFinish = true;
        //地址获取成功跳转页面
        jumpToMainActivity(location);
    }

    /**
     * 获取当前位置失败
     *
     * @param reason
     */
    @Override
    public void onFail(String reason) {
        Log.d(TAG, "onFail: getLocationFailed = " + reason);
        locationIsFinish = true;
        //地址获取失败也跳转页面
        jumpToMainActivity("未知");
    }

    /**
     * 百度定位Api
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        private LocationCallback callback;

        private MyLocationListener(LocationCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onReceiveLocation(BDLocation loc) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取位置描述信息相关的结果
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (loc != null) {
                String city = loc.getCity();    //获取位置描述信息
                Log.d(TAG, "MyLocationListener city: " + city);
                if (city != null && !"".equals(city)) {
                    callback.onSuccess(city);
                } else {
                    callback.onFail("城市为空或者无内容，city = " + city);
                }
            } else {
                callback.onFail("loc 对象为空");
                Log.d(TAG, "onReceiveLocation: loc 为空");
            }
        }
    }
}
