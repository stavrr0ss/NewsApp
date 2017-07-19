package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Bogdan on 7/18/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl ;

    public NewsLoader (Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl==null){
            return null;
        }
        List<News> news = QueryUtils.fetchNews(mUrl);
        return news ;
    }
}
