package app.jiyi.com.mjoke.aty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.MyUtils;
import app.jiyi.com.mjoke.utilview.SettingItemView;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener,SettingItemView.SettingRippleViewComplete{

    private TextView tv_title;
    private Button bt_close;
    private AlertDialog alertDialog;
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.about_title);

        ((SettingItemView)findViewById(R.id.about_share_bt)).setOnRippleViewComplete(this);
        ((SettingItemView)findViewById(R.id.about_law_bt)).setOnRippleViewComplete(this);
        ((SettingItemView)findViewById(R.id.about_contact_bt)).setOnRippleViewComplete(this);
    }

    //提供外部进入该activity的方法
    public static void enterSendQuesAty(Context context){
        Intent i=new Intent(context,AboutUsActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
            case R.id.contact_dialog_close:
                alertDialog.dismiss();
                break;
        }
    }

    @Override
    public void onRvComplete(SettingItemView settingItemView) {
        switch (settingItemView.getId()){
            case R.id.about_share_bt:
                MyUtils.shareMsg(AboutUsActivity.this,"分享Joke","分享Joke", MyConfig.DOWNLOAD_URL,null);
                break;
            case R.id.about_law_bt:
                LawActivity.enterLawAty(AboutUsActivity.this);
                break;
            case R.id.about_contact_bt:
                showAlert();
                break;

        }
    }

    private void showAlert(){
        if(alertDialog==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this);
            View view = LayoutInflater.from(AboutUsActivity.this).inflate(R.layout.contact_dialog, null);
            bt_close = (Button) view.findViewById(R.id.contact_dialog_close);
            bt_close.setOnClickListener(this);
            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();
        }else{
            alertDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toptilebar!=null){
            toptilebar.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
    }
}
