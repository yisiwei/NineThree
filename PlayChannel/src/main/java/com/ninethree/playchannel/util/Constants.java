package com.ninethree.playchannel.util;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 */
public class Constants {

    public static final String LOGIN_GUID = "E6A4D9AD-7CCF-4643-91E1-4E9E7994890E";
    public static final String PUBLIC_GUID = "628C94BE-E584-404C-B60C-1E9E9D4BAF35";
    public static final int TYPE_ID = 2010;
    public static final int RETURN_ID = 1;
    public static final String DB_ID = "Android.Wcf";
    public static final String PROCEDURE_NAME = "Login_ByAndroid";

    public static final String UPGRADE_URL = "http://m.93966.net:1210/AndroidDown/ylpd_upgrade.json";

    public static final String APP_ID = "wx6d8c5c154a61661c";
    public static final String APP_SECRET = "a790e9f9ca37dce39e3cbbf93192c72f";

    /**
     * apk下载目录
     */
    public static final String UPGRADE_DOWNLOAD_PATH = Environment
            .getExternalStorageDirectory() + File.separator;

    /**
     * apk文件名
     */
    public static final String APK_NAME = "ylpd.apk";


    /**
     * @return String
     * @Title: getCode
     * @Description: 获取验证码
     */
    public static String getCode() {
        String code = "";
        int codeLength = 4;
        String[] random = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z"};// 随机数
        for (int i = 0; i < codeLength; i++) {
            int index = (int) Math.floor(Math.random() * 36);
            code += random[index];
        }
        return code;
    }

}
