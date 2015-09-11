package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.net.PublicJokeNoImgNet;
import app.jiyi.com.mjoke.utiltool.Base64Util;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class PublishActivity extends BaseActivity implements View.OnClickListener{

    private App mapp;
    private TextView tv_title;
    private ImageView iv_tag_01,iv_tag_02,iv_tag_03;
    private TextView tv_tag_01,tv_tag_02,tv_tag_03;
    private EditText et_content;

    private ImageView iv_pic_add,iv_pic_cancle;
    private boolean ishaveimg=false;
    private String tag= SingleJoke.TYPE_JOKE;//默认分类
    private String mContent;
    private String userid="0";
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        mapp=App.getAppInstance();
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterPublishAty(Context context){
        Intent i=new Intent(context,PublishActivity.class);
        context.startActivity(i);
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);//标题后退按钮
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);//标题的title文字
        tv_title.setText(R.string.publish_title_name);

        if(mapp.getisLogin()){
            userid=mapp.getUserToken();
        }else{
            userid="0";
        }

        et_content= (EditText) findViewById(R.id.publish_et_content);//内容文字
        iv_pic_add= (ImageView) findViewById(R.id.publish_iv_pick_pic);//添加的图片
        iv_pic_add.setOnClickListener(this);
        iv_pic_cancle= (ImageView) findViewById(R.id.publish_iv_img_cancle);//添加后图片右上角的叉号
        iv_pic_cancle.setOnClickListener(this);
        if(ishaveimg){
            iv_pic_cancle.setVisibility(View.VISIBLE);
        }else{
            iv_pic_cancle.setVisibility(View.GONE);
        }
        //--------下面为tag标签
        findViewById(R.id.publish_tag_01).setOnClickListener(this);
        findViewById(R.id.publish_tag_02).setOnClickListener(this);
        findViewById(R.id.publish_tag_03).setOnClickListener(this);
        iv_tag_01= (ImageView) findViewById(R.id.p_tag_gaoxiao);
        iv_tag_02= (ImageView) findViewById(R.id.p_tag_girl);
        iv_tag_03= (ImageView) findViewById(R.id.p_tag_other);
        tv_tag_01= (TextView) findViewById(R.id.p_tag_tv_gaoxiao);
        tv_tag_02= (TextView) findViewById(R.id.p_tag_tv_girl);
        tv_tag_03= (TextView) findViewById(R.id.p_tag_tv_other);
        //----------
        findViewById(R.id.publish_bt_pub).setOnClickListener(this);//最后的发表按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back://返回按钮
                this.finish();
                break;
            case R.id.publish_iv_pick_pic://添加图片，默认图片时可以点击，当选择图片后不能点击
                DialogPickPhoto(PublishActivity.this);
                break;
            case R.id.publish_iv_img_cancle://当有选择图片后显示该控件并且可点击
                cancelPic();
                break;
            case R.id.publish_tag_01:
                setAllTagDefault();
                iv_tag_01.setImageResource(R.mipmap.radio_pressed);
                tv_tag_01.setTextColor(getResources().getColor(R.color.public_tag_pressed));
                tag=SingleJoke.TYPE_JOKE;
                break;
            case R.id.publish_tag_02:
                setAllTagDefault();
                iv_tag_02.setImageResource(R.mipmap.radio_pressed);
                tv_tag_02.setTextColor(getResources().getColor(R.color.public_tag_pressed));
                tag=SingleJoke.TYPE_GIRL;
                break;
            case R.id.publish_tag_03:
                setAllTagDefault();
                iv_tag_03.setImageResource(R.mipmap.radio_pressed);
                tv_tag_03.setTextColor(getResources().getColor(R.color.public_tag_pressed));
                tag=SingleJoke.TYPE_OTHERS;
                break;
            case R.id.publish_bt_pub:
                publishShare();
                break;
        }
    }

    //将所有tag设置为默认
    private void setAllTagDefault(){
        iv_tag_01.setImageResource(R.mipmap.radio_normal);
        iv_tag_02.setImageResource(R.mipmap.radio_normal);
        iv_tag_03.setImageResource(R.mipmap.radio_normal);
        tv_tag_01.setTextColor(getResources().getColor(R.color.public_tag_normal));
        tv_tag_02.setTextColor(getResources().getColor(R.color.public_tag_normal));
        tv_tag_03.setTextColor(getResources().getColor(R.color.public_tag_normal));
    }

    private void cancelPic(){
        ishaveimg=false;
        iv_pic_add.setImageResource(R.mipmap.publish_add_pic);
        iv_pic_cancle.setVisibility(View.GONE);
//        if(!Save_path.equals("")&&Save_path!=null){
//            File f=new File(Save_path);
//            if(f.exists()) {
//                f.delete();
//            }
//        }
    }

    //分享按钮事件
    private void publishShare(){
        mContent= MyUtils.getDeleteSpaceStr(et_content.getText().toString());
        if(mContent.equals("")){
            ShowToast.show(PublishActivity.this,"请输入内容!");
            return;
        }else if(mContent.length()<5){
            ShowToast.show(PublishActivity.this,"不能少于5个字!");
            return;
        }
//        ShowToast.show(PublishActivity.this, mContent);
        if(ishaveimg) {
            new Thread(uploadImageRunnable).start();
        }else{
            sendNoImagJoke(userid,tag,mContent);
        }
    }


    //显示Photo/Camera Dialog----即ListItemDialog
    private void DialogPickPhoto(Context context){
        String[] items=new String[]{"从相册中选择","拍   照"};
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {//设置listitem从相册和拍照

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        switch (arg1) {
                            case 0:
                                pickphoto();
                                break;
                            case 1:
                                takephoto();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//取消按钮，并设置点击事件
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.dismiss();//将对话框取消
                    }
                });
        builder.create();
        builder.show();
    }

    //从相册获取图片
    private void pickphoto() {
        // TODO Auto-generated method stub
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//系统在相册里intent-filter里定义好了，你通过这个设置，系统会帮你找寻带这个标志的activity
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        //这个意思指打开新activity选择后返回数据，而此类型也是在intent-filter中定义好了
        startActivityForResult(intent, MyConfig.SELECT_IMG_FROM_PHOTO);

    }


    //从相机获取图片,获得缩略图
