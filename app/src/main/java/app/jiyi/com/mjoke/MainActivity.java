package app.jiyi.com.mjoke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.jiyi.com.mjoke.aty.AUILSampleActivity;
import app.jiyi.com.mjoke.aty.AtyMainView;
import app.jiyi.com.mjoke.aty.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImmerseLayout(findViewById(R.id.mainact_out));
    }

    public void enterMain(View view){
        Intent i=new Intent(MainActivity.this, AtyMainView.class);
        startActivity(i);
    }
    public void enterTest(View view){
        Intent i1=new Intent(MainActivity.this, AUILSampleActivity.class);
        startActivity(i1);
    }

}
