package com.esioner.oneread.listener;

import okhttp3.Response;

/**
 * Created by Esioner on 2018/6/10.
 */

public interface HttpListener {
    void connectSuccess(Response response);
    void connectFail(Exception e);
}
