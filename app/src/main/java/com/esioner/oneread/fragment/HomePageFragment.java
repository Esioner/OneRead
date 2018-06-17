package com.esioner.oneread.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.activity.MainActivity;
import com.esioner.oneread.adapter.ContentRVAdapter;
import com.esioner.oneread.adapter.PastListMonthRVAdapter;
import com.esioner.oneread.bean.HomePageData;
import com.esioner.oneread.bean.ContentHtmlData;
import com.esioner.oneread.bean.PastDate;
import com.esioner.oneread.bean.PastListData;
import com.esioner.oneread.utils.CommonUtils;
import com.esioner.oneread.utils.HttpUtils;
import com.esioner.oneread.utils.TextFormatUtil;
import com.esioner.oneread.utils._URL;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Esioner on 2018/6/12.
 */

public class HomePageFragment extends Fragment {

    private static final String TAG = HomePageFragment.class.getSimpleName();

    private MainActivity mActivity;
    private Context mContext;
    private HomePageFragmentHandler mHandler = new HomePageFragmentHandler(this);

    private HomePageData mHomePageData;

    private List<PastDate> pastDateList;
    private List<HomePageData.Data.ContentData> contentDataList;
    private List<HomePageData.Data.ContentData> todayHomePageDataList;
    private ContentHtmlData mMusicDetailData;


    private View view;
    private TextView tvTempCity;
    private TextView tvTempClimate;
    private TextView tvTempTemperature;
    private TextView tvMonth;
    private TextView tvDay;
    private TextView tvYear;
    private RelativeLayout dateBar;
    private ImageView ivWeatherIcon;
    private LinearLayout llArticleListView;
    private LinearLayout llPastListView;
    private RecyclerView rvPastList;
    private PastListMonthRVAdapter mPastListMonthRVAdapter;
    private RecyclerView rvContent;


