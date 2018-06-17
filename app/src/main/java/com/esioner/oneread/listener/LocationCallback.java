package com.esioner.oneread.listener;

/**
 * 监听地址是否加载成功
 */
public interface LocationCallback {
    void onSuccess(String location);

    void onFail(String reason);
}