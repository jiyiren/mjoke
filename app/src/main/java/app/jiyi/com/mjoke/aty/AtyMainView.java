package app.jiyi.com.mjoke.aty;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.jiyi.com.mjoke.R;
import app.jiyi.com.mjoke.fragment.Tab01Fragment;
import app.jiyi.com.mjoke.fragment.Tab02Fragment;
import app.jiyi.com.mjoke.fragment.Tab03Fragment;
import app.jiyi.com.mjoke.fragment.Tab04Fragment;

public class AtyMainView extends BaseActivity implements View.OnClickListener{

    private int mCurrentPage=0;

    private ViewPager mViewPager;
    private List<Fragment> mDatas;
    private FragmentPagerAdapter mAdapter;

    private TextView tv_tab[]=new TextView[4];
    private View tabline;
    private int tabWidth;

    //标题栏上的处理变量
    private PopupWindow mPopWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_main_view);
        setImmerseLayout(findViewById(R.id.main_title));
        initView();
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.tab_viewpager);
        tv_tab[0]= (TextView) findViewById(R.id.tv_tab01);
        tv_tab[1]= (TextView) findViewById(R.id.tv_tab02);
        tv_tab[2]= (TextView) findViewById(R.id.tv_tab03);
        tv_tab[3]= (TextView) findViewById(R.id.tv_tab04);
        tabline=findViewById(R.id.tab_line);

        initTabline();//设置tabline的初始宽度
        setAllTextColorOrigin();//将所有文本设置为未点击颜色
        setTextColor(mCurrentPage);

        Tab01Fragment tab01=new Tab01Fragment();
        Tab02Fragment tab02=new Tab02Fragment();
        Tab03Fragment tab03=new Tab03Fragment();
        Tab04Fragment tab04=new Tab04Fragment();
        mDatas=new ArrayList<Fragment>();
        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);
        mDatas.add(tab04);

        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabline.setTranslationX((positionOffset+position)*tabWidth);
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage=position;
                setAllTextColorOrigin();//先将全部设置为默认颜色
                setTextColor(position);//再将下一个设置为需要颜色
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //设置tabline的初始宽度
    private void initTabline() {
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int ScreenWidth=metrics.widthPixels;
        tabWidth=ScreenWidth/tv_tab.length;

        ViewGroup.LayoutParams lp=tabline.getLayoutParams();
        lp.width=tabWidth;
    }

    private void setTextColor(int position){
        if(position<tv_tab.length) {
            tv_tab[position].setTextColor(getResources().getColor(R.color.maincolor));
        }
    }

    //将textView颜色设置原始色
    private void setAllTextColorOrigin(){
        for (int i=0;i<tv_tab.length;i++){
            tv_tab[i].setTextColor(getResources().getColor(R.color.maincolor_off));
        }
    }

    //tab栏的点击事件
    public void tabClick(View view){
        switch (view.getId()){
            case R.id.rl_tab01:
                if(mCurrentPage!=0){
                    mViewPager.setCurrentItem(0);
                }
                break;
            case R.id.rl_tab02:
                if(mCurrentPage!=1){
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.rl_tab03:
                if(mCurrentPage!=2){
                    mViewPager.setCurrentItem(2);
                }
                break;
            case R.id.rl_tab04:
                if(mCurrentPage!=3){
                    mViewPager.setCurrentItem(3);
                }
                break;
        }

    }

    //title点击事件
    public void titleButtonClick(View view){
        switch (view.getId()){
            case R.id.title_rl_me:
//                Toast.makeText(AtyMainView.this,"个人中心",Toast.LENGTH_SHORT).show();
                AtyPersonCenter.enterAtyPersonCenter(AtyMainView.this);
                break;
            case R.id.title_rl_menu:
                if (mPopWin != null&&mPopWin.isShowing()) {
                    mPopWin.dismiss();
                return;
            } else {
                    initPopWindow();
                    int fiftypx=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                            getResources().getDisplayMetrics());
//                    Toast.makeText(AtyMainView.this,"fifty:"+fiftypx,Toast.LENGTH_SHORT).show();
                    mPopWin.showAsDropDown(view, -fiftypx, 5);//展示在哪个控件下
//                    mPopWin.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 10,10);
            }
            break;
        }
    }

    //初始化弹窗菜单
    private void initPopWindow(){
        View mPopView= LayoutInflater.from(AtyMainView.this).inflate(R.layout.popmenu_layout,null);

        mPopWin=new PopupWindow(mPopView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
//        mPopWin.setAnimationStyle(R.style.AnimationFade);
        ColorDrawable cd = new ColorDrawable(-0000);
        mPopWin.setBackgroundDrawable(cd);
        mPopWin.setFocusable(true);
        mPopWin.setOutsideTouchable(true);
        mPopWin.update();
    }

    public void popMenuItemClick(View view){
        String stip=null;
        if (mPopWin != null&&mPopWin.isShowing()) {
            mPopWin.dismiss();
        }
        switch (view.getId()){
            case R.id.popmenu_item_set:
                stip="设置";
                break;
            case R.id.popmenu_item_refresh:
                stip="刷新";
                break;
            case R.id.popmenu_item_edit:
                stip="发表";
                break;
            case R.id.popmenu_item_search:
                stip="搜索";
                break;
            case R.id.popmenu_item_email:
                stip="反馈";
                break;
        }
        Toast.makeText(this,"点击了"+stip,Toast.LENGTH_SHORT).show();
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

}
