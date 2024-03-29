package com.pkasemer.worshipcenterdowntown.Utils;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class BetterPlayerCache {
    private static volatile SimpleCache instance;

    public static SimpleCache createCache(Context context, long cacheFileSize) {
        if (instance == null) {
            synchronized (BetterPlayerCache.class) {
                if (instance == null) {
                    instance = new SimpleCache(
                            new File(context.getCacheDir(), "MwonyaPlayerCache"),
                            new LeastRecentlyUsedCacheEvictor(cacheFileSize),
                            new StandaloneDatabaseProvider(context));
                }
            }
        }
        return instance;
    }

    public static void releaseCache() {
        try {
            if (instance != null) {
                instance.release();
                instance = null;
            }
        } catch (Exception exception) {
            Log.e("MwonyaPlayerCache", exception.toString());
        }
    }
}