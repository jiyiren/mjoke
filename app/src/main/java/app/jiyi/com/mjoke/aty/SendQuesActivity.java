package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.net.SendQuesNet;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utiltool.ShowToast;

public class SendQuesActivity extends BaseActivity implements OnClickListener{

    private TextView tv_title;
    private EditText et_content,et_email;
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_ques);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterSendQuesAty(Context context){
        Intent i=new Intent(context,SendQuesActivity.class);
        context.startActivity(i);
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.ques_title);
        findViewById(R.id.sendques_tijiao).setOnClickListener(this);
        et_content= (EditText) findViewById(R.id.sendques_content);
        et_email= (EditText) findViewById(R.id.sendques_email);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
            case R.id.sendques_tijiao:
                doTijiao();
                break;
        }
    }


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConfig.SUCCESS:
                    ShowToast.show(SendQuesActivity.this,"发送成功!");
                    SendQuesActivity.this.finish();
                    break;
                case MyConfig.FAIL:
                    ShowToast.show(SendQuesActivity.this,"发送失败,请检查网络!");
                    break;
            }
        }
    };

    private void doTijiao() {
        String content= MyUtils.getDeleteSpaceStr(et_content.getText().toString());
        if(content.equals("")){
            ShowToast.show(SendQuesActivity.this,"请输入内容!");
            return;
        }
        String email=MyUtils.getDeleteSpaceStr(et_email.getText().toString());
        if(email.equals("")){
            ShowToast.show(SendQuesActivity.this,"请输入邮箱!");
            return;
        }

        new SendQuesNet(content, email, new SendQuesNet.SuccessSendQuesCallback() {
            @Override
            public void onSuccess(String result) {
                String res=MyUtils.getGson(result,MyConfig.KEY_RESULT);
                if(res.equals(MyConfig.RESULT_SUCCESS)){
                    mHandler.sendEmptyMessage(MyConfig.SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(MyConfig.FAIL);
                }
            }
        }, new SendQuesNet.FailSendQuesCallback() {
            @Override
            public void onFail() {
                mHandler.sendEmptyMessage(MyConfig.FAIL);
            }
        });

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
