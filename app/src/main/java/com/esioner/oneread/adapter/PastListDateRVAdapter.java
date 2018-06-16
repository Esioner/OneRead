package com.esioner.oneread.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.oneread.R;
import com.esioner.oneread.bean.PastListData;

import java.util.List;

/**
 * Created by Esioner on 2018/6/10.
 */

public class PastListDateRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PastListData.PastDateData> pastDataList;
    private DateViewOnClickListener mListener;

    public PastListDateRVAdapter(Context context, List<PastListData.PastDateData> pastListDataList) {
        this.mContext = context;
        this.pastDataList = pastListDataList;
    }


    public class CommonViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate;
        public ImageView ivCover;
        public View view;

        public CommonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivCover = itemView.findViewById(R.id.iv_past_list_cover);
            tvDate = itemView.findViewById(R.id.tv_past_list_date);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_past_list_rv_date_item, null, false);
        holder = new CommonViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PastListData.PastDateData dateData = pastDataList.get(position);
        ((CommonViewHolder) holder).tvDate.setText(dateData.getDate());
        Glide.with(mContext).load(dateData.getCover()).into(((CommonViewHolder) holder).ivCover);
        ((CommonViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, dateData.getDate());
            }
        });

    }

    @Override
    public int getItemCount() {
        return pastDataList.size();
    }

    public void setDateViewOnClickListener(DateViewOnClickListener listener) {
        this.mListener = listener;
    }

    public interface DateViewOnClickListener {
        void onClick(View view, String date);
    }
}
