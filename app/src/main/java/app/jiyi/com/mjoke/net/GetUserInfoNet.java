package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/30.
 */
public class GetUserInfoNet {
    public GetUserInfoNet(String token, final SuccessGetInfoCallback successGetInfoCallback,
                          final FailGetInfoCallback failGetInfoCallback){
        new NetConnection(MyConfig.URL_GETUSERINFO, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successGetInfoCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failGetInfoCallback.onFail();
            }
        },MyConfig.KEY_TOKEN,token);

    }

    public static interface SuccessGetInfoCallback{
        void onSuccess(String result);
    }

    public static interface FailGetInfoCallback{
        void onFail();
    }
}
