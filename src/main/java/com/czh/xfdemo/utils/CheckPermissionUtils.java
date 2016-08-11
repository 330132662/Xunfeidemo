package com.czh.xfdemo.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LiJianfei on 2016/7/25.
 */
public class CheckPermissionUtils {
    private static final String TAG = "debug";

    /**
     * A  首先调用这个
     * check this permission is need to request
     * 检测权限是否需要请求
     * 请检查此权限是需要请求的
     *
     * @param context         context
     * @param permissionArray permissionArray
     * @return
     */
    public static String[] getNeededPermission(Context context, String[] permissionArray) {
        if (context == null || permissionArray == null || permissionArray.length == 0) {
            return new String[]{};
        }

        List<String> permissionList = new ArrayList<>();
        for (String aPermissionArray : permissionArray) {
            if (CheckPermissionUtils.isNeedAddPermission(context, aPermissionArray)) {
//                返回true 就是权限没被同意  添加到集合中 转为数组返回
                permissionList.add(aPermissionArray);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    /**
     * check permission and show setting dialog for user
     * 为用户检测权限和显示设置窗口
     *
     * @param getPermissionListener getPermissionListener
     * @param context               context
     * @param permissions           permissions
     * @param grantResults          grantResults  这个值到底从哪里获得？？
     */
    public static void checkPermissionResult(OnHasGetPermissionListener getPermissionListener, Context context
            , String[] permissions, int[] grantResults) {
        if (context == null || permissions == null || grantResults == null || getPermissionListener == null) {
            Log.d(TAG, "context=" + context + "\n"
                    + "permissions=" + Arrays.toString(permissions) + "\n"
                    + "grantResults=" + Arrays.toString(grantResults));
            return;
        }

        Log.d(TAG, "permissions=" + Arrays.toString(permissions) + ",grantResults=" + Arrays.toString(grantResults));
        // save the request permission
        List<PermissionInfo> list = new ArrayList<>();//Your  permission list

        if (grantResults.length <= 0) {
            return;
        }

        // check permission request result 检查权限请求结果(PERMISSION_GRANTED = 0
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                PermissionInfo info = null;
                try {
                    info = context.getPackageManager().getPermissionInfo(permissions[i], 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (info != null) {
                    list.add(info);
                }
            }
        }

        // is we has all permission 是否有所有权限
        if (list.size() == 0) {
            Log.d(TAG, "checkPermissionResult onSuccess");
            getPermissionListener.onPermissionSuccess();
        } else {
            getPermissionListener.onPermissionFail();
            // show the dialog for user to setting
            Log.d(TAG, "checkPermissionResult onFail");
        }
    }

    public interface OnHasGetPermissionListener {
        void onPermissionSuccess();

        void onPermissionFail();
    }

    /**
     * 内部调用  外部不用调用
     * check permission is need ?
     * 检测权限是否需要
     *
     * @return true: need permission  false: don't need permission
     */
    private static boolean isNeedAddPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }
}