//    private void takephoto() {
//        // TODO Auto-generated method stub
//        if(MyUtils.ExistSDCard()){
//            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            camera_path=MyUtils.CreateFileCamePhoto(MyConfig.BASE_DIR_NAME,"original.png");
////            //此处的Uri是为了保存相机拍的图片，后面要传入CutPhoto时也要传入此Uri
////            File f=new File(camera_path);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//            startActivityForResult(intent, MyConfig.SELECT_IMG_FROM_CAMERA);
//        }else{
//            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_SHORT).show();
//        }
//    }
    String camera_path="";//相机拍照的图片地址
    //从相机获取图片
    private void takephoto() {
        // TODO Auto-generated method stub
        if(MyUtils.ExistSDCard()){
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera_path=MyUtils.CreateFileCamePhoto(MyConfig.BASE_DIR_NAME,"original.png");
            //此处的Uri是为了保存相机拍的图片，后面要传入CutPhoto时也要传入此Uri
            File f=new File(camera_path);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, MyConfig.SELECT_IMG_FROM_CAMERA);
        }else{
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //这里是如果在相册界面没有选择相册而返回时，进行的判断
        if(resultCode==RESULT_CANCELED){
            return;
        }

        switch (requestCode) {
            //选择图库
            case MyConfig.SELECT_IMG_FROM_PHOTO:
                if(intent!=null){
                    Uri uri=intent.getData();
                    String imgurl=MyUtils.getRealFilePath(PublishActivity.this, uri);
                    SaveAndShow(imgurl);
                }else{
                    Toast.makeText(this, "选择图片出错!", Toast.LENGTH_SHORT).show();
                }
                break;
            //照相
            case MyConfig.SELECT_IMG_FROM_CAMERA:
                SaveAndShow(camera_path);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }


    String Save_path="";//最终保存头像的地址
    public void SaveAndShow(String imgdir){
//        Bitmap bitmap=MyUtils.getimage(imgdir,800f,400f);
        Bitmap bitmap=MyUtils.getimage(imgdir,800f,400f);
        Save_path=Environment.getExternalStorageDirectory()+"/"+MyConfig.BASE_DIR_NAME+"/photopick.jpg";
        MyUtils.SavePhoto(bitmap,Save_path );
        iv_pic_add.setImageBitmap(bitmap);
        iv_pic_cancle.setVisibility(View.VISIBLE);
        ishaveimg=true;
    }

    //线程设置
    Runnable uploadImageRunnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            uploadFile(Save_path,MyConfig.URL_PUBLISHJOKE);
        }

    };

    public void uploadFile(String srcPath,String uploadurl){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try
        {
            URL url = new URL(uploadurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
//            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

//            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            String content=
                    twoHyphens + boundary + end+"Content-Disposition: form-data; name=\""+MyConfig.KEY_TOKEN+"\"\r\n\r\n"+userid+"\r\n"+
                            twoHyphens + boundary + end+"Content-Disposition: form-data; name=\""+MyConfig.KEY_JOKE_TYPE+"\"\r\n\r\n"+tag+"\r\n"+
                            twoHyphens + boundary + end+"Content-Disposition: form-data; name=\""+MyConfig.KEY_JOKE_CONTENT+"\"\r\n\r\n"+
                            Base64Util.encode(mContent.getBytes()) + "\r\n";
            dos.writeBytes(content);
            //写什么
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"addpic\"; filename=\""
                    + srcPath.substring(srcPath.lastIndexOf("/") + 1)
                    + "\""
                    + end);
            dos.writeBytes(end);

            //写文件
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1)
            {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null)
            {
                sb.append(line);
            }

            dos.close();
            is.close();

            MyLog.i("jiyiren", sb.toString());
            Message mes=new Message();
            if(MyUtils.getGson(sb.toString(), MyConfig.KEY_RESULT).equals(MyConfig.RESULT_SUCCESS)){
                mes.what=MyConfig.SUCCESS;
            }else{
                mes.what=MyConfig.FAIL;
            }
            mHandler.sendMessage(mes);


        } catch (Exception e)
        {
            mHandler.sendEmptyMessage(MyConfig.FAIL);
            MyLog.i("jiyiren","发表失败!");
            e.printStackTrace();
        }

    }

    //发送无图说说
    private void sendNoImagJoke(String userid,String type,String mContent){
        new PublicJokeNoImgNet(userid, type, mContent, new PublicJokeNoImgNet.SuccessPublicJokeNoImgCallback() {
            @Override
            public void onSuccess(String result) {
                if(MyUtils.getGson(result,MyConfig.KEY_RESULT).equals(MyConfig.RESULT_SUCCESS)){
                    mHandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(MyConfig.FAIL);
                }
            }
        }, new PublicJokeNoImgNet.FailPublicJokeNoImgCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!Save_path.equals("")&&Save_path!=null){
                File f=new File(Save_path);
                if(f.exists()) {
                    f.delete();
                }
            }
            switch (msg.what){
                case MyConfig.SUCCESS:
                    ShowToast.show(PublishActivity.this,"发表成功！");
                    PublishActivity.this.finish();
                    break;
                case MyConfig.FAIL:
                    ShowToast.show(PublishActivity.this,"发表失败,请检查网络!");
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
    }
}
