package com.esioner.oneread.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.adapter.CommentRVAdapter;
import com.esioner.oneread.adapter.SpaceItemDecoration;
import com.esioner.oneread.bean.CommentRootData;
import com.esioner.oneread.bean.ContentHtmlData;
import com.esioner.oneread.bean.SerialIdListData;
import com.esioner.oneread.utils.HttpUtils;
import com.esioner.oneread.utils._URL;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esioner on 2018/6/14.
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    /**
     * 文章带语音阅读
     */
    public static final int TYPE_ARTICLE_WITH_SPEAKER = 1;
    /**
     * 连载
     */
    public static final int TYPE_SERIAL = 2;
    /**
     * 音乐
     */
    public static final int TYPE_MUSIC = 4;

    private WebView webView;
    private LinearLayout commentView;
    private LinearLayout likeView;
    private ImageView ivShareView;
    private WebSettings webSettings;
    private TextView tvCommentNum;
    private TextView tvLikeNum;
    private WebViewActivityHandler mHandler = new WebViewActivityHandler(this);
    //音频
    private MediaPlayer player;
    private TextView tvMediaDuration;
    private Thread mediaPlayTimeThread;
    private LinearLayout llMediaDetail;
    private ImageView ivMediaCover;
    private TextView tvMediaType;
    private TextView tvMediaSpeaker;
    private ImageView ivMediaPlayingBG;
    private AnimationDrawable mediaPlayAnimationDrawable;

    //连载
    private LinearLayout llSerialBottomBar;
    private LinearLayout serialNextPage;
    private LinearLayout serialChapterList;
    private LinearLayout serialPreviousPage;
    /**
     * 当前页面的连载的页面id，是json中的id，不是serial_id
     */
    private String mSerialCurrPageId;
    /**
     * 连载的页面id list，根据这个id从接口中获取页面信息
     */
    private List<String> mSerialsIdList;
    /**
     * 连载文章列表加载完成
     */
    private boolean serialIDsIsFinish = false;
    /**
     * 文章内容获取完成
     */
    private boolean webViewDataIsFinish = false;
    //音频
    private boolean isPlayMedia = false;
    private boolean isMediaPause = false;
    //全局
    private int pageCategory;
    private Context mContext = this;
    private String pageId;
    private ContentHtmlData contentHtmlData;
    private ImageView ivLoading;
    private AnimationDrawable loadingAnimationDrawable;
    private boolean isLoading = false;
    private CheckBox cbCommentBarLike;
    private boolean isLikeed = false;
    private LinearLayout llMusicBar;
    private CheckBox cbMusicPlayButton;
    /**
     * 评论列表
     */
    private List<CommentRootData.CommentData.CommentDetailData> mCommentDataList;
    private String mLastCommentId = "0";
    private CommentRVAdapter commentRVAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageId = getIntent().getStringExtra("itemId");

        pageCategory = Integer.parseInt(getIntent().getStringExtra("category"));
        Log.d(TAG, "onCreate: category = " + pageCategory);
        Log.d(TAG, "onCreate: pageId = " + pageId);
        setContentView(R.layout.layout_webview_activity);

        initView(pageCategory);

        loadDetailContentById(pageCategory, pageId);

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

    @Override
    public void onBackPressed() {
        if (isPlayMedia) {
            new MaterialDialog.Builder(mContext)
                    .title("警告")
                    .content("媒体正在播放，是否确定退出")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .negativeText("取消")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击评论打开评论界面
            case R.id.ll_comment_bar_comment:
                Toast.makeText(mContext, "打开评论页面", Toast.LENGTH_SHORT).show();
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_view, null, false);
                RecyclerView rvComment = view.findViewById(R.id.rv_comment);
                commentRVAdapter = new CommentRVAdapter(mCommentDataList, mContext);
                final LinearLayoutManager manager = new LinearLayoutManager(mContext);
                rvComment.setLayoutManager(manager);
                rvComment.setAdapter(commentRVAdapter);
                rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();//可见范围内的最后一项的位置
                        int itemCount = manager.getItemCount();//recyclerview中的item的所有的数目
                        //当可见范围的最后一项在倒数第五个，开始加载更多
                        if (lastVisibleItemPosition == (itemCount - 5)) {
                            loadCommentData(pageCategory, pageId, mLastCommentId);
                        }
                    }
                });
                Dialog dialog = new Dialog(mContext, R.style.Dialog_Fullscreen);
                dialog.getWindow().setWindowAnimations(R.style.DialogEnterAndExitAnimation);//设置Dialog动画
                dialog.setContentView(view);
                dialog.show();
                break;
            //点击喜欢按钮
            case R.id.ll_comment_bar_like:
                int praiseNum = Integer.parseInt(contentHtmlData.getData().getPraiseNum());
                if (!isLikeed) {
//                    Toast.makeText(mContext, "点击喜欢", Toast.LENGTH_SHORT).show();
                    praiseNum = praiseNum + 1;
                    contentHtmlData.getData().setPraiseNum(praiseNum + "");
                    tvLikeNum.setText(contentHtmlData.getData().getPraiseNum());
                    cbCommentBarLike.setChecked(true);
                    isLikeed = true;
                } else {
//                    Toast.makeText(mContext, "你已选择喜欢", Toast.LENGTH_SHORT).show();
                    praiseNum = praiseNum - 1;
                    contentHtmlData.getData().setPraiseNum(praiseNum + "");
                    tvLikeNum.setText(contentHtmlData.getData().getPraiseNum());
                    cbCommentBarLike.setChecked(false);
                    isLikeed = false;
                }
                break;
            //点击分享按钮
            case R.id.ll_comment_bar_share:
                Toast.makeText(mContext, "点击分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 初始化控件
     *
     * @param category
     */
    private void initView(int category) {
        webView = findViewById(R.id.web_view);
        ivLoading = findViewById(R.id.iv_webview_loading);
        (findViewById(R.id.ll_comment_bar_comment)).setOnClickListener(this);
        (findViewById(R.id.ll_comment_bar_like)).setOnClickListener(this);
        (findViewById(R.id.ll_comment_bar_share)).setOnClickListener(this);
        tvCommentNum = findViewById(R.id.tv_comment_bar_comment_num);
        cbCommentBarLike = findViewById(R.id.cb_comment_bar_like);
        tvLikeNum = findViewById(R.id.tv_comment_bar_like_num);

        //根据 category 来选择需要加载的组件
        switch (category) {
            case TYPE_MUSIC:
                llMusicBar = findViewById(R.id.ll_bottom_bar_music_play);
                cbMusicPlayButton = findViewById(R.id.cb_bottom_bar_music_play_button);
                cbMusicPlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //开始播放
                            startPlayMedia(contentHtmlData.getData().getMusicId());
                        } else if (!isChecked) {
                            pausePlayMedia();
                        }
                    }
                });
                break;
            case TYPE_ARTICLE_WITH_SPEAKER:
                //媒体播放页面内容
                llMediaDetail = findViewById(R.id.rl_bottom_bar_media_detail);
                ivMediaCover = findViewById(R.id.iv_bottom_bar_media_cover);
                tvMediaType = findViewById(R.id.tv_bottom_bar_media_type);
                tvMediaSpeaker = findViewById(R.id.tv_bottom_bar_media_read_author_name);
                tvMediaDuration = findViewById(R.id.tv_bottom_bar_media_time);
                ivMediaPlayingBG = findViewById(R.id.iv_bottom_bar_media_play_button);
                //点击Media 栏
                llMediaDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isPlayMedia) {
                            startPlayMedia(contentHtmlData.getData().getAudio());
                        } else if (isPlayMedia) {
                            pausePlayMedia();
                        }
                    }
                });
                break;
            case TYPE_SERIAL:
                //连载页面内容
                llSerialBottomBar = findViewById(R.id.ll_bottom_bar_serial_tool_bar);
                serialNextPage = findViewById(R.id.ll_bottom_bar_serial_next);
                serialChapterList = findViewById(R.id.ll_bottom_bar_serial_chapter_list);
                serialPreviousPage = findViewById(R.id.ll_bottom_bar_serial_previous);
                //连载底部工具栏点击事件
                //打开连载页面
                serialChapterList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (serialIDsIsFinish && webViewDataIsFinish) {

                            new MaterialDialog.Builder(mContext)
                                    .title("章节列表")
                                    .items(mSerialsIdList)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                            Log.d(TAG, "onItemClick: serialId = " + mSerialsIdList.get(which));
                                            loadDetailContentById(2, mSerialsIdList.get(which));
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(mContext, "数据正在加载，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                serialNextPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: 点击下一页");
                        Log.d(TAG, "onClick: " + serialIDsIsFinish + "\t" + webViewDataIsFinish);

                        if (serialIDsIsFinish && webViewDataIsFinish) {
                            int currPosition;
                            if (mSerialsIdList.contains(mSerialCurrPageId)) {
                                currPosition = mSerialsIdList.indexOf(mSerialCurrPageId);
                                Log.d(TAG, "onClick: currPosition = " + currPosition);
                                //如果 当前位置等于最后一章节的位置
                                if (currPosition == (mSerialsIdList.size() - 1)) {
                                    Toast.makeText(mContext, "已经是最后一章，请等待更新", Toast.LENGTH_SHORT).show();
                                } else if (currPosition >= 0 && currPosition < (mSerialsIdList.size() - 1)) {
                                    loadDetailContentById(TYPE_SERIAL, mSerialsIdList.get(currPosition + 1));
                                }
                            } else {
                                Log.d(TAG, "onClick: curr不存在");
                            }
                        } else {
                            Toast.makeText(mContext, "数据正在加载，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                serialPreviousPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: 点击上一页");
                        Log.d(TAG, "onClick: " + serialIDsIsFinish + "\t" + webViewDataIsFinish);
                        if (serialIDsIsFinish && webViewDataIsFinish) {
                            int currPosition;
                            Log.d(TAG, "onClick: " + mSerialsIdList);
                            Log.d(TAG, "onClick: " + mSerialCurrPageId);
                            if (mSerialsIdList.contains(mSerialCurrPageId)) {
                                currPosition = mSerialsIdList.indexOf(mSerialCurrPageId);
                                Log.d(TAG, "onClick: currPosition = " + currPosition);
                                //如果 当前位置等于第一章节的位置
                                if (currPosition == 0) {
                                    Toast.makeText(mContext, "已经是第一章", Toast.LENGTH_SHORT).show();
                                } else if (currPosition > 0 && currPosition <= (mSerialsIdList.size() - 1)) {
                                    loadDetailContentById(TYPE_SERIAL, mSerialsIdList.get(currPosition - 1));
                                }
                            } else {
                                Log.d(TAG, "onClick: curr不存在");
                            }
                        } else {
                            Toast.makeText(mContext, "数据正在加载，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

        //声明WebSettings子类
        webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }


    /**
     * 根据 itemId 加载数据
     *
     * @param category
     * @param itemId
     */
    private void loadDetailContentById(final int category, final String itemId) {
        //开始刷新
        startLoading();
        //先将控制变量进行初始化
        serialIDsIsFinish = false;
        webViewDataIsFinish = false;

        //加载数据
        String url = _URL.getHtmlContent(category, itemId);
        Log.d(TAG, "loadDetailContentById: URL = " + url);
        StringCallback callback = new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                if (response.code() == 200) {
                    String jsonString = response.body();
                    contentHtmlData = new Gson().fromJson(jsonString, ContentHtmlData.class);
                    pageCategory = contentHtmlData.getData().getCategory();
                    //如果是连载，加载连载id列表
                    if (category == TYPE_SERIAL) {
                        //获取连载 id
                        String id = contentHtmlData.getData().getSerialId();
                        mSerialCurrPageId = contentHtmlData.getData().getId();
                        loadSerialArrById(id);
                    }
                    pageId = contentHtmlData.getData().getId();
                    webViewDataIsFinish = true;
                    //数据展示并处理
                    showData(contentHtmlData);
                }
            }
        };
        HttpUtils.getAsync(url, callback);
        //加载评论
        loadCommentData(category, itemId, null);


    }

    /**
     * 加载评论
     *
     * @param category
     * @param itemId
     */
    private void loadCommentData(int category, String itemId, @Nullable String lastCommentId) {
        if (lastCommentId == null) {
            lastCommentId = mLastCommentId;
        }
        String commentUrl = _URL.getCommentUrl(category, itemId, lastCommentId);
        Log.d(TAG, "loadCommentData: commentUrl = " + commentUrl);
        HttpUtils.getAsync(commentUrl, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.d(TAG, "onSuccess: code = " + response.code());
                String responseBody = response.body();
                Log.d(TAG, "loadCommentData onSuccess: responseBody = " + responseBody);
                if (response.code() == 200) {
                    CommentRootData commentRootData = new Gson().fromJson(response.body(), CommentRootData.class);
                    double commentCount = commentRootData.getData().getCount();
                    Log.d(TAG, "loadCommentData : onSuccess: commentCount = " + commentCount);
                    //获取评论列表
                    if (mCommentDataList == null) {
                        mCommentDataList = new ArrayList<>();
                    }
                    //添加数据前，list的长度
                    int prevSize = mCommentDataList.size();
                    Log.d(TAG, "loadCommentData onSuccess: 加载之前的评论列表长度 = " + prevSize);
                    mCommentDataList.addAll(commentRootData.getData().getData());
                    Log.d(TAG, "loadCommentData onSuccess: 加载之后的评论列表长度 = " + mCommentDataList.size());
                    mLastCommentId = mCommentDataList.get(mCommentDataList.size() - 1).getId();
                    if (commentRVAdapter != null) {
                        commentRVAdapter.notifyItemChanged(prevSize);
                    }
                }
            }
        });
    }

    /**
     * 连载需要，根据连载文章的id获取所有连载的id
     *
     * @param serialId
     */
    private void loadSerialArrById(String serialId) {
        Log.d(TAG, "loadSerialArrById: currentThread = " + Thread.currentThread().getName());
        //拼接 URL
        String url = _URL.getSerialIdList(serialId);
        Log.d(TAG, "loadSerialArrById: url = " + url);

        StringCallback callback = new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                Log.d(TAG, "loadSerialArrById onResponse: currentThread = " + Thread.currentThread().getName());
                if (response.code() == 200) {
                    String serialIdJson = response.body();
                    Log.d(TAG, "loadSerialArrById onSuccess: serialIdJson = \n" + serialIdJson);
                    SerialIdListData mSerialIdListData = new Gson().fromJson(serialIdJson, SerialIdListData.class);
                    String serialFinished = mSerialIdListData.getData().getFinished();
                    Log.d(TAG, "onSuccess: serialFinished = " + serialFinished);
                    String serialTitle = mSerialIdListData.getData().getTitle();
                    Log.d(TAG, "onSuccess: serialTitle = " + serialTitle);
                    String id = mSerialIdListData.getData().getId();
                    Log.d(TAG, "onSuccess: id = " + id);
                    //遍历list ,取出 id 留着下一页或者上一页用
                    List<SerialIdListData.SerialIdData.SerialIdInfo> serialIdDataList = mSerialIdListData.getData().getSerialIdInfoList();
                    if (mSerialsIdList == null) {
                        mSerialsIdList = new ArrayList<>();
                    } else if (mSerialsIdList.size() != 0) {
                        mSerialsIdList.clear();
                    }
                    for (SerialIdListData.SerialIdData.SerialIdInfo info : serialIdDataList) {
                        mSerialsIdList.add(info.getId());
                    }
                    serialIDsIsFinish = true;
                }
            }
        };
        HttpUtils.getAsync(url, callback);
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
            Glide.with(mContext).load(htmlData.getData().getAuthorInfoList().get(0).getWebUrl()).into(ivMediaCover);
            htmlType = 1;
        } else if (category == 2) {
            htmlType = TYPE_SERIAL;
            llSerialBottomBar.setVisibility(View.VISIBLE);
//            Glide.with(mContext).load(htmlData.getData().getAuthorInfoList().get(0).getWebUrl()).into(ivMediaCover);
        } else if (category == 4) {
            htmlType = TYPE_MUSIC;
            llMusicBar.setVisibility(View.VISIBLE);
        }
        tvCommentNum.setText(htmlData.getData().getCommentNum() + "");
        tvLikeNum.setText(htmlData.getData().getPraiseNum() + "");
        //加载html
        final int finalHtmlType = htmlType;
        String htmlDetail = parseHtmlByJsoup(htmlData.getData().getHtmlContent(), finalHtmlType);
