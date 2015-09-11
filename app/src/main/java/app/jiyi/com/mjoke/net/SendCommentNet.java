package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/9.
 */
public class SendCommentNet {

    public SendCommentNet(String joke_id,String token,String username,String content, final SuccessSendCommentCallback successSendCommentCallback,
                          final FailSendCommentCallback failSendCommentCallback){
        new NetConnection(MyConfig.URL_SENDCOMMENT, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successSendCommentCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failSendCommentCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.KEY_TOKEN,token,MyConfig.KEY_USERNAME,username,MyConfig.COMMENT_CONTENT,content);

    }

    public static interface SuccessSendCommentCallback{
        void onSuccess(String result);
    }

    public static interface FailSendCommentCallback{
        void onFail();
    }
}
