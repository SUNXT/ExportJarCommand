package com.sy37.sun.utils;

public class SystemUtils {

    //系统相关
    public static final int WINDOWS_OS = 1;
    public static final int LINUX_OS = 2;
    public static final int UNKNOWN_OS = 3;


    /**
     * 获取系统类型
     * @return
     */
    public static int getSystemOS(){
        LogUtils.log("Get the system's OS!");
        String os = System.getProperty("os.name");
        LogUtils.log("Current System is " + os);
        if (os.contains("Windows"))
            return WINDOWS_OS;
        if (os.contains("Linux"))
            return LINUX_OS;
        return UNKNOWN_OS;
    }
}
