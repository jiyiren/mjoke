package app.jiyi.com.mjoke.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class ShareDialogActivity extends Activity implements View.OnClickListener{

    private SingleJoke singleJoke;
    public static final String WEIXIN_ID="wxbb32102a1a8b36f3";
    private IWXAPI wexinapi;
    private static final int WEIXIN_FRIEND=0;//微信好友
    private static final int WEIXIN_ZONE=1;//微信朋友圈
    private static final int QQ_FRIEND=2;//QQ好友
    private static final int QQ_ZONE=3;//QQ空间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dialog);
        initIntentData();
        initShare();//初始化微信分享
        initView();
    }

    private void initIntentData() {
        Intent i=getIntent();
        singleJoke=i.getParcelableExtra("share_joke");
        if(singleJoke==null){
            ShowToast.show(this, "出错!");
            return;
        }
    }

    //提供外部进入该activity的方法
    public static void enterAtyShareCenter(Context context,SingleJoke singleJoke){
        Intent i=new Intent(context,ShareDialogActivity.class);
        i.putExtra("share_joke", singleJoke);
        context.startActivity(i);
    }

    private void initShare(){
        wexinapi= WXAPIFactory.createWXAPI(this, WEIXIN_ID);
        wexinapi.registerApp(WEIXIN_ID);
    }


    private void initView() {
        findViewById(R.id.share_btn_exit).setOnClickListener(this);
        findViewById(R.id.share_qq).setOnClickListener(this);
        findViewById(R.id.share_qq_zone).setOnClickListener(this);
        findViewById(R.id.share_xinlang).setOnClickListener(this);
        findViewById(R.id.share_weixin).setOnClickListener(this);
        findViewById(R.id.share_weixin_zone).setOnClickListener(this);
        findViewById(R.id.share_other).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_btn_exit:
                this.finish();
                break;
            case R.id.share_qq:
                send_Binary_Img(WEIXIN_ZONE);
                break;
            case R.id.share_qq_zone:
                send_Url_Img(WEIXIN_FRIEND);
                break;
            case R.id.share_xinlang:
                send_Url_Img(WEIXIN_ZONE);
                break;
            case R.id.share_weixin:
                shareWeixin(WEIXIN_FRIEND);
                this.finish();
                break;
            case R.id.share_weixin_zone:
                shareWeixin(WEIXIN_ZONE);
                this.finish();
                break;
            case R.id.share_other:
//                send_Binary_Img(WEIXIN_FRIEND);
                MyUtils.shareMsg(ShareDialogActivity.this, "ShareDialogActivity", "这是标题", "这是内容", null);
//                this.finish();
                break;
        }
    }



    private void shareWeixin(int type){
//        wexinapi.openWXApp();
        String mcontent=singleJoke.getContent();
//        MyLog.i("jiyiren","分享内容:"+mcontent);
        //创建分享文本
        WXTextObject wtext=new WXTextObject();
        wtext.text=mcontent;

        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=wtext;
        msg.description=mcontent;

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.message=msg;
        req.transaction=buildTransaction("jiyitext");

        if(type==WEIXIN_FRIEND){//好友
            req.scene=SendMessageToWX.Req.WXSceneSession;
        }else {//朋友圈
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        wexinapi.sendReq(req);

    }

    //生成唯一标识--微信
    private String buildTransaction(String type){
        return (type==null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
    }

    private void send_URL_Img(){

    }


    //分享二进制图像
    private void send_Binary_Img(int type){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.mhead);//从资源文件里
        WXImageObject imgobj=new WXImageObject(bitmap);

        //msg
        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=imgobj;
            Bitmap thumbBmp=Bitmap.createScaledBitmap(bitmap,120,150,true);
             bitmap.recycle();
        msg.thumbData=bmpToByteArray(thumbBmp,true);
        //req
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.message=msg;
        req.transaction=buildTransaction("jiyiimg");

        if(type==WEIXIN_FRIEND){//好友
            req.scene=SendMessageToWX.Req.WXSceneSession;
        }else {//朋友圈
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        wexinapi.sendReq(req);

    }

    private void send_Url_Img(int type){
        WXWebpageObject  webobj=new WXWebpageObject();
        webobj.webpageUrl="http://www.baidu.com";

        WXMediaMessage msg=new WXMediaMessage(webobj);
        msg.title="这是标题";
        msg.description="这是描述";
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.mhead);//从资源文件里
        Bitmap thumbBmp=Bitmap.createScaledBitmap(bitmap, 120, 150, true);
        msg.thumbData=bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = buildTransaction("jiyiwebpage");
        if(type==WEIXIN_FRIEND){//好友
            req.scene=SendMessageToWX.Req.WXSceneSession;
        }else {//朋友圈
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        wexinapi.sendReq(req);
    }

    private byte[] bmpToByteArray(Bitmap bitmap,boolean needRecycle){
        ByteArrayOutputStream outout=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outout);
        if(needRecycle){
            bitmap.recycle();
        }
        byte[] result=outout.toByteArray();
        try {
            outout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
