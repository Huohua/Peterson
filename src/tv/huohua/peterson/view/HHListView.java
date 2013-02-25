package tv.huohua.peterson.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class HHListView extends ListView {
	private boolean interceptTouchEventAllowed;

	public HHListView(final Context context) {
		super(context);
		init();
	}

	public HHListView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HHListView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		interceptTouchEventAllowed = true;
	}

	public boolean isInterceptTouchEventAllowed() {
		return interceptTouchEventAllowed;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		return (interceptTouchEventAllowed) ? super.onInterceptTouchEvent(event) : false;
	}

	public void setInterceptTouchEventAllowed(boolean interceptTouchEventAllowed) {
		this.interceptTouchEventAllowed = interceptTouchEventAllowed;
	}
}
