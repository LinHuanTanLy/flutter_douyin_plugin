package com.ly.dy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.aweme.base.ImageObject;
import com.bytedance.sdk.open.aweme.base.MediaContent;
import com.bytedance.sdk.open.aweme.base.MixObject;
import com.bytedance.sdk.open.aweme.base.VideoObject;
import com.bytedance.sdk.open.aweme.share.Share;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.DouYinOpenConfig;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Map<String, Object> map = new HashMap<>();
        map.put("authCode", authCode);
        getChanel().invokeMethod("getAccessToken", map, new MethodChannel.Result() {
            @Override
            public void success(@Nullable Object result) {
                Log.d("lht", "result--" + result);
            }

            @Override
            public void error(@NonNull String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {

            }

            @Override
            public void notImplemented() {

            }
        });
    }

    /**
     * 分享编辑页面结果
     *
     * @param resultMsg
     * @return
     */
    public void getSharePageResult(String resultMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", resultMsg);
        getChanel().invokeMethod("getSharePageResult", map, new MethodChannel.Result() {
            @Override
            public void success(@Nullable Object result) {
                Log.d("lht", "result--" + result);
            }

            @Override
            public void error(@NonNull String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {

            }

            @Override
            public void notImplemented() {

            }
        });
    }

    public boolean shareToEditPage(
            List<String> imgPathList,
            List<String> videoPathList
    ) {
        boolean onlyImg = false;
        boolean onlyVideo = false;
        boolean isMix = false;
        if (imgPathList.isEmpty() && videoPathList.isEmpty()) {
            return false;
        }
        if (!imgPathList.isEmpty() && !videoPathList.isEmpty()) {
            isMix = douYinOpenApi.isAppSupportMixShare();
        }
        if (!imgPathList.isEmpty() && videoPathList.isEmpty()) {
            onlyImg = true;
        }
        if (imgPathList.isEmpty()) {
            onlyVideo = true;
        }
        Share.Request request = new Share.Request();
        ArrayList<String> mUri = new ArrayList<>();
        request.callerLocalEntry = "com.ly.dy.douyinapi.DouYinEntryActivity";
        if (onlyImg) {
            //分享单图/多图
            mUri.addAll(imgPathList);
            ImageObject imageObject = new ImageObject();
            imageObject.mImagePaths = mUri;
            MediaContent mediaContent = new MediaContent();
            mediaContent.mMediaObject = imageObject;
            request.mMediaContent = mediaContent;
        } else if (onlyVideo) {
            // 分享视频
            mUri.addAll(videoPathList);
            VideoObject videoObject = new VideoObject();
            videoObject.mVideoPaths = mUri;
            MediaContent videoContent = new MediaContent();
            videoContent.mMediaObject = videoObject;
            request.mMediaContent = videoContent;
        } else if (isMix) {
            ///混合分享
            mUri.addAll(imgPathList);
            mUri.addAll(videoPathList);
            MixObject mixObject = new MixObject();
            mixObject.mMediaPaths = mUri;
            MediaContent mixContent = new MediaContent();
            mixContent.mMediaObject = mixObject;
            request.mMediaContent = mixContent;
        }
        return douYinOpenApi.share(request);
    }



}
