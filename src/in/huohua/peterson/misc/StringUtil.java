package in.huohua.peterson.misc;

import java.util.List;

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

    static public String implode(List<String> strings, String glue) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings == null) return "";
        for (int i = 0; i < strings.size(); i++) {
            stringBuilder.append(strings.get(i));
            if (i != strings.size() - 1) {
                stringBuilder.append(glue);
            }
        }
        return stringBuilder.toString();
    }

}