package com.ly.dy.douyinapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bytedance.sdk.open.aweme.CommonConstants;
import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.aweme.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.aweme.common.model.BaseReq;
import com.bytedance.sdk.open.aweme.common.model.BaseResp;
import com.bytedance.sdk.open.aweme.share.Share;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;
import com.ly.dy.model.ResultModel;
import com.ly.dy.utils.DyUtils;

public class DouYinEntryActivity extends Activity implements IApiEventHandler {

    DouYinOpenApi douYinOpenApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        douYinOpenApi = DouYinOpenApiFactory.create(this);
        douYinOpenApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.d("lht", "onReq: " + req.callerPackage);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("lht", "onResp: " + resp.getType());
        // 授权成功可以获得authCode
        if (resp.getType() == CommonConstants.ModeType.SEND_AUTH_RESPONSE) {
            Authorization.Response response = (Authorization.Response) resp;
            if (resp.isSuccess()) {
                String authCode = response.authCode;
                DyUtils.getInstance().getAccessToken(authCode);
            } else {
                Toast.makeText(this, "授权失败",
                        Toast.LENGTH_LONG).show();
            }

        } else if (resp.getType() == CommonConstants.ModeType.SHARE_CONTENT_TO_TT_RESP) {
            //发布回调
            Share.Response response = (Share.Response) resp;
            ResultModel shareResult = new ResultModel();
            if (response.isSuccess()) {
                shareResult.setCode(200);
                shareResult.setErrorMessage("分享成功");
            } else {
                shareResult.setCode(-1);
                shareResult.setErrorMessage(response.errorMsg);
            }
            Log.d("lht", "onResp: " + shareResult.toString());

            DyUtils.getInstance().getSharePageResult(shareResult);
        }
        this.finish();
    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        // 错误数据
        Toast.makeText(this, "intent出错啦", Toast.LENGTH_LONG).show();
        this.finish();
    }
}