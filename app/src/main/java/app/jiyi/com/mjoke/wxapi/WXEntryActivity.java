package app.jiyi.com.mjoke.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import app.jiyi.com.mjoke.aty.ShareDialogActivity;
import app.jiyi.com.mjoke.utiltool.ShowToast;

/**
 * Created by JIYI on 2015/9/9.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, ShareDialogActivity.WEIXIN_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq arg0) { }

    @Override
    public void onResp(BaseResp resp) {
//        LogManager.show(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:"
//                + resp.errStr, 1);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ShowToast.show(getApplicationContext(),"分享成功!");
                WXEntryActivity.this.finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ShowToast.show(getApplicationContext(),"分享取消!");
                WXEntryActivity.this.finish();
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ShowToast.show(getApplicationContext(),"分享拒绝!");
                WXEntryActivity.this.finish();
                //分享拒绝
                break;
        }
    }
}
