package tv.huohua.peterson.misc;

import java.io.IOException;
import java.io.InputStream;

final public class IOUtils {
    public static String toString(final InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        StringBuilder builder = new StringBuilder();
        int numRead = 0;
        while ((numRead = inputStream.read(bytes)) >= 0) {
            builder.append(new String(bytes, 0, numRead));
        }
        return builder.toString();
    }

    private IOUtils() {
        // TODO empty method
    }
}
