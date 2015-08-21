package app.jiyi.com.mjoke.utilview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by JIYI on 2015/8/10.
 */
public class ScaleScrollView extends ScrollView{
    private  boolean isonce;
    private LinearLayout mParentView;
    private ViewGroup mTopView;
    private ViewGroup mDownView;

    private int mScreenHeight;
    private int mTopViewHeight;

    private int mCurrentOffset=0;//当前右侧滚轮的偏移量

    private ObjectAnimator oa;

    public ScaleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight=metrics.heightPixels;
        mTopViewHeight=mScreenHeight/2-(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isonce) {
            mParentView = (LinearLayout) this.getChildAt(0);
            mTopView = (ViewGroup) mParentView.getChildAt(0);
            mDownView= (ViewGroup) mParentView.getChildAt(1);
            mTopView.getLayoutParams().height = mTopViewHeight;
            isonce=true;
        }
    }

    private float startY=0;
    private boolean isBig;
    private boolean isTouchOne;//是否是一次连续的MOVE，默认为false,
    //在MoVe时，如果发现滑动标签位移量为0，则获取此时的Y坐标，作为起始坐标，然后置为true,为了在连续的Move中只获取一次起始坐标
    //当Up弹起时，一次触摸移动完成，将isTouchOne置为false
    private float distance=0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action =ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(mCurrentOffset<=0){
                    if(!isTouchOne){
                        startY=ev.getY();
                        isTouchOne=true;
                    }
                    distance=ev.getY()-startY;
//                    Log.i("jiyi","distance:"+distance);
                    if(distance>0){
                        isBig=true;
                        setT((int)-distance/4);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isBig) {
                    reset();
                    isBig=false;
                }
                isTouchOne=false;
            break;
        }
        return super.onTouchEvent(ev);
    }

    public void setT(int t) {
        scrollTo(0, 0);
        if (t < 0) {
            mTopView.getLayoutParams().height = mTopViewHeight-t;
            mTopView.requestLayout();
        }
    }

    private void reset() {
        if (oa != null && oa.isRunning()) {
            return;
        }
        oa = ObjectAnimator.ofInt(this, "t", (int)-distance / 4, 0);
        oa.setDuration(150);
        oa.start();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mCurrentOffset = t;//右边滑动标签相对于顶端的偏移量

        //当手势上滑，则右侧滚动条下滑，下滑的高度小于TopView的高度，则让TopView的上滑速度小于DownView的上滑速度
        //DownView的上滑速度是滚动条的速度，也就是滚动的距离是右侧滚动条的距离
        //则TopView的速度要小，只需要将右侧滚动条的偏移量也就是t缩小一定倍数就行了。我这里除以2速度减小1倍
        if (t <= mTopViewHeight&&t>=0&&!isBig) {
//            float scale = t * 1.0f / mTopViewHeight;//0~1
//            float topscale = 1 - 0.6f * scale;//1-0.4
            mTopView.setTranslationY(t / 2);//使得TopView滑动的速度小于滚轮滚动的速度
        }
        if(isBig){
            scrollTo(0,0);
        }

    }
}
