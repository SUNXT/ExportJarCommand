package com.sy37.sun.utils;

import java.util.Date;

public class LogUtils {

    private static boolean debug = true;

    public static void setDebug(boolean debug) {
        LogUtils.debug = debug;
    }

    public static void log(String message){
        System.out.println(new Date().toLocaleString() + " >>>> " + message);
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
