package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/7.
 */
public class UserCollectNet {

    public UserCollectNet(String joke_id,String token,String type,final SuccessUserCollectCallback successUserCollectCallback,
                           final FailUserCollectCallback failUserCollectCallback){
        new NetConnection(MyConfig.URL_USERCOLLECT, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successUserCollectCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failUserCollectCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.KEY_TOKEN,token,MyConfig.JOKE_TYPE,type);

    }

    public static interface SuccessUserCollectCallback{
        void onSuccess(String result);
    }

    public static interface FailUserCollectCallback{
        void onFail();
    }

}
