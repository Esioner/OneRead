package com.esioner.oneread.bean;

import java.util.List;

/**
 * Created by Esioner on 2018/6/24.
 */

public class ReadingRootData {
    private List<HomePageData.Data.ContentData> data;
    private int res;

    public List<HomePageData.Data.ContentData> getData() {
        return data;
    }

    public void setData(List<HomePageData.Data.ContentData> data) {
        this.data = data;
    }
}
