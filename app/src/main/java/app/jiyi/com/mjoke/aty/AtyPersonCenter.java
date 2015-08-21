package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.jiyi.com.mjoke.R;

public class AtyPersonCenter extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_person_center);
        setImmerseLayout(findViewById(R.id.personcenter_title));
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterAtyPersonCenter(Context context){
        Intent i=new Intent(context,AtyPersonCenter.class);
        context.startActivity(i);
    }

    private void initView() {
        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
        }
    }
}
