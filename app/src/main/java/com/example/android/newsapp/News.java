package com.example.android.newsapp;

/**
 * Created by Bogdan on 7/3/2017.
 */

public class News {
    private static String mTitle;
    private static String mSection;
    private static String mUrl;

    public News ( String title, String section, String url){
        mTitle = title;
        mSection = section ;
        mUrl = url;
    }

    public String getTitle(){
        return mTitle;
    }
    public String getSection(){
        return mSection;
    }
    public String getUrl(){
        return mUrl;
    }
}
