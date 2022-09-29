package com.ly.dy.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import io.flutter.plugin.common.MethodChannel;

public class FileUtils {
    private static FileUtils fileUtils;
    private static MethodChannel channel;

    public static FileUtils getInstance() {
        return FileUtils.Holder.INSTANCE;
    }

    public static class Holder {
        private static final FileUtils INSTANCE = new FileUtils();
    }

    private FileUtils() {
    }
    public ArrayList<String> convert2FileProvider(Context context, ArrayList<String> uriList) {
        ArrayList<String> result = new ArrayList<>();
        for (String path : uriList) {
            try {
                String[] uriParts = path.split("\\.");
                if (uriParts.length > 0) {
                    String suffix = uriParts[uriParts.length - 1];
                    File file = new File(context.getExternalFilesDir(null), "/newMedia");
                    boolean isMkdirsSuc = file.mkdirs();
                    Log.d("lht", "isMkdirsSuc" + isMkdirsSuc);
                    File tempFile = File.createTempFile("share_demo", "." + suffix, file);
                    if (copyFile(new File(path), tempFile)) {
                        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", tempFile);
                        context.grantUriPermission("com.ss.android.ugc.aweme", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Toast.makeText(context, "" + uri.toString(), Toast.LENGTH_SHORT).show();
                        result.add(uri.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean copyFile(File src, File dest) {
        boolean result = false;
        if (src == null || dest == null) {
            return false;
        }
        if (dest.exists()) {
            dest.delete();
        }

        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            dest.createNewFile();
            srcChannel = new FileInputStream(src).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
                if (srcChannel != null) {
                    destChannel.close();
                }
            } catch (Exception e) {
            }
        }

        return result;
    }
}
