package app.jiyi.com.mjoke.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.jiyi.com.mjoke.bean.UserCollectBean;
import app.jiyi.com.mjoke.bean.UserLookBean;
import app.jiyi.com.mjoke.bean.UserZanBean;
import app.jiyi.com.mjoke.utiltool.MyLog;

/**
 * Created by JIYI on 2015/9/6.
 */
public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    public DBManager(Context context){
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        String pdb=context.getDatabasePath(DBHelper.DATABASE_NAME).getPath();
        MyLog.i("jiyihaha", "Database_path:" + pdb);
        dbHelper=new DBHelper(context);
        db=dbHelper.getWritableDatabase();
    }

    //插入操作，添加收藏
    public void addCollect(UserCollectBean userCollectBean){
        if(!queryCollect(userCollectBean)) {
            db.beginTransaction();
            try {
                db.execSQL("INSERT INTO joke_collect(joke_id,user_id) VALUES(?,?)",
                        new Object[]{userCollectBean.getJoke_id(), userCollectBean.getUser_id()});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }else{
            return;
        }
    }

    //查询是否有Collect
    public boolean queryCollect(UserCollectBean userCollectBean){
        Cursor c=db.rawQuery("SELECT * FROM joke_collect WHERE joke_id=? AND user_id=?",
                new String[]{userCollectBean.getJoke_id(), userCollectBean.getUser_id()});
        if (c.moveToNext()){
            return true;
        }
        return false;
    }

    //删除Collect
    public void deleteCollect(UserCollectBean userCollectBean){
        db.delete("joke_collect", "joke_id=? AND user_id=?",
                new String[]{userCollectBean.getJoke_id(), userCollectBean.getUser_id()});
    }

    //查询出所有的Collect
    public List<String> queryAllCollect(String user_id){
        List<String> mList=new ArrayList<String>();
        Cursor c=db.rawQuery("SELECT joke_id FROM joke_collect WHERE user_id=?",
                new String[]{user_id});
        while (c.moveToNext()){
            String joke_id=c.getString(c.getColumnIndex("joke_id"));
            mList.add(joke_id);
        }
        return mList;
    }
//--------------------------------------------------------------------------------------------------
    //插入操作，添加浏览
    public void addLook(UserLookBean userLookBean){
        if(!queryLook(userLookBean)) {
            db.beginTransaction();
            try {
                db.execSQL("INSERT INTO joke_look(joke_id,user_id) VALUES(?,?)",
                        new Object[]{userLookBean.getJoke_id(), userLookBean.getUser_id()});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }else{
            return;
        }
    }

    //查询是否有Look
    public boolean queryLook(UserLookBean userLookBean){
        Cursor c=db.rawQuery("SELECT * FROM joke_look WHERE joke_id=? AND user_id=?",
                new String[]{userLookBean.getJoke_id(), userLookBean.getUser_id()});
        if (c.moveToNext()){
            return true;
        }
        return false;
    }

    //查询出所有的Look
    public List<String> queryAllLook(String user_id){
        List<String> mList=new ArrayList<String>();
        Cursor c=db.rawQuery("SELECT joke_id FROM joke_look WHERE user_id=?",
                new String[]{user_id});
        while (c.moveToNext()){
            String joke_id=c.getString(c.getColumnIndex("joke_id"));
            mList.add(joke_id);
        }
        return mList;
    }
//--------------------------------------------------------------------------------------------------
    public void addZan(UserZanBean userZanBean){
        if(queryZan(userZanBean).equals("-1")) {
            db.beginTransaction();
            try {
                db.execSQL("INSERT INTO joke_zan(joke_id,user_id,iszan) VALUES(?,?,?)",
                        new Object[]{userZanBean.getJoke_id(), userZanBean.getUser_id(), userZanBean.getIszan()});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }else{
            return;
        }
    }

    //更新赞类型
    public void upgradeZan(UserZanBean userZanBean){
        ContentValues cv=new ContentValues();
        cv.put("iszan",userZanBean.getIszan());
        db.update("joke_zan",cv,"joke_id=? AND user_id=?",new String[]{userZanBean.getJoke_id(),userZanBean.getJoke_id()});
    }

    //查询是否有赞，有则返回赞的标识(赞或者Bad)，否则返回-1
    public String queryZan(UserZanBean userZanBean){
        Cursor c=db.rawQuery("SELECT iszan FROM joke_zan WHERE joke_id=? AND user_id=?",
                new String[]{userZanBean.getJoke_id(),userZanBean.getUser_id()});
        if (c.moveToNext()){
            return c.getString(c.getColumnIndex("iszan"));
        }
        return "-1";
    }




    //在整个应用结束关闭
    public void closeDb(){
        db.close();
    }

}
