package app.jiyi.com.mjoke.utiltool;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JIYI on 2015/8/21.
 * 需要一些权限: <br>
 *     http://www.open-open.com/lib/view/open1409641529760.html
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> <br>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /><br>
 *
 */
public class MyLog {

    public static final String CACHE_DIR_NAME = "dPhoneLog";

    public static boolean isDebugModel = false;// 是否输出日志
    public static boolean isSaveDebugInfo = false;// 是否保存调试日志
    public static boolean isSaveCrashInfo = false;// 是否保存报错日志

    public static void v(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.v(tag, "--> " + msg);
        }
    }

    public static void d(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.d(tag, "--> " + msg);
        }
    }

    public static void i(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.i(tag, "--> " + msg);
        }
    }

    public static void w(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.w(tag, "--> " + msg);
        }
    }

    /**
     * 调试日志，便于开发跟踪。
     * @param tag
     * @param msg
     */
    public static void e(final String tag, final String msg)
    {
        if (isDebugModel)
        {
            Log.e(tag, "--> " + msg);
        }

        if (isSaveDebugInfo)
        {
            new Thread()
            {
                public void run()
                {
                    write(time() + tag + " --> " + msg + "\n");
                };
            }.start();
        }
    }

    /**
     * try catch 时使用，上线产品可上传反馈。
     * @param tag
     * @param tr
     */
    public static void e(final String tag, final Throwable tr)
    {
        if (isSaveCrashInfo)
        {
            new Thread()
            {
                public void run()
                {
                    write(time() + tag + " [CRASH] --> "
                            + getStackTraceString(tr) + "\n");
                };
            }.start();
        }
    }

    /**
     * 获取捕捉到的异常的字符串
     * @param tr
     * @return
     */
    public static String getStackTraceString(Throwable tr)
    {
        if (tr == null)
        {
            return "";
        }

        Throwable t = tr;
        while (t != null)
        {
            if (t instanceof UnknownHostException)
            {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 标识每条日志产生的时间
     * @return
     */
    private static String time()
    {
        return "["
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
                System.currentTimeMillis())) + "] ";
    }

    /**
     * 以年月日作为日志文件名称
     * @return
     */
    private static String date()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System
                .currentTimeMillis()));
    }

    /**
     * 保存到日志文件
     * @param content
     */
    public static synchronized void write(String content)
    {
        try
        {
            FileWriter writer = new FileWriter(getFile(), true);
            writer.write(content);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志文件路径
     * @return
     */
    public static String getFile()
    {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + CACHE_DIR_NAME);
        if (!cacheDir.exists())
            cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator + date() + ".txt");

        return filePath.toString();
    }
}
