package com.pkasemer.worshipcenterdowntown.Utils;


import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class CacheDataSourceFactory implements DataSource.Factory {
    private Context context;
    private long maxFileSize, maxCacheSize;
    private static DataSource.Factory httpDataSourceFactory;
    private static DefaultDataSource upStreamSource;

    public CacheDataSourceFactory(
            Context context,
            long maxCacheSize,
            long maxFileSize) {
        super();
        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        upStreamSource = new DefaultDataSource.Factory(context, new DefaultHttpDataSource.Factory()).createDataSource();
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public CacheDataSource createDataSource() {
        SimpleCache betterPlayerCache = BetterPlayerCache.createCache(context, maxCacheSize);
//        streamFactory = new DefaultDataSource.Factory(context, getHttpDataSourceFactory(context));


        return new CacheDataSource(
                betterPlayerCache,
                upStreamSource,
                new FileDataSource(),
                new CacheDataSink(betterPlayerCache, maxFileSize),
//                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                null);
    }

    public static synchronized DataSource.Factory getHttpDataSourceFactory(Context context) {
        if (httpDataSourceFactory == null) {
            if (httpDataSourceFactory == null) {
                // We don't want to use Cronet, or we failed to instantiate a CronetEngine.
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieHandler.setDefault(cookieManager);
                httpDataSourceFactory = new DefaultHttpDataSource.Factory();
            }
        }
        return httpDataSourceFactory;
    }
}