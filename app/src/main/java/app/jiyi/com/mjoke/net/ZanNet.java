package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/11.
 */
public class ZanNet {
    public ZanNet(String joke_id,String zantype, final SuccessZanCallback successZanCallback,
                  final FailZanCallback failZanCallback){
        new NetConnection(MyConfig.URL_ZAN, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successZanCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failZanCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.TYPE_ZAN,zantype);

    }

    public static interface SuccessZanCallback{
        void onSuccess(String result);
    }

    public static interface FailZanCallback{
        void onFail();
    }
}
