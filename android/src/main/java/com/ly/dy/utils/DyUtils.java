package com.ly.dy.utils;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.aweme.base.ImageObject;
import com.bytedance.sdk.open.aweme.base.MediaContent;
import com.bytedance.sdk.open.aweme.base.MicroAppInfo;
import com.bytedance.sdk.open.aweme.base.MixObject;
import com.bytedance.sdk.open.aweme.base.VideoObject;
import com.bytedance.sdk.open.aweme.share.Share;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.DouYinOpenConfig;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;
import com.google.gson.Gson;
import com.ly.dy.model.ResultModel;

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


    /**
     * 获取 access_token
     *
     * @param authCode
     * @return
     */
    public void getAccessToken(String authCode) {
        if (!authCode.isEmpty()) {
            ResultModel authCodeModel = new ResultModel(200, authCode, "获取AuthCode成功");
            Map<String, Object> authCodeMap = authCodeModel.toMap();
            getChanel().invokeMethod("getAccessToken", authCodeMap, new MethodChannel.Result() {
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
    }

    /**
     * 分享编辑页面结果
     *
     * @param result
     * @return
     */
    public void getSharePageResult(ResultModel result) {
        Map<String, Object> shareResultMap = result.toMap();
        getChanel().invokeMethod("getSharePageResult", shareResultMap, new MethodChannel.Result() {
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
     * @param imgPathList    图片路径列表
     * @param videoPathList  视频路径列表
     * @param shareToPublish 是否分享到发布页
     * @param mHashTagList   话题tag
     * @param mState         1：传入自定义字符串，可在 Response 中获取到该值，集成方可唯一标识这次请求,传入 OpenAPI 中申请 ShareID，分享结果会通过 Webhooks 进行回调。
     * @return
     */
    public ResultModel shareToEditPage(
            ArrayList<String> imgPathList,
            ArrayList<String> videoPathList,
            ArrayList<String> mHashTagList,
            String mState,
            String appId,
            String appTitle,
            String description,
            String appUrl,
            boolean shareToPublish
    ) {
        boolean onlyImg = false;
        boolean onlyVideo = false;
        boolean isMix = false;
        if (imgPathList.isEmpty() && videoPathList.isEmpty()) {
            return new ResultModel(-1, "", "图片和视频不可以同时为空");
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
        if (shareToPublish && (isMix || onlyVideo)) {
            if (videoPathList.size() > 1) {
                return new ResultModel(-1, "", "只能分享一个视频");
            }
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

        ///话题tag
        if (mHashTagList != null && !mHashTagList.isEmpty()) {
            request.mHashTagList = mHashTagList;
        }

        ///mState
        if (!mState.isEmpty()) {
            request.mState = mState;
        }
        ///携带的小程序
        if (!appId.isEmpty() && !appTitle.isEmpty() && !appUrl.isEmpty()) {
            MicroAppInfo microAppInfo = new MicroAppInfo();
            microAppInfo.setAppId(appId);
            microAppInfo.setAppTitle(appTitle);
            microAppInfo.setDescription(description);
            microAppInfo.setAppUrl(appUrl);
            request.mMicroAppInfo = microAppInfo;
        }

        if (shareToPublish && douYinOpenApi.isAppSupportShareToPublish()) {
            request.shareToPublish = true;
        }
        boolean ifShareSuc = douYinOpenApi.share(request);
        if (ifShareSuc) {
            return new ResultModel(200, "", "调用分享api成功");

        } else {
            return new ResultModel(-1, "", "调用分享api失败");
        }
    }


}
