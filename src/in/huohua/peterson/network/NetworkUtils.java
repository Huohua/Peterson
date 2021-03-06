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

package in.huohua.peterson.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

final public class NetworkUtils {
    final private static DefaultHttpClient HTTP_CLIENT;
    static {
        synchronized (NetworkUtils.class) {
            final DefaultHttpClient client = new DefaultHttpClient();
            final ClientConnectionManager mgr = client.getConnectionManager();
            final HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 40 * 1000);
            HttpConnectionParams.setSoTimeout(params, 30 * 1000);

            HTTP_CLIENT = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()),
                    params);
            HTTP_CLIENT.addRequestInterceptor(new HttpRequestInterceptor() {
                @Override
                public void process(org.apache.http.HttpRequest request, HttpContext context) throws HttpException,
                        IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
            HTTP_CLIENT.addResponseInterceptor(new HttpResponseInterceptor() {
                public void process(final HttpResponse response, final HttpContext context) throws HttpException,
                        IOException {
                    final HttpEntity entity = response.getEntity();
                    final Header encodingHeader = entity.getContentEncoding();
                    if (encodingHeader != null) {
                        final HeaderElement[] codecs = encodingHeader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            });
        }
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

    public static HttpResponse httpQuery(final HttpRequest request) throws ClientProtocolException, IOException {
        final HttpRequestBase requestBase;
        if (request.getHttpMethod().equals(HttpRequest.HTTP_METHOD_GET)) {
            final StringBuilder builder = new StringBuilder(request.getUrl());
            if (request.getParams() != null) {
                if (!request.getUrl().contains("?")) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(request.getParamsAsString());
            }
            final HttpGet get = new HttpGet(builder.toString());
            requestBase = get;
        } else if (request.getHttpMethod().equals(HttpRequest.HTTP_METHOD_POST)) {
            final HttpPost post = new HttpPost(request.getUrl());
            if (request.getEntity() != null) {
                post.setEntity(request.getEntity());
            } else {
                post.setEntity(new UrlEncodedFormEntity(request.getParamsAsList(), "UTF-8"));
            }
            requestBase = post;
        } else if (request.getHttpMethod().equals(HttpRequest.HTTP_METHOD_PUT)) {
            final HttpPut put = new HttpPut(request.getUrl());
            if (request.getEntity() != null) {
                put.setEntity(request.getEntity());
            } else {
                put.setEntity(new UrlEncodedFormEntity(request.getParamsAsList(), "UTF-8"));
            }
            requestBase = put;
        } else if (request.getHttpMethod().equals(HttpRequest.HTTP_METHOD_DELETE)) {
            final StringBuilder builder = new StringBuilder(request.getUrl());
            if (request.getParams() != null) {
                if (!request.getUrl().contains("?")) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(request.getParamsAsString());
            }
            final HttpDelete delete = new HttpDelete(builder.toString());
            requestBase = delete;
        } else {
            return null;
        }

        if (request.getHeaders() != null) {
            final Iterator<Map.Entry<String, String>> iterator = request.getHeaders().entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> kv = iterator.next();
                requestBase.setHeader(kv.getKey(), kv.getValue());
            }
        }
        return HTTP_CLIENT.execute(requestBase);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    
    public static boolean isWifiConnected(Context context) {
    	ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	return mWifi.isConnected();
    }

    private NetworkUtils() {
    }
}
