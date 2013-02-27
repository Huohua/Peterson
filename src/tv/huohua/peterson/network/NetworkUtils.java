package tv.huohua.peterson.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

final public class NetworkUtils {
    static {
        HttpURLConnection.setFollowRedirects(true);
    }

    public static Map<String, List<String>> getQueryParams(final String url) {
        try {
            final Map<String, List<String>> params = new HashMap<String, List<String>>();
            final String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                final String query = urlParts[1];
                for (final String param : query.split("&")) {
                    final String[] pair = param.split("=");
                    final String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }
            return params;
        } catch (final UnsupportedEncodingException exception) {
            return new HashMap<String, List<String>>();
        }
    }

    public static HttpURLConnection getUrlConnection(final HttpRequest request) throws MalformedURLException,
            IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        conn.setConnectTimeout(40 * 1000);
        conn.setReadTimeout(30 * 1000);
        conn.setDoInput(true);
        if (!conn.getRequestMethod().equals(HttpRequest.HTTP_METHOD_GET)) {
            conn.setRequestMethod(request.getHttpMethod());
            if (request.getParams().size() > 0) {
                conn.setDoOutput(true);
                conn.getOutputStream().write(request.getParamsAsByteArray());
                conn.getOutputStream().flush();
                conn.getOutputStream().close();
            }
        }
        return conn;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private NetworkUtils() {
    }
}
