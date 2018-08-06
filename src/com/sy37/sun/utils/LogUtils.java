package com.sy37.sun.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LogUtils {

    private static boolean debug = true;

    public static void setDebug(boolean debug) {
        LogUtils.debug = debug;
    }

    /**获取时间
     * @return String
     */
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(new Date());
    }

    public static void log(String message){
        System.out.println(getDate() + " >>>> " + message);
    }

    public static void debug(String message){
        if (debug){
            log(message);
        }
    }

    /**释放流信息，防止进程阻塞
     * @param proc
     */
    public static void printStream(Process proc){
        try {
            StreamLog errorGobbler = new StreamLog(	proc.getErrorStream(),"Error", null);
            errorGobbler.start();
            StreamLog outputGobbler = new StreamLog(proc.getInputStream(),"Output", null);
            outputGobbler.start();
        } catch (Exception e) {
            log("获取数据流出错");
        }
    }

}
