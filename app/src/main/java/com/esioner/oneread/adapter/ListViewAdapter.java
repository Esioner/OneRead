package com.esioner.oneread.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esioner.oneread.R;
import com.esioner.oneread.bean.HomePageData;

import java.util.List;

/**
 * Created by Esioner on 2018/6/17.
 */

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = ListViewAdapter.class.getSimpleName();
    private List<HomePageData.Data.ContentMenu.VolDetail> volList;

    public ListViewAdapter(List<HomePageData.Data.ContentMenu.VolDetail> menuList) {
        this.volList = menuList;
    }

    @Override
    public int getCount() {
        return volList.size();
    }

    @Override
    public Object getItem(int position) {
        return volList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomePageData.Data.ContentMenu.VolDetail volDetail = volList.get(position);
        HomePageData.Data.ContentMenu.VolDetail.ContentTag contentTag = volDetail.getTag();
        String tag;
        if (contentTag != null) {
            tag = contentTag.getTitle();
        } else {
            Log.d(TAG, "getView: getContentType = " + volDetail.getContentType());
            tag = getTagByType(volDetail.getContentType());
        }
        ViewHolder lvHolder;
        if (convertView == null) {
            lvHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_listview_item, parent, false);
            lvHolder.tvTag = convertView.findViewById(R.id.tv_hp_list_item_tag);
            lvHolder.tvTitle = convertView.findViewById(R.id.tv_hp_list_item_title);
            convertView.setTag(lvHolder);
        } else {
            lvHolder = (ViewHolder) convertView.getTag();
        }
        lvHolder.tvTitle.setText(volList.get(position).getTitle());
        lvHolder.tvTag.setText(tag);
        Log.d(TAG, "getView: tvTitle = " + volList.get(position).getTitle());
        Log.d(TAG, "getView: tvTag = " + tag);
        return convertView;
    }

    class ViewHolder {
        TextView tvTag;
        TextView tvTitle;
    }

    public String getTagByType(String type) {
        int i = Integer.parseInt(type);
        Log.d(TAG, "getTagByType: i = " + i);
        String tag = "";
        switch (i) {
            case 1:
                tag = "阅读";
                break;
            case 2:
                tag = "连载";
                break;
            case 3:
                tag = "问答";
                break;
            case 4:
                tag = "音乐";
                break;
            case 5:
                tag = "影视";
                break;
            case 8:
                tag = "电台";
                break;
            default:
                return "";
        }
        return tag;
    }
}
