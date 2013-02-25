package tv.huohua.peterson.widget;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.graphics.Bitmap;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class ImageAdapter extends BaseAdapter {
    public abstract int getDefaultImageResource();

    public void loadImage(final String uri, final ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(getDefaultImageResource())
                .showImageForEmptyUri(getDefaultImageResource()).resetViewBeforeLoading().cacheInMemory().cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory().cacheOnDisc().build();
        ImageLoader.getInstance().cancelDisplayTask(imageView);
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }
}
