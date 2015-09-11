package app.jiyi.com.mjoke.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.jiyi.com.mjoke.utiltool.MyLog;

/**
 * Created by JIYI on 2015/9/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mjoke.db";//数据库名
    private static final int DATABASE_VERSION = 5;//数据库版本，用于更新

    String sql_collect="CREATE TABLE IF NOT EXISTS joke_collect(" +
            "id integer primary key autoincrement,"+
            "joke_id VARCHAR,"+
            "user_id VARCHAR)";
    String sql_look="CREATE TABLE IF NOT EXISTS joke_look(" +
            "id integer primary key autoincrement,"+
            "joke_id VARCHAR,"+
            "user_id VARCHAR)";
    String sql_zan="CREATE TABLE IF NOT EXISTS joke_zan(" +
            "id integer primary key autoincrement,"+
            "joke_id VARCHAR,"+
            "user_id VARCHAR,"+
            "iszan VARCHAR)";
    public DBHelper(Context context){
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_collect);
        db.execSQL(sql_look);
        db.execSQL(sql_zan);
        MyLog.i("jiyihaha","DBHelper_OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //这里可以用于更新版本时操作
        db.execSQL("drop table if exists joke_zan");
        db.execSQL("drop table if exists joke_collect");
        db.execSQL("drop table if exists joke_look");
        onCreate(db);
        //http://blog.csdn.net/guolin_blog/article/details/39151617
    }
}
