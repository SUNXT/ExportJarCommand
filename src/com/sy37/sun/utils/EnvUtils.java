package com.sy37.sun.utils;

import java.util.HashMap;
import java.util.Map;

public class EnvUtils {

    private static Map<String , String> mEnvMap;//保存环境变量的map
    //环境变量相关
    public static final String PARAM_PACK_JAVA_PROJECT_PATH = "PARAM_PACK_JAVA_PROJECT_PATH";//打包项目
    public static final String PARAM_EXPORT_JAR_OUTPUT_PATH = "PARAM_EXPORT_JAR_OUTPUT_PATH";//输出的jar包的绝对路径，包含文件名
    public static final String PARAM_MANIFEST_PATH = "PARAM_MANIFEST_PATH";//项目的manifest配置文件的绝对路径。

    static {
        mEnvMap = System.getenv();
        if (mEnvMap == null){
            LogUtils.log("The System's Env is null or empty!");
            mEnvMap = new HashMap<>();
        }
    }

    /**
     * 获取环境变量
     * @param key
     * @return
     */
    public static String getEnv(String key){
        String value = mEnvMap.get(key);
        LogUtils.log("Use the system env. The key is " + key + ", get the value is " + value);
        return value;
    }

}
