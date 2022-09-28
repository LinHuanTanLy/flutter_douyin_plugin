package com.ly.dy.utils;

import android.app.Activity;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.DouYinOpenConfig;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class DyUtils {
    private static DouYinOpenApi douYinOpenApi;
    private static MethodChannel channel;

    public static DyUtils getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        private static final DyUtils INSTANCE = new DyUtils();
    }

    private DyUtils() {
    }

    private DouYinOpenApi getDouYinOpenApi(Activity activity) {
        if (douYinOpenApi == null) {
            douYinOpenApi = DouYinOpenApiFactory.create(activity);
        }
        return douYinOpenApi;
    }

    public void initChanel(MethodChannel channel) {
        DyUtils.channel = channel;
    }

    public MethodChannel getChanel() {
        return channel;
    }


    /**
     * 初始化sdk
     *
     * @param clientKey 客户端key
     * @return 是否初始化成功
     */
    public boolean initSdk(String clientKey) {
        return DouYinOpenApiFactory.init(new DouYinOpenConfig(clientKey));
    }

    /**
     * 登录
     *
     * @param activity 对应ac
     * @return 是否调用成功
     */
    public boolean loginWithSdk(Activity activity) {
        Authorization.Request request = new Authorization.Request();
        request.scope = "user_info";
        request.state = "ww";
        request.callerLocalEntry = "com.ly.dy.douyinapi.DouYinEntryActivity";
        return getDouYinOpenApi(activity).authorize(request);
    }

    public void openEditPage() {
    }

    /**
     * 获取 access_token
     *
     * @param authCode
     * @return
     */
    public void getAccessToken(String authCode) {
        Map<String,Object> map= new HashMap<>();
        map.put("authCode",authCode);
        getChanel().invokeMethod("getAccessToken", map, new MethodChannel.Result() {
            @Override
            public void success(@Nullable Object result) {
                Log.d("lht","result--"+result);
            }

            @Override
            public void error(@NonNull String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {

            }

            @Override
            public void notImplemented() {

            }
        });
    }
}
