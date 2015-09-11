package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/4.
 * 无图文的说说发表
 */
public class PublicJokeNoImgNet {
    public PublicJokeNoImgNet(String token,String type,String content, final SuccessPublicJokeNoImgCallback successPublicJokeNoImgCallback,
                              final FailPublicJokeNoImgCallback failPublicJokeNoImgCallback){
        new NetConnection(MyConfig.URL_PUBLISHJOKE_NOIMG, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successPublicJokeNoImgCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failPublicJokeNoImgCallback.onFail();
            }
        },MyConfig.KEY_TOKEN,token,MyConfig.KEY_JOKE_TYPE,type,MyConfig.KEY_JOKE_CONTENT,content);

    }

    public static interface SuccessPublicJokeNoImgCallback{
        void onSuccess(String result);
    }
    public static interface FailPublicJokeNoImgCallback{
        void onFail();
    }
}
