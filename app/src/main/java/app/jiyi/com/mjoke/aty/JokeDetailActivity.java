package app.jiyi.com.mjoke.aty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.OneCommentBean;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.bean.UserZanBean;
import app.jiyi.com.mjoke.db.DBManager;
import app.jiyi.com.mjoke.net.GetAllCommentByid;
import app.jiyi.com.mjoke.net.SendCommentNet;
import app.jiyi.com.mjoke.utiltool.Base64Util;
import app.jiyi.com.mjoke.utiltool.BitmapCache;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

/**
 * 点击joke进入此界面，joke的详细界面
 *
 */
public class JokeDetailActivity extends BaseActivity implements View.OnClickListener{

    public static final String KEY_SINGLEJOKE="msinglejoke";
    public static final int PER_COMMENT=10;

    private SingleJoke singleJoke;
    private TextView tv_title;//标题栏
    private ImageLoader loader;//图片加载
    private PopupWindow mPopWin;//弹出评论界面
    private InputMethodManager inputMethodManager;//输入法设置
    private String userid;//用户id判断
    private DBManager dbManager;//本地数据库

    private ListView mListView;//评论列表
    private ArrayList<OneCommentBean> mListDatas;
    private ArrayList<OneCommentBean> mTempDatas;
    private CommentAdapter mAdapter;
    private EditText et_content;//评论输入
    private Button bt_send;//评论发送

    private RelativeLayout rl_pg,rl_sofa;
    private LinearLayout ll_comment;
    private boolean isonce=true;

    private FrameLayout toptilebar;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_detail);
        setImmerseLayout(findViewById(R.id.toptitlebar));
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userid=App.getAppInstance().getUserToken();
        dbManager=new DBManager(this);
        if(userid==null){
            userid="0";
        }
        initIntentData();
        initView();
    }

    private void initIntentData() {
        Intent i=getIntent();
        singleJoke=i.getParcelableExtra(KEY_SINGLEJOKE);
        if(singleJoke==null){
            ShowToast.show(this,"出错!");
            return;
        }
    }

    //提供外部进入该activity的方法
    public static void enterJokeDetailAty(Context context,SingleJoke singleJoke) {
        Intent i = new Intent(context,JokeDetailActivity.class);
        i.putExtra(KEY_SINGLEJOKE, singleJoke);
        context.startActivity(i);
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.jokedetail);
        findViewById(R.id.ll_joke_detail_comment).setOnClickListener(this);
