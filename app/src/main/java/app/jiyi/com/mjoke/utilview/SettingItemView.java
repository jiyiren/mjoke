package app.jiyi.com.mjoke.utilview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;

import app.jiyi.com.mjoke.R;

/**
 * Created by JIYI on 2015/9/4.
 */
public class SettingItemView extends LinearLayout implements com.andexert.library.RippleView.OnRippleCompleteListener{

    private String lefttext,righttext;
    private int type;

    private TextView tv_left,tv_right;
    private ImageView iv_arrow;

    private RippleView rippleView;
    private SettingRippleViewComplete settingRippleViewComplete;
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray=context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        lefttext=typeArray.getString(R.styleable.SettingItemView_st_lefttext);
        type=typeArray.getInt(R.styleable.SettingItemView_st_type,0);
        if(type==1){
            righttext=typeArray.getString(R.styleable.SettingItemView_st_righttext);
        }
        typeArray.recycle();
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.setting_item, this);
        rippleView= (RippleView) v.findViewById(R.id.setting_item_rippleview);
        rippleView.setOnRippleCompleteListener(this);
        tv_left= (TextView) v.findViewById(R.id.setting_item_lefttext);
        iv_arrow= (ImageView) v.findViewById(R.id.setting_item_arrow);
        tv_right= (TextView) v.findViewById(R.id.setting_item_righttext);
        tv_left.setText(lefttext);
        if(type==1){
            iv_arrow.setVisibility(GONE);
            tv_right.setVisibility(VISIBLE);
            tv_right.setText(righttext);
        }else{
            iv_arrow.setVisibility(VISIBLE);
            tv_right.setVisibility(GONE);
        }
    }

    public void setLefttext(int strid){
        tv_left.setText(strid);
    }
    public void setLefttext(String str){
        tv_left.setText(str);
    }
    public void setRighttext(int strid){
        tv_right.setText(strid);
    }
    public void setRighttext(String str){
        tv_right.setText(str);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if(settingRippleViewComplete!=null) {
            settingRippleViewComplete.onRvComplete(this);
        }
    }

    public void setOnRippleViewComplete(SettingRippleViewComplete rippleViewComplete){
        this.settingRippleViewComplete=rippleViewComplete;
    }

    public interface SettingRippleViewComplete{
        public void onRvComplete(SettingItemView settingItemView);
    }
}
