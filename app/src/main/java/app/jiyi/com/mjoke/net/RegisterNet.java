package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/21.
 */
public class RegisterNet {
    public RegisterNet(String username,String pwd_md5,final  SuccessRegisterCallback successRegisterCallback,
                     final FailRegisterCallback failRegisterCallback){
        new NetConnection(MyConfig.URL_REGISTER, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successRegisterCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failRegisterCallback.onFail();
            }
        },MyConfig.KEY_USERNAME,username,MyConfig.KEY_PWD,pwd_md5);

    }

    public static interface SuccessRegisterCallback{
        void onSuccess(String result);
    }

    public static interface FailRegisterCallback{
        void onFail();
    }
}
