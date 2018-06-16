package com.esioner.oneread.utils;

/**
 * Created by Esioner on 2018/6/7.
 */

public class _URL {

    public static final String BASE_URL = "http://v3.wufazhuce.com:8000/api/";
    /**
     * 获取近十天图文集合ID的数组URL
     */
    public static String ARTICLE_PICTURE_URL = BASE_URL + "onelist/idlist";

    /**
     * 获取当天的图文集合信息以及当地天气信息
     *
     * @param location 位置
     * @return
     */
    public static String getArticleList(String location) {
        return BASE_URL + "channel/one/0/" + location;
    }

    /**
     * http://v3.wufazhuce.com:8000/api/feeds/list/2018-05?channel=cool
     * 获取往期列表
     *
     * @param dateString 格式：2018-05
     * @return
     */
    public static String getPastList(String dateString) {
        return BASE_URL + "feeds/list/" + dateString + "?channel=cool";
    }

    /**
     * 单击往期列表某一天列表的详情URL
     *
     * @param date
     * @return
     */
    public static String getOneDayArticleList(String date) {
        return BASE_URL + "channel/one/" + date + "/0";
    }

    /**
     * 获取详情
     */
    public static String getHtmlContent(int category, String itemId) throws Throwable {
        String categoryName;
        switch (category) {
            case 1:

                categoryName = "essay";
                break;
            case 2:
                categoryName = "serialcontent";
                break;
            case 3:
                categoryName = "question";
                break;
            case 4:
                categoryName = "music";
                break;
            case 5:
                categoryName = "essay";
                break;
            default:
                throw new Throwable("category 格式错误");
        }
        return BASE_URL + categoryName + "/htmlcontent/" + itemId;
    }

    /**
     * http://v3.wufazhuce.com:8000/api/music/htmlcontent/2678
     * 获取音乐详情
     *
     * @param itemId
     * @return
     */
    public static String getMusicHtml(String itemId) {
        return BASE_URL + "music/htmlcontent/" + itemId;
    }

    /**
     * http://v3.wufazhuce.com:8000/api/serial/list/
     * 获取连载列表
     * @param serialId
     * @return
     */
    public static String getSerialIdList(String serialId) {
        return BASE_URL + "serial/list/" + serialId;
    }
}
