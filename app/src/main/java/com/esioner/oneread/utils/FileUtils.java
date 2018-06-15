package com.esioner.oneread.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Esioner on 2018/6/15.
 */

public class FileUtils {
    public static void checkFileIsExist(File f) throws IOException {
        //1. 判断当前文件是文件夹还是文件
        if (f.isFile()) {
            //2. 判断当前文件是否存在，不存在新建文件
            if (!f.exists()) {
                f.createNewFile();
            }
        } else if (f.isDirectory()) {
            if (!f.exists()) {
                f.mkdir();
            }
        }
        //
        File file = f.getParentFile();
        if (file != null) {
            checkFileIsExist(file);
        }
    }
}
