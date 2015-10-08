package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.R;

public class LawActivity extends BaseActivity {

    private TextView tv_title;
    private FrameLayout toptilebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.about_law);
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LawActivity.this.finish();
            }
        });
    }

    //提供外部进入该activity的方法
    public static void enterLawAty(Context context){
        Intent i=new Intent(context,LawActivity.class);
        context.startActivity(i);
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
