package tv.huohua.peterson.api;

import java.io.Serializable;

import android.content.Context;

public interface IHHApi extends Serializable{
	ApiCallResponse call(Context context);
}