    private ContentRVAdapter mContentRVAdapter;
    /**
     * 当前页面是否是往期列表
     * true 为是往期列表
     */
    private boolean currentPageIsPastList = false;
    /**
     * 当前位置
     */
    private String location;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 当前显示的 menu 内容
     */
    private HomePageData.Data.ContentMenu contentMenuData;
    /**
     * 今天的日期
     */
    private String todayDate;
    /**
     * 当前的日期是否为今天
     */
    private boolean currentPageIsToday = true;
    /**
     * 当前页面显示的日期
     */
    private String currentShowDate;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mActivity = (MainActivity) getActivity();
        Log.d(TAG, "onAttach: ");
    }

    /**
     * 避免耗时操作，可以获取从宿主activity传来的参数
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当天的日期
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(date);
//        location = savedInstanceState.getString(ConstantValue.WEATHER_LOCATION);
        Log.d(TAG, "onCreate: ");
    }

    /**
     * 注意的是：不要将视图层次结构附加到传入的ViewGroup父元素中，该关联会自动完成。如果在此回调中将碎片的视图层次结构附加到父元素，很可能会出现异常。
     * 这句话什么意思呢？就是不要把初始化的view视图主动添加到container里面，以为这会系统自带，所以inflate函数的第三个参数必须填false，而且不能出现container.addView(v)的操作。
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.layout_fragment_home_page, container, false);
        //初始化数据列表
        contentDataList = new ArrayList<>();
        pastDateList = new ArrayList<>();

        initUi();
        return view;
    }

    /**
     * 在调用onActivityCreated()之前，Activity的视图层次结构已经准备好了，这是在用户看到用户界面之前你可对用户界面执行的最后调整的地方
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        //获取当前位置
        this.location = mActivity.getLocation();
        Log.d(TAG, "onActivityCreated: location = " + location);
        String url = _URL.getArticleList(location);

        getArticleList(url);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    /**
     * 该回调方法在视图层次结构与Fragment分离之后调用。
     */
    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    /**
     * 不再使用Fragment时调用。
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    /**
     * Fragment生命周期最后回调函数，调用后，Fragment不再与Activity绑定，释放资源。
     */
    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
    
    /**
     * 初始化控件
     */
    public void initUi() {
        ivWeatherIcon = view.findViewById(R.id.iv_datebar_weather);
        llPastListView = view.findViewById(R.id.ll_past_list_view);
        llArticleListView = view.findViewById(R.id.ll_article_list_view);
        rvPastList = view.findViewById(R.id.rv_past_list);
        rvContent = view.findViewById(R.id.rv_content);
        tvTempCity = view.findViewById(R.id.tv_datebar_city);
        tvTempClimate = view.findViewById(R.id.tv_datebar_climate);
        tvTempTemperature = view.findViewById(R.id.tv_datebar_temperature);
        tvYear = view.findViewById(R.id.tv_year);
        tvMonth = view.findViewById(R.id.tv_month);
        tvDay = view.findViewById(R.id.tv_date);
        dateBar = view.findViewById(R.id.date_bar);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout_home_page);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticleList(_URL.getOneDayArticleList(currentShowDate));
            }
        });

        //点击状态栏弹出或则消失往期列表页面
        dateBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageIsPastList) {//当前页面是往期列表页面
                    dismissPastListView();
                    Toast.makeText(mContext, "消失往期列表", Toast.LENGTH_SHORT).show();
                } else {//如果当前页面不是往期列表页面，进行展示
                    showPastListView();
                    Toast.makeText(mContext, "显示往期列表", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPastListMonthRVAdapter = new PastListMonthRVAdapter(mContext, pastDateList);
        LinearLayoutManager pastListManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvPastList.setLayoutManager(pastListManager);

        rvPastList.setAdapter(mPastListMonthRVAdapter);
        PastListMonthRVAdapter.MonthViewOnClickListener listener = new PastListMonthRVAdapter.MonthViewOnClickListener() {
            @Override
            public void onClick(View view, String date) {
                Log.d(TAG, "ViewOnClickListener.onClick: date = " + date);
                getArticleList(_URL.getOneDayArticleList(date));
            }
        };
        mPastListMonthRVAdapter.setMonthViewOnClickListener(listener);

        mPastListMonthRVAdapter.setOnLoadMoreListener(new PastListMonthRVAdapter.LoadMoreListener() {
            @Override
            public void loadMore(String date) {
                try {
                    String formatedDate = TextFormatUtil.getLastMonth(date);
                    loadPastListData(formatedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        mContentRVAdapter = new ContentRVAdapter(mContext, contentDataList, HomePageFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.setAdapter(mContentRVAdapter);


    }


    /**
     * 获取到数据并将其展示
     *
     * @param data
     */
    public void initDataToUi(HomePageData data) {
        mHomePageData = data;
        String dateString = data.getData().getDate();
        Log.d(TAG, "initDataToUi: date = " + dateString);

        String[] dateArr = TextFormatUtil.getDateBarDateArray(dateString);
        if (dateArr.length != 0) {
            tvYear.setText(dateArr[0]);
            tvMonth.setText(dateArr[1]);
            tvDay.setText(dateArr[2]);
        }
        HomePageData.Data.Weather weatherData = data.getData().getWeather();
        tvTempCity.setText(weatherData.getCityName());
        tvTempClimate.setText(weatherData.getClimate());
        tvTempTemperature.setText(weatherData.getTemperature());
        //根据传来的天气 icon 来选择天气的icon
        Log.d(TAG, "initDataToUi: iconName = " + weatherData.getIcons().getDay());
        int iconId = CommonUtils.getResourceIdByStringName(mContext, weatherData.getIcons().getDay());
        Log.d(TAG, "initDataToUi: iconName = " + iconId);
        Glide.with(mContext).load(iconId).into(ivWeatherIcon);

        //如果当前页面内容不为空，保存今天的信息，然后将获取的内容保存给contentData
        if (data.getData().getContentList().size() != 0) {
            todayHomePageDataList = contentDataList;
            contentDataList.clear();
        }
        contentDataList.addAll(data.getData().getContentList());
        mContentRVAdapter.notifyDataSetChanged();

        stopLoading();
    }


    /**
     * 加载往期列表
     *
     * @param date
     */
    public void loadPastListData(final String date) {
        HttpUtils.getAsync(_URL.getPastList(date), new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                int code = response.code();
                Log.d(TAG, "connectSuccess:获取往期列表 code = " + code);
                if (code == 200) {
                    String jsonBody = response.body();
                    Log.d(TAG, "connectSuccess: jsonBody = \n" + jsonBody);
                    PastListData pastListData = new Gson().fromJson(jsonBody, PastListData.class);
                    boolean isContain = false;
                    Log.d(TAG, "onSuccess: date = " + date);
                    for (PastDate past : pastDateList) {
                        if (date.equals(past.getDate())) {
                            isContain = true;
                        } else {
                            isContain = false;
                        }
                    }
                    if (!isContain) {
                        PastDate pastDate = new PastDate();
                        pastDate.setDate(date);
                        pastDate.setPastDateDataList(pastListData.getData());
                        pastDateList.add(pastDate);
                    }
                    //展示数据
                    mPastListMonthRVAdapter.notifyItemChanged(pastDateList.size());
                }
            }
        });
    }


    /**
     * 展示往期列表页面
     */
    public void showPastListView() {
        //判断往期列表是否有数据，如果没有，获取日期，加载数据
        if (pastDateList.size() <= 0) {
            String date = TextFormatUtil.getFormatedDate(mHomePageData.getData().getDate(), TextFormatUtil.Y_M_D_H_M_S, TextFormatUtil.Y_M);
            loadPastListData(date);
        }
        //判断当前頁面是否是往期页面，如果不是，显示往期列表布局，并将 currentPageIsPastList 置为true
        if (!currentPageIsPastList) {
            //展示往期列表
            llPastListView.setVisibility(View.VISIBLE);
            Animation appearAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_in);
            llPastListView.setAnimation(appearAnim);

            currentPageIsPastList = true;
            Log.d(TAG, "showPastListView: 已经展示" + currentPageIsPastList);
        }
    }

    /**
     * 退出往期列表页面
     */
    public void dismissPastListView() {
        //判断当前页面是不是往期列表页面，如果是将列表布局消失，并将 currentPageIsPastList 置为 false
        if (currentPageIsPastList) {

            Animation dismissAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_out);
            llPastListView.setAnimation(dismissAnim);
            llPastListView.setVisibility(View.GONE);
            currentPageIsPastList = false;
            Log.d(TAG, "dismissPastListView: 已经消失：" + currentPageIsPastList);
        }
    }

    /**
     * 开始获取列表数据
     *
     * @param url
     */
    private void getArticleList(final String url) {
        //开始加载动画
        startLoading();
        //判断当前是不是往期列表，如果是往期列表页面，回到主页面
        if (currentPageIsPastList) {
            dismissPastListView();
            Log.d(TAG, "getArticleList: " + currentPageIsPastList);
        }
        Log.d(TAG, "getArticleList: URL = " + url);
        HttpUtils.getAsync(url, new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                int responseCode = response.code();
                Log.d(TAG, "onSuccess: responseCode = " + responseCode);
                String pageDataJsonString = response.body();
                HomePageData homePageData = new Gson().fromJson(pageDataJsonString, HomePageData.class);
                Log.d(TAG, "onSuccess: " + homePageData.getData().getWeather().getCityName() + "" + homePageData.getData().getWeather().getClimate());

                //获取当前显示页面的日期详情
                String dateStr = homePageData.getData().getDate();
                currentShowDate = TextFormatUtil.getFormatedDate(dateStr, TextFormatUtil.Y_M_D_H_M_S, TextFormatUtil.Y_M_D);
                Log.d(TAG, "onSuccess:currentShowDate = " + currentShowDate);
                Log.d(TAG, "onSuccess: todayDate = " + todayDate);
                //添加menu数据
                HomePageData.Data.ContentData menuContentData = homePageData.getData().new ContentData();
                menuContentData.setCategory("-1");
                homePageData.getData().getContentList().add(1, menuContentData);
                //设置 menuData
                contentMenuData = homePageData.getData().getMenu();
                //根据 音乐Id 获取音乐数据
                String musicItemId = "";
                for (HomePageData.Data.ContentData contentData : homePageData.getData().getContentList()) {
                    int category = Integer.parseInt(contentData.getCategory());
                    if (category == 4) {
                        musicItemId = contentData.getItemId();
                    }
                }
                if (!"".equals(musicItemId)) {
                    //开始获取音乐详情
                    getMusicInfoFromInternet(musicItemId);
                }
                initDataToUi(homePageData);
            }
        });
    }

    /**
     * 获取音乐详情
     *
     * @param musicItemId
     */
    private void getMusicInfoFromInternet(String musicItemId) {
        String url = _URL.getMusicHtml(musicItemId);
        HttpUtils.getAsync(url, new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                Log.d(TAG, "getMusicInfoFromInternet: response.code() = " + response.code());
                if (response.code() == 200) {
                    String musicJson = response.body();
                    Log.d(TAG, "getMusicInfoFromInternet: musicJson = \n" + musicJson);
                    mMusicDetailData = new Gson().fromJson(musicJson, ContentHtmlData.class);

                    if (mMusicDetailData != null) {
                        Document document = Jsoup.parse(mMusicDetailData.getData().getHtmlContent());
                        Element element = document.getElementsByClass("one-music-header-info").get(0);
                        String musicTitle = element.text();
                        Log.d(TAG, "getMusicDetailData: musicTitle = " + musicTitle);
                        mMusicDetailData.getData().setMusicTitle(musicTitle);
                    }
                }
            }
        });
    }

    /**
     * 获取音乐背景下的标题
     *
     * @return
     */
    public ContentHtmlData getMusicDetailData() {
        return mMusicDetailData;
    }

    /**
     * 获取当前页面的菜单对象
     *
     * @return
     */
    public HomePageData.Data.ContentMenu getContentMenuData() {
        return this.contentMenuData;
    }

    /**
     * 开始加载 加载动画
     */
    private void startLoading() {
        if (swipeRefreshLayout != null) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }
        mActivity.startLoading();
    }

    /**
     * 停止加载 加载动画消失
     */
    private void stopLoading() {
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        mActivity.stopLoading();
    }

    /**
     * 获取当前 activity 对象
     *
     * @return
     */
    public MainActivity getmActivity() {
        return mActivity;
    }

    /**
     * 避免内存泄漏的Handler
     */
    public static class HomePageFragmentHandler extends Handler {
        WeakReference<HomePageFragment> weakReference;

        public HomePageFragmentHandler(HomePageFragment fragment) {
            weakReference = new WeakReference<HomePageFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
