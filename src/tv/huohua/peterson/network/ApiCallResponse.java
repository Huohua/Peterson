package tv.huohua.peterson.network;

import tv.huohua.peterson.api.IHHApi;

public class ApiCallResponse {
    private long accessTime;
    private IHHApi<?> api;
    private boolean isSucceeded;

    public long getAccessTime() {
        return accessTime;
    }

    public IHHApi<?> getApi() {
        return api;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setAccessTime(final long accessTime) {
        this.accessTime = accessTime;
    }

    public void setApi(final IHHApi<?> api) {
        this.api = api;
    }

    public void setSucceeded(final boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }
}
