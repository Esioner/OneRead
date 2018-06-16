package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Esioner on 2018/6/16.
 */

public class UnKnow {

    /**
     * Created by Esioner on 2018/6/15.
     */

    public class SerialIdListData {
        private List<com.esioner.oneread.bean.SerialIdListData.SerialIdData> data;
        private int res;

        public List<com.esioner.oneread.bean.SerialIdListData.SerialIdData> getData() {
            return data;
        }

        public void setData(List<com.esioner.oneread.bean.SerialIdListData.SerialIdData> data) {
            this.data = data;
        }

        public int getRes() {
            return res;
        }

        public void setRes(int res) {
            this.res = res;
        }

        public class SerialIdData {
            @SerializedName("author_list")
            private List<AuthorInfo> authorList;
            private int category;
            @SerializedName("content_id")
            private int contentId;
            private String cover;
            @SerializedName("serial_list")
            private String[] serialIds;
            private String title;

            public List<AuthorInfo> getAuthorList() {
                return authorList;
            }

            public void setAuthorList(List<AuthorInfo> authorList) {
                this.authorList = authorList;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public int getContentId() {
                return contentId;
            }

            public void setContentId(int contentId) {
                this.contentId = contentId;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String[] getSerialIds() {
                return serialIds;
            }

            public void setSerialIds(String[] serialIds) {
                this.serialIds = serialIds;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }

}