//        et_content= (EditText) findViewById(R.id.huifu_et_content);//评论内容
//        bt_send= (Button) findViewById(R.id.huifu_bt_send);//评论发送按钮

        rl_pg= (RelativeLayout) findViewById(R.id.rl_detail_pg);
        rl_sofa= (RelativeLayout) findViewById(R.id.rl_detail_shafa);
        ll_comment= (LinearLayout) findViewById(R.id.ll_detail_com);

        mListView= (ListView) findViewById(R.id.joke_detail_comment_lv);
        if(mListDatas==null){
            mListDatas=new ArrayList<OneCommentBean>();
        }
        mAdapter=new CommentAdapter(this,mListDatas,R.layout.item_comment);
        mListView.setAdapter(mAdapter);
        if(loader==null) {
            loader = new ImageLoader(App.getAppInstance().getQueues(), new BitmapCache());
        }
        getMoredata();
        initData();
    }



    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if(rl_pg.getVisibility()==View.VISIBLE){
                rl_pg.setVisibility(View.GONE);
            }
            switch (msg.what){
                case MyConfig.SUCCESS:
                    if(ll_comment.getVisibility()==View.GONE){
                        ll_comment.setVisibility(View.VISIBLE);
                    }
                    if(rl_sofa.getVisibility()==View.VISIBLE){
                        rl_sofa.setVisibility(View.GONE);
                    }

                    if(mTempDatas!=null&&mTempDatas.size()>0) {
                        mListDatas.addAll(mListDatas.size(), mTempDatas);
                        mTempDatas.clear();
                        MyUtils.setListViewHeightBasedOnChildren(mListView);
                        mAdapter.notifyDataSetChanged();
                        MyLog.i("jiyiren", "loadmore_size:" + mListDatas.size());
                    }else{
                        mHandler.sendEmptyMessage(MyConfig.FAIL);
                        return;
                    }
//                    mListView.onLoadComplete();
//                    ShowToast.show(JokeDetailActivity.this, "加载数据成功！");
                    isonce=false;
                    break;
                case MyConfig.FAIL:
                    if(isonce) {
                        if (ll_comment.getVisibility() == View.VISIBLE) {
                            ll_comment.setVisibility(View.GONE);
                        }
                        if (rl_sofa.getVisibility() == View.GONE) {
                            rl_sofa.setVisibility(View.VISIBLE);
                        }
                        isonce=false;
                    }
                    break;
            }
        }
    };

    //获取更多评论
    private void getMoredata(){
        String cur_lou=null;
        if(mListDatas==null||mListDatas.size()==0){
            cur_lou="0";
        }else{
            cur_lou=mListDatas.get(mListDatas.size()-1).getCom_lou();
        }
        getPinglun(singleJoke.getJoke_id(),cur_lou,PER_COMMENT);
    }

    //获取评论
    private void getPinglun(String joke_id,String cur_lou,int count){
        new GetAllCommentByid(joke_id, cur_lou, count, new GetAllCommentByid.SuccessGetAllCommentCallback() {
            @Override
            public void onSuccess(String result) {
                String res= MyUtils.getGson(result, MyConfig.KEY_RESULT);

                if(res.equals(MyConfig.RESULT_SUCCESS)){
                    try {
                        JSONObject re=new JSONObject(result);
                        JSONArray data=re.getJSONArray(MyConfig.KEY_DATAS);
                        if(mTempDatas==null){
                            mTempDatas=new ArrayList<OneCommentBean>();
                        }else {
                            mTempDatas.clear();
                        }
                        for (int i=0;i<data.length();i++){
                            JSONObject mcom= (JSONObject) data.get(i);
                            OneCommentBean ocb=new OneCommentBean();
                            ocb.setCom_id(mcom.getString(MyConfig.DB_COM_ID));
                            ocb.setJoke_id(mcom.getString(MyConfig.DB_COM_JOKE_ID));
                            ocb.setUser_id(mcom.getString(MyConfig.DB_COM_USER_ID));
                            ocb.setUser_name(mcom.getString(MyConfig.DB_COM_USERNAME));
                            ocb.setCom_content(mcom.getString(MyConfig.DB_COM_CONTENT));
                            ocb.setCom_lou(mcom.getString(MyConfig.DB_COM_LOU));
                            ocb.setCom_time(mcom.getString(MyConfig.DB_COM_TIME));
                            mTempDatas.add(ocb);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(MyConfig.FAIL);
                }
            }
        }, new GetAllCommentByid.FailGetAllCommentCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });
    }



    //初始化joke的显示界面数据
    private void initData(){
        ImageView iv_head= (ImageView) findViewById(R.id.item_head);//头像
        if(!singleJoke.getUser_id().equals("0")) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_head, R.mipmap.user_big_icon,
                    R.mipmap.user_big_icon);
            loader.get(MyConfig.BASE_IMG + "/" + singleJoke.getUser_id() + ".png", listener);
        }

        TextView tv_name= (TextView) findViewById(R.id.item_tv_name);//昵称
        ImageView iv_sex= (ImageView) findViewById(R.id.item_iv_sex);//性别
        String name=singleJoke.getUsername();
        if(name!=null&&!name.equals("")) {
            tv_name.setText(new String(Base64Util.decode(name)));
            if(singleJoke.getSex().equals(MyConfig.VALUE_SEX_MAN)){
                iv_sex.setImageResource(R.mipmap.user_sex_man);
            }else{
                iv_sex.setImageResource(R.mipmap.user_sex_woman);
            }
        }else{
            tv_name.setText(R.string.niming);
            iv_sex.setVisibility(View.GONE);
        }

        TextView tv_time = (TextView) findViewById(R.id.item_tv_time);//时间
        String timetme=singleJoke.getCreatetime();
        tv_time.setText(timetme.substring(0,timetme.length()-2));
        TextView tv_content = (TextView) findViewById(R.id.item_tv_content);//内容
