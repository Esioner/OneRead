package com.esioner.oneread.listener;

/**
 * Created by Esioner on 2018/6/15.
 */

public interface DownloadListener {

    void onStart();

    void onFinish(String filePath);

    void onProgress(int progress);

    void onError(Exception e);
}
