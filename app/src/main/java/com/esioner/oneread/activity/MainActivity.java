package com.esioner.oneread.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.esioner.oneread.R;
import com.esioner.oneread.fragment.AllPageFragment;
import com.esioner.oneread.fragment.HomePageFragment;
import com.esioner.oneread.utils.ConstantValue;

import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext = this;
    private MainActivity.MyHandler mHandler = new MainActivity.MyHandler(this);


    private HomePageFragment homePageFragment;
    private Fragment currentFragment = new Fragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private AllPageFragment allPageFragment;

    private String mLocation;
    /**
     * 第一次点击 back key 的时间
     */
    private long firstClickKeyBackTime = 0;
    private MaterialDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocation = getIntent().getStringExtra(ConstantValue.WEATHER_LOCATION);
        Log.d(TAG, "onCreate: mLocation = " + mLocation);

        initUi();
    }

    private void initUi() {
        homePageFragment = new HomePageFragment();
        allPageFragment = new AllPageFragment();

        BottomNavigationBar navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        navigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                super.onTabSelected(position);
                switch (position) {
                    case 0:
                        Log.d(TAG, "onTabSelected: ");
                        switchFragment(homePageFragment);
                        break;
                    case 1:
                        switchFragment(allPageFragment);
                        break;
                    case 2:

                        break;
                }
            }
        });

        navigationBar.addItem(new BottomNavigationItem(R.drawable.one_fill, "ONE").setActiveColorResource(android.R.color.holo_green_light))
                .addItem(new BottomNavigationItem(R.drawable.all_fill, "ALL").setActiveColorResource(android.R.color.holo_red_light))
                .addItem(new BottomNavigationItem(R.drawable.me_fill, "ME").setActiveColorResource(android.R.color.holo_blue_bright))
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        navigationBar.selectTab(0, true);
    }

    private void switchFragment(android.support.v4.app.Fragment fragment) {
        if (currentFragment != fragment) {
            transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.frame_layout, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    /**
     * 返回当前定位
     *
     * @return
     */

    public String getLocation() {
        return mLocation;
    }

    /**
     * 防止内存泄漏的Handler写法
     */
    public static class MyHandler extends Handler {
        public WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 开始加载，弹出加载对话框
     */
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(mContext).
                    title("请稍后")
                    .content("正在加载数据，请稍后")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .show();
        } else if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 加载完成，对话框消失
     */
    public void stopLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - firstClickKeyBackTime) < 2000) {
            super.onBackPressed();
        } else {
            firstClickKeyBackTime = System.currentTimeMillis();
            Toast.makeText(mContext, "再按一次，退出", Toast.LENGTH_SHORT).show();
        }
    }
}