//        tv_content.setText(singleJoke.getContent());
        tv_content.setText(singleJoke.getBase64DecodeContent());

        ImageView iv_content= (ImageView) findViewById(R.id.iv_item_pic);//内容图片
        if(singleJoke.getIshasing().equals("1")){
            iv_content.setVisibility(View.VISIBLE);
            ImageLoader.ImageListener mlistener=ImageLoader.getImageListener(iv_content, R.mipmap.item_default_bg,
                    R.mipmap.item_default_bg);
            final String imgurl=MyConfig.BASE_IMG_CONTENT+"/"+singleJoke.getImgurl()+".jpg";
            loader.get(imgurl,mlistener);
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AUILSampleActivity.enterAUILSampleQuesAty(JokeDetailActivity.this,imgurl);
                }
            });
        }else{
            iv_content.setVisibility(View.GONE);
        }

        final TextView tv_good= (TextView) findViewById(R.id.item_tv_good);
        tv_good.setText(singleJoke.getLook_count());
        TextView tv_share = (TextView) findViewById(R.id.item_tv_share);//分享次数
        tv_share.setText(singleJoke.getShare_count());
        final TextView tv_collect = (TextView) findViewById(R.id.item_tv_collect);//收藏次数
        tv_collect.setText(singleJoke.getCollect_count());
