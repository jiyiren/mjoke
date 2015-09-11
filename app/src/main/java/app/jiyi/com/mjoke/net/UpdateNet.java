package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/10.
 */
public class UpdateNet {
    public UpdateNet(String user_banbennum, final SuccessUpdateCallback successUpdateCallback,
                     final FailUpdateCallback failUpdateCallback){
        new NetConnection(MyConfig.URL_UPDATE, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successUpdateCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failUpdateCallback.onFail();
            }
        },MyConfig.USER_BANBENNUM,user_banbennum);

    }

    public static interface SuccessUpdateCallback{
        void onSuccess(String result);
    }

    public static interface FailUpdateCallback{
        void onFail();
    }
}
