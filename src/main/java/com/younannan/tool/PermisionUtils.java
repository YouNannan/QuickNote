package com.younannan.tool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/*
在android6.0以后方便请求权限的工具类
 */
public class PermisionUtils {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // Internet Permissions
    private static final int REQUEST_INTERNET = 2;
    private static String[] PERMISSIONS_INTERNET = {
            Manifest.permission.INTERNET};

    // Internet Permissions
    private static final int REQUEST_CALL = 3;
    private static String[] PERMISSIONS_CALL = {
            Manifest.permission.CALL_PHONE};

    private static final int REQUEST_ALL = 4;
    private static String[] PERMISSIONS_ALL = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET};
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    public static void verifyInternetPermissions(Activity activity) {
        // Check if we have internet permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_INTERNET,
                    REQUEST_INTERNET);
        }
    }
    public static void verifyCallPermissions(Activity activity) {
        // Check if we have internet permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CALL,
                    REQUEST_CALL);
        }
    }
    public static void verifyAllPermissions(Activity activity) {
        // Check if we have internet permission
        int permission1 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);
        int permission2 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);
        int permission3 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);

        if (permission1 != PackageManager.PERMISSION_GRANTED
                || permission2 != PackageManager.PERMISSION_GRANTED
                || permission3 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_ALL,
                    REQUEST_ALL);
        }
    }
}
