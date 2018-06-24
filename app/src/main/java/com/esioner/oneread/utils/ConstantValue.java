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

    /**
     * 表示是 viewpager 的数据
     */
    int TYPE_VIEWPAGER_DATA = 3;
    /**
     * 表示是纵向列表的数据类型
     */
    int TYPE_VERTICAL_RV_DATA = 4;
    /**
     * 表示是横向列表的数据类型
     */
    int TYPE_HORIZONTAL_RV_DATA = 5;

}
