package app.jiyi.com.mjoke.utilview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;

import app.jiyi.com.mjoke.R;

/**
 *
 这个是圆角矩形
 android:clickable="true"
 android:src="@drawable/image1"
 app:borderColor="#fff"  //边框颜色
 app:borderWidth="2dp"   //边框宽度
 app:hoverColor="#22000000" //点击时的颜色，也是默认颜色
 app:borderRadius="6dp" //圆角矩形的圆角度数
 */

public class RoundImageView extends HoverImageView{
	private float radius = 10;
	
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup(attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(attrs);
	}

	public RoundImageView(Context context) {
		super(context);
	}

	protected void setup(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
		radius = typedArray.getDimension(R.styleable.RoundImageView_borderRadius, radius);
		typedArray.recycle();
	}

	@Override
	public void buildBoundPath(Path boundPath){
		boundPath.reset();
		
		final int width = getWidth();
		final int height = getHeight();
		radius = Math.min(radius, Math.min(width, height) * 0.5f);
		
		RectF rect = new RectF(0, 0, width, height);
		boundPath.addRoundRect(rect , radius, radius, Direction.CW);
	}
	
	@Override
	public void buildBorderPath(Path borderPath) {
		borderPath.reset();
		
		final float halfBorderWidth = getBorderWidth() * 0.5f;
		
		final int width = getWidth();
		final int height = getHeight();
		radius = Math.min(radius, Math.min(width, height) * 0.5f);
		
		RectF rect = new RectF(halfBorderWidth, halfBorderWidth,
				width - halfBorderWidth, height - halfBorderWidth);
		borderPath.addRoundRect(rect , radius, radius, Direction.CW);
	}

}
