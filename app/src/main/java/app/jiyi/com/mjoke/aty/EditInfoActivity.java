package app.jiyi.com.mjoke.aty;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.net.ModifyNet;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class EditInfoActivity extends BaseActivity implements View.OnClickListener{
    private App mapp;
    private TextView tv_title;
    private ImageView iv_header;
    ProgressDialog pd;

    private String current_username,current_sex,current_motto;
    private EditText et_username,et_motto;
    private ImageView iv_sex_man,iv_sex_woman;
    private FrameLayout toptilebar;
    private boolean isonce=true;//这是提示修改主题
    private boolean isfrompickphoto=false;//这是为了选择照片后回来显示正确的照片
    private boolean isheadchanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        mapp=App.getAppInstance();
        initView();
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        pd=showSpinnerDialog();
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);//返回按钮
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.editinfo_title_name);//初始化默认标题栏题目
        iv_header= (ImageView) findViewById(R.id.editinfo_head);//头像
        iv_header.setOnClickListener(this);
        loadHeader();//初始化头像
        findViewById(R.id.editinfo_sex_man).setOnClickListener(this);//男点击
        iv_sex_man= (ImageView) findViewById(R.id.iv_sex_man);
        iv_sex_woman= (ImageView) findViewById(R.id.iv_sex_woman);
        findViewById(R.id.editinfo_sex_women).setOnClickListener(this);//女点击
        findViewById(R.id.editinfo_save_edit).setOnClickListener(this);//保存按钮
        findViewById(R.id.editinfo_cancle).setOnClickListener(this);//取消按钮

        //获取最初的值
        current_username=mapp.getUserName();
        current_sex=mapp.getUserSex();
        current_motto=mapp.getUserMotto();

        //将初值全部应用于初始化控件上
        et_username= (EditText) findViewById(R.id.editinfo_username);
        et_username.setText(current_username);

        et_motto= (EditText) findViewById(R.id.editinfo_motto);
        et_motto.setText(current_motto);

        //如果存储了 男则男为选择上的，女未选择，否则是默认
        if(current_sex!=null&&!current_sex.equals("")) {
            if (current_sex.equals(MyConfig.VALUE_SEX_MAN)) {
                iv_sex_man.setImageResource(R.mipmap.radio_pressed);
                iv_sex_woman.setImageResource(R.mipmap.radio_normal);
            } else {
                iv_sex_man.setImageResource(R.mipmap.radio_normal);
                iv_sex_woman.setImageResource(R.mipmap.radio_pressed);
            }
        }else{
            iv_sex_man.setImageResource(R.mipmap.radio_normal);
            iv_sex_woman.setImageResource(R.mipmap.radio_pressed);
        }

    }

    //提供外部进入该activity的方法
    public static void enterEditInfoAty(Context context){
        if(App.getAppInstance().getisLogin()) {
            Intent i = new Intent(context, EditInfoActivity.class);
            context.startActivity(i);
        }else{
            ShowToast.showFirstLogin(context);
        }
    }

    //初始化变化界面
    private void initChangeView(){
        if(!isfrompickphoto) {
            loadHeader();
        }
        isfrompickphoto=false;
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back://返回按钮
                this.finish();
                break;
            case R.id.editinfo_sex_man:
                if(isonce){
                    ShowToast.show(EditInfoActivity.this,R.string.sex_choose_tip_theme);
                    isonce=false;
                }
                iv_sex_man.setImageResource(R.mipmap.radio_pressed);
                iv_sex_woman.setImageResource(R.mipmap.radio_normal);
                current_sex=MyConfig.VALUE_SEX_MAN;
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color09));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color09));
                break;
            case R.id.editinfo_sex_women:
                if(isonce){
                    ShowToast.show(EditInfoActivity.this,R.string.sex_choose_tip_theme);
                    isonce=false;
                }
                iv_sex_man.setImageResource(R.mipmap.radio_normal);
                iv_sex_woman.setImageResource(R.mipmap.radio_pressed);
                current_sex=MyConfig.VALUE_SEX_WOMAN;
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color02));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color02));
                break;
            case R.id.editinfo_save_edit:
                saveUserInfo();
                break;
            case R.id.editinfo_cancle:
                this.finish();
                break;
            case R.id.editinfo_head://头像修改
                DialogPickPhoto(EditInfoActivity.this);
                break;
        }
    }

    private void saveUserInfo(){
        current_username=et_username.getText().toString().replace(" ","");
        if(current_username==null||current_username.equals("")){
            ShowToast.show(EditInfoActivity.this,"昵称不能为空！");
            return;
        }
        current_motto=et_motto.getText().toString().replace(" ","");
        if(current_motto==null){
            current_motto="";
        }
        if(current_sex==null){
            current_sex= MyConfig.VALUE_SEX_WOMAN;
        }
        String idtoken=mapp.getUserToken();
        if(idtoken.equals("")||idtoken==null){
            this.finish();
            return;
        }
        pd.show();
        new ModifyNet(idtoken, current_username, current_sex, current_motto, new ModifyNet.SuccessModifyCallback() {
            @Override
            public void onSuccess(String result) {
                if(MyUtils.getGson(result,MyConfig.KEY_RESULT).equals(MyConfig.RESULT_SUCCESS)){
                    mHandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(MyConfig.FAIL);
                }
            }
        }, new ModifyNet.FailModifyCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });

    }


    private static final int SUCCESS_IMG_UPLOAD=0x12;
    private static final int FAIL_IMG_UPLOAD=0x13;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.SUCCESS:
                    mapp.setUserName(current_username);
                    mapp.setUserSex(current_sex);
                    mapp.setUserMotto(current_motto);
                    if(isheadchanged&&!Save_path.equals("")) {
                        new Thread(uploadImageRunnable).start();
                    }else{
                        pd.dismiss();
                        ShowToast.show(EditInfoActivity.this, "修改成功!");
                        EditInfoActivity.this.finish();
                    }
                    break;
                case MyConfig.FAIL:
                    pd.dismiss();
                    ShowToast.show(EditInfoActivity.this,"修改昵称或检查网络!");
                    break;
                case SUCCESS_IMG_UPLOAD:
                    pd.dismiss();
                    ShowToast.show(EditInfoActivity.this, "修改成功!");
                    EditInfoActivity.this.finish();
                    break;
                case FAIL_IMG_UPLOAD:
                    pd.dismiss();
                    ShowToast.show(EditInfoActivity.this, "修改信息成功!");
                    EditInfoActivity.this.finish();
                    break;
            }
        }
    };

    protected ProgressDialog showSpinnerDialog() {
//activity = modifyDialogContext(activity);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
//        if (!isFinishing()) {
//            dialog.show();
//        }
        return dialog;
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
            isheadchanged=true;
            //在控件里显示
//            Drawable drawable=new BitmapDrawable(null,photo);
//            iv_head.setImageDrawable(null);
//            iv_head.setBackground(drawable);
//            isFinish_head=true;
            //开启线程上传到服务器
        }
    }

    //线程设置
    Runnable uploadImageRunnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            uploadFile(Save_path,MyConfig.URL_UPLOAD_IMG);
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

            MyLog.i("jiyiren", sb.toString());
            Message mes=new Message();
            if(MyUtils.getGson(sb.toString(), MyConfig.KEY_RESULT).equals("1")){
                mes.what=SUCCESS_IMG_UPLOAD;
            }else{
                mes.what=FAIL_IMG_UPLOAD;
            }
            mHandler.sendMessage(mes);


        } catch (Exception e)
        {
            mHandler.sendEmptyMessage(FAIL_IMG_UPLOAD);
            MyLog.i("jiyiren","上传头像失败");
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
        initChangeView();
    }
}
