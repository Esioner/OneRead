package com.esioner.oneread.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.activity.WebViewActivity;
import com.esioner.oneread.bean.AllPagerRVRootData;
import com.esioner.oneread.bean.TopicDetailData;
import com.esioner.oneread.utils.ConstantValue;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esioner on 2018/6/22.
 */

public class AllPagerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AllPagerRVAdapter.class.getSimpleName();
    private Context mContext;
    private List<AllPagerRVRootData> allDataList;
    /**
     * 轮播图的标题
     */
    private List<String> bannerTitles = new ArrayList<>();
    /**
     * 轮播图的图片
     */
    private List<String> bannerImages = new ArrayList<>();

    public AllPagerRVAdapter(Context context, List<AllPagerRVRootData> allDataList) {
        this.mContext = context;
        this.allDataList = allDataList;
    }

    /**
     * 轮播图的 ViewHolder
     */
    private class BannerViewHolder extends RecyclerView.ViewHolder {

        private final Banner banner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner_all_page);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case ConstantValue.TYPE_VIEWPAGER_DATA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fragment_all_pager_topic_rv_banner, parent, false);
                holder = new BannerViewHolder(view);
                break;
            case ConstantValue.TYPE_VERTICAL_RV_DATA:
                break;
            case ConstantValue.TYPE_HORIZONTAL_RV_DATA:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final List<TopicDetailData> topicDataList = allDataList.get(position).getData();
        if (holder instanceof BannerViewHolder) {
            for (TopicDetailData data : topicDataList) {
                if (bannerImages.size() > 0 || bannerTitles.size() > 0) {
                    bannerTitles.clear();
                    bannerImages.clear();
                }
                bannerImages.add(data.getCover());
                bannerTitles.add(data.getTitle());
                Log.d(TAG, "onBindViewHolder: cover = " + data.getCover());
                Log.d(TAG, "onBindViewHolder: title = " + data.getTitle());
            }

            Banner banner = ((BannerViewHolder) holder).banner;
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Log.d(TAG, "OnBannerClick: banner : title = " + topicDataList.get(position).getTitle());
                    Log.d(TAG, "OnBannerClick: banner : category = " + topicDataList.get(position).getCategory());
                    Log.d(TAG, "OnBannerClick: banner : position = " + position);
                    Log.d(TAG, "OnBannerClick: banner : contentId = " + topicDataList.get(position).getContentId());
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("itemId", topicDataList.get(position).getContentId() + "");
                    intent.putExtra("category", topicDataList.get(position).getCategory() + "");
                    mContext.startActivity(intent);

                }
            });
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片加载器
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(context).load(path).into(imageView);
                }
            });
            //设置图片集合
            banner.setImages(bannerImages);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置标题集合（当banner样式有显示title时）
            banner.setBannerTitles(bannerTitles);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

//    /**
//     * 初始化轮播图
//     *
//     * @param bannerImages
//     * @param bannerTitles
//     */
//    private void initBanner(List<String> bannerImages, List<String> bannerTitles) {
//

//    }
//

    @Override
    public int getItemCount() {
        return allDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: viewType = " + allDataList.get(position).getDataType());
        return allDataList.get(position).getDataType();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
