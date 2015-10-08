package app.jiyi.com.mjoke.aty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.umeng.analytics.MobclickAgent;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.SettingItemView;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener,SettingItemView.SettingRippleViewComplete {

    private TextView tv_title;
    private SettingItemView siv_banben;

    private AlertDialog alertDialog;
    private Button bt_theme_close;
    private FrameLayout toptilebar;
    private ImageView setting_sys_theme_yang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        findViewById(R.id.toptitlebar).setBackgroundColor(App.getAppInstance().getThemeColor());
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterSettingAty(Context context){
        Intent i=new Intent(context,SettingActivity.class);
        context.startActivity(i);
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.setting);

        setting_sys_theme_yang= (ImageView) findViewById(R.id.setting_sys_theme_yangshi);

        ((SettingItemView)findViewById(R.id.setting_person_center)).setOnRippleViewComplete(this);
        ((SettingItemView)findViewById(R.id.setting_person_edit)).setOnRippleViewComplete(this);
        ((RippleView)findViewById(R.id.setting_sys_theme)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                setTheme();
            }
        });
        ((SettingItemView)findViewById(R.id.setting_sys_font)).setOnRippleViewComplete(this);
        ((SettingItemView)findViewById(R.id.setting_other_prob)).setOnRippleViewComplete(this);


        siv_banben=((SettingItemView) findViewById(R.id.setting_other_banben));//版本
        siv_banben.setOnRippleViewComplete(this);
        siv_banben.setRighttext(MyConfig.CURRENT_BANBEN);

        ((SettingItemView)findViewById(R.id.setting_other_law)).setOnRippleViewComplete(this);
        ((SettingItemView)findViewById(R.id.setting_other_about)).setOnRippleViewComplete(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
            case R.id.theme_dialog_close:
                alertDialog.dismiss();
                break;
            case R.id.theme_color_01:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color01));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color01));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color01));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_02:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color02));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color02));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color02));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_03:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color03));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color03));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color03));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_04:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color04));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color04));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color04));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_05:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color05));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color05));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color05));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_06:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color06));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color06));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color06));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_07:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color07));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color07));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color07));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_08:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color08));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color08));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color08));
                alertDialog.dismiss();
                break;
            case R.id.theme_color_09:
                App.getAppInstance().setThemeColor(getResources().getColor(R.color.color09));
                toptilebar.setBackgroundColor(getResources().getColor(R.color.color09));
                setting_sys_theme_yang.setBackgroundColor(getResources().getColor(R.color.color09));
                alertDialog.dismiss();
                break;
        }
    }

    @Override
    public void onRvComplete(SettingItemView settingItemView) {
        switch (settingItemView.getId()){
            case R.id.setting_person_edit:
                EditInfoActivity.enterEditInfoAty(SettingActivity.this);
                break;
            case R.id.setting_person_center:
                AtyPersonCenter.enterAtyPersonCenter(SettingActivity.this);
                break;
            case R.id.setting_sys_font:
                break;
            case R.id.setting_other_prob:
                SendQuesActivity.enterSendQuesAty(SettingActivity.this);
                break;
            case R.id.setting_other_banben:
                ShowToast.show(SettingActivity.this,"当前已是最新版本!");
                break;
            case R.id.setting_other_law:
                LawActivity.enterLawAty(SettingActivity.this);
                break;
            case R.id.setting_other_about:
                AboutUsActivity.enterSendQuesAty(SettingActivity.this);
                break;
        }
    }

    private void setTheme(){
        if(alertDialog==null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            View view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.theme_dialog, null);
            bt_theme_close = (Button) view.findViewById(R.id.theme_dialog_close);
            view.findViewById(R.id.theme_color_01).setOnClickListener(this);
            view.findViewById(R.id.theme_color_02).setOnClickListener(this);
            view.findViewById(R.id.theme_color_03).setOnClickListener(this);
            view.findViewById(R.id.theme_color_04).setOnClickListener(this);
            view.findViewById(R.id.theme_color_05).setOnClickListener(this);
            view.findViewById(R.id.theme_color_06).setOnClickListener(this);
            view.findViewById(R.id.theme_color_07).setOnClickListener(this);
            view.findViewById(R.id.theme_color_08).setOnClickListener(this);
            view.findViewById(R.id.theme_color_09).setOnClickListener(this);
            bt_theme_close.setOnClickListener(this);
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
        if(setting_sys_theme_yang!=null){
            setting_sys_theme_yang.setBackgroundColor(App.getAppInstance().getThemeColor());
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
