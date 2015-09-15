package app.jiyi.com.mjoke.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.aty.MyApdater;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.net.GetJokesNet;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.LoadMoreListView;


public class Tab04Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,LoadMoreListView.OnLoadMore{
    public static int pagers_count_per_second=5;
    private boolean isonce=true;
    private App mapp;
    private View mview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreListView mListView;
    private ArrayList<SingleJoke> mListDatas;

    private ArrayList<SingleJoke> mTempDatas;
    private MyApdater mAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab04,null);
        mapp=App.getAppInstance();
        initView();
        return mview;
    }

    private void initView() {
        swipeRefreshLayout= (SwipeRefreshLayout) mview.findViewById(R.id.tab04_swipe_ly);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        mListView= (LoadMoreListView) mview.findViewById(R.id.tab04_lv);
        mListView.setLoadMoreListen(this);
        if(mListDatas==null){
            mListDatas=new ArrayList<SingleJoke>();
        }
        mAdapter = new MyApdater(getActivity(), mListDatas, R.layout.item_joke);
        mListView.setAdapter(mAdapter);
        if(isonce) {
            swipeRefreshLayout.setRefreshing(true);
            refresh();
            isonce=false;
        }
    }


    public static final int GETJOKE_SUCCESS=1;
    public static final int GETJOKE_FAIL=2;
    public static final int LOADMORE_GETJOKE_SUCCESS=3;
    public static final int LOADMORE_GETJOKE_FAIL=4;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
            switch (msg.what){
                case GETJOKE_SUCCESS:
                    if(mListDatas.size()<=0){
                        mListDatas.addAll(0,mTempDatas);
                    }else {
                        for (int i = 0; i < mTempDatas.size(); i++) {
                            mListDatas.add(0, mTempDatas.get(i));
                            mListDatas.remove(mListDatas.size()-1);//每添加一条就删掉最后一条，保持Listview数据集大小不超过5个
                        }
                    }
                    mTempDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    MyLog.i("jiyiren", "listsize:" + mListDatas.size());
//                    swipeRefreshLayout.setRefreshing(false);
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
//                    ShowToast.show(getActivity(), "更新数据成功！");
                    break;
                case GETJOKE_FAIL:
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    ShowToast.show(getActivity(), "暂时····还没有新的哦！");
                    break;
                case LOADMORE_GETJOKE_SUCCESS:
                    if(mTempDatas!=null&&mTempDatas.size()>0) {
//                        if(mListDatas.size()>=10){
//                            MyLog.i("jiyiren","原来："+mListDatas.get(0).toString());
//                            for(int i=0;i<5;i++){
//                                mListDatas.remove(0);
//                            }
//                            MyLog.i("jiyiren","现在:"+mListDatas.get(0).toString());
//                        }
                        mListDatas.addAll(mListDatas.size(), mTempDatas);
                        mTempDatas.clear();
                        mAdapter.notifyDataSetChanged();
                        MyLog.i("jiyiren","loadmore_size:"+mListDatas.size());
                    }else{
                        mHandler.sendEmptyMessage(LOADMORE_GETJOKE_FAIL);
                    }
                    mListView.onLoadComplete();
//                    ShowToast.show(getActivity(), "加载数据成功！");
                    break;
                case LOADMORE_GETJOKE_FAIL:
                    mListView.onLoadComplete();
                    ShowToast.show(getActivity(), "没有更多啦~~");
                    break;
            }
        }
    };


    //下拉刷新数据
    private void getRefreshData(String joke_id,int count){
        new GetJokesNet(joke_id, MyConfig.SCROLL_REFRESH, count,MyConfig.FRAGMENT_TYPE_GIRL,new GetJokesNet.SuccessGetJokeCallback() {
            @Override
            public void onSuccess(String result) {
//                MyLog.i("jiyihahah",""+result);
                String res= MyUtils.getGson(result, MyConfig.KEY_RESULT);
                if(res.equals(MyConfig.RESULT_SUCCESS)){
                    try {
                        JSONObject re=new JSONObject(result);
                        JSONArray data=re.getJSONArray(MyConfig.KEY_DATAS);
                        if(mTempDatas==null){
                            mTempDatas=new ArrayList<SingleJoke>();
                        }else {
                            mTempDatas.clear();
                        }
                        for (int i=0;i<data.length();i++){
                            JSONObject joke= (JSONObject) data.get(i);
                            SingleJoke sjoke=new SingleJoke();
                            String jokeid=joke.getString(MyConfig.JOKE_ID);
                            sjoke.setJoke_id(jokeid);
                            sjoke.setUser_id(joke.getString(MyConfig.JOKE_USER_ID));
                            sjoke.setUsername(joke.getString(MyConfig.JOKE_USERNAME));
                            sjoke.setSex(joke.getString(MyConfig.JOKE_USERSEX));
                            sjoke.setType(joke.getString(MyConfig.JOKE_TYPE));
                            sjoke.setCreatetime(joke.getString(MyConfig.JOKE_TIME));
                            sjoke.setContent(joke.getString(MyConfig.JOKE_CONTENT));
                            sjoke.setImgurl(joke.getString(MyConfig.JOKE_IMGURL));
                            sjoke.setIshasing(joke.getString(MyConfig.JOKE_ISHASIMG));
                            sjoke.setShare_count(joke.getString(MyConfig.JOKE_SHARE));
                            sjoke.setCollect_count(joke.getString(MyConfig.JOKE_COLLECT));
                            sjoke.setLook_count(joke.getString(MyConfig.JOKE_LOOK));
                            sjoke.setIscollect(0);
                            sjoke.setIszan(-1);
                            mTempDatas.add(sjoke);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Gson g=new Gson();
//                    mListDatas=g.fromJson(data, new TypeToken<List<SingleJoke>>(){}.getType());
                    mHandler.sendEmptyMessage(GETJOKE_SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(GETJOKE_FAIL);
                }
            }
        }, new GetJokesNet.FailGetJokeCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(GETJOKE_FAIL);
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh(){
        String id=null;
        if(mListDatas!=null&&mListDatas.size()>0) {
            id= mListDatas.get(0).getJoke_id();
        }else{
            id="0";
        }
        MyLog.i("jiyiren","id:"+id);
        getRefreshData(id, pagers_count_per_second);
    }

    @Override
    public void onResume() {
        // refresh();
        super.onResume();
    }

    @Override
    public void loadMore() {
        getMoreData(mListDatas.get(mListDatas.size()-1).getJoke_id(),pagers_count_per_second);
    }


    private void getMoreData(String joke_id,int count){
        new GetJokesNet(joke_id, MyConfig.SCROLL_LOADINGMORE, count,MyConfig.FRAGMENT_TYPE_GIRL,new GetJokesNet.SuccessGetJokeCallback() {
            @Override
            public void onSuccess(String result) {
                String res= MyUtils.getGson(result,MyConfig.KEY_RESULT);
                if(res.equals(MyConfig.RESULT_SUCCESS)){
                    try {
                        JSONObject re=new JSONObject(result);
                        JSONArray data=re.getJSONArray(MyConfig.KEY_DATAS);
                        if(mTempDatas==null){
                            mTempDatas=new ArrayList<SingleJoke>();
                        }else {
                            mTempDatas.clear();
                        }
                        for (int i=0;i<data.length();i++){
                            JSONObject joke= (JSONObject) data.get(i);
                            SingleJoke sjoke=new SingleJoke();
                            String jokeid=joke.getString(MyConfig.JOKE_ID);
                            sjoke.setJoke_id(jokeid);
                            sjoke.setUser_id(joke.getString(MyConfig.JOKE_USER_ID));
                            sjoke.setUsername(joke.getString(MyConfig.JOKE_USERNAME));
                            sjoke.setSex(joke.getString(MyConfig.JOKE_USERSEX));
                            sjoke.setType(joke.getString(MyConfig.JOKE_TYPE));
                            sjoke.setCreatetime(joke.getString(MyConfig.JOKE_TIME));
                            sjoke.setContent(joke.getString(MyConfig.JOKE_CONTENT));
                            sjoke.setImgurl(joke.getString(MyConfig.JOKE_IMGURL));
                            sjoke.setIshasing(joke.getString(MyConfig.JOKE_ISHASIMG));
                            sjoke.setShare_count(joke.getString(MyConfig.JOKE_SHARE));
                            sjoke.setCollect_count(joke.getString(MyConfig.JOKE_COLLECT));
                            sjoke.setLook_count(joke.getString(MyConfig.JOKE_LOOK));
                            sjoke.setIscollect(0);
                            sjoke.setIszan(-1);
                            mTempDatas.add(sjoke);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Gson g=new Gson();
//                    mListDatas=g.fromJson(data, new TypeToken<List<SingleJoke>>(){}.getType());
                    mHandler.sendEmptyMessage(LOADMORE_GETJOKE_SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(LOADMORE_GETJOKE_FAIL);
                }
            }
        }, new GetJokesNet.FailGetJokeCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(LOADMORE_GETJOKE_FAIL);
            }
        });
    }

    public void refreshForAct(){
        mListView.setSelection(0);
        swipeRefreshLayout.setRefreshing(true);
        refresh();
    }
}
