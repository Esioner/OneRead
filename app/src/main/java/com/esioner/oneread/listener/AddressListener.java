package com.esioner.oneread.listener;

/**
 * 监听地址是否加载成功
 */
public interface AddressListener{
        void success(String address);
        void fail(String reason);
    }