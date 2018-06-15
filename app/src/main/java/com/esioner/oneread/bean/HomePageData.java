package com.esioner.oneread.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Esioner on 2018/6/10.
 */

public class HomePageData {
    private Data data;
    private int res;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public class Data {
        @SerializedName("content_list")
        private List<ContentData> contentList;
        private String date;
        private String id;
        private ContentMenu menu;
        private Weather weather;

        public List<ContentData> getContentList() {
            return contentList;
        }

        public void setContentList(List<ContentData> contentList) {
            this.contentList = contentList;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ContentMenu getMenu() {
            return menu;
        }

        public void setMenu(ContentMenu menu) {
            this.menu = menu;
        }

        public Weather getWeather() {
            return weather;
        }

        public void setWeather(Weather weather) {
            this.weather = weather;
        }

        public class ContentMenu {
            private List<VolDetail> list;
            private String vol;

            public List<VolDetail> getList() {
                return list;
            }

            public void setList(List<VolDetail> list) {
                this.list = list;
            }

            public String getVol() {
                return vol;
            }

            public void setVol(String vol) {
                this.vol = vol;
            }

            public class VolDetail {
                @SerializedName("content_id")
                private String contentId;
                private String contentType;
                private String title;
                private ContentTag tag;

                public ContentTag getTag() {
                    return tag;
                }

                public void setTag(ContentTag tag) {
                    this.tag = tag;
                }

                public String getContentId() {
                    return contentId;
                }

                public void setContentId(String contentId) {
                    this.contentId = contentId;
                }

                public String getContentType() {
                    return contentType;
                }

                public void setContentType(String contentType) {
                    this.contentType = contentType;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public class ContentTag {
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

        public class Weather {
            @SerializedName("city_name")
            private String cityName;
            private String climate;
            private String temperature;
            private String date;
            private String humidity;
            private String hurricane;
            private WeatherIcons icons;
//            @SerializedName("wind_direction")
//            private List<WindDirection> windDirection;

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getClimate() {
                return climate;
            }

            public void setClimate(String climate) {
                this.climate = climate;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getHurricane() {
                return hurricane;
            }

            public void setHurricane(String hurricane) {
                this.hurricane = hurricane;
            }

            public WeatherIcons getIcons() {
                return icons;
            }

            public void setIcons(WeatherIcons icons) {
                this.icons = icons;
            }

            public class WeatherIcons {
                private String day;
                private String night;

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getNight() {
                    return night;
                }

                public void setNight(String night) {
                    this.night = night;
                }
            }

            public class WindDirection {

            }
        }

        public class ContentData {
            @SerializedName("ad_closetime")
            private String adCloseTime;
            @SerializedName("ad_id")
            private int adId;
            @SerializedName("ad_linkurl")
            private String adLinkUrl;
            @SerializedName("ad_makettime")
            private String adMaketTime;
            @SerializedName("ad_pvurl")
            private String adPvUrl;
            @SerializedName("ad_pvurl_vendor")
            private String adPvUrlVendor;
            @SerializedName("ad_share_cnt")
            private String adShareCnt;
            @SerializedName("ad_type")
            private int adType;
            @SerializedName("audio_platform")
            private int audioPlatform;
            @SerializedName("audio_url")
            private String audioUrl;
            @SerializedName("author")
            private Author author;
            @SerializedName("category")
            private String category;
            @SerializedName("content_bgcolor")
            private String contentBgColor;
            @SerializedName("content_id")
            private String contentId;
            @SerializedName("content_type")
            private String contentType;
            @SerializedName("display_category")
            private String displayCategory;
            @SerializedName("forward")
            private String forward;
            @SerializedName("has_reading")
            private int hasReading;
            @SerializedName("id")
            private String id;
            @SerializedName("img_url")
            private String imgUrl;
            @SerializedName("item_id")
            private String itemId;
            @SerializedName("last_update_date")
            private String lastUpdateDate;
            @SerializedName("like_count")
            private String likeCount;
            @SerializedName("movie_story_id")
            private int movieStoryId;
            @SerializedName("number")
            private int number;
            @SerializedName("pic_info")
            private String picInfo;
            @SerializedName("post_date")
            private String postDate;
            @SerializedName("serial_id")
            private int serialId;
            @SerializedName("serial_list")
            private String[] serialArr;
            @SerializedName("share_info")
            private ShareInfo shareInfo;
            @SerializedName("share_list")
            private ShareList shareList;
            @SerializedName("share_url")
            private String shareUrl;
            @SerializedName("start_video")
            private String startVideo;
            @SerializedName("subtitle")
            private String subtitle;
            @SerializedName("tag_list")
            private List<ContentMenu.VolDetail.ContentTag> tagList;
            @SerializedName("title")
            private String title;
            @SerializedName("video_url")
            private String videoUrl;
            @SerializedName("volume")
            private String volume;
            @SerializedName("words_info")
            private String wordsInfo;

            //本地时间线
            private String timeLineDate;

            public String getTimeLineDate() {
                return timeLineDate;
            }

            public void setTimeLineDate(String timeLineDate) {
                this.timeLineDate = timeLineDate;
            }

            public String getAdCloseTime() {
                return adCloseTime;
            }

            public void setAdCloseTime(String adCloseTime) {
                this.adCloseTime = adCloseTime;
            }

            public int getAdId() {
                return adId;
            }

            public void setAdId(int adId) {
                this.adId = adId;
            }

            public String getAdLinkUrl() {
                return adLinkUrl;
            }

            public void setAdLinkUrl(String adLinkUrl) {
                this.adLinkUrl = adLinkUrl;
            }

            public String getAdMaketTime() {
                return adMaketTime;
            }

            public void setAdMaketTime(String adMaketTime) {
                this.adMaketTime = adMaketTime;
            }

            public String getAdPvUrl() {
                return adPvUrl;
            }

            public void setAdPvUrl(String adPvUrl) {
                this.adPvUrl = adPvUrl;
            }

            public String getAdPvUrlVendor() {
                return adPvUrlVendor;
            }

            public void setAdPvUrlVendor(String adPvUrlVendor) {
                this.adPvUrlVendor = adPvUrlVendor;
            }

            public String getAdShareCnt() {
                return adShareCnt;
            }

            public void setAdShareCnt(String adShareCnt) {
                this.adShareCnt = adShareCnt;
            }

            public int getAdType() {
                return adType;
            }

            public void setAdType(int adType) {
                this.adType = adType;
            }

            public int getAudioPlatform() {
                return audioPlatform;
            }

            public void setAudioPlatform(int audioPlatform) {
                this.audioPlatform = audioPlatform;
            }

            public String getAudioUrl() {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl) {
                this.audioUrl = audioUrl;
            }

            public Author getAuthor() {
                return author;
            }

            public void setAuthor(Author author) {
                this.author = author;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getContentBgColor() {
                return contentBgColor;
            }

            public void setContentBgColor(String contentBgColor) {
                this.contentBgColor = contentBgColor;
            }

            public String getContentId() {
                return contentId;
            }

            public void setContentId(String contentId) {
                this.contentId = contentId;
            }

            public String getContentType() {
                return contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getDisplayCategory() {
                return displayCategory;
            }

            public void setDisplayCategory(String displayCategory) {
                this.displayCategory = displayCategory;
            }

            public String getForward() {
                return forward;
            }

            public void setForward(String forward) {
                this.forward = forward;
            }

            public int getHasReading() {
                return hasReading;
            }

            public void setHasReading(int hasReading) {
                this.hasReading = hasReading;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getLastUpdateDate() {
                return lastUpdateDate;
            }

            public void setLastUpdateDate(String lastUpdateDate) {
                this.lastUpdateDate = lastUpdateDate;
            }

            public String getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(String likeCount) {
                this.likeCount = likeCount;
            }

            public int getMovieStoryId() {
                return movieStoryId;
            }

            public void setMovieStoryId(int movieStoryId) {
                this.movieStoryId = movieStoryId;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getPicInfo() {
                return picInfo;
            }

            public void setPicInfo(String picInfo) {
                this.picInfo = picInfo;
            }

            public String getPostDate() {
                return postDate;
            }

            public void setPostDate(String postDate) {
                this.postDate = postDate;
            }

            public int getSerialId() {
                return serialId;
            }

            public void setSerialId(int serialId) {
                this.serialId = serialId;
            }

            public ShareInfo getShareInfo() {
                return shareInfo;
            }

            public void setShareInfo(ShareInfo shareInfo) {
                this.shareInfo = shareInfo;
            }

            public ShareList getShareList() {
                return shareList;
            }

            public void setShareList(ShareList shareList) {
                this.shareList = shareList;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }

            public String getStartVideo() {
                return startVideo;
            }

            public void setStartVideo(String startVideo) {
                this.startVideo = startVideo;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public List<ContentMenu.VolDetail.ContentTag> getTagList() {
                return tagList;
            }

            public void setTagList(List<ContentMenu.VolDetail.ContentTag> tagList) {
                this.tagList = tagList;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public String getWordsInfo() {
                return wordsInfo;
            }

            public void setWordsInfo(String wordsInfo) {
                this.wordsInfo = wordsInfo;
            }

            public String[] getSerialArr() {
                return serialArr;
            }

            public void setSerialArr(String[] serialArr) {
                this.serialArr = serialArr;
            }

            public class Author {
                private String desc;
                @SerializedName("fans_total")
                private String fansTotal;
                @SerializedName("is_settled")
                private String isSettled;
                @SerializedName("settled_type")
                private String settledType;
                @SerializedName("summary")
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

            public class ShareInfo {
                private String content;
                private String image;
                private String title;
                private String url;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public class ShareList {
                private ShareInfo qq;
                private ShareInfo weibo;
                private ShareInfo wx;
                @SerializedName("wx_timeline")
                private ShareInfo wxTimeLine;

                public ShareInfo getQq() {
                    return qq;
                }

                public void setQq(ShareInfo qq) {
                    this.qq = qq;
                }

                public ShareInfo getWeibo() {
                    return weibo;
                }

                public void setWeibo(ShareInfo weibo) {
                    this.weibo = weibo;
                }

                public ShareInfo getWx() {
                    return wx;
                }

                public void setWx(ShareInfo wx) {
                    this.wx = wx;
                }

                public ShareInfo getWxTimeLine() {
                    return wxTimeLine;
                }

                public void setWxTimeLine(ShareInfo wxTimeLine) {
                    this.wxTimeLine = wxTimeLine;
                }
            }
        }
    }


}
