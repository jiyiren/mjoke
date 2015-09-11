package app.jiyi.com.mjoke.net;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import app.jiyi.com.mjoke.utiltool.MyLog;

/**
 * Created by JIYI on 2015/8/21.
 */
public class NetConnection {
    public NetConnection(final String url,final HttpMethod httpMethod,final SuccessCallback successCallback,
                         final FailCallback failCallback,final String ... kvs){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                StringBuffer paramStr=new StringBuffer();
                for (int i=0;i<kvs.length;i+=2){
                    paramStr.append(kvs[i]).append("=").append(kvs[i+1]);
                    if(i!=kvs.length-2){
                        paramStr.append("&");
                    }
                }
                MyLog.i("jiyiren","paramStr:"+paramStr.toString());//日志

                try {
                    URLConnection connection=null;
                    switch (httpMethod){
                        case GET:
                            connection=new URL(url+"?"+paramStr).openConnection();
//                            connection.connect();
                            break;
                        case POST:
                            connection=new URL(url).openConnection();
                            connection.setDoOutput(true);
                            connection.setConnectTimeout(5000);
//                            connection.setDoInput(true);
                            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));
                            bw.write(paramStr.toString());
                            bw.flush();
//                            connection.connect();
                            break;
                    }
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                    StringBuffer resultBuffer=new StringBuffer();
                    String line=null;
                    while ((line=br.readLine())!=null){
                        resultBuffer.append(line);
                    }
                    MyLog.i("jiyiren","resultStr:"+resultBuffer.toString());//日志
                    return resultBuffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null){
                    if(successCallback!=null){
                        successCallback.onSuccess(s);
                    }
                }else{
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(s);
            }
        }.execute();

    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }

}
