package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.bean.UserZanBean;
import app.jiyi.com.mjoke.db.DBManager;
import app.jiyi.com.mjoke.net.UserCollectNet;
import app.jiyi.com.mjoke.net.UserLookNet;
import app.jiyi.com.mjoke.net.ZanNet;
import app.jiyi.com.mjoke.utiltool.BitmapCache;
import app.jiyi.com.mjoke.utiltool.CommonAdapter;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utiltool.ViewHolder;

/**
 * Created by JIYI on 2015/8/27.
 */
public class MyApdater extends CommonAdapter<SingleJoke> {

    private Context context;
    private ImageLoader loader;
    private DBManager dbManager;

//    private String joke_id;
    public MyApdater(Context context, List<SingleJoke> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
        dbManager=new DBManager(context);
        if(loader==null) {
            loader = new ImageLoader(App.getAppInstance().getQueues(), new BitmapCache());
        }
    }

    @Override
    public void convert(ViewHolder holder, final SingleJoke singleJoke) {
//        joke_id=singleJoke.getJoke_id();
        ImageView iv_head=holder.getView(R.id.item_head);//头像

        String userid=singleJoke.getUser_id();
        if(!userid.equals("0")) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_head, R.mipmap.user_big_icon,
                    R.mipmap.user_big_icon);
            loader.get(MyConfig.BASE_IMG + "/" + singleJoke.getUser_id() + ".png", listener);
        }else{
            iv_head.setImageResource(R.mipmap.user_big_icon);
        }

        TextView tv_name=holder.getView(R.id.item_tv_name);//昵称
        ImageView iv_sex=holder.getView(R.id.item_iv_sex);//性别
        String name=singleJoke.getUsername();
        if(name!=null&&!name.equals("")) {
            tv_name.setText(name);
            if(singleJoke.getSex().equals(MyConfig.VALUE_SEX_MAN)){
                iv_sex.setImageResource(R.mipmap.user_sex_man);
            }else{
                iv_sex.setImageResource(R.mipmap.user_sex_woman);
            }
        }else{
            tv_name.setText(R.string.niming);
            iv_sex.setVisibility(View.GONE);
        }

        TextView tv_time=holder.getView(R.id.item_tv_time);//时间
        tv_time.setText(singleJoke.getCreatetime());
        TextView tv_content=holder.getView(R.id.item_tv_content);//内容
        tv_content.setText(singleJoke.getContent());

        ImageView iv_content=holder.getView(R.id.iv_item_pic);//内容图片
        if(singleJoke.getIshasing().equals("1")){
            iv_content.setVisibility(View.VISIBLE);
            ImageLoader.ImageListener mlistener=ImageLoader.getImageListener(iv_content, R.mipmap.item_default_bg,
                    R.mipmap.item_default_bg);
            loader.get(MyConfig.BASE_IMG_CONTENT+"/"+singleJoke.getJoke_id()+".jpg",mlistener);
        }else{
            iv_content.setVisibility(View.GONE);
        }

        final TextView tv_good=holder.getView(R.id.item_tv_good);
        tv_good.setText(singleJoke.getLook_count());
        TextView tv_share=holder.getView(R.id.item_tv_share);//分享次数
        tv_share.setText(singleJoke.getShare_count());
        final TextView tv_collect=holder.getView(R.id.item_tv_collect);//收藏次数
        tv_collect.setText(singleJoke.getCollect_count());

        LinearLayout ll_content=holder.getView(R.id.ll_item_jokeconetent);//joke内容
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=getUserid();
                if(!id.equals("0")) {
//                    dbManager.addLook(new UserLookBean(joke_id, id));
                    doUserLook(singleJoke.getJoke_id(),id);
                }
                JokeDetailActivity.enterJokeDetailAty(context,singleJoke);
            }
        });


        final ImageView iv_collect=holder.getView(R.id.item_joke_collect);//收藏星号
        int iscollect=singleJoke.getIscollect();
        if(iscollect==1){
            iv_collect.setImageResource(R.mipmap.item_star);
        }else{
            iv_collect.setImageResource(R.mipmap.item_unstar);
        }
        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=getUserid();
                if(id.equals("0")){
                    ShowToast.show(context, "请先登录!");
                }else {
//                    MyLog.i("jiyihahah","userid"+user_id);
                    if(singleJoke.getIscollect()==1){
                        ShowToast.show(context, "取消收藏!");
//                        dbManager.deleteCollect(new UserCollectBean(joke_id, id));
                        doUserCollect(singleJoke.getJoke_id(),id,"0");
                        iv_collect.setImageResource(R.mipmap.item_unstar);
                        singleJoke.setIscollect(0);
                        int coun=Integer.parseInt(tv_collect.getText().toString())-1;
                        tv_collect.setText(coun + "");
                        singleJoke.setCollect_count(coun + "");

                    }else{
                        ShowToast.show(context, "收藏成功!");
//                        dbManager.addCollect(new UserCollectBean(joke_id, id));
                        doUserCollect(singleJoke.getJoke_id(),id,"1");
                        iv_collect.setImageResource(R.mipmap.item_star);
                        singleJoke.setIscollect(1);
                        int coun=Integer.parseInt(tv_collect.getText().toString())+1;
                        tv_collect.setText(coun + "");
                        singleJoke.setCollect_count(coun + "");
                    }
                }
            }
        });

        RelativeLayout rl_comment=holder.getView(R.id.item_joke_comment);//评论

        final ImageView iv_good=holder.getView(R.id.item_joke_good);//good好评
        final ImageView iv_bad=holder.getView(R.id.item_joke_bad);//差评
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
                String id=getUserid();
                if(id!=null){
                    if(singleJoke.getIszan()==0){
                        iv_bad.setImageResource(R.mipmap.bad_normal);
                        iv_good.setImageResource(R.mipmap.good_pressed);
                        dbManager.upgradeZan(new UserZanBean(singleJoke.getJoke_id(),id,UserZanBean.TYEP_ZAN));
                        int count = Integer.parseInt(singleJoke.getLook_count()) + 2;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(1);
                        doZan(singleJoke.getJoke_id(),MyConfig.TYPE_ZAN_INC);
                    }else if(singleJoke.getIszan()==-1){
                        iv_bad.setImageResource(R.mipmap.bad_normal);
                        iv_good.setImageResource(R.mipmap.good_pressed);
                        dbManager.addZan(new UserZanBean(singleJoke.getJoke_id(), id, UserZanBean.TYEP_ZAN));
                        int count = Integer.parseInt(singleJoke.getLook_count()) + 1;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(1);
                        doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_INC);
                    }
                }
            }
        });

        iv_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=getUserid();
                if(id!=null){
                    if(singleJoke.getIszan()==1){
                        iv_bad.setImageResource(R.mipmap.bad_pressed);
                        iv_good.setImageResource(R.mipmap.good_normal);
                        dbManager.upgradeZan(new UserZanBean(singleJoke.getJoke_id(),id,UserZanBean.TYPE_BAD));
                        int count = Integer.parseInt(singleJoke.getLook_count()) - 2;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(0);
                        doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_DES);
                    }else if(singleJoke.getIszan()==-1){
                        iv_bad.setImageResource(R.mipmap.bad_pressed);
                        iv_good.setImageResource(R.mipmap.good_normal);
                        dbManager.addZan(new UserZanBean(singleJoke.getJoke_id(), id, UserZanBean.TYPE_BAD));
                        int count = Integer.parseInt(singleJoke.getLook_count()) - 1;
                        tv_good.setText(count + "");
                        singleJoke.setLook_count(count + "");
                        singleJoke.setIszan(0);
                        doZan(singleJoke.getJoke_id(), MyConfig.TYPE_ZAN_DES);
                    }
                }

            }
        });

        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JokeDetailActivity.enterJokeDetailAty(context, singleJoke);
            }
        });

        RelativeLayout rl_share=holder.getView(R.id.item_joke_share);//分享按钮
        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShowToast.show(context,"点击了分享!");
                ShareDialogActivity.enterAtyShareCenter(context,singleJoke);
            }
        });

    }

    private String getUserid(){
        String id=App.getAppInstance().getUserToken();
        if(id==null){
            return "0";
        }else{
            return id;
        }
    }

    public static void doUserCollect(String joke_id,String token,String type){
        new UserCollectNet(joke_id, token,type,new UserCollectNet.SuccessUserCollectCallback() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("jiyiren","collect:result:"+result);
            }
        }, new UserCollectNet.FailUserCollectCallback() {
            @Override
            public void onFail() {

            }
        });
    }

    public static void doUserLook(String joke_id,String token){
        new UserLookNet(joke_id, token, new UserLookNet.SuccessUserLookCallback() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("jiyiren","look:result:"+result);
            }
        }, new UserLookNet.FailUserLookCallback() {
            @Override
            public void onFail() {

            }
        });
    }

    public static void doZan(String joke_id,String type){
        new ZanNet(joke_id, type, new ZanNet.SuccessZanCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, new ZanNet.FailZanCallback() {
            @Override
            public void onFail() {

            }
        });
    }
}
