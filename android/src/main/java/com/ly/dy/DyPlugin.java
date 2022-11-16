package com.ly.dy;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ly.dy.model.ResultModel;
import com.ly.dy.utils.DyUtils;
import com.ly.dy.utils.FileUtils;

import java.util.ArrayList;
import java.util.Objects;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * DyPlugin
 */
public class DyPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        DyUtils.getInstance().initChanel(new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dy"));
        DyUtils.getInstance().getChanel().setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        String method = call.method;
        switch (method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "initSdk":
                String clientKey = call.argument("clientKey");
                boolean initResult = DyUtils.getInstance().initSdk(clientKey);
                result.success("" + initResult);
                break;
            case "loginInWithDouyin":
                String scope = call.argument("scope");
                boolean loginResult = DyUtils.getInstance().loginWithSdk(activity, scope);
                result.success("" + loginResult);
                break;
            case "shareToEditPage":
                shareToDyWithScene(call, result, false);
                break;
            case "shareToPublishPage":
                shareToDyWithScene(call, result, true);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void shareToDyWithScene(@NonNull MethodCall call, @NonNull Result result, boolean shareToPublish) {
        try {
            ArrayList<String> tempImgPathList = (Objects.requireNonNull(call.argument("imgPathList")));
            ArrayList<String> tempVideoPathList = (Objects.requireNonNull(call.argument("videoPathList")));
            ArrayList<String> mHashTagList = (Objects.requireNonNull(call.argument("mHashTagList")));
            String mState = (Objects.requireNonNull(call.argument("mState")));
            String appId = (Objects.requireNonNull(call.argument("appId")));
            String appTitle = (Objects.requireNonNull(call.argument("appTitle")));
            String description = (Objects.requireNonNull(call.argument("description")));
            String appUrl = (Objects.requireNonNull(call.argument("appUrl")));
            ArrayList<String> imgPathList = new ArrayList<>(FileUtils.getInstance().convert2FileProvider(activity, tempImgPathList));
            ArrayList<String> videoPathList = new ArrayList<>(FileUtils.getInstance().convert2FileProvider(activity, tempVideoPathList));
            ResultModel resultModel = DyUtils.getInstance().shareToEditPage(imgPathList, videoPathList,
                    mHashTagList, mState, appId, appTitle, description, appUrl, shareToPublish);
            result.success(resultModel.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        DyUtils.getInstance().getChanel().setMethodCallHandler(null);
    }


    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }
}
