package com.esioner.oneread.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Esioner on 2018/6/16.
 */

public class SPUtils {
    public static SharedPreferences sp;

    private SPUtils(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        }
    }

    public static SPUtils getInstance(Context context) {
        return new SPUtils(context);
    }

    /**
     * 写入String数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putString(String key, String value) {
        if (sp != null) {
            sp.edit().putString(key, value).apply();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取 String 数据
     */
    public String getString(String key, String df) {
        if (sp != null) {
            return sp.getString(key, df);
        } else {
            return df;
        }
    }
}
