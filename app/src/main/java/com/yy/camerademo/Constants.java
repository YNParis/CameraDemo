package com.yy.camerademo;

import android.os.Environment;
import java.io.File;

public class Constants {

    //================= URL =====================
    /**
     * 北京测试服务器
     */
    public static final String BASE_URL = "http://218.92.211.30:10017/";
    public static final String BASE_URL_SYS = BASE_URL + "sys/";//系统管理
    public static final String BASE_URL_CMS = BASE_URL + "cms/";//内容管理
    public static final String ICON_UPLOAD_URL = BASE_URL + "file/";//头像文件

    //================= 正则 ===================
    public static final String eMailRegex = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    public static final String telRegex = "[1][3456789]\\d{9}";
    public static final String IDcard = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X|x)$";
    public static final String INTREGEX = "^[1-9]\\d*$";   //匹配正整数

    //================= 接口返回结果 ===================
    public static final String SUCCESS = "1";//成功
    public static final String TOKEN_INVAILD = "2";//TOKEN实效
    public static final String NEED_LOGIN = "3";//账号已在别处登录
    public static final String FAILED = "0";//失败

    //================= PATH ====================

    public static final String
        PATH_DATA = Environment.getDataDirectory().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "code" + File.separator + "Menu";

    //================= CODE =================
    public static final int TO_LOGIN = 0X001;
    public static final int RETURN_PERSONAL = 0X002;

    //个人信息跳转到名字和签名编辑页面码
    public static final int EDIT_NAME_CODE = 0X003;
    public static final int EDIT_DESCRIPTION_CODE = 0X004;

    //================= pageSize ========================
    public static final String PAGE_SIZE = "20";
}
