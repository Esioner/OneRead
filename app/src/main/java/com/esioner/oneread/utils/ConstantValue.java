package com.esioner.oneread.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Esioner on 2018/6/16.
 */

public interface ConstantValue {
    /**
     * 文件保存路径
     */
    String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "EasyRead";

    /**
     * 地址
     */
    String WEATHER_LOCATION = "WEATHER_LOCATION";
    /**
     * 获取地址的时间
     */
    String GET_LOCATION_TIME = "GET_LOCATION_TIME";



}
