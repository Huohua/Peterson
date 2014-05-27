/*******************************************************************************
 * Copyright (c) 2013 Zheng Sun.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Zheng Sun - initial API and implementation
 ******************************************************************************/

package in.huohua.peterson.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.huohua.android.peterson.R;

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
