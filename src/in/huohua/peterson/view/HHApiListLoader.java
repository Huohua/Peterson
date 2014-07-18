package in.huohua.peterson.view;

import java.util.ArrayList;
import java.util.List;

import in.huohua.peterson.api.AbsListApi;
import in.huohua.peterson.api.ApiCallResponse;
import in.huohua.peterson.network.NetworkMgr;
import in.huohua.peterson.network.NetworkMgr.OnApiCallFinishedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_ListView;

public class HHApiListLoader<T> implements OnApiCallFinishedListener {
    public interface LoadingFinishedJudger<T> {
        boolean isLoadingFinished(ApiCallResponse<T> response);
    }

    public interface OnLoadListener {
        void onLoadingFailed();

        void onLoadingFinished();

        void onLoadingSucceeded();

        void onStartLoading();
    }

    static final private long LOADING_EXPIRE_TIME = 500;
    private int dataListOffset = 0;

    private IHHListAdapter<T> adapter;
    private long cacheExpireTime;
    private String cacheKey;
    private List<T> dataList;
    private boolean hasMoreData;
    private boolean isLoading;
    private boolean isLoadOnce;
    private long lastLoadingTime;
    private AbsListApi<T> listApi;
    final private AbsListView listView;
    final private PLA_ListView pla_ListView;
    private LoadingFinishedJudger<T> loadingFinishedJudger;
    private OnLoadListener onLoadListener;

    public HHApiListLoader(final IHHListAdapter<T> adapter, final AbsListView listView, final AbsListApi<T> listAPI) {
        this.listApi = listAPI;
        this.adapter = adapter;
        this.dataList = new ArrayList<T>();
        this.hasMoreData = true;
        this.isLoading = false;
        this.isLoadOnce = false;
        this.listView = listView;
        this.pla_ListView = null;
    }

    public HHApiListLoader(final IHHListAdapter<T> adapter, final PLA_ListView pla_ListView, final AbsListApi<T> listAPI) {
        this.listApi = listAPI;
        this.adapter = adapter;
        this.dataList = new ArrayList<T>();
        this.hasMoreData = true;
        this.isLoading = false;
        this.isLoadOnce = false;
        this.listView = null;
        this.pla_ListView = pla_ListView;
    }

    public void setDataListOffset(int dataListOffset) {
        this.dataListOffset = dataListOffset;
    }

    public long getCacheExpireTime() {
        return cacheExpireTime;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public List<T> getData() {
        return dataList;
    }

    public LoadingFinishedJudger<T> getLoadingFinishedJudger() {
        return loadingFinishedJudger;
    }

    public OnLoadListener getOnLoadListener() {
        return onLoadListener;
    }

    /*
     * Don't call this function too early (just call it before the first
     * loading).
     */
    public void init() {
        if (listView != null) {
            listView.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                        final int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount + 5 >= totalItemCount) {
                        loadData(false, true);
                    }
                }

                @Override
                public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                    // TODO Auto-generated method stub
                }
            });
        } else if (pla_ListView != null) {
            pla_ListView.setOnScrollListener(new com.huewu.pla.lib.internal.PLA_AbsListView.OnScrollListener() {
                @Override
                public void onScroll(PLA_AbsListView view, int firstVisibleItem, int visibleItemCount,
                        int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount + 5 >= totalItemCount) {
                        loadData(false, true);
                    }
                }

                @Override
                public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLoadOnce() {
        return isLoadOnce;
    }

    public void load() {
        loadData(false);
    }

    private void loadData(final boolean reload) {
        loadData(reload, false);
    }

    private void loadData(boolean reload, final boolean forceLoad) {
        if (isLoading || !hasMoreData || lastLoadingTime >= System.currentTimeMillis() - LOADING_EXPIRE_TIME) {
            return;
        }
        lastLoadingTime = System.currentTimeMillis();
        if (reload || forceLoad) {
            int offset = (reload) ? 0 : dataList.size();
            listApi.setOffset(offset);
            isLoading = true;
            /*
             * Be sure to call it before adding it to syncer (onStartLoading()
             * should be called before onLoadingFinished()).
             */
            if (onLoadListener != null) {
                onLoadListener.onStartLoading();
            }
            NetworkMgr.getInstance().startSync(listApi);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onApiCallFinished(final ApiCallResponse<?> response) {
        if (response.getApi() == listApi) {
            if (response.isSucceeded()) {
                final T[] result = (T[]) response.getData();
                boolean loadingFinished = (result != null && result.length < listApi.getLimit() / 2);
                if (loadingFinishedJudger != null) {
                    loadingFinished = loadingFinishedJudger.isLoadingFinished((ApiCallResponse<T>) response);
                }
                if (result != null) {
                    final int offset = listApi.getOffset();
                    while (dataList.size() - dataListOffset > offset) {
                        dataList.remove(offset + dataListOffset);
                    }
                    for (int i = 0; i < result.length; i++) {
                        dataList.add(result[i]);
                    }
                    if (setData()) {
                        loadingFinished = true;
                    }
                }
                if (onLoadListener != null) {
                    onLoadListener.onLoadingSucceeded();
                }
                if (isLoadOnce) {
                    loadingFinished = true;
                }
                if (loadingFinished) {
                    hasMoreData = false;
                    if (onLoadListener != null) {
                        onLoadListener.onLoadingFinished();
                    }
                }
            } else {
                if (onLoadListener != null) {
                    onLoadListener.onLoadingFailed();
                }
            }
            isLoading = false;
        }
    }

    public void reload() {
        loadData(true);
    }

    /**
     * @return if return true, the loadingFinished method would be called to
     *         interrupt the following request.
     */
    public boolean setData() {
        return adapter.setListData(dataList);
    }

    public boolean setData(final List<T> data) {
        this.dataList = data;
        return setData();
    }

    public void setLoadingFinishedJudger(final LoadingFinishedJudger<T> loadingFinishedJudger) {
        this.loadingFinishedJudger = loadingFinishedJudger;
    }

    public void setLoadOnce(boolean isLoadOnce) {
        this.isLoadOnce = isLoadOnce;
    }

    public void setOnLoadListener(final OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setUpCache(final String key, final long expireTime) {
        this.cacheKey = key;
        this.cacheExpireTime = expireTime;

        load();
    }
}