package app.jiyi.com.mjoke.utiltool;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by JIYI on 2015/8/12.
 * dp与像素的转化工具类
 */
public class DensityUtil {
    public static int dp2px(Context context,int dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(Context context,int spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());

    }
}
