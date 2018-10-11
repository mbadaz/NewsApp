package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<Article>> {
    private final String url;

    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Nullable
    @Override
    public List<Article> loadInBackground() {
        return new HttpQueryUtils().queryUrl(url);
    }

}
