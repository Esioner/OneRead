package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

public class AuthorInfo {
    private String desc;
    @SerializedName("fans_total")
    private String fansTotal;
    @SerializedName("is_settled")
    private String isSettled;
    @SerializedName("settled_type")
    private String settledType;
    private String summary;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("wb_name")
    private String wbName;
    @SerializedName("web_url")
    private String webUrl;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFansTotal() {
        return fansTotal;
    }

    public void setFansTotal(String fansTotal) {
        this.fansTotal = fansTotal;
    }

    public String getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(String isSettled) {
        this.isSettled = isSettled;
    }

    public String getSettledType() {
        return settledType;
    }

    public void setSettledType(String settledType) {
        this.settledType = settledType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

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

    public String getWbName() {
        return wbName;
    }

    public void setWbName(String wbName) {
        this.wbName = wbName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}