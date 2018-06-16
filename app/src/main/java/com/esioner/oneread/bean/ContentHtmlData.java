package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Esioner on 2018/6/14.
 */

public class ContentHtmlData {
    private int res;

    private HtmlData data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public HtmlData getData() {
        return data;
    }

    public void setData(HtmlData data) {
        this.data = data;
    }

    public class HtmlData {
        /**
         * 朗读者
         */
        private String anchor;
        /**
         * 音频url
         */
        private String audio;
        @SerializedName("author_list")
        private List<AuthorInfo> authorInfoList;
        private int category;
        @SerializedName("commentnum")
        private int commentNum;
        @SerializedName("html_content")
        private String htmlContent;
        private String id;
        @SerializedName("music_exception")
        private String musicException;
        @SerializedName("music_id")
        private String musicId;
        private String platform;
        @SerializedName("platform_icon")
        private String platformIcon;
        @SerializedName("platform_name")
        private String platformName;
        @SerializedName("praisenum")
        private String praiseNum;
        @SerializedName("share_list")
        private HomePageData.Data.ContentData.ShareList shareList;
        @SerializedName("tag_list")
        private List<TagInfo> tagList;
        private String title;
        @SerializedName("web_url")
        private String webUrl;
        @SerializedName("serial_id")
        private String serialId;
        @SerializedName("serial_title")
        private String serialTitle;

        //本地属性
        private String musicTitle;

        public String getMusicTitle() {
            return musicTitle;
        }

        public void setMusicTitle(String musicTitle) {
            this.musicTitle = musicTitle;
        }

        public String getAnchor() {
            return anchor;
        }

        public void setAnchor(String anchor) {
            this.anchor = anchor;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public List<AuthorInfo> getAuthorInfoList() {
            return authorInfoList;
        }

        public void setAuthorInfoList(List<AuthorInfo> authorInfoList) {
            this.authorInfoList = authorInfoList;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getHtmlContent() {
            return htmlContent;
        }

        public void setHtmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMusicException() {
            return musicException;
        }

        public void setMusicException(String musicException) {
            this.musicException = musicException;
        }

        public String getMusicId() {
            return musicId;
        }

        public void setMusicId(String musicId) {
            this.musicId = musicId;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getPlatformIcon() {
            return platformIcon;
        }

        public void setPlatformIcon(String platformIcon) {
            this.platformIcon = platformIcon;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(String praiseNum) {
            this.praiseNum = praiseNum;
        }

        public HomePageData.Data.ContentData.ShareList getShareList() {
            return shareList;
        }

        public void setShareList(HomePageData.Data.ContentData.ShareList shareList) {
            this.shareList = shareList;
        }

        public List<TagInfo> getTagList() {
            return tagList;
        }

        public void setTagList(List<TagInfo> tagList) {
            this.tagList = tagList;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getSerialId() {
            return serialId;
        }

        public void setSerialId(String serialId) {
            this.serialId = serialId;
        }

        public String getSerialTitle() {
            return serialTitle;
        }

        public void setSerialTitle(String serialTitle) {
            this.serialTitle = serialTitle;
        }

        public class TagInfo {
            private String id;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
