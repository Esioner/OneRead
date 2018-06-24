package com.esioner.oneread.bean;

import java.util.List;

/**
 * Created by Esioner on 2018/6/19.
 */

public class AllPagerRVRootData {
    private List<TopicDetailData> data;
    private int res;
    /**
     * 本地属性
     * 3:表示是viewPager数据
     * 4:表示是纵向RV数据
     * 5:表示是横向RV数据
     */
    private int dataType;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<TopicDetailData> getData() {
        return data;
    }

    public void setData(List<TopicDetailData> data) {
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }


}
