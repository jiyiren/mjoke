package app.jiyi.com.mjoke.aty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.jiyi.com.mjoke.App;
import app.jiyi.com.mjoke.MyConfig;
import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.utiltool.MyLog;
import app.jiyi.com.mjoke.utiltool.ShowToast;
import app.jiyi.com.mjoke.utilview.staggergridview.StaggeredGridView;

/**
 * 个人中心点击我的下载，显示该界面
 * AtyPersonCenter---->this
 */
public class DownLoadActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_title;
    private StaggeredGridView staggeredGridView;

    private List<Bitmap> mListData;
    private String imgdir;
    private GridViewAdapter gva;
    private ArrayList<String> paths = new ArrayList<String>();//所有图片地址
    private FrameLayout toptilebar;
    private RelativeLayout rl_none;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        setImmerseLayout(findViewById(R.id.toptitlebar));
        initView();
    }

    private void initView() {
        toptilebar= (FrameLayout) findViewById(R.id.toptitlebar);
        imgdir= Environment.getExternalStorageDirectory()+"/"+ MyConfig.BASE_DIR_NAME+"/"+MyConfig.BASE_DIR_IMAGE_DOWNLOAD+"/";
        MyLog.i("jiyiren","imgdir:"+imgdir);
        tv_title= (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_title.setText(R.string.person_center_download);

        findViewById(R.id.rl_toptitlebar_back).setOnClickListener(this);
        staggeredGridView= (StaggeredGridView) findViewById(R.id.staggeredGridView1);
        rl_none= (RelativeLayout) findViewById(R.id.rl_down_load_none);
        if(mListData==null){
            mListData=new ArrayList<Bitmap>();
        }
        File baseFile = new File(imgdir);
        if(!baseFile.exists()){
            ShowToast.show(DownLoadActivity.this,"您还没有下载哦!");
            staggeredGridView.setVisibility(View.GONE);
            rl_none.setVisibility(View.VISIBLE);
            return;
        }
        paths=imagePath(baseFile);
        if(paths==null||paths.size()==0){
            ShowToast.show(DownLoadActivity.this,"您还没有下载哦~~");
            staggeredGridView.setVisibility(View.GONE);
            rl_none.setVisibility(View.VISIBLE);
            return;
        }
        gva=new GridViewAdapter(this);
        staggeredGridView.setAdapter(gva);
        gva.notifyDataSetChanged();
        staggeredGridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
            @Override
            public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                DownImgSeeActivity.enterDownImgseeAty(DownLoadActivity.this,paths.get(position));
            }
        });
    }


    //提供外部进入该activity的方法
    public static void enterDownLoadAty(Context context){
            Intent i = new Intent(context, DownLoadActivity.class);
            context.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_toptitlebar_back:
                this.finish();
                break;
        }
    }
    /**
     * 获取图片地址列表
     * @param file
     * @return
     */
    private  ArrayList<String> imagePath(File file) {
        ArrayList<String> list = new ArrayList<String>();

        File[] files = file.listFiles();
        for (File f : files) {
            list.add(f.getAbsolutePath());
            MyLog.i("jiyiren", "imgdir:" + f.getAbsolutePath());
        }
//        Collections.sort(list);
        return list;
    }

    private  class GridViewAdapter extends BaseAdapter{

        private Context context;
        public GridViewAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object getItem(int position) {
            return paths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.gridview_item,null);
            }
            imageView= (ImageView) convertView.findViewById(R.id.iv_down_pic);
            Bitmap b= BitmapFactory.decodeFile(paths.get(position));
            imageView.setImageBitmap(b);
            return convertView;

        }
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
