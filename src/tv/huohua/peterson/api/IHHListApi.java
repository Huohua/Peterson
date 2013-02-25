package tv.huohua.peterson.api;

import java.util.List;

public interface IHHListApi<Data> extends IHHApi<List<Data>> {
    public int getOffset();

    public int getLimit();

    public void setLimit(int limit);

    public void setOffset(int offset);
}
