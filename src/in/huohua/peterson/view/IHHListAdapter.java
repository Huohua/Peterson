package in.huohua.peterson.view;

import java.util.List;

public interface IHHListAdapter<T> {
    List<T> getListData();
    boolean setListData(List<T> data);
}
