package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/29.
 */
public class GetTopJokesNet {
    public GetTopJokesNet(String look_count, String scroll_type, int count,String fragtype,final SuccessGetTopJokeCallback successGetJokeCallback,
                          final FailGetTopJokeCallback failGetJokeCallback){
        new NetConnection(MyConfig.URL_GETTOPJOKES, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                successGetJokeCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failGetJokeCallback.onFail();
            }
        },MyConfig.JOKE_LOOK,look_count,MyConfig.KEY_SCROLL_TYPE,scroll_type,MyConfig.KEY_COUNT,count+"",MyConfig.FRAGMENT_TYPE,fragtype);

    }

    public static interface SuccessGetTopJokeCallback{
        void onSuccess(String result);
    }
    public static interface FailGetTopJokeCallback{
        void onFail();
    }
}
