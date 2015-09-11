package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/7.
 */
public class UserLookNet {
    public UserLookNet(String joke_id, String token, final SuccessUserLookCallback successUserLookCallback,
                       final FailUserLookCallback failUserLookCallback){
        new NetConnection(MyConfig.URL_USERLOOK, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successUserLookCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failUserLookCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.KEY_TOKEN,token);

    }

    public static interface SuccessUserLookCallback{
        void onSuccess(String result);
    }

    public static interface FailUserLookCallback{
        void onFail();
    }
}
