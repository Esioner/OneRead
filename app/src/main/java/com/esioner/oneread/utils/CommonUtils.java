package com.esioner.oneread.utils;

import android.content.Context;

/**
 * Created by Esioner on 2018/6/17.
 */

public class CommonUtils {

    /**
     * 根据传入的字符串名称在drawable中查找资源，返回资源id，如果没有则返回0
     *
     * @param context
     * @param imageName
     * @return
     */
    public static int getResourceIdByStringName(Context context, String imageName) {
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }
}