//        webView.loadData(htmlDetail, "text/html", "UTF-8");
        webView.loadData(htmlDetail, "text/html; charset=UTF-8", null);
        //停止刷新
        stopLoading();
    }

    /**
     * 使用 Jsoup 解析网页，删除不需要的标签
     *
     * @param html     网页内容
     * @param htmlType 当前文章类型
     * @return
     */
    public String parseHtmlByJsoup(String html, int htmlType) {
        Document document = Jsoup.parse(html);
//        Log.d(TAG, "run: document = \n" + document.toString());
        try {
            if (htmlType == TYPE_ARTICLE_WITH_SPEAKER) {
                //移出语音阅读标签
                document.getElementsByClass("onevue-readingaudio-box").get(0).remove();
            } else if (htmlType == TYPE_SERIAL) {
                //移除章节和上一章和下一章按钮
                (document.getElementsByClass("one-icon one-icon-chapter one-serial-nav-ids-btn").first()).remove();
                (document.getElementsByClass("one-serial-nav-box").first()).remove();
            } else if (htmlType == TYPE_MUSIC) {
                //移出网页的音乐播放头部
                (document.getElementsByClass("one-music-header-box one-page-header-image").first()).remove();
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
            stopPlayMediaAnim();
            player.pause();
            isPlayMedia = false;
            isMediaPause = true;
            Toast.makeText(mContext, "已暂停", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始||继续播放
     *
     * @param mediaUrl 音频地址
     */
    private void startPlayMedia(String mediaUrl) {
        try {
            if (!isPlayMedia) {
                if (!isMediaPause) {
                    player = new MediaPlayer();
                    player.setDataSource(mediaUrl);
                    player.prepareAsync();
                    int duration = player.getDuration();
                    if (tvMediaDuration != null) {
                        tvMediaDuration.setText(duration + "");
                    }
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
                startPlayMediaAnim();
            }
            isPlayMedia = true;

            if (pageCategory == TYPE_ARTICLE_WITH_SPEAKER) {
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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始刷新
     */
    private void startLoading() {
        if (!isLoading) {
            ivLoading.setVisibility(View.VISIBLE);
            ivLoading.setImageResource(R.drawable.anim_loading_article);
            if (loadingAnimationDrawable == null) {
                loadingAnimationDrawable = (AnimationDrawable) ivLoading.getDrawable();
            }
            loadingAnimationDrawable.start();
            isLoading = true;
        }
    }

    /**
     * 停止刷新
     */
    private void stopLoading() {
        if (isLoading) {
            if (loadingAnimationDrawable != null) {
                loadingAnimationDrawable.stop();
                ivLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        }
    }

    /**
     * 开始音频播放动画
     */
    private void startPlayMediaAnim() {
        if (ivMediaPlayingBG != null) {
            ivMediaPlayingBG.setImageResource(R.drawable.anim_media_playing);
            if (!isPlayMedia) {
                if (mediaPlayAnimationDrawable == null) {
                    mediaPlayAnimationDrawable = (AnimationDrawable) ivMediaPlayingBG.getDrawable();
                }
                mediaPlayAnimationDrawable.start();
                Log.d(TAG, "startPlayMediaAnim: 正在播放");
            }
        }
    }

    /**
     * 停止音频播放动画
     */
    private void stopPlayMediaAnim() {
        if (ivMediaPlayingBG != null) {
            if (isPlayMedia) {
                if (mediaPlayAnimationDrawable != null) {
                    mediaPlayAnimationDrawable.stop();
                    ivMediaPlayingBG.setImageResource(R.drawable.anim_media_play_03);
                    Log.d(TAG, "stopPlayMediaAnim: 已停止");
                }
            }
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
