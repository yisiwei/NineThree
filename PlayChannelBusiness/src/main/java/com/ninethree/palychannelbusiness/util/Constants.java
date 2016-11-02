package com.ninethree.palychannelbusiness.util;

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

    public static final String APP_ID = "wxbfb5e910250253b2";
    public static final String APP_SECRET = "778098323d6e2d60b3ced312c2369ba1";

    /**
     * apk下载目录
     */
    public static final String UPGRADE_DOWNLOAD_PATH = Environment
            .getExternalStorageDirectory() + File.separator;

    /**
     * apk文件名
     */
    public static final String APK_NAME = "ylqyj.apk";


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

    /**
     * 0	未分配
     * 1000	下线
     * 1010	上线
     * -1000	废弃
     */
    public static String getTerminaState(Integer typeId){
        String state = "";
        switch (typeId){
            case 0:
                state = "未分配";
                break;
            case 1000:
                state = "下线";
                break;
            case 1010:
                state = "上线";
                break;
            case -1000:
                state = "废弃";
                break;
        }
        return state;
    }

}
