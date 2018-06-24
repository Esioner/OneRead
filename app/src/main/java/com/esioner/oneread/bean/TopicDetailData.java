package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

public class TopicDetailData {

        private int category;
        @SerializedName("content_id")
        private String contentId;
        private String cover;
        private int id;
        @SerializedName("is_stick")
        private boolean isStick;
        @SerializedName("link_url")
        private String linkUrl;
        private String title;

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isStick() {
            return isStick;
        }

        public void setStick(boolean stick) {
            isStick = stick;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }