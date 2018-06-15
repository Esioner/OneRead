package com.esioner.oneread.activity;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.bean.ContentHtmlData;
import com.esioner.oneread.bean.SerialIdListData;
import com.esioner.oneread.utils.HttpUtils;
import com.esioner.oneread.utils._URL;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import okhttp3.Response;

/**
 * Created by Esioner on 2018/6/14.
 */

public class WebViewActivity extends BaseActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    /**
     * 文章带语音阅读
     */
    public static final int TYPE_ARTICLE_WITH_SPEAKER = 1;
    /**
     * 章节
     */
    public static final int TYPE_SERIAL = 2;

    private WebView webView;
    private LinearLayout commentView;
    private LinearLayout likeView;
    private ImageView ivShareView;
    private WebSettings webSettings;

    private WebViewActivityHandler mHandler = new WebViewActivityHandler(this);
    private LinearLayout llMediaDetail;
    private ImageView ivMediaCover;
    //    private ProgressBar pbPlayProgress;
    private TextView tvMediaType;
    private TextView tvMediaSpeaker;
    private TextView tvCommentNum;
    private TextView tvLikeNum;

    private Context mContext = this;
    private boolean isPlayMedia = false;
    private boolean isMediaPause = false;

    private MediaPlayer player;
    private TextView tvMediaDuration;
    private Thread mediaPlayTimeThread;
    private LinearLayout llSerialBottomBar;
    private LinearLayout serialNextPage;
    private LinearLayout serialChapterList;
    private LinearLayout serialPreviousPage;
    private SerialIdListData mSerialIdListData;
    private String serialId;
    private int nextSerialId = -1;
    private int prevSerialId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String itemId = getIntent().getStringExtra("itemId");
        int category = Integer.parseInt(getIntent().getStringExtra("category"));
        Log.d(TAG, "onCreate: category = " + category);
        Log.d(TAG, "onCreate: itemId = " + itemId);
        setContentView(R.layout.layout_webview_activity);

        loadDetailContentById(category, itemId);

        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null && webSettings != null) {
            webSettings.setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null && webSettings != null) {
            webSettings.setJavaScriptEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSettings != null) {
            webSettings = null;
        }
        if (webView != null) {
            webView = null;
        }
        if (player != null) {
            player.stop();
            player.release();
            isPlayMedia = false;
            isMediaPause = false;
        }
        if (mediaPlayTimeThread != null) {
            if (mediaPlayTimeThread.isAlive()) {
                mediaPlayTimeThread = null;
            }
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        webView = findViewById(R.id.web_view);
        commentView = findViewById(R.id.ll_comment_bar_comment);
        likeView = findViewById(R.id.ll_comment_bar_like);
        ivShareView = findViewById(R.id.ll_comment_bar_share);
        tvCommentNum = findViewById(R.id.tv_comment_bar_comment_num);
        tvLikeNum = findViewById(R.id.tv_comment_bar_like_num);

        //媒体播放页面内容
        llMediaDetail = findViewById(R.id.rl_bottom_bar_media_detail);
        ivMediaCover = findViewById(R.id.iv_bottom_bar_media_cover);
        tvMediaType = findViewById(R.id.tv_bottom_bar_media_type);
        tvMediaSpeaker = findViewById(R.id.tv_bottom_bar_media_read_author_name);
        tvMediaDuration = findViewById(R.id.tv_bottom_bar_media_time);
        //连载页面内容
        llSerialBottomBar = findViewById(R.id.ll_bottom_bar_serial_tool_bar);
        serialNextPage = findViewById(R.id.ll_bottom_bar_serial_next);
        serialChapterList = findViewById(R.id.ll_bottom_bar_serial_chapter_list);
        serialPreviousPage = findViewById(R.id.ll_bottom_bar_serial_previous);


        //声明WebSettings子类
        webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    /**
     * 根据 itemId 加载数据
     *
     * @param category
     * @param itemId
     */
    private void loadDetailContentById(final int category, final String itemId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //如果是连载，加载连载id列表
                    Log.d(TAG, "run:category = " + category);
                    Log.d(TAG, "run: itemId = " + itemId);
                    if (category == 2) {
                        serialId = itemId;
                        getSerialIds(serialId);
                    }
                    Response response = HttpUtils.getSync(_URL.getHtmlContent(category, itemId));
                    if (response.code() == 200) {
                        String jsonString = response.body().string();
                        final ContentHtmlData htmlData = new Gson().fromJson(jsonString, ContentHtmlData.class);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showData(htmlData);
                            }
                        });
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param serialId
     */
    private void getSerialIds(String serialId) {
        try {
            String url = _URL.getSerialIdList(serialId);
            Log.d(TAG, "getSerialIds: url = " + url);
            Response responseSerialIdList = HttpUtils.getSync(url);
            if (responseSerialIdList.code() == 200) {
                String serialIdJson = responseSerialIdList.body().string();
                mSerialIdListData = new Gson().fromJson(serialIdJson, SerialIdListData.class);
                String[] serialIds = mSerialIdListData.getData().get(0).getSerialIds();
                Log.d(TAG, "getSerialIds: " + Arrays.toString(serialIds));
                Log.d(TAG, "getSerialIds: arr.length = " + serialIds.length);
                Log.d(TAG, "getSerialIds: " + serialId);
                for (int i = 0; i < serialIds.length; i++) {
                    if (serialId.equals(serialIds[i])) {
                        Log.d(TAG, "getSerialIds: serialId = " + serialIds[i]);
                        nextSerialId = i + 1;
                        prevSerialId = i - 1;
                        Log.d(TAG, "getSerialIds: i = " + i);
                        Log.d(TAG, "getSerialIds: nextSerialId = " + nextSerialId);
                        Log.d(TAG, "getSerialIds: prevSerialId = " + prevSerialId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示加载完成的数据
     *
     * @param htmlData
     */
    public void showData(final ContentHtmlData htmlData) {
        int category = htmlData.getData().getCategory();
        int htmlType = 0;
        if (category == 1 && !"".equals(htmlData.getData().getAudio().trim())) {
            llMediaDetail.setVisibility(View.VISIBLE);
            tvMediaSpeaker.setText(htmlData.getData().getAnchor());
            Glide.with(mContext).load(htmlData.getData().getAuthorInfoList().get(0).getWebUrl());
            htmlType = 1;
        } else if (category == 2) {
            htmlType = TYPE_SERIAL;
            llSerialBottomBar.setVisibility(View.VISIBLE);
        }


        mediaPlayBottomBarClickListener(htmlData);
        serialBottomBarClickListener(htmlData);

        tvCommentNum.setText(htmlData.getData().getCommentNum() + "");
        tvLikeNum.setText(htmlData.getData().getPraiseNum() + "");
        Log.d(TAG, "showData: getAudio = " + htmlData.getData().getAudio().trim());

        //加载html
        final int finalHtmlType = htmlType;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String htmlDetail = parseHtmlByJsoup(htmlData.getData().getHtmlContent(), finalHtmlType);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadData(htmlDetail, "text/html", "UTF-8");
                    }
                });
            }
        }).start();

    }

    /**
     * 音频播放底部工具栏
     *
     * @param htmlData
     */
    private void mediaPlayBottomBarClickListener(final ContentHtmlData htmlData) {
        //点击Media 栏
        llMediaDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayMedia) {
                    startPlayMedia(htmlData.getData());
                } else if (isPlayMedia) {
                    pausePlayMedia();
                }
            }
        });
    }

    /**
     * 连载界面底部工具栏按键点击事件
     *
     * @param htmlData
     */
    private void serialBottomBarClickListener(ContentHtmlData htmlData) {
        //打开连载页面
        //连载底部工具栏点击事件
        serialChapterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] serialIds = mSerialIdListData.getData().get(0).getSerialIds();
                if (serialIds != null) {
                    final Dialog dialog = new Dialog(mContext);
                    ListView listView = new ListView(mContext);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, serialIds);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "onItemClick: serialId = " + serialIds[position]);
                            loadDetailContentById(2, serialIds[position]);
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(listView);
                    dialog.show();
                } else {
                    Toast.makeText(mContext, "数据正在加载，请稍后再试", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getSerialIds(serialId);
                        }
                    }).start();
                }
            }
        });
        serialNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] serialIds = mSerialIdListData.getData().get(0).getSerialIds();
                Log.d(TAG, "onClick: serialIds = " + Arrays.toString(serialIds));
                Log.d(TAG, "onClick: nextSerialId = " + nextSerialId);
                if (nextSerialId >= serialIds.length) {
                    Toast.makeText(mContext, "已经是最后一章", Toast.LENGTH_SHORT).show();
                } else if (nextSerialId < serialIds.length && nextSerialId >= 0) {
                    loadDetailContentById(2, serialIds[nextSerialId]);
                }
            }
        });
        serialPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] serialIds = mSerialIdListData.getData().get(0).getSerialIds();
                Log.d(TAG, "onClick: serialIds = " + Arrays.toString(serialIds));
                Log.d(TAG, "onClick: prevSerialId = " + prevSerialId);
                if (prevSerialId <= 0) {
                    Toast.makeText(mContext, "已经是第一章了", Toast.LENGTH_SHORT).show();
                } else if (prevSerialId < serialIds.length && prevSerialId >= 0) {
                    loadDetailContentById(2, serialIds[prevSerialId]);
                }
            }
        });
    }

    public String parseHtmlByJsoup(String html, int htmlType) {
        Document document = Jsoup.parse(html);
//        Log.d(TAG, "run: document = \n" + document.toString());
        try {
            if (htmlType == TYPE_ARTICLE_WITH_SPEAKER) {
                //移出语音阅读标签
                document.getElementsByClass("onevue-readingaudio-box").get(0).remove();
            }
            if (htmlType == TYPE_SERIAL) {
                //移除章节和上一章和下一章按钮
                (document.getElementsByClass("one-icon one-icon-chapter one-serial-nav-ids-btn").first()).remove();
                (document.getElementsByClass("one-serial-nav-box").first()).remove();
            }
            //移出评论标签
            Element element = document.getElementsByClass("one-comments-box").get(0);
            element.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document.outerHtml();
    }

    /**
     * 暂停播放
     */
    private void pausePlayMedia() {
        if (player != null && player.isPlaying()) {
            isPlayMedia = false;
            isMediaPause = true;
            player.pause();
            Toast.makeText(mContext, "已暂停", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始继续播放
     *
     * @param data
     */
    private void startPlayMedia(ContentHtmlData.HtmlData data) {
        try {
            if (!isPlayMedia) {
                if (!isMediaPause) {
                    player = new MediaPlayer();
                    player.setDataSource(data.getAudio());
                    player.prepareAsync();
                    int duration = player.getDuration();
                    tvMediaDuration.setText(duration + "");
//                    pbPlayProgress.setMax(duration);
                    Log.d(TAG, "startPlayMedia:duration = " + duration);
                    Toast.makeText(mContext, "开始播放", Toast.LENGTH_SHORT).show();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            player.start();
                        }
                    });
                } else if (isMediaPause) {
                    player.start();
                    Toast.makeText(mContext, "继续播放", Toast.LENGTH_SHORT).show();
                }
            }
            isPlayMedia = true;

            if (mediaPlayTimeThread == null) {
                mediaPlayTimeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isPlayMedia) {
                            final int dur = player.getCurrentPosition() / 1000;
                            Log.d(TAG, "startPlayMedia run: " + dur);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String time = (int) (dur / 60) + " : " + (int) (dur % 60);
                                    Log.d(TAG, "startPlayMedia:time=  " + time);
//                                                pbPlayProgress.setProgress(dur);
                                    tvMediaDuration.setText(time);
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                mediaPlayTimeThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 防止内存泄漏的 Handler
     */
    public static class WebViewActivityHandler extends Handler {
        private WeakReference<WebViewActivity> weakReference;

        public WebViewActivityHandler(WebViewActivity activity) {
            weakReference = new WeakReference<WebViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WebViewActivity webViewActivity = weakReference.get();
            super.handleMessage(msg);
        }
    }

}
