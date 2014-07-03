package in.huohua.peterson.misc;

import android.text.TextUtils;

public class StringUtil {
	static public String implode(String[] strings, String glue) {
		StringBuilder stringBuilder = new StringBuilder();
        if (strings == null) return "";
		for (int i = 0; i < strings.length; i++) {
			stringBuilder.append(strings[i]);
		    if (i != strings.length - 1) {
		    	stringBuilder.append(glue);
		    }
		}
		return stringBuilder.toString();
	}
}