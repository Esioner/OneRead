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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.activity.WebViewActivity;
import com.esioner.oneread.bean.HomePageData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Esioner on 2018/6/24.
 */

public class TabRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private String TAG = TabRVAdapter.class.getSimpleName();
    private Context mContext;
    private List<HomePageData.Data.ContentData> contentDataList;

    /**
     * 阅读
     */
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthorName;
        private final TextView tvPostTime;
        private final TextView tvTitle;
        private final TextView tvForward;
        private final ImageView ivArticleImage;
        private final ImageView ivMusicImage;
        private final ImageView ivMovieImage;
        private final CircleImageView ivUserImage;
        private final View view;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvAuthorName = itemView.findViewById(R.id.tv_tab_rv_item_author_name);
            tvPostTime = itemView.findViewById(R.id.tv_tab_rv_last_post_time);
            tvTitle = itemView.findViewById(R.id.tv_tab_rv_item_title);
            tvForward = itemView.findViewById(R.id.tv_tab_rv_item_forward);
            ivArticleImage = itemView.findViewById(R.id.iv_tab_rv_item_article_image);
            ivMusicImage = itemView.findViewById(R.id.iv_tab_rv_item_music_image);
            ivMovieImage = itemView.findViewById(R.id.iv_tab_rv_item_movie_image);
            ivUserImage = itemView.findViewById(R.id.iv_tab_rv_item_author_image);
        }
    }

    public TabRVAdapter(Context context, List<HomePageData.Data.ContentData> data) {
        this.mContext = context;
        this.contentDataList = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_tab_rv_artile_item, parent, false);
                holder = new ArticleViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final HomePageData.Data.ContentData data = contentDataList.get(position);
        int category = Integer.parseInt(data.getCategory());


//        Log.d(TAG, "onBindViewHolder: ");
        //当是文章阅读列表的时候
        if (holder instanceof ArticleViewHolder) {

            ArticleViewHolder articleHolder = (ArticleViewHolder) holder;
            //初始化重置可见状态
            articleHolder.ivMusicImage.setVisibility(View.GONE);
            articleHolder.ivArticleImage.setVisibility(View.GONE);
            articleHolder.ivMovieImage.setVisibility(View.GONE);

            ImageView iv = null;
            if (category == 1) {
                articleHolder.ivArticleImage.setVisibility(View.VISIBLE);
                iv = articleHolder.ivArticleImage;
            } else if (category == 4) {
                articleHolder.ivMusicImage.setVisibility(View.VISIBLE);
                iv = articleHolder.ivMusicImage;
            } else if (category == 5) {
                articleHolder.ivMovieImage.setVisibility(View.VISIBLE);
                iv = articleHolder.ivMovieImage;
            }
            Glide.with(mContext).load(data.getImgUrl()).into(iv);
            Log.d(TAG, "onBindViewHolder: title = " + data.getTitle());
            Log.d(TAG, "onBindViewHolder: imgUrl = " + data.getImgUrl());

            Glide.with(mContext).load(data.getAuthor().getWebUrl()).into(articleHolder.ivUserImage);
            articleHolder.tvAuthorName.setText(data.getAuthor().getUserName());
            articleHolder.tvPostTime.setText(data.getLastUpdateDate());
            articleHolder.tvForward.setText(data.getForward());
            articleHolder.tvTitle.setText(data.getTitle());

            articleHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + data.getContentId());
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("itemId", data.getItemId());
                    intent.putExtra("category", data.getCategory());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contentDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int category = Integer.parseInt(contentDataList.get(position).getCategory());
        return category;
    }
}
