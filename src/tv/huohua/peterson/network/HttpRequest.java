package tv.huohua.peterson.network;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HttpRequest {
    final public static String HTTP_METHOD_DELETE = "DELETE";
    final public static String HTTP_METHOD_GET = "GET";
    final public static String HTTP_METHOD_POST = "POST";
    final public static String HTTP_METHOD_PUT = "PUT";

    final private String httpMethod;
    final private SortedMap<String, String> params;
    final private String url;

    public HttpRequest(final String url) {
        this(url, HTTP_METHOD_GET, new TreeMap<String, String>());
    }

    public HttpRequest(final String url, final String httpMethod) {
        this(url, httpMethod, new TreeMap<String, String>());
    }

    public HttpRequest(final String url, final String httpMethod, final SortedMap<String, String> params) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.params = params;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public SortedMap<String, String> getParams() {
        return params;
    }

    @SuppressWarnings("deprecation")
    public byte[] getParamsAsByteArray() {
        final StringBuilder builder = new StringBuilder();
        final Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, String> kv = iterator.next();
            builder.append(URLEncoder.encode(kv.getKey()));
            builder.append("=");
            builder.append(URLEncoder.encode(kv.getValue()));
            builder.append("&");
        }
        return builder.toString().getBytes();
    }

    public String getUrl() {
        return url;
    }
}
