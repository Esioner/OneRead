package com.esioner.oneread.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.bean.HomePageVPRootData;

import java.util.List;

/**
 * Created by Esioner on 2018/6/19.
 */

public class HomePageVPAdapter extends PagerAdapter {
    private List<HomePageVPRootData.VPDetailData> vpDataList;
    private OnClickListener listener;

    public HomePageVPAdapter(List<HomePageVPRootData.VPDetailData> vpDataList) {
        this.vpDataList = vpDataList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        HomePageVPRootData.VPDetailData data = vpDataList.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_fragment_all_view_pager_item, container, false);
        Glide.with(container.getContext()).load(data.getCover()).into((ImageView) view.findViewById(R.id.iv_vp_item_image));
        ((TextView) view.findViewById(R.id.tv_vp_item_title)).setText(data.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = container.getChildAt(position);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return vpDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }
}
