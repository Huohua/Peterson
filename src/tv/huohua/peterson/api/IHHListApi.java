package tv.huohua.peterson.api;

public interface IHHListApi extends IHHApi {
    public int getOffset();

    public int getLimit();

    public void setLimit(int limit);

    public void setOffset(int offset);
}
