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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequest {
    final public static String HTTP_METHOD_DELETE = "DELETE";
    final public static String HTTP_METHOD_GET = "GET";
    final public static String HTTP_METHOD_POST = "POST";
    final public static String HTTP_METHOD_PUT = "PUT";

    private HttpEntity entity;
    private SortedMap<String, String> headers;
    final private String httpMethod;
    private SortedMap<String, String> params;
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
        this.headers = new TreeMap<String, String>();
    }

    public void addHeader(final String name, final String value) {
        headers.put(name, value);
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public String getHeader(final String name) {
        return headers.get(name);
    }

    public SortedMap<String, String> getHeaders() {
        return headers;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public SortedMap<String, String> getParams() {
        return params;
    }

    public byte[] getParamsAsByteArray() {
        return getParamsAsString().getBytes();
    }

    public List<NameValuePair> getParamsAsList() {
        final List<NameValuePair> result = new ArrayList<NameValuePair>();
        if (params != null) {
            final Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> kv = iterator.next();
                result.add(new BasicNameValuePair(kv.getKey(), kv.getValue()));
            }
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public String getParamsAsString() {
        if (params == null) {
            return "";
        } else {
            final StringBuilder builder = new StringBuilder();
            final Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> kv = iterator.next();
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(URLEncoder.encode(kv.getKey()));
                builder.append("=");
                builder.append(URLEncoder.encode(kv.getValue()));
            }
            return builder.toString();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    public void setHeaders(final SortedMap<String, String> headers) {
        this.headers = headers;
    }

    public void setParams(final SortedMap<String, String> params) {
        this.params = params;
    }
}