//        TextView tv_look= (TextView) findViewById(R.id.item_tv_look);//浏览次数
//        tv_look.setText(singleJoke.getLook_count());

        RelativeLayout rl_comment= (RelativeLayout) findViewById(R.id.item_joke_comment);//评论
        rl_comment.setOnClickListener(this);
        RelativeLayout rl_share= (RelativeLayout) findViewById(R.id.item_joke_share);//分享按钮
        rl_share.setOnClickListener(this);

        final ImageView iv_collect= (ImageView) findViewById(R.id.item_joke_collect);//收藏星号
        int iscollect=singleJoke.getIscollect();
        if(iscollect==1){
            iv_collect.setImageResource(R.mipmap.item_star);
        }else{
            iv_collect.setImageResource(R.mipmap.item_unstar);
        }
        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid.equals("0")){
                    ShowToast.show(JokeDetailActivity.this, "请先登录!");
                }else {
//                    MyLog.i("jiyihahah","userid"+user_id);
                    if(singleJoke.getIscollect()==1){
                        ShowToast.show(JokeDetailActivity.this, "取消收藏!");
//                        dbManager.deleteCollect(new UserCollectBean(joke_id, id));
                        MyApdater.doUserCollect(singleJoke.getJoke_id(), userid, "0");
                        iv_collect.setImageResource(R.mipmap.item_unstar);
                        singleJoke.setIscollect(0);
                        int coun=Integer.parseInt(tv_collect.getText().toString())-1;
                        tv_collect.setText(coun+"");
                        singleJoke.setCollect_count(coun+"");

                    }else{
                        ShowToast.show(JokeDetailActivity.this, "收藏成功!");
//                        dbManager.addCollect(new UserCollectBean(joke_id, id));
                        MyApdater.doUserCollect(singleJoke.getJoke_id(), userid, "1");
                        iv_collect.setImageResource(R.mipmap.item_star);
                        singleJoke.setIscollect(1);
                        int coun=Integer.parseInt(tv_collect.getText().toString())+1;
                        tv_collect.setText(coun + "");
                        singleJoke.setCollect_count(coun + "");
                    }
                }
            }
        });

        final ImageView iv_good= (ImageView) findViewById(R.id.item_joke_good);//good好评
        final ImageView iv_bad= (ImageView) findViewById(R.id.item_joke_bad);//差评
        int zantype=singleJoke.getIszan();
        if(zantype==1){
            iv_good.setImageResource(R.mipmap.good_pressed);
            iv_bad.setImageResource(R.mipmap.bad_normal);
        }else if(zantype==0){
            iv_good.setImageResource(R.mipmap.good_normal);
            iv_bad.setImageResource(R.mipmap.bad_pressed);
        }else{
            iv_good.setImageResource(R.mipmap.good_normal);
            iv_bad.setImageResource(R.mipmap.bad_normal);
        }

        iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid!=null){
                    if(singleJoke.getIszan()==0){
                        iv_bad.setImageResource(R.mipmap.bad_normal);
                        iv_good.setImageResource(R.mipmap.good_pressed);
                        dbManager.upgradeZan(new UserZanBean(singleJoke.getJoke_id(),userid,UserZanBean.TYEP_ZAN));
                        int count = Integer.parseInt(singleJoke.getLook_count()) + 2;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(1);
                        MyApdater.doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_INC);
                    }else if(singleJoke.getIszan()==-1){
                        iv_bad.setImageResource(R.mipmap.bad_normal);
                        iv_good.setImageResource(R.mipmap.good_pressed);
                        dbManager.addZan(new UserZanBean(singleJoke.getJoke_id(), userid, UserZanBean.TYEP_ZAN));
                        int count = Integer.parseInt(singleJoke.getLook_count()) + 1;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(1);
                        MyApdater.doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_INC);
                    }
                }
            }
        });

        iv_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid!=null){
                    if(singleJoke.getIszan()==1){
                        iv_bad.setImageResource(R.mipmap.bad_pressed);
                        iv_good.setImageResource(R.mipmap.good_normal);
                        dbManager.upgradeZan(new UserZanBean(singleJoke.getJoke_id(),userid,UserZanBean.TYPE_BAD));
                        int count = Integer.parseInt(singleJoke.getLook_count()) - 2;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(0);
                        MyApdater.doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_DES);
                    }else if(singleJoke.getIszan()==-1){
                        iv_bad.setImageResource(R.mipmap.bad_pressed);
                        iv_good.setImageResource(R.mipmap.good_normal);
                        dbManager.addZan(new UserZanBean(singleJoke.getJoke_id(), userid, UserZanBean.TYPE_BAD));
                        int count = Integer.parseInt(singleJoke.getLook_count()) - 1;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(0);
                        MyApdater.doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_DES);
                    }
                }

            }
        });


    }

    private void initPopWin(){
        if(mPopWin==null) {
            View mPopView = LayoutInflater.from(JokeDetailActivity.this).inflate(R.layout.popwin_huifu, null);
            et_content = (EditText) mPopView.findViewById(R.id.huifu_et_content);
            bt_send = (Button) mPopView.findViewById(R.id.huifu_bt_send);
            bt_send.setOnClickListener(this);
            mPopWin = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopWin.setBackgroundDrawable(cd);
        mPopWin.setFocusable(true);
        mPopWin.setOutsideTouchable(true);
        mPopWin.update();

        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        mPopWin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
            case R.id.item_joke_comment:
            case R.id.ll_joke_detail_comment://没有登录时不能评论
                if(!App.getAppInstance().getisLogin()){
                    ShowToast.showFirstLogin(JokeDetailActivity.this);
                }else{
                    if (mPopWin != null&&mPopWin.isShowing()) {
                        mPopWin.dismiss();
                        return;
                    }else {
                        initPopWin();
                        mPopWin.showAtLocation((View) v.getParent(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        popupInputMethodWindow();
                    }
                }
                break;
            case R.id.item_joke_share:
//                ShowToast.show(JokeDetailActivity.this,"点击了分享!");
                ShareDialogActivity.enterAtyShareCenter(JokeDetailActivity.this,singleJoke);
                break;
            case R.id.huifu_bt_send:
                sendComment();
                break;
        }
    }


    //设置所有的隐藏
    private void setAllGone(){
        rl_pg.setVisibility(View.GONE);
        rl_sofa.setVisibility(View.GONE);
        ll_comment.setVisibility(View.GONE);
    }
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.SUCCESS:
                    et_content.setText("");
                    mPopWin.dismiss();

                    rl_sofa.setVisibility(View.GONE);
                    ll_comment.setVisibility(View.GONE);
                    rl_pg.setVisibility(View.VISIBLE);
                    getMoredata();
                    ShowToast.show(JokeDetailActivity.this, "评论成功!");

                    break;
                case MyConfig.FAIL:
                    ShowToast.show(JokeDetailActivity.this,"评论失败!请检查网络!");
                    break;
            }
        }
    };

    //发送评论事件
    private void sendComment() {
        String content=MyUtils.getDeleteSpaceStr(et_content.getText().toString());
        if(content.equals("")||content==null){
            ShowToast.show(JokeDetailActivity.this,"请输入内容!");
            return;
        }

        String username=App.getAppInstance().getUserName();
        if(username==null){
            ShowToast.showFirstLogin(JokeDetailActivity.this);
            return;
        }

        dialog.setMessage("正在上传评论!");
        dialog.show();
        new SendCommentNet(singleJoke.getJoke_id(), userid, username, content, new SendCommentNet.SuccessSendCommentCallback() {
            @Override
            public void onSuccess(String result) {
                String res=MyUtils.getGson(result,MyConfig.KEY_RESULT);
                if(res.equals(MyConfig.RESULT_SUCCESS)){
                    mhandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mhandler.sendEmptyMessage(MyConfig.FAIL);
                }
            }
        }, new SendCommentNet.FailSendCommentCallback() {
            @Override
            public void onFail() {
                mhandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });

    }

    //下面为异步弹出键盘
    private Handler handler=new Handler();
    private void popupInputMethodWindow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
