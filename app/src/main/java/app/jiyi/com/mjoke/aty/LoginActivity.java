package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.net.LoginNet;
import app.jiyi.com.mjoke.utiltool.Base64Util;
import app.jiyi.com.mjoke.utiltool.MyMd5;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private App mapp;
    private String loginType="0";
    private TextView tv_title;

    private EditText et_username,et_pwd;
    private Button bt_login;
    private FrameLayout toptitlebar_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        setImmerseLayout(findViewById(R.id.toptitlebar_right));
        mapp=App.getAppInstance();
        super.onCreate(savedInstanceState);
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterLoginAty(Context context){
        Intent i=new Intent(context,LoginActivity.class);
        context.startActivity(i);
    }

    //初始化界面view
    private void initView() {
        toptitlebar_right= (FrameLayout) findViewById(R.id.toptitlebar_right);
        //toptitle初始化
        tv_title= (TextView) findViewById(R.id.tv_toptitlebarright_name);
        tv_title.setText(R.string.login_title);
        findViewById(R.id.rl_toptitlebarright_back).setOnClickListener(this);
        findViewById(R.id.rl_toptitlebarright_rightbt).setOnClickListener(this);
        //注册view初始化
        et_username= (EditText) findViewById(R.id.login_et_username);
        et_pwd= (EditText) findViewById(R.id.login_et_password);
        bt_login= (Button) findViewById(R.id.login_bt_login);
//        bt_login.setClickable(false);
//        bt_login.setBackgroundResource(R.color.login_bt_bg_on);
        bt_login.setOnClickListener(this);
    }

    //onClick事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebarright_back:
                this.finish();
                break;
            case R.id.rl_toptitlebarright_rightbt:
                RegisterActivity.enterRegisterAty(LoginActivity.this);
                break;
            case R.id.login_bt_login:
                btLoginClick();
                break;
        }
    }

    private static final int LOGIN_SUCCESS=0X11;
    private static final int LOGIN_FAIL_NOMAN=0X12;
    private static final int LOGIN_FAIL_PWDFAIL=0X13;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:

                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                    break;
                case LOGIN_FAIL_NOMAN:
                    et_username.setFocusable(true);
                    Toast.makeText(LoginActivity.this,"没有该用户，请核实用户名",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAIL_PWDFAIL:
                    et_pwd.setFocusable(true);
                    Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //登录按钮点击事件
    public void btLoginClick(){
        final String username=et_username.getText().toString();
        String userpwd=et_pwd.getText().toString();
        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(userpwd)){

            new LoginNet(loginType, Base64Util.encode(username.getBytes()), MyMd5.MD5(userpwd), new LoginNet.SuccessLoginCallback() {
                @Override
                public void onSuccess(String result) {
//                    ShowToast.show(LoginActivity.this,result);
                    String res=MyUtils.getGson(result,MyConfig.KEY_RESULT);
                        if(res.equals(MyConfig.RESULT_SUCCESS)){
                            mapp.setisLogin(true);
                            mapp.setUserName(username);
                            mapp.setUserToken(MyUtils.getGson(result, MyConfig.KEY_TOKEN));
                            String sex=MyUtils.getGson(result, MyConfig.KEY_SEX);
                            if(!sex.equals("")) {
                                mapp.setUserSex(sex);
                            }else{
                                mapp.setUserSex(MyConfig.VALUE_SEX_MAN);
                            }
                            String motto=new String(Base64Util.decode(MyUtils.getGson(result, MyConfig.KEY_MOTTO)));
                            if(!motto.equals("")) {
                                mapp.setUserMotto(motto);
                            }
//                            MyLog.i("jiyiren",MyUtils.getGson(result,MyConfig.KEY_TOKEN));
                            mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                        }else if(res.equals(MyConfig.RESULT_FAIL_NOMAN)){
                            mHandler.sendEmptyMessage(LOGIN_FAIL_NOMAN);
                        }else if(res.equals(MyConfig.RESULT_FAIL_PWDFAIL)){
                            mHandler.sendEmptyMessage(LOGIN_FAIL_PWDFAIL);
                        }
                }
            }, new LoginNet.FailLoginCallback() {
                @Override
                public void onFail() {
                    ShowToast.showNetConnFail(LoginActivity.this);
                }
            });
        }else{
            ShowToast.show(LoginActivity.this,R.string.not_empty_username_pwd);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toptitlebar_right!=null){
            toptitlebar_right.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

