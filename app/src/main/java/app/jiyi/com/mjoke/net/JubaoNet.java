package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/15.
 */
public class JubaoNet {
    public JubaoNet(String joke_id,String user_id, final SuccessJubaoCallback successJubaoCallback,
                    final FailJubaoCallback failJubaoCallback){
        new NetConnection(MyConfig.URL_JUBAO, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successJubaoCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failJubaoCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.KEY_TOKEN,user_id);
    }

    public static interface SuccessJubaoCallback{
        void onSuccess(String result);
    }

    public static interface FailJubaoCallback{
        void onFail();
    }
}
