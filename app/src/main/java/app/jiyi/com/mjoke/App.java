package app.jiyi.com.mjoke;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import app.jiyi.com.mjoke.bean.User;

/**
 * Created by JIYI on 2015/8/23.
 */
public class App extends Application {

    public static RequestQueue queues;
    private static App mapp;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        mapp=this;
        queues= Volley.newRequestQueue(getApplicationContext());
        super.onCreate();
    }

    public static RequestQueue getQueues(){
        return queues;
    }

    //同一时间只允许有一个Application实例
    public static synchronized App getAppInstance(){
        return mapp;
    }

    public synchronized SharedPreferences getSharedPreferenceInstance(){
        if(sp==null) {
            sp = getSharedPreferences(MyConfig.SHAREDPREFERENCE_NAME, Activity.MODE_PRIVATE);
        }
        return sp;
    }
//----------------登录状态----------------
    //获取登录状态
    public boolean getisLogin(){
       return getSharedPreferenceInstance().getBoolean(MyConfig.SHARED_ISLOGIN,false);
    }

    //设置登录成功或者退出登录
    public void setisLogin(boolean islogin){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putBoolean(MyConfig.SHARED_ISLOGIN, islogin);
        e.commit();
        if(islogin==false){
            clearCurrentUser();
        }
    }
//----------------登录状态----------------
    public void saveCurrentUser(User user){
        SharedPreferences.Editor editor=getSharedPreferenceInstance().edit();
        editor.putString(MyConfig.SHARED_USER_TOKEN,user.getToken());
        editor.putString(MyConfig.SHARED_USER_NAME,user.getUsername());
        editor.putString(MyConfig.SHARED_USER_PWD,user.getUserpwd());
        editor.putString(MyConfig.SHARED_USER_SEX,user.getSex());
        editor.putString(MyConfig.SHARED_USER_MOTTO,user.getMotto());
        editor.putString(MyConfig.SHARED_USER_HEADERURL, user.getHeaderurl());
        editor.commit();
    }

    public void clearCurrentUser(){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.clear();//clear连islogin的也会清楚
        e.commit();
    }

    //返回用户的token
    public String getUserToken(){
        if(!getisLogin()){
            return null;
        }
        return getSharedPreferenceInstance().getString(MyConfig.SHARED_USER_TOKEN,null);
    }

    public void setUserToken(String token){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putString(MyConfig.SHARED_USER_TOKEN,token);
        e.commit();
    }

    //返回用户名
    public String getUserName(){
        if(!getisLogin()){
            return null;
        }
        return getSharedPreferenceInstance().getString(MyConfig.SHARED_USER_NAME,null);
    }

    public void setUserName(String name){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putString(MyConfig.SHARED_USER_NAME,name);
        e.commit();
    }

    //返回用户性别 “man” "woman"
    public String getUserSex(){
        if(!getisLogin()){
            return null;
        }
        return getSharedPreferenceInstance().getString(MyConfig.SHARED_USER_SEX,null);
    }

    public void setUserSex(String sex){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putString(MyConfig.SHARED_USER_SEX,sex);
        e.commit();
    }
    //返回用户座右铭
    public String getUserMotto(){
        if(!getisLogin()){
            return null;
        }
        return getSharedPreferenceInstance().getString(MyConfig.SHARED_USER_MOTTO,null);
    }

    public void setUserMotto(String motto){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putString(MyConfig.SHARED_USER_MOTTO,motto);
        e.commit();
    }

    //或的主题颜色
    public int getThemeColor(){
        return getSharedPreferenceInstance().getInt(MyConfig.SHARED_THEME_COLOR,getResources().getColor(R.color.maincolor));
    }

    //设置主题颜色
    public void setThemeColor(int color){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putInt(MyConfig.SHARED_THEME_COLOR,color);
        e.commit();
    }

    //获得是否显示悬浮按钮
    public boolean getIsSetFlotingBt(){
        return  getSharedPreferenceInstance().getBoolean(MyConfig.SHARED_IS_FLOTING,true);
    }
    //设置是否显示悬浮
    public void setIsSetFlotingBt(boolean isflot){
        SharedPreferences.Editor e=getSharedPreferenceInstance().edit();
        e.putBoolean(MyConfig.SHARED_IS_FLOTING,isflot);
        e.commit();
    }
    
}
