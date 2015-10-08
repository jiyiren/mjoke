package app.jiyi.com.mjoke.aty;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.utilview.LoadMoreListView;

/**
 * 个人中心中我的浏览和我的评论的父Activity
 */
public abstract class ListBaseActivity extends BaseActivity implements OnClickListener,LoadMoreListView.OnLoadMore{

    protected LoadMoreListView mListView;
    protected ArrayList<SingleJoke> mListDatas;
    protected ArrayList<SingleJoke> mTempDatas;
    protected MyApdater mAdapter;
    protected TextView tv_title;
    protected String userid;
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_base);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        userid= App.getAppInstance().getUserToken();
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        mListView= (LoadMoreListView) findViewById(R.id.list_base_lv);
        mListView.setLoadMoreListen(this);
        if(mListDatas==null){
            mListDatas=new ArrayList<SingleJoke>();
        }
        mAdapter=new MyApdater(this,mListDatas,R.layout.item_joke);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
        }
    }

    @Override
    public void loadMore() {
        getLoadMoreData();
    }

    public abstract void getLoadMoreData();

    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
    }
}
