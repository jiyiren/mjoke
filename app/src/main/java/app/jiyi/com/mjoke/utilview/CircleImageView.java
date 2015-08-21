package app.jiyi.com.mjoke.utilview;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;

/**
 * 这个是正圆
 android:clickable="true"
 android:src="@drawable/image1"
 app:borderColor="#fff"  //边框颜色
 app:borderWidth="2dp"   //边框宽度
 app:hoverColor="#22000000" //点击时的颜色
 */
public class CircleImageView extends HoverImageView{

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public CircleImageView(Context context) {
		super(context);
		setup();
	}

	protected void setup() {
		
	}

	@Override
	public void buildBoundPath(Path borderPath){
		borderPath.reset();
		
		final int width = getWidth();
		final int height = getHeight();
		final float cx = width * 0.5f;
		final float cy = height * 0.5f;
		final float radius = Math.min(width, height) * 0.5f;
		
		borderPath.addCircle(cx, cy, radius, Direction.CW);
	}
	
	@Override
	public void buildBorderPath(Path borderPath) {
		borderPath.reset();
		
		final float halfBorderWidth = getBorderWidth() * 0.5f;
		
		final int width = getWidth();
		final int height = getHeight();
		final float cx = width * 0.5f;
		final float cy = height * 0.5f;
		final float radius = Math.min(width, height) * 0.5f;
		
		borderPath.addCircle(cx, cy, radius - halfBorderWidth, Direction.CW);
	}
}
