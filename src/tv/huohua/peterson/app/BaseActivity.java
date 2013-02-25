package tv.huohua.peterson.app;

import java.io.File;

import tv.huohua.peterson.network.NetworkMgr;


import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.URLConnectionImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    private static boolean managersInitialized = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!managersInitialized) {
            managersInitialized = true;

            NetworkMgr.init(getApplicationContext());

            final File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "image/");
            final ImageLoader imageLoader = ImageLoader.getInstance();
            final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
                    .build();
            final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .memoryCacheExtraOptions(480, 800).discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
                    .threadPoolSize(2).threadPriority(Thread.MIN_PRIORITY).denyCacheImageMultipleSizesInMemory()
                    .offOutOfMemoryHandling().memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                    .discCache(new FileCountLimitedDiscCache(cacheDir, 512))
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .imageDownloader(new URLConnectionImageDownloader(10 * 1000, 30 * 1000))
                    .tasksProcessingOrder(QueueProcessingType.FIFO).defaultDisplayImageOptions(defaultOptions).build();
            imageLoader.init(config);
        }
    }
}
