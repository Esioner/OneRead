package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Esioner on 2018/6/18.
 */

public class CommentRootData {
    private CommentData data;
    private int res;

    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public class CommentData {
        private int count;
        private List<CommentDetailData> data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<CommentDetailData> getData() {
            return data;
        }

        public void setData(List<CommentDetailData> data) {
            this.data = data;
        }

        public class CommentDetailData {
            private String id;
            private String quote;
            public String content;
            @SerializedName("praisenum")
            private int praiseNum;
            @SerializedName("device_token")
            private String deviceToken;
            @SerializedName("del_flag")
            private String delFlag;
            private int reviewed;
            @SerializedName("input_date")
            private String inputDate;
            @SerializedName("created_at")
            private String createdAt;
            /**
             * 有人回復 ,type = 1;沒人回復 type = 0；
             */
            private int type;
            @SerializedName("updated_at")
            private String updatedAt;
            @SerializedName("user_info_id")
            private String userInfoId;
            private UserInfo user;
            @SerializedName("touser")
            private UserInfo toUser;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuote() {
                return quote;
            }

            public void setQuote(String quote) {
                this.quote = quote;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPraiseNum() {
                return praiseNum;
            }

            public void setPraiseNum(int praiseNum) {
                this.praiseNum = praiseNum;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public String getDelFlag() {
                return delFlag;
            }

            public void setDelFlag(String delFlag) {
                this.delFlag = delFlag;
            }

            public int getReviewed() {
                return reviewed;
            }

            public void setReviewed(int reviewed) {
                this.reviewed = reviewed;
            }

            public String getInputDate() {
                return inputDate;
            }

            public void setInputDate(String inputDate) {
                this.inputDate = inputDate;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(String userInfoId) {
                this.userInfoId = userInfoId;
            }

            public UserInfo getUser() {
                return user;
            }

            public void setUser(UserInfo user) {
                this.user = user;
            }

            public UserInfo getToUser() {
                return toUser;
            }

            public void setToUser(UserInfo toUser) {
                this.toUser = toUser;
            }

            @Override
            public String toString() {
                return "CommentDetailData{" +
                        "id='" + id + '\'' +
                        ", quote='" + quote + '\'' +
                        ", content='" + content + '\'' +
                        ", praiseNum=" + praiseNum +
                        ", deviceToken='" + deviceToken + '\'' +
                        ", delFlag='" + delFlag + '\'' +
                        ", reviewed=" + reviewed +
                        ", inputDate='" + inputDate + '\'' +
                        ", createdAt='" + createdAt + '\'' +
                        ", type=" + type +
                        ", updatedAt='" + updatedAt + '\'' +
                        ", userInfoId='" + userInfoId + '\'' +
                        ", user=" + user +
                        ", toUser=" + toUser +
                        '}';
            }

            public class UserInfo {
                @SerializedName("user_id")
                private String userId;
                @SerializedName("user_name")
                private String userName;
                @SerializedName("web_url")
                private String webUrl;

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getWebUrl() {
                    return webUrl;
                }

                public void setWebUrl(String webUrl) {
                    this.webUrl = webUrl;
                }

                @Override
                public String toString() {
                    return "UserInfo{" +
                            "userId='" + userId + '\'' +
                            ", userName='" + userName + '\'' +
                            ", webUrl='" + webUrl + '\'' +
                            '}';
                }
            }
        }
    }
}
