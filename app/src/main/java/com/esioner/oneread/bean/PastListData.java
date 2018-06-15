package com.esioner.oneread.bean;

import java.util.List;

/**
 * Created by Esioner on 2018/6/10.
 */

public class PastListData {
    private List<PastDateData> data;
    private int res;

    public List<PastDateData> getData() {
        return data;
    }

    public void setData(List<PastDateData> data) {
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public class PastDateData {
        private String cover;
        private String date;
        private int id;

        private String timeLine;

        public String getTimeLine() {
            return timeLine;
        }

        public void setTimeLine(String timeLine) {
            this.timeLine = timeLine;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PastDateData) {
                return false;
            }
            PastDateData dateData = (PastDateData) obj;
            if (this.getDate().equals(dateData.getDate())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
