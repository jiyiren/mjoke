package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/8/29.
 */
public class GetJokesNet {
    public GetJokesNet(String joke_id,String scroll_type,int count,String frgtype,final SuccessGetJokeCallback successGetJokeCallback,
                       final FailGetJokeCallback failGetJokeCallback){
        new NetConnection(MyConfig.URL_GETJOKES, HttpMethod.POST, new NetConnection.SuccessCallback() {

            @Override
            public void onSuccess(String result) {
                successGetJokeCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failGetJokeCallback.onFail();
            }
        },MyConfig.KEY_JOKE_ID,joke_id,MyConfig.KEY_SCROLL_TYPE,scroll_type,MyConfig.KEY_COUNT,count+"",MyConfig.FRAGMENT_TYPE,frgtype);

    }

    public static interface SuccessGetJokeCallback{
        void onSuccess(String result);
    }
    public static interface FailGetJokeCallback{
        void onFail();
    }
}
