package app.jiyi.com.mjoke;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import app.jiyi.com.mjoke.aty.AtyMainView;
import app.jiyi.com.mjoke.aty.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImmerseLayout(findViewById(R.id.mainact_out));

        delayThread();
    }

    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            enterMain();

        }
    };

    private void delayThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mhandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void enterMain(){
        Intent i=new Intent(MainActivity.this, AtyMainView.class);
        startActivity(i);
        MainActivity.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        this.finish();
    }

}
