package com.esioner.oneread.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.esioner.oneread.R;
import com.esioner.oneread.activity.WebViewActivity;
import com.esioner.oneread.bean.HomePageData;
import com.esioner.oneread.bean.ContentHtmlData;
import com.esioner.oneread.fragment.HomePageFragment;
import com.esioner.oneread.listener.DownloadListener;
import com.esioner.oneread.utils.ConstantValue;
import com.esioner.oneread.utils.FileUtils;
import com.esioner.oneread.utils.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Esioner on 2018/6/10.
 */

public class ContentRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = ContentRVAdapter.class.getSimpleName();
    /**
     * 图文
     */
    private static final int IMAGE_TEXT = 0;
    /**
     * 阅读
     */
    private static final int ESSAY = 1;
    /**
     * 连载
     */
    private static final int SERIAL = 2;
    /**
     * 问答
     */
    private static final int QUESTION = 3;
    /**
     * 音乐
     */
    private static final int MUSIC = 4;
    /**
     * 影视
     */
    private static final int MOVIE = 5;
    /**
     * 电台
     */
    private static final int RADIO = 8;
    /**
     * 时间线
     */
    private static final int TIME_LINE = 9;

    private List<HomePageData.Data.ContentData> contentDataList;
    private HomePageFragment homePageFragment;
    private Context mContext;

    public ContentRVAdapter(Context context, List<HomePageData.Data.ContentData> contentDatas, Fragment fragment) {
        this.mContext = context;
        this.homePageFragment = (HomePageFragment) fragment;
        this.contentDataList = contentDatas;
    }

    public class ImageWithTextViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWordInfo;
        public TextView tvForward;
        public TextView tvPicInfo;
        public TextView tvTitle;
        public ImageView ivPicture;

        public ImageWithTextViewHolder(View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_text_image_picture);
            tvTitle = itemView.findViewById(R.id.tv_text_image_title);
            tvPicInfo = itemView.findViewById(R.id.tv_text_image_pic_info);
            tvForward = itemView.findViewById(R.id.tv_text_image_forward);
            tvWordInfo = itemView.findViewById(R.id.tv_text_image_word_info);
        }
    }

    public class ArticleCommonViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvForward;
        public TextView tvTitle;
        public TextView tvAuthorName;
        public TextView tvCategoryTitle;

        public ArticleCommonViewHolder(View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tv_content_item_author_name);
            tvCategoryTitle = itemView.findViewById(R.id.tv_category_title);
            tvTitle = itemView.findViewById(R.id.tv_content_item_title);
            tvForward = itemView.findViewById(R.id.tv_content_item_forward);
            ivImage = itemView.findViewById(R.id.iv_content_item_image);
        }
    }


    public class MusicHolder extends RecyclerView.ViewHolder {

        public TextView tvMusicArticleTitle;
        public TextView tvMusicArticleaAuthorName;
        public TextView tvMusicCategoryTitle;
        public TextView tvMusicArticleForward;
        public CircleImageView ivMusicCover;

        public MusicHolder(View itemView) {
            super(itemView);
            tvMusicArticleTitle = itemView.findViewById(R.id.tv_music_article_title);
            tvMusicArticleaAuthorName = itemView.findViewById(R.id.tv_music_article_author_name);
            tvMusicCategoryTitle = itemView.findViewById(R.id.tv_music_category_title);
            tvMusicArticleForward = itemView.findViewById(R.id.tv_music_article_forward);
            ivMusicCover = itemView.findViewById(R.id.iv_music_cover_image);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case IMAGE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_text_image, parent, false);
                holder = new ImageWithTextViewHolder(view);
                break;
            case ESSAY:
            case SERIAL:
            case MOVIE:
            case QUESTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_common_view, parent, false);
                holder = new ArticleCommonViewHolder(view);
                break;
            case MUSIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_music, parent, false);
                holder = new MusicHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final HomePageData.Data.ContentData contentData = contentDataList.get(position);
        int category = Integer.parseInt(contentData.getCategory());
        if (holder instanceof ImageWithTextViewHolder) {
            Glide.with(mContext).load(contentData.getImgUrl()).into(((ImageWithTextViewHolder) holder).ivPicture);
            ((ImageWithTextViewHolder) holder).tvForward.setText(contentData.getForward());
            ((ImageWithTextViewHolder) holder).tvTitle.setText(contentData.getTitle());
            ((ImageWithTextViewHolder) holder).tvPicInfo.setText(contentData.getPicInfo());
            ((ImageWithTextViewHolder) holder).tvWordInfo.setText(contentData.getWordsInfo());
        } else if (holder instanceof ArticleCommonViewHolder) {

            String categoryTitle = "";
            switch (category) {
                case ESSAY:
                    categoryTitle = "- 阅读 -";
                    break;
                case SERIAL:
                    categoryTitle = "- 连载 -";
                    break;
                case QUESTION:
                    categoryTitle = "- 问答 -";
                    break;
                case MOVIE:
                    categoryTitle = "- 影视 -";
                    ((ArticleCommonViewHolder) holder).ivImage.setImageResource(R.drawable.movie_item_src);
                    break;
            }
            ((ArticleCommonViewHolder) holder).tvCategoryTitle.setText(categoryTitle);
            Glide.with(mContext).load(contentData.getImgUrl()).into(((ArticleCommonViewHolder) holder).ivImage);
            ((ArticleCommonViewHolder) holder).tvAuthorName.setText("文 / " + contentData.getAuthor().getUserName());
            ((ArticleCommonViewHolder) holder).tvForward.setText(contentData.getForward());
            ((ArticleCommonViewHolder) holder).tvTitle.setText(contentData.getTitle());
        } else if (holder instanceof MusicHolder) {
            ContentHtmlData data = homePageFragment.getMusicDetailData();
            if (data != null) {
                ((MusicHolder) holder).tvMusicArticleaAuthorName.setText("文 / " + data.getData().getAuthorInfoList().get(0).getUserName());
                ((MusicHolder) holder).tvMusicCategoryTitle.setText(data.getData().getMusicTitle());
            }
            ((MusicHolder) holder).tvMusicArticleTitle.setText(contentData.getTitle());
            Glide.with(mContext).load(contentData.getImgUrl()).into(((MusicHolder) holder).ivMusicCover);
            ((MusicHolder) holder).tvMusicArticleForward.setText(contentData.getForward());
        }
        if (category == ESSAY || category == QUESTION || category == SERIAL || category == MUSIC || category == MOVIE) {
            //点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("itemId", contentData.getItemId());
                    intent.putExtra("category", contentData.getCategory());
                    mContext.startActivity(intent);
                }
            });
        }
        if (category == IMAGE_TEXT) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext, R.style.Dialog_Fullscreen);

                    final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_text_detail, null, false);
                    TextView tvVOLId = view.findViewById(R.id.tv_vol_id);
                    TextView tvTittle = view.findViewById(R.id.tv_text_image_dialog_title);
                    TextView tvPicInfo = view.findViewById(R.id.tv_text_image_dialog_pic_info);
                    final ImageView iv = view.findViewById(R.id.iv_image_text_dialog_image);
                    tvVOLId.setText(contentData.getVolume());
                    tvTittle.setText(contentData.getTitle());
                    tvPicInfo.setText(contentData.getPicInfo());
                    Glide.with(mContext).load(contentData.getImgUrl()).into(iv);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    iv.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //弹出对话框
                            new MaterialDialog.Builder(mContext)
                                    .title("更多操作")
                                    .positiveText("保存图片")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @SuppressLint("CheckResult")
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Toast.makeText(mContext, "保存图片", Toast.LENGTH_SHORT).show();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String filePath = ConstantValue.FILE_PATH + File.separator + contentData.getPicInfo() + ".jpg";
                                                    HttpUtils.download(contentData.getImgUrl(), filePath, new DownloadListener() {
                                                        @Override
                                                        public void onStart() {
                                                            Log.d(TAG, "onStart: 开始下载");
                                                        }

                                                        @Override
                                                        public void onFinish(final String filePath) {
                                                            Log.d(TAG, "onFinish: 已完成");
                                                            homePageFragment.getmActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(mContext, "已成功下载至\n" + filePath, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onProgress(int progress) {
                                                            Log.d(TAG, "onProgress: progress = " + progress);
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Log.d(TAG, "onError: 任务出错");
                                                            e.printStackTrace();
                                                        }
                                                    });
                                                }
                                            }).start();

                                        }
                                    })
                                    .negativeText("取消")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                            return true;
                        }
                    });
                    dialog.setContentView(view);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setWindowAnimations(R.style.DialogEnterAndExitAnimation);//设置Dialog动画
                    dialog.show();
//                    dialog.getWindow().setBackgroundDrawable();
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
        String category = contentDataList.get(position).getCategory();
        int contentType = Integer.parseInt(category.trim());
//        Log.d(TAG, "getItemViewType: contentType = " + contentType);
        return contentType;
    }
}
