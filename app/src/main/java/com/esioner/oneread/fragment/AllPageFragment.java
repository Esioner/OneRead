package com.esioner.oneread.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esioner.oneread.R;
import com.esioner.oneread.adapter.HomePageVPAdapter;
import com.esioner.oneread.bean.HomePageVPRootData;
import com.esioner.oneread.utils.HttpUtils;
import com.esioner.oneread.utils._URL;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esioner on 2018/6/13.
 */

public class AllPageFragment extends Fragment {

    private String TAG = AllPageFragment.class.getSimpleName();

    private View view;
    /**
     * 轮播图数据 List
     */
    private List<HomePageVPRootData.VPDetailData> vpDataList = new ArrayList<>();
    private HomePageVPAdapter vpAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_all_page, container, false);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //加載ViewPager數據
        loadVPData();


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        ViewPager vpAll = view.findViewById(R.id.vp_all_page);
        vpAdapter = new HomePageVPAdapter(vpDataList);
        vpAll.setAdapter(vpAdapter);
        vpAll.setOffscreenPageLimit(8);
        vpAdapter.setOnClickListener(new HomePageVPAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomePageVPRootData.VPDetailData data = vpDataList.get(position);
                Log.d(TAG, "onClick: ViewPager = " + data.getTitle());
            }
        });
    }


    private void loadVPData() {

        HttpUtils.getAsync(_URL.BANNER_DATA, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.d(TAG, "onSuccess: code = " + response.code());
                if (response.code() == 200) {
                    String jsonData = response.body();
                    Log.d(TAG, "onSuccess: jsonData = \n" + jsonData);
                    HomePageVPRootData vpRootData = new Gson().fromJson(jsonData, HomePageVPRootData.class);
                    if (vpRootData.getData() != null) {
                        if (vpDataList.size() > 0) {
                            vpDataList.clear();
                        }
                        vpDataList.addAll(vpRootData.getData());
                        if (vpAdapter != null) {
                            vpAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

}
