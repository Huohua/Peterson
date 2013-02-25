package tv.huohua.peterson.view;

import com.huohua.android.peterson.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FixedAspectRatioImageView extends ImageView {
    private float aspectRatio;
    private boolean verticalAppending;

    public FixedAspectRatioImageView(final Context context) {
        super(context);
    }

    public FixedAspectRatioImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public FixedAspectRatioImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioView);

        aspectRatio = a.getFloat(R.styleable.FixedAspectRatioView_aspectRatio, -1.0f);
        verticalAppending = a.getBoolean(R.styleable.FixedAspectRatioView_verticalAppending, true);

        a.recycle();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (aspectRatio < 0.0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (verticalAppending) {
            int calculatedHeight = (int) (originalWidth * aspectRatio);
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY));
        } else {
            int calculatedWidth = (int) (originalHeight / aspectRatio);
            super.onMeasure(MeasureSpec.makeMeasureSpec(calculatedWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
    }
}