package com.sy37.sun;

import com.sy37.sun.command.IExportJarCommand;
import com.sy37.sun.command.impl.ExportJarCommandImpl;
import com.sy37.sun.utils.EnvUtils;
import com.sy37.sun.utils.LogUtils;


public class Main {

    public static void main(String[] args) {
        IExportJarCommand exportJarCommand = new ExportJarCommandImpl();
        String projectPath = EnvUtils.getEnv(EnvUtils.PARAM_PACK_JAVA_PROJECT_PATH);
        String manifestPath = EnvUtils.getEnv(EnvUtils.PARAM_MANIFEST_PATH);
        String outputJarPath = EnvUtils.getEnv(EnvUtils.PARAM_EXPORT_JAR_OUTPUT_PATH);

//        projectPath = "D:\\IntellijIDEA\\ExportJarCommand";
//        manifestPath = projectPath + "\\src\\META-INF\\MANIFEST.MF";
//        outputJarPath = projectPath + "\\test.jar";
        LogUtils.log("projectPath = " + projectPath);
        LogUtils.log("manifestPath = " + manifestPath);
        LogUtils.log("outputJarPath = " + outputJarPath);
        boolean javacSuccess = exportJarCommand.javac(projectPath, manifestPath);
        if (javacSuccess){
            exportJarCommand.jar(projectPath, manifestPath, outputJarPath);
        }else {
            LogUtils.log(">>>Javac failed!!!");
        }

    }

}
