package tv.huohua.peterson.misc;

final public class JavaLangUtils {
    public static String implode(final Object[] input, final String seperator) {
        final StringBuilder builder = new StringBuilder();
        if (input.length > 0) {
            builder.append(input[0]);
            for (int i = 1; i < input.length; i++) {
                builder.append(seperator);
                builder.append(input[i]);
            }
        }
        return builder.toString();
    }
}
