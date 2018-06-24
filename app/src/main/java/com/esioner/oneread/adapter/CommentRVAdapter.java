package com.esioner.oneread.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.bean.CommentRootData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Esioner on 2018/6/18.
 */

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.ViewHolder> {

    private String TAG = CommentRVAdapter.class.getSimpleName();
    private List<CommentRootData.CommentData.CommentDetailData> commentList;
    private Context mContext;

    public CommentRVAdapter(List<CommentRootData.CommentData.CommentDetailData> commentDetailDataList, Context context) {
        this.commentList = commentDetailDataList;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView ivUserHeaderImage;
        private final TextView tvUserName;
        private final TextView tvUserSnedTime;
        private final TextView tvToUserAndContent;
        private final TextView tvUserContent;
        private final LinearLayout llAgreeToUser;

        private final RelativeLayout rlCommentToUser;
        private final CheckBox cbAgreeTo;
        private final TextView tvAgreeCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUserHeaderImage = itemView.findViewById(R.id.iv_comment_view_user_header_image);
            tvUserName = itemView.findViewById(R.id.tv_comment_view_user_name);
            tvUserSnedTime = itemView.findViewById(R.id.tv_comment_view_send_time);
            tvToUserAndContent = itemView.findViewById(R.id.tv_comment_view_to_user_content);
            tvUserContent = itemView.findViewById(R.id.tv_comment_view_comment);
            llAgreeToUser = itemView.findViewById(R.id.ll_comment_view_praise);
            rlCommentToUser = itemView.findViewById(R.id.rl_comment_view_to_user);
            cbAgreeTo = itemView.findViewById(R.id.cb_comment_view_praise);
            tvAgreeCount = itemView.findViewById(R.id.tv_comment_view_agree_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_view_rv_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //初始化控件
        holder.llAgreeToUser.setClickable(false);
        holder.rlCommentToUser.setVisibility(View.GONE);


        CommentRootData.CommentData.CommentDetailData data = commentList.get(position);


        Log.d(TAG, "onBindViewHolder: data = \n" + data);
        int type = data.getType();
        Log.d(TAG, "onBindViewHolder: type = " + type);
        //表示有人回復這條回復
        if (type == 1) {
            holder.rlCommentToUser.setVisibility(View.VISIBLE);
            if (data.getToUser() != null) {
                String toUserName = data.getToUser().getUserName() == null ? "" : data.getToUser().getUserName();
                String getQuote = data.getQuote();
                holder.tvToUserAndContent.setText(toUserName + ":" + getQuote);
            }
            if (data.getQuote().equals("")) {
                holder.rlCommentToUser.setVisibility(View.GONE);
            }
        }
        String userName = data.getUser().getUserName() == null ? "" : data.getUser().getUserName();
        holder.tvUserName.setText(userName);

        holder.tvUserSnedTime.setText(data.getInputDate());

        holder.tvAgreeCount.setText(data.getPraiseNum() + "");
        holder.tvUserContent.setText(data.getContent());
        Glide.with(mContext).load(data.getUser().getWebUrl()).into(holder.ivUserHeaderImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
