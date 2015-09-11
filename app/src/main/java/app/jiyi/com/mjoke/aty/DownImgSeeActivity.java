package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.photoview.PhotoView;

public class DownImgSeeActivity extends BaseActivity {

    private PhotoView photoView;
    private String imgdir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_img_see);
        setImmerseLayout(findViewById(R.id.downsee_fl));
        initData();
        initView();
    }

    private void initData() {
        imgdir=getIntent().getStringExtra("imgdir");
        if(imgdir==null){
            ShowToast.show(DownImgSeeActivity.this,"出现意外!");
            return;
        }
    }

    //提供外部进入该activity的方法
    public static void enterDownImgseeAty(Context context,String imgdir){
        Intent i=new Intent(context,DownImgSeeActivity.class);
        i.putExtra("imgdir", imgdir);
        context.startActivity(i);
    }

    private void initView() {
        photoView= (PhotoView) findViewById(R.id.downsee_iv_photo);
        Bitmap b=BitmapFactory.decodeFile(imgdir);
        if(b!=null){
            photoView.setImageBitmap(b);
        }else{
            ShowToast.show(DownImgSeeActivity.this,"非同寻常的事发生了!");
        }
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownImgSeeActivity.this.finish();
            }
        });
    }
}
