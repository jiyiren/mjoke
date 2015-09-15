package app.jiyi.com.mjoke.aty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
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

    private InputStream imgstream;
    private Bitmap b;
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
        wexinapi= WXAPIFactory.createWXAPI(this, WEIXIN_ID,true);
        wexinapi.registerApp(WEIXIN_ID);
    }


    private void initView() {
//        dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCancelable(false);
        findViewById(R.id.share_btn_exit).setOnClickListener(this);
//        findViewById(R.id.share_qq).setOnClickListener(this);
//        findViewById(R.id.share_qq_zone).setOnClickListener(this);
//        findViewById(R.id.share_xinlang).setOnClickListener(this);
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
//            case R.id.share_qq:
//                send_Binary_Img(WEIXIN_ZONE);
//                break;
//            case R.id.share_qq_zone:
////                send_Url_Img(WEIXIN_FRIEND);
//                break;
//            case R.id.share_xinlang:
////                send_Url_Img(WEIXIN_ZONE);
//                break;
            case R.id.share_weixin://微信好友
                if(!(wexinapi.isWXAppInstalled()&&wexinapi.isWXAppSupportAPI())){
                    ShowToast.show(ShareDialogActivity.this,"微信未安装！");
                    return;
                }
                if(singleJoke.getIshasing().equals("1")) {
                    String imgurl=MyConfig.BASE_IMG_CONTENT+"/"+singleJoke.getJoke_id()+".jpg";
//                    String imgurl="https://www.baidu.com/img/bd_logo1.png";
                    shareToWeixinUrl(imgurl,WEIXIN_FRIEND);
                }else{
                    shareWeixin(WEIXIN_FRIEND);
                    this.finish();
                }
//                send_Url_Img(null,WEIXIN_FRIEND);
//                shareToWeixinUrl("http://7xknpe.com1.z0.glb.clouddn.com/author.png",WEIXIN_FRIEND);

                break;
            case R.id.share_weixin_zone://微信朋友圈
                if(!(wexinapi.isWXAppInstalled()&&wexinapi.isWXAppSupportAPI())){
                    ShowToast.show(ShareDialogActivity.this,"微信未安装！");
                    return;
                }
                if(singleJoke.getIshasing().equals("1")) {
                    String imgurl=MyConfig.BASE_IMG_CONTENT+"/"+singleJoke.getJoke_id()+".jpg";
                    shareToWeixinUrl(imgurl,WEIXIN_ZONE);
                }else{
                    shareWeixin(WEIXIN_ZONE);
                    this.finish();
                }
//                send_Url_Img(null,WEIXIN_ZONE);
//                shareToWeixinUrl("http://7xknpe.com1.z0.glb.clouddn.com/author.png",WEIXIN_ZONE);
                break;
            case R.id.share_other:
//                send_Binary_Img(WEIXIN_FRIEND);
                String content=singleJoke.getContent();
                String title;
                if(content.length()>8) {
                    title = content.substring(0, 8) + "...";
                }else{
                    title = content;
                }
                MyUtils.shareMsg(ShareDialogActivity.this, "分享mjoke内容", title, content+MyConfig.BASE_SHARE_TO_OTHER+singleJoke.getJoke_id(), null);
                this.finish();
                break;
        }
    }

    //分享url
private void shareToWeixinUrl(final String imgurl, final int weixintype){
    ImageRequest imageRequest=new ImageRequest(imgurl, new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap bitmap) {
            send_Url_Img(bitmap,weixintype);

        }
    }, 72, 72, Bitmap.Config.RGB_565, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            ShowToast.show(ShareDialogActivity.this,"分享出错!请检查网络!");
            ShareDialogActivity.this.finish();
        }
    });
    App.getAppInstance().getQueues().add(imageRequest);
}

    private void send_Url_Img(Bitmap bitmap,int type){
        WXWebpageObject  webobj=new WXWebpageObject();
        webobj.webpageUrl= MyConfig.BASE_SHARE_TO_OTHER+singleJoke.getJoke_id();//分享的url地址

        String content=singleJoke.getContent();
        WXMediaMessage msg=new WXMediaMessage(webobj);
        if(content.length()>8) {
            msg.title = content.substring(0, 8) + "....";
        }else{
            msg.title = content;
        }
        msg.description=content;
//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.mhead);//从资源文件里
        Bitmap thumbBmp=Bitmap.createScaledBitmap(bitmap, 120, 120, true);
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
        this.finish();
    }


//    private static final int WEIXIN_FAIL=0x11;
//    private Handler mhandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
////            if(dialog.isShowing()){
////                dialog.dismiss();
////            }
//            switch (msg.what){
//                case WEIXIN_FRIEND:
//                    send_Url_Img(WEIXIN_FRIEND);
//                    break;
//                case WEIXIN_ZONE:
//                    send_Url_Img(WEIXIN_ZONE);
//                    break;
//                case WEIXIN_FAIL:
//                    break;
//            }
//        }
//    };


    //分享文字内容
    private void shareWeixin(int type){
//        wexinapi.openWXApp();//打开微信客户端
        String mcontent=singleJoke.getContent();
        //创建分享文本
        WXTextObject wtext=new WXTextObject();
        wtext.text=mcontent+MyConfig.SHARE_WEIXIN_TEXT_HOUZHUI;

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

    //下面的方法没有用到-----------------------------------------------------------------------
    //分享本地二进制图像
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

    //根据imgurl获得流
    public InputStream getImageStream(String imgurl){
        URL url = null;
        try {
            url = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                return conn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
