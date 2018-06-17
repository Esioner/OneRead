package com.esioner.oneread;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esioner on 2018/6/17.
 */

public class ActivityController {

    private static List<AppCompatActivity> mActivityList = new ArrayList<>();

    /**
     * 增加一个Activity
     *
     * @param activity
     */
    public void addActivity(AppCompatActivity activity) {
        mActivityList.add(activity);
    }

    /**
     * 关闭一个Activity
     *
     * @param activity
     */
    public void removeActivity(AppCompatActivity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    /**
     * 结束全部
     */
    public void finishAll() {
        for (AppCompatActivity activity : mActivityList) {
            activity.finish();
        }
    }
}
