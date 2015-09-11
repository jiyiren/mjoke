package app.jiyi.com.mjoke.utiltool;

import android.content.Context;
import android.widget.Toast;

import app.jiyi.com.mjoke.R;

/**
 * Created by JIYI on 2015/8/22.
 */
public class ShowToast {

    //普通Toast提示
    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
    //普通Toast提示-重载
    public static void show(Context context,int resstrid){
        Toast.makeText(context,resstrid,Toast.LENGTH_LONG).show();
    }

    //网络连接错误提示
    public static void showNetConnFail(Context context){
        Toast.makeText(context, R.string.fail_to_net,Toast.LENGTH_SHORT).show();
    }

    //提示登录
    public static void showFirstLogin(Context context){
        Toast.makeText(context,"请先登录！",Toast.LENGTH_SHORT).show();
    }
}
