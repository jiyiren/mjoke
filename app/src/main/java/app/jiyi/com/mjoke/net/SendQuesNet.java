package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/8.
 */
public class SendQuesNet {

    public SendQuesNet(String content,String email, final SuccessSendQuesCallback successSendQuesCallback,
                       final FailSendQuesCallback failSendQuesCallback){
        new NetConnection(MyConfig.URL_SENDQUES, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successSendQuesCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failSendQuesCallback.onFail();
            }
        },MyConfig.QUES_CONTENT,content,MyConfig.QUES_EMAIL,email);

    }


    public static interface SuccessSendQuesCallback{
        void onSuccess(String result);
    }

    public static interface FailSendQuesCallback{
        void onFail();
    }
}
