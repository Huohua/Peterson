package tv.huohua.peterson.api;

import java.io.Serializable;

import android.content.Context;

public interface IHHApi<Data> extends Serializable{
	boolean call(Context context);

	Data getData();
}
