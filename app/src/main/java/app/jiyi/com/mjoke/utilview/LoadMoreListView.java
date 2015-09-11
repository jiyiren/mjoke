package app.jiyi.com.mjoke.utilview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import app.jiyi.com.mjoke.R;

/**
 * Created by JIYI on 2015/8/31.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener{
    private int totalItem;
    private int lastItem;
    private View footer;
    private LayoutInflater layoutInflater;

    private boolean isloading=false;

    private OnLoadMore onLoadMore;

    public LoadMoreListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        layoutInflater=LayoutInflater.from(context);
        footer = layoutInflater.inflate(R.layout.listview_booter, null);
        footer.setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
            if ((this.totalItem == lastItem) && (scrollState == SCROLL_STATE_TOUCH_SCROLL)) {
//            Log.v("isLoading", "yes");
                if (!isloading) {
                    if(onLoadMore!=null) {
                        isloading = true;
                        footer.setVisibility(View.VISIBLE);
                        onLoadMore.loadMore();
                    }
                }
            }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem=firstVisibleItem+visibleItemCount;
        this.totalItem=totalItemCount;
    }

    public void setLoadMoreListen(OnLoadMore onLoadMore){
        this.onLoadMore = onLoadMore;
    }
    /**
     * 加载完成调用此方法
     */
    public void onLoadComplete(){
        footer.setVisibility(View.GONE);
        isloading = false;

    }

    public interface OnLoadMore{
        public void loadMore();
    }
}
