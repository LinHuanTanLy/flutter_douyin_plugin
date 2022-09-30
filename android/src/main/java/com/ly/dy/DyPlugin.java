package com.ly.dy;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.ly.dy.utils.DyUtils;
import com.ly.dy.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
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
                boolean loginResult = DyUtils.getInstance().loginWithSdk(activity);
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

            List<String> imgPathList = new ArrayList<>(FileUtils.getInstance().convert2FileProvider(activity, tempImgPathList));
            List<String> videoPathList = new ArrayList<>(FileUtils.getInstance().convert2FileProvider(activity, tempVideoPathList));
            boolean shareResult = DyUtils.getInstance().shareToEditPage(imgPathList, videoPathList, shareToPublish,activity);
            result.success("" + shareResult);
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
