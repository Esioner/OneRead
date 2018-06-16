package com.esioner.oneread.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.esioner.oneread.listener.DownloadListener;
import com.esioner.oneread.listener.HttpListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Esioner on 2018/6/7.
 */

public class HttpUtils {

    public static Response get(String url) throws IOException {
        Log.d("TAG", "get: url = " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 阻塞线程获取数据 GET方式
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Response getSync(String url) throws IOException {
        Response response = OkGo.<String>get(url).execute();
        return response;
    }

    /**
     * 异步获取数据 GET方式
     * @param url
     * @param callback
     */
    public static void getAsync(String url, com.lzy.okgo.callback.Callback<String> callback) {
        OkGo.<String>get(url).execute(callback);
    }

    public static void download(String url, String filePath, DownloadListener listener) {
        InputStream input = null;
        OutputStream output = null;
        try {
            listener.onStart();
            Response response = OkGo.<String>get(url).execute();
            if (response.code() == 200) {
                File file = new File(filePath);
                long fileSize = response.body().contentLength();
                if (file.exists()) {
                    if (file.length() != fileSize) {
                        file.delete();
                    }
                }
                FileUtils.checkFileIsExist(file);

                int len;
                byte[] bs = new byte[1024];
                long temp = 0;
                int position = 0;
                input = response.body().byteStream();
                output = new FileOutputStream(file);

                while ((len = input.read(bs)) != -1) {
                    output.write(bs, 0, len);
                    temp += len;
                    position = (int) ((temp / fileSize) * 100.0);
                    listener.onProgress(position);
                }
                listener.onFinish(file.getAbsolutePath());
                input.close();
                output.close();
            }
        } catch (Exception e) {
            listener.onError(e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
