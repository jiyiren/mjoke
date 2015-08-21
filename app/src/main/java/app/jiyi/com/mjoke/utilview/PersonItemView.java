package app.jiyi.com.mjoke.utilview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.jiyi.com.mjoke.R;


public class PersonItemView extends LinearLayout {

	private Drawable msrc;
	private String content;
	private int value_line;
	private float x,y;
	
	private ImageView iv;
	private TextView tv,tv_long;
	public PersonItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typeArray=context.obtainStyledAttributes(attrs, R.styleable.PersonItemView);
		msrc=typeArray.getDrawable(R.styleable.PersonItemView_msrc);
		content=typeArray.getString(R.styleable.PersonItemView_text);
		value_line=typeArray.getInt(R.styleable.PersonItemView_showline, -1);
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		x = dm.xdpi;  
		y = dm.ydpi;  
		typeArray.recycle();
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		View v=inflater.inflate(R.layout.personitemview,this);
		iv=(ImageView) v.findViewById(R.id.personitem_iv_icon);
		tv=(TextView) v.findViewById(R.id.personitem_tv_info);
		iv.setImageDrawable(msrc);
		tv.setText(content);
		
		if(value_line==0){
			tv_long=(TextView) v.findViewById(R.id.personitem_long);
			LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,
					2);
			lp.setMargins(0, 0, 0, 0);
			tv_long.setLayoutParams(lp);
		}
/*		if(value_line==1){
			lp.setMargins((int)(75*x/160), 0, 0, 0);
		}
*/		
		
	}
	
	public void setImgsrc(int resid){
		iv.setImageResource(resid);
	}
	public void setTextcon(String con){
		tv.setText(con);
	}

	

	

}
