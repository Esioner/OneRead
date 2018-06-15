package com.esioner.oneread.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esioner.oneread.R;
import com.esioner.oneread.bean.PastDate;

import java.util.List;

/**
 * Created by Esioner on 2018/6/15.
 */

public class PastListMonthRVAdapter extends RecyclerView.Adapter<PastListMonthRVAdapter.ViewHolder> {
    private static final String TAG = PastListMonthRVAdapter.class.getSimpleName();
    private MonthViewOnClickListener mListener;
    private Context mContext;
    private List<PastDate> pastDateList;
    private LoadMoreListener loadMoreListener;

    public PastListMonthRVAdapter(Context context, List<PastDate> pastDates) {
        this.mContext = context;
        this.pastDateList = pastDates;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTimeLineDate;
        public RecyclerView rvMonthDate;
        public LinearLayout llTimeLine;

        public ViewHolder(View itemView) {
            super(itemView);
            View view = itemView;
            llTimeLine = itemView.findViewById(R.id.ll_past_list_time_line);
            tvTimeLineDate = itemView.findViewById(R.id.tv_time_line);
            rvMonthDate = itemView.findViewById(R.id.rv_past_list_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_past_list_time_line_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (pastDateList.get(position).getDate() != null) {
            holder.llTimeLine.setVisibility(View.VISIBLE);
            holder.tvTimeLineDate.setText(pastDateList.get(position).getDate());
        }
        PastListDateRVAdapter dateRVAdapter = new PastListDateRVAdapter(mContext, pastDateList.get(position).getPastDateDataList());
        final GridLayoutManager manager = new GridLayoutManager(mContext, 2);

        PastListDateRVAdapter.DateViewOnClickListener listener = new PastListDateRVAdapter.DateViewOnClickListener() {
            @Override
            public void onClick(View view, String date) {
                Log.d(TAG, "ViewOnClickListener.onClick: date = " + date);
//                getArticleList(_URL.getOneDayArticleList(date));
                mListener.onClick(view, date);
            }
        };
        dateRVAdapter.setDateViewOnClickListener(listener);


        holder.rvMonthDate.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                Log.d(TAG, "onScrolled: 可见范围内的第一项的位置" + firstVisibleItemPosition);
                Log.d(TAG, "onScrolled: 可见范围内的最后一项的位置" + lastVisibleItemPosition);
                Log.d(TAG, "onScrolled: recyclerview中的item的所有的数目" + itemCount);

                if (itemCount > 0 && lastVisibleItemPosition == (itemCount - 1)) {
                    //当最后一项也在可见范围加载更多
                    String lastDate = pastDateList.get(position).getPastDateDataList().get(lastVisibleItemPosition).getDate();
                    Log.d(TAG, "onScrolled: " + lastDate);

                    loadMoreListener.loadMore(lastDate);

                }
            }
        });

        holder.rvMonthDate.setLayoutManager(manager);
        //设置item间距，30dp
        holder.rvMonthDate.addItemDecoration(new SpaceItemDecoration(30));
        holder.rvMonthDate.setAdapter(dateRVAdapter);


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + pastDateList.size());
        return pastDateList.size();

    }

    public void setMonthViewOnClickListener(MonthViewOnClickListener listener) {
        this.mListener = listener;
    }

    public interface MonthViewOnClickListener {
        void onClick(View view, String date);
    }

    public void setOnLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public interface LoadMoreListener {
        void loadMore(String date);
    }

}
