package com.esioner.oneread.bean;

import java.util.List;

/**
 * Created by Esioner on 2018/6/15.
 */

public class PastDate {
    private String date;
    private List<PastListData.PastDateData> pastDateDataList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PastListData.PastDateData> getPastDateDataList() {
        return pastDateDataList;
    }

    public void setPastDateDataList(List<PastListData.PastDateData> pastDateDataList) {
        this.pastDateDataList = pastDateDataList;
    }
}
