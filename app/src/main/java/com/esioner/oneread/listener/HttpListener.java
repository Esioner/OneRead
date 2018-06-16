package com.esioner.oneread.listener;

import okhttp3.Response;

/**
 * Created by Esioner on 2018/6/10.
 */

public interface HttpListener {
    void onSuccess(Response response);
    void onFail(Exception e);
}
