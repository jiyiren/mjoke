package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/7.
 */
public class GetUserAboutJokeNet {

    public GetUserAboutJokeNet(String joke_id, String token, String count, String joketype,final SuccessGetUserJokeCallback successGetUserCollectCallback,
                               final FailGetUserJokeCallback failGetUserCollectCallback){
        new NetConnection(MyConfig.URL_GETUSER_ABOUT_JOKE, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
            successGetUserCollectCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failGetUserCollectCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.KEY_TOKEN,token,MyConfig.KEY_COUNT,count,MyConfig.USER_ABOUT_JOKE_TYPE,joketype);

    }

    public static interface SuccessGetUserJokeCallback{
        void onSuccess(String result);
    }

    public static interface FailGetUserJokeCallback{
        void onFail();
    }
}
