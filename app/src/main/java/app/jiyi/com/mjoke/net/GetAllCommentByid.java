package app.jiyi.com.mjoke.net;

import app.jiyi.com.mjoke.MyConfig;

/**
 * Created by JIYI on 2015/9/8.
 */
public class GetAllCommentByid {
    public GetAllCommentByid(String joke_id,String cur_lou,int count, final SuccessGetAllCommentCallback successGetAllCommentCallback,
                             final FailGetAllCommentCallback failGetAllCommentCallback){
        new NetConnection(MyConfig.URL_GETALLCOMMENTBYID, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                successGetAllCommentCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failGetAllCommentCallback.onFail();
            }
        },MyConfig.JOKE_ID,joke_id,MyConfig.COMMENT_CUR_LOU,cur_lou,MyConfig.KEY_COUNT,count+"");

    }

    public static interface SuccessGetAllCommentCallback{
        void onSuccess(String result);
    }
    public static interface FailGetAllCommentCallback{
        void onFail();
    }
}
