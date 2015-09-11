package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/21.
 */
public class LoginNet {
    public LoginNet(String type,String username,String pwd_md5,final SuccessLoginCallback successLoginCallbackCallback,
                    final FailLoginCallback failLoginCallback){
        new NetConnection(MyConfig.URL_LOGIN, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successLoginCallbackCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failLoginCallback.onFail();
            }
        },MyConfig.KEY_TYPE,type,MyConfig.KEY_USERNAME,username,MyConfig.KEY_PWD,pwd_md5);

    }

    public static interface SuccessLoginCallback{
        void onSuccess(String result);
    }
    public static interface FailLoginCallback{
        void onFail();
    }
}
