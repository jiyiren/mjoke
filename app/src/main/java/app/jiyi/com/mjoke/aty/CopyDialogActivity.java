package app.jiyi.com.mjoke.aty;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.bean.SingleJoke;
import app.jiyi.com.mjoke.net.JubaoNet;
import app.jiyi.com.mjoke.utiltool.ShowToast;

/**
 * 长按jokeitem,弹出此对话框
 *
 * 主要由复制和举报功能
 */
public class CopyDialogActivity extends Activity implements View.OnClickListener{

    private SingleJoke singleJoke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_dialog);
        initIntentData();
        initView();
    }

    //提供外部进入该activity的方法
    public static void enterAtyCopyDialog(Context context,SingleJoke singleJoke){
        Intent i=new Intent(context,CopyDialogActivity.class);
        i.putExtra("share_joke", singleJoke);
        context.startActivity(i);
    }

    private void initIntentData() {
        Intent i=getIntent();
        singleJoke=i.getParcelableExtra("share_joke");
        if(singleJoke==null){
            ShowToast.show(this, "出错!");
            return;
        }
    }

    private void initView() {
        findViewById(R.id.copydialog_copy).setOnClickListener(this);
        findViewById(R.id.copydialog_jubao).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.copydialog_copy:
                copytoClipBoard();
                ShowToast.show(CopyDialogActivity.this, "复制成功!");
                CopyDialogActivity.this.finish();
                break;
            case R.id.copydialog_jubao:
                jubaoDo();
                ShowToast.show(getApplicationContext(),"举报成功!");
                CopyDialogActivity.this.finish();
                break;
        }
    }

    private void jubaoDo() {
        new JubaoNet(singleJoke.getJoke_id(), App.getAppInstance().getUserToken(), new JubaoNet.SuccessJubaoCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, new JubaoNet.FailJubaoCallback() {
            @Override
            public void onFail() {

            }
        });
    }

    private void copytoClipBoard() {

        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("mjoke", singleJoke.getBase64DecodeContent()+ MyConfig.SHARE_WEIXIN_TEXT_HOUZHUI));

        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            c.setText(singleJoke.getContent()+MyConfig.SHARE_WEIXIN_TEXT_HOUZHUI);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
