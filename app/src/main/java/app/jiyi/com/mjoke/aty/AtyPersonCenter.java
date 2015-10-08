package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.PersonItemView;
import app.jiyi.com.mjoke.utilview.ScaleScrollView;

public class AtyPersonCenter extends BaseActivity implements View.OnClickListener,PersonItemView.RippleViewComplete{

    private boolean isfrompickphoto;//是否从取图界面过来的，因为从取吐界面过来resume方法会从新设置头像
                                    //但是上传网络有延迟，所以不需要在传过后再从网络获取，直接从截图里设置头像
    private App mapp;
    private ImageView iv_header,iv_edit_sex;//头像,编辑加性别图标
    private ScaleScrollView sslv;//整个界面自定义view
    private TextView tv_username,tv_motto;

    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_person_center);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        mapp=App.getAppInstance();
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterAtyPersonCenter(Context context){
        Intent i=new Intent(context,AtyPersonCenter.class);
        context.startActivity(i);
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        sslv= (ScaleScrollView) findViewById(R.id.personcenter_scalesv);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);//返回按钮
        iv_header= (ImageView) findViewById(R.id.person_center_top_header);//头像
        iv_header.setOnClickListener(this);//头像点击
        findViewById(R.id.pc_bt_exit_login).setOnClickListener(this);
        findViewById(R.id.pc_bt_edit_info).setOnClickListener(this);

        iv_edit_sex= (ImageView) findViewById(R.id.edit_sex);//编辑性别图标
        tv_username= (TextView) findViewById(R.id.top_tv_username);//用户名
        tv_motto= (TextView) findViewById(R.id.et_motto);//座右铭
        tv_username.setOnClickListener(this);//用户名点击
        findViewById(R.id.halftop_motto).setOnClickListener(this);//座右铭点击
        findViewById(R.id.pc_bt_edit_info).setOnClickListener(this);//编辑信息点击

        ((PersonItemView)findViewById(R.id.center_down_mypublish)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_share)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_collect)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_look)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_comment)).setOnRippleViewComplete(this);

        ((PersonItemView)findViewById(R.id.center_down_publish)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_search)).setOnRippleViewComplete(this);
        ((PersonItemView)findViewById(R.id.center_down_setting)).setOnRippleViewComplete(this);

        initChangeView();
    }

    //初始化变化界面
    private void initChangeView(){

        if(!mapp.getisLogin()) {
            sslv.setVisibleLogReg();
            findViewById(R.id.rl_person_center_login).setOnClickListener(this);//登录按钮
            findViewById(R.id.rl_person_center_reg).setOnClickListener(this);//注册按钮
        }else {
            //登陆成功情况下
            sslv.setInVisibleLogReg();
            if(!isfrompickphoto) {
                loadHeader();
            }
            loadUserText();
            isfrompickphoto=false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back://顶部返回按钮
                this.finish();
                break;
            case R.id.rl_person_center_login://中间登录按钮
                LoginActivity.enterLoginAty(AtyPersonCenter.this);
                break;
            case R.id.rl_person_center_reg://中间注册按钮
                RegisterActivity.enterRegisterAty(AtyPersonCenter.this);
                break;
            case R.id.person_center_top_header://头像
                if(mapp.getisLogin()) {
                    DialogPickPhoto(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.top_tv_username://昵称的点击
            case R.id.halftop_motto://座右铭的点击
            case R.id.pc_bt_edit_info://编辑信息按钮
                if(mapp.getisLogin()) {
                    EditInfoActivity.enterEditInfoAty(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.pc_bt_exit_login:
                mapp.setisLogin(false);
                AtyPersonCenter.this.finish();
                break;
        }
    }

    //加载用户的文本资料
    private void loadUserText(){
        String username=mapp.getUserName();
        tv_username.setText(username);
        String sex=mapp.getUserSex();
        String motto=mapp.getUserMotto();
        if(sex!=null&&sex!=""){
            if(sex.equals(MyConfig.VALUE_SEX_MAN)){
                iv_edit_sex.setImageResource(R.mipmap.user_sex_man);
            }else{
                iv_edit_sex.setImageResource(R.mipmap.user_sex_woman);
            }
        }else{
            iv_edit_sex.setImageResource(R.mipmap.user_sex_woman);
        }
        if(motto!=null&&!motto.equals("")){
            tv_motto.setText(motto);
        }else{
            tv_motto.setText(R.string.personcenter_edit_motto);
        }
    }

//    //加载图片用缓存
//    private void loadHeader(){
//        String url=MyConfig.BASE_IMG+"/"+mapp.getUserToken()+".png";
//        ImageLoader loader=new ImageLoader(mapp.getQueues(),new BitmapCache());
//        //view,默认图片，加载出错图片
//        ImageLoader.ImageListener listener=ImageLoader.getImageListener(iv_header, R.mipmap.user_big_icon,
//                R.mipmap.user_big_icon);
//        loader.get(url, listener);
//    }

    //加载用户的头像
    private void loadHeader(){
        String url=MyConfig.BASE_IMG+"/"+mapp.getUserToken()+".png";
        ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                iv_header.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String imgpath= Environment.getExternalStorageDirectory()+"/"+ MyConfig.BASE_DIR_NAME+"/"+mapp.getUserToken()+".png";
                File f=new File(imgpath);
                if(f.exists()){
                    Bitmap bitmap= BitmapFactory.decodeFile(imgpath);
                    iv_header.setImageBitmap(bitmap);
                }else{
                    iv_header.setImageResource(R.mipmap.user_big_icon);
                }
            }
        });
        mapp.getQueues().add(imageRequest);
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
        Intent intent=new Intent();
        intent.setType("image/*");//系统在相册里intent-filter里定义好了，你通过这个设置，系统会帮你找寻带这个标志的activity
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //这个意思指打开新activity选择后返回数据，而此类型也是在intent-filter中定义好了
        startActivityForResult(intent, MyConfig.SELECT_IMG_FROM_PHOTO);

    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //这里是如果在相册界面没有选择相册而返回时，进行的判断
        if(resultCode==RESULT_CANCELED){
            return;
        }

        switch (requestCode) {
            //选择图库
            case MyConfig.SELECT_IMG_FROM_PHOTO:
                CutPhoto(data.getData());//try catch
                break;
            //照相
            case MyConfig.SELECT_IMG_FROM_CAMERA:
                //此处的Uri是为了调出拍好的照片，也因此前面拍照时要设置地址，那个地址要和这个地址一样。
                File f=new File(camera_path);
                CutPhoto(Uri.fromFile(f));
                break;
            case MyConfig.SELECT_IMG_CUT_FINISHED:
                if(data!=null){
                    SaveAndShow(data);
                }else{
                    Toast.makeText(this, "无法裁剪", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //剪切图片
    public void CutPhoto(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");//到剪裁窗口
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);//设置可剪切
        //aspectX,aspectY是宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //outputX outputY是剪切图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, MyConfig.SELECT_IMG_CUT_FINISHED);
        //剪裁窗口剪裁后，将剪裁的信息传给CUT_PHOTO_OK处理，
        //onActivityResult再通过CUT_PHOTO_OK传给SaveAndShow方法处理，中间的所有参数，都是该  --剪裁的intent--
    }

    String Save_path="";//最终保存头像的地址
    //还有上传到服务器
    public void SaveAndShow(Intent picdata){
        Bundle extras=picdata.getExtras();
        if(extras!=null){
            Bitmap photo=extras.getParcelable("data");//此处的data是intent中的属性，它是一个Uri,裁剪的图片的Uri就存在这个里面
            //保存剪切后的头像
            //String imagename=System.currentTimeMillis()+".png";
            Save_path=MyUtils.CreateFileCamePhoto(MyConfig.BASE_DIR_NAME,mapp.getUserToken()+".png");
            //String path=Environment.getExternalStorageDirectory()+"/aajjc/"+"head.png";
            MyUtils.SavePhoto(photo, Save_path);
            //Config.saveSharedData(this, Config.KEY_USERPICLOC, Save_path);//保存头像
            if(!(camera_path==null||camera_path=="")){
                File f=new File(camera_path);
                f.delete();
            }
            iv_header.setImageBitmap(photo);
            isfrompickphoto=true;
            //在控件里显示
//            Drawable drawable=new BitmapDrawable(null,photo);
//            iv_head.setImageDrawable(null);
//            iv_head.setBackground(drawable);
//            isFinish_head=true;
            //开启线程上传到服务器
            new Thread(uploadImageRunnable).start();
        }
    }

    //线程设置
    Runnable uploadImageRunnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //uploadFile(Save_path,MyConfig.URL_UPLOAD_IMG);
            ossuploadfile(Save_path);
        }

    };

    //下面的这个是同步的方法，因为它是在Runnable里执行的，也就是在线程里，所有没有用异步方法。
    //在主线程执行则要用异步方法
    private void ossuploadfile(String srcPath){
        OSSService ossService=mapp.getOssService();
        OSSBucket ossBucket=ossService.getOssBucket(App.bucketName);
        OSSFile ossFile=mapp.getOssService().getOssFile(ossBucket,mapp.getUserToken()+".png");
        try {
            ossFile.setUploadFilePath(srcPath,"png");
            ossFile.enableUploadCheckMd5sum();
            ossFile.upload();
            mHandler.sendEmptyMessage(MyConfig.SUCCESS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MyConfig.FAIL);
            return;
        } catch (OSSException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MyConfig.FAIL);
            return;
        }


    }


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
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());

            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            String content=
                    twoHyphens + boundary + end+"Content-Disposition: form-data; name=\""+MyConfig.KEY_TOKEN+"\"\r\n\r\n"+mapp.getUserToken()+"\r\n";
            dos.writeBytes(content);
            //写什么
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"headimg\"; filename=\""
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

            MyLog.i("jiyiren",sb.toString());
            Message mes=new Message();
            if(MyUtils.getGson(sb.toString(), MyConfig.KEY_RESULT).equals("1")){
                mes.what=MyConfig.SUCCESS;
            }else{
                mes.what=MyConfig.FAIL;
            }
            mHandler.sendMessage(mes);


        } catch (Exception e)
        {
            mHandler.sendEmptyMessage(MyConfig.FAIL);
            MyLog.i("jiyiren","上传头像失败");
            e.printStackTrace();
        }

    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.SUCCESS:
                    ShowToast.show(AtyPersonCenter.this,"上传头像成功！");
                    break;
                case MyConfig.FAIL:
                    ShowToast.show(AtyPersonCenter.this,"上传头像失败！");
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(mapp.getThemeColor());
        }
        initChangeView();
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onRvComplete(PersonItemView personItemView) {
        switch (personItemView.getId()){
            case R.id.center_down_mypublish:
                if(mapp.getisLogin()) {
                    SelfPubActivity.enterSelfPubAty(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.center_down_share:
                DownLoadActivity.enterDownLoadAty(AtyPersonCenter.this);
                break;
            case R.id.center_down_collect:
                if(mapp.getisLogin()) {
                    CollectActivity.enterCollectAty(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.center_down_look:
                if(mapp.getisLogin()) {
                    LookActivity.enterLookAty(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.center_down_comment:
                if(mapp.getisLogin()) {
                    CommentActivity.enterCommentAty(AtyPersonCenter.this);
                }else{
                    ShowToast.showFirstLogin(AtyPersonCenter.this);
                }
                break;
            case R.id.center_down_publish:
                PublishActivity.enterPublishAty(AtyPersonCenter.this);
                break;
            case R.id.center_down_search:
                SendQuesActivity.enterSendQuesAty(AtyPersonCenter.this);
                break;
            case R.id.center_down_setting:
                SettingActivity.enterSettingAty(AtyPersonCenter.this);
                break;

        }
    }

}
