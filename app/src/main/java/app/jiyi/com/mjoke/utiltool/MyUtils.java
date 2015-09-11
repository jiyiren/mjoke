package app.jiyi.com.mjoke.utiltool;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by JIYI on 2015/8/22.
 */
public class MyUtils {


    //md5加密
    public static String md5(String string) {

        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //判断是否是email
    public static boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    //判断字符串是否为空
    //0不为空且没有空字符串，1为空，2不为空但是包含空字符
    public static int isEmpty( String input )
    {
        if ( input == null || "".equals( input ) ||input.length()==0)
            return 1;

        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            if ( c == ' ' || c == '\t' || c == '\r' || c == '\n' )
            {
                return 2;

            }
        }
        return 0;
    }

    //去除大部分的空格，制表符，回车等空格
    public static String getDeleteSpaceStr(String str){
        return str.replaceAll("\\s*", "");
    }

    //判断是否有外置sd卡
    //内存卡是否存在
    public static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    //创建根目录下的一个目录和文件
    public static String CreateFileCamePhoto(String parentName,String fileName){
        File f=new File(Environment.getExternalStorageDirectory(),parentName);
        if(!f.exists()){
            f.mkdirs();
        }
        File file=new File(f,fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory()+"/"+parentName+"/"+fileName;
    }

    //从json获取 键 所对应的 值 并返回
    public static String getGson(String json, String vaule) {
        try {
            JSONObject dataJson = new JSONObject(json);
            vaule = dataJson.getString(vaule);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vaule;
    }

    //网络连接判断
    public static boolean isNetworkConnected(Context context){
        if(context!=null){
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){
                return networkInfo.isAvailable();
            }
        }
        return  false;
    }

    //WIFi是否可用
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //移动网络是否可用
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //获取当前网络类型0为mobile,1为wifi
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    //判断app是否在后台
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
//                Log.i(context.getPackageName(), "此appimportace ="
//                        + appProcess.importance
//                        + ",context.getClass().getName()="
//                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    Log.i(context.getPackageName(), "处于后台"
//                            + appProcess.processName);
                    return true;
                } else {
//                    Log.i(context.getPackageName(), "处于前台"
//                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
      * Try to return the absolute file path from the given Uri
      *通过图片Uri返回图片绝对路径
      * @param context
      * @param uri
      * @return the file path or null
      */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );}
                }
                cursor.close();
            }
        }
        return data;
    }

    public static Bitmap decodeSampledBitmapFromFile(String fileurl,
                                                     int reqWidth, int reqHeight) {

        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeFile(fileurl, options);

        // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options. inJustDecodeBounds = false;
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeFile(fileurl, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        final int height = options. outHeight;
        final int width = options. outWidth;
        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //质量压缩，循环压缩直到图片大小小于lastweighKB，比如lastweigh=100就代表100KB
    //这个方法和下面的getimage方法可以合用，先宽高压缩再质量压缩
    public static Bitmap compressImageByWeight(Bitmap image,int lastweigh) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>lastweigh) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 20;//每次都减少10
            if(options<=0){
                break;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //从本地路径里获得压缩图片，压缩宽高为参数
    public static Bitmap getimage(String srcPath,float lasth,float lastw) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = lasth;//这里设置高度为800f
        float ww = lastw;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
//        if(!(w<=ww&&h<=hh)){
//            int xh=(int) (newOpts.outHeight / hh);
//            int xw=(int) (newOpts.outWidth / ww);
//            if(xh>xw){
//                be=xh;
//            }else{
//                be=xw;
//            }
//        }
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImageByWeight(bitmap,300);//压缩好比例大小后再进行质量压缩
    }

    //将bitmap保存到制定路径
    public static void SavePhoto(Bitmap bitmap,String path){
        FileOutputStream fos=null;
        try {
            File file=new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,70, fos);//100无压缩
            //保存为png格式，图片压缩质量为75，对于png来说这个参数会被忽略
            //jpg则使用CompressFormat.JPEG
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 分享功能
     *
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public static void shareMsg(Context context,String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }
}
