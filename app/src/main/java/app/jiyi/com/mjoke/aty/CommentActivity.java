package app.jiyi.com.mjoke.aty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.fragment.Tab01Fragment;
import app.jiyi.com.mjoke.net.GetUserAboutJokeNet;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class CommentActivity extends ListBaseActivity {

    private ProgressDialog dialog;
    private boolean isonce=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_look);
        tv_title.setText(R.string.personcenter_down_comment);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        if(isonce) {
            dialog.show();
            getLoadMoreData();
            isonce=false;
        }
    }

    //提供外部进入该activity的方法
    public static void enterCommentAty(Context context){
        Intent i=new Intent(context,CommentActivity.class);
        context.startActivity(i);
    }
    @Override
    public void getLoadMoreData() {
        String jokeid=null;
        if(mListDatas!=null&&mListDatas.size()>0){
            jokeid=mListDatas.get(mListDatas.size()-1).getJoke_id();
        }else{
            jokeid="0";
        }
        getMoreData(jokeid, userid, Tab01Fragment.pagers_count_per_second);
    }

    private static final int GET_MORE_DATA_SUCCESS=0X11;
    private static final int GET_MORE_DATA_FAIL=0X22;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            switch (msg.what){
                case GET_MORE_DATA_SUCCESS:
                    if(mTempDatas!=null&&mTempDatas.size()>0) {
                        if(mListDatas.size()<=0){
                            mListDatas.addAll(0,mTempDatas);
                        }else {
                            mListDatas.addAll(mListDatas.size(), mTempDatas);
                        }
                        mTempDatas.clear();
                        mAdapter.notifyDataSetChanged();
                        MyLog.i("jiyiren", "loadmore_size:" + mListDatas.size());
                    }else{
                        mHandler.sendEmptyMessage(GET_MORE_DATA_FAIL);
                    }
                    mListView.onLoadComplete();
//                    ShowToast.show(CommentActivity.this, "加载数据成功！");
                    break;
                case GET_MORE_DATA_FAIL:
                    mListView.onLoadComplete();
                    ShowToast.show(CommentActivity.this, "没有啦！");
                    break;
            }
        }
    };

    private void getMoreData(String joke_id,String token,int count){
        new GetUserAboutJokeNet(joke_id, token, count + "", MyConfig.USER_ABOUT_JOKE_TYPE_COMMENT ,new GetUserAboutJokeNet.SuccessGetUserJokeCallback() {
            @Override
            public void onSuccess(String result) {
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
                    mHandler.sendEmptyMessage(GET_MORE_DATA_SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(GET_MORE_DATA_FAIL);
                }
            }
        }, new GetUserAboutJokeNet.FailGetUserJokeCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(GET_MORE_DATA_FAIL);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
