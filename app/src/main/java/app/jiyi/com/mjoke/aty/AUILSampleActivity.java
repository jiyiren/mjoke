package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.BitmapCache;
import app.jiyi.com.mjoke.utiltool.MyMd5;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.photoview.PhotoView;

/**
 * 主要是完整显示joke图片
 * 从joke详细信息里点击图片进入这个界面
 * jokeDetailActivity---->this
 */
public class AUILSampleActivity extends BaseActivity implements View.OnClickListener{

    private PhotoView photoView;
    private String imgurl,imgname;
    private ImageLoader loader;//图片加载
    private ImageView iv_download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setImmerseLayout(findViewById(R.id.simple_fl));
        initData();
        initView();
    }


    private void initData() {
        imgurl=getIntent().getStringExtra("imgurl");
        if(imgurl==null){
            ShowToast.show(AUILSampleActivity.this,"网络出错!");
            return;
        }
    }
    private void initView() {
        if(loader==null) {
            loader = new ImageLoader(App.getAppInstance().getQueues(), new BitmapCache());
        }
        photoView = (PhotoView) findViewById(R.id.iv_photo);
        iv_download= (ImageView) findViewById(R.id.bt_download);
        iv_download.setOnClickListener(this);
        ImageLoader.ImageListener mlistener=ImageLoader.getImageListener(photoView, R.mipmap.item_default_bg,
                R.mipmap.item_default_bg);
        loader.get(imgurl, mlistener);
    }

    //提供外部进入该activity的方法
    public static void enterAUILSampleQuesAty(Context context,String imgurl){
        Intent i=new Intent(context,AUILSampleActivity.class);
        i.putExtra("imgurl",imgurl);
        context.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_photo:
                this.finish();
                break;
            case R.id.bt_download:
                save_Image();
                break;
        }
    }

    private static final int EXITS=3;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.FAIL:
                    ShowToast.show(AUILSampleActivity.this,"保存失败，请检查网络!");
                    break;
                case MyConfig.SUCCESS:
                    ShowToast.show(AUILSampleActivity.this,"保存在SD卡:"+imgname);
                    iv_download.setImageResource(R.mipmap.download_suc);
                    break;
                case EXITS:
                    ShowToast.show(AUILSampleActivity.this,"已经保存:"+imgname);
                    iv_download.setImageResource(R.mipmap.download_suc);
                    break;
            }
        }
    };
    private void save_Image() {
        if(imgurl==null) {
            ShowToast.show(AUILSampleActivity.this,"保存出错!");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap= BitmapFactory.decodeStream(getImageStream(imgurl));
                String save_dir= Environment.getExternalStorageDirectory()+"/"+ MyConfig.BASE_DIR_NAME+"/"+MyConfig.BASE_DIR_IMAGE_DOWNLOAD;
                File f=new File(save_dir);
                if(!f.exists()){
                    f.mkdirs();
                }
                imgname=save_dir+"/"+ MyMd5.MD5(imgurl)+".jpg";
                File ff=new File(imgname);
                if(ff.exists()){
                    mHandler.sendEmptyMessage(EXITS);
                    return;
                }else {
                    if (bitmap != null) {
                        SavePhoto(bitmap,imgname);
                        mHandler.sendEmptyMessage(MyConfig.SUCCESS);
                    }else{
                        mHandler.sendEmptyMessage(MyConfig.FAIL);
                    }
                }
            }
        }).start();
    }

    //将bitmap保存到制定路径
    public static void SavePhoto(Bitmap bitmap,String path){
        FileOutputStream fos=null;
        try {
            File file=new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80, fos);//100无压缩
            //保存为png格式，图片压缩质量为75，对于png来说这个参数会被忽略
            //jpg则使用CompressFormat.JPEG
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
