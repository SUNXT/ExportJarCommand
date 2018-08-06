package com.sy37.sun.utils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ManifestUtils {

    public static void main(String[] args){
        getMainClass("D:\\IntellijIDEA\\ExportJarCommand\\src\\META-INF\\MANIFEST.MF");
    }

    /**
     * 读取项目配置文件，读出main类
     * @param manifestPath
     * @return xxx/xxx/xxx.java
     */
    public static String getMainClass(String manifestPath) {
        BufferedReader bufferedReader = null;
        String mainClass = "";
        try {
            List<String> lines = new LinkedList<>();
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(manifestPath), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!"".equals(line)) {
                    lines.add(line);
                }
            }

            LogUtils.log("Manifest content are");
            LogUtils.log("----------------------");
            for (String str : lines) {
                LogUtils.log(str);
            }
            LogUtils.log("----------------------");

            //处理出mainClass
            for (String str: lines){
                if (str.contains("Main-Class")){
                    mainClass = str.substring(str.indexOf(":") + 1, str.length());
                }
            }
            mainClass = mainClass.trim();
            mainClass = mainClass.replace(".", "/");
            mainClass += ".java";
            LogUtils.log("读取到的mainClass: " + mainClass);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    bufferedReader = null;
                }
            }
        }
        return mainClass;
    }
}
