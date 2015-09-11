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

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.net.RegisterNet;
import app.jiyi.com.mjoke.utiltool.MyMd5;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title;
    private EditText et_username,et_pwd;
    private Button bt_register;
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterRegisterAty(Context context){
        Intent i=new Intent(context,RegisterActivity.class);
        context.startActivity(i);
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        //标题栏
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.register_title);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        //注册界面
        et_username= (EditText) findViewById(R.id.register_et_username);
        et_pwd= (EditText) findViewById(R.id.register_et_password);
        bt_register= (Button) findViewById(R.id.register_bt_login);
        bt_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
            case R.id.register_bt_login:
                btRegisterClick();
                break;
        }
    }

    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.SUCCESS:
                    ShowToast.show(RegisterActivity.this,"注册成功!");
                    LoginActivity.enterLoginAty(RegisterActivity.this);
                    RegisterActivity.this.finish();
                    break;
                case MyConfig.FAIL:
                    ShowToast.show(RegisterActivity.this,"注册失败!请更换用户名或者检查网络!");
                    break;
            }
        }
    };

    //注册按钮事件处理
    public void btRegisterClick(){
        String username=et_username.getText().toString();
        String userpwd=et_pwd.getText().toString();
        
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(userpwd)){
            //不能为空
            ShowToast.show(RegisterActivity.this,R.string.not_empty_username_pwd);
            return;
        }

        int mark= MyUtils.isEmpty(username);
        if(mark==2){
            //用户名不能包含空字符串
            ShowToast.show(RegisterActivity.this,R.string.not_blank_username);
            return;
        }

        if(userpwd.length()<6){
            ShowToast.show(RegisterActivity.this,R.string.pwd_not_less_six);
            return;
        }

        new RegisterNet(username, MyMd5.MD5(userpwd), new RegisterNet.SuccessRegisterCallback() {
            @Override
            public void onSuccess(String result) {
//                ShowToast.show(RegisterActivity.this,result);
                String re=MyUtils.getGson(result,MyConfig.KEY_RESULT);
                if(re.equals(MyConfig.RESULT_SUCCESS)){
                    mhandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mhandler.sendEmptyMessage(MyConfig.FAIL);
                }
                //继续
            }
        }, new RegisterNet.FailRegisterCallback() {
            @Override
            public void onFail() {
//                ShowToast.showNetConnFail(RegisterActivity.this);
                mhandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
    }
}
