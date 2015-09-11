package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/27.
 */
public class ModifyNet {
    public ModifyNet(String usertoken,String username,String usersex,String motto, final SuccessModifyCallback successModifyCallback,
                     final FailModifyCallback failModifyCallback){
        new NetConnection(MyConfig.URL_MODIFY_USERINFO, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                successModifyCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failModifyCallback.onFail();
            }
        },MyConfig.KEY_TOKEN,usertoken,MyConfig.KEY_USERNAME,username,MyConfig.KEY_SEX,usersex
                ,MyConfig.KEY_MOTTO,motto);


    }

    public static interface SuccessModifyCallback{
        void onSuccess(String result);
    }
    public static interface FailModifyCallback{
        void onFail();
    }


}
