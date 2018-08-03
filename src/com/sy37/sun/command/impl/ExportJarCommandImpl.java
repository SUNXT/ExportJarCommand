package com.sy37.sun.command.impl;

import com.sy37.sun.command.IExportJarCommand;
import com.sy37.sun.utils.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ExportJarCommandImpl implements IExportJarCommand{

    @Override
    public void javac(String projectPath, String manifestPath) {

        LogUtils.log("Use javac command!!!");

        LogUtils.log("Begin to javac the project, the path is projectPath...");
        File projectDir = new File(projectPath);
        if (!projectDir.exists()){
            LogUtils.log("The project is not exists! End...");
            return;
        }

        File manifestFile = new File(manifestPath);
        if (!manifestFile.exists()){
            LogUtils.log("The manifest file is not exists! End...");
            return;
        }
        //从配置文件中读出编译的Main类
        String mainClassPath = ManifestUtils.getMainClass(manifestPath);
        if ("".equals(mainClassPath)){
            LogUtils.log("The main class path is null! Enc...");
            return;
        }

        LogUtils.log("Step 1: Find the dependent jar in libs dir...");
        Set<String> dependentLibPaths = new HashSet<>();//用于保存编译过程中需要依赖的jar包或者其他包路径
        //通过查找项目目录下的libs目录，如果存在，则需要将其目录下的jar文件添加到编译中
        File libsDir = new File(projectDir.getPath() + File.separator + "libs");
        if (libsDir.exists()){
            LogUtils.log("The project has libs dir, add libs path...");
            File[] files = libsDir.listFiles((dir, name) -> name.endsWith("jar"));
            if (files != null){
                for (File jarFile: files){
                    dependentLibPaths.add(jarFile.getPath());
                }
            }
        }else {
            LogUtils.log("The project doesn't have lib dir...");
        }
        //查找项目下的所有包名 及路径
        LogUtils.log("Step 2: Find all packages in project...");
        File srcDir = new File(projectPath + File.separator + "src");
        if (!srcDir.exists()){
            LogUtils.log("The src is not exist!!! End...");
            return;
        }

        Set<String> packageSet = new HashSet<>();
        //通过递归查出所有包名
        findPackage(srcDir, packageSet);
        //将所有包名添加要依赖库集合中
        dependentLibPaths.addAll(packageSet);

        for (String packName: dependentLibPaths){
            LogUtils.debug(packName);
        }

        //处理编译后的class输出文件夹
        File classesOutputDir = new File(projectPath + File.separator + COMPILE_CLASS_DIR_NAME);
        if (classesOutputDir.exists()){
            classesOutputDir.delete();
        }
        classesOutputDir.mkdirs();

        //执行javac命令
        LogUtils.log("Step 3: Run the javac command...");
        StringBuilder commandStringBuilder = new StringBuilder();
        String libSpiltChar = ";";
        if (SystemUtils.LINUX_OS == SystemUtils.getSystemOS()){
            libSpiltChar = ":";
        }

        commandStringBuilder.append("javac -classpath .");
        for (String dependentLib: dependentLibPaths){
            commandStringBuilder.append(libSpiltChar).append(dependentLib.endsWith(".jar") ? dependentLib : dependentLib.replace(srcDir.getPath() + File.separator, ""));
        }

        commandStringBuilder.append(" -sourcepath .");
        for (String packagePath: packageSet){
            commandStringBuilder.append(libSpiltChar).append(packagePath.replace(srcDir.getPath() + File.separator, ""));
        }

        commandStringBuilder.append(" -encoding utf-8");//编码
        commandStringBuilder.append(" -d ").append(classesOutputDir.getPath());//class输出路径
        commandStringBuilder.append(" -Xlint:unchecked -Xlint:deprecation");

        //接下来对存在java文件的包名进行编译
        String commandStr = commandStringBuilder.toString() + " " + mainClassPath;
        //对MainClass进行编译，不需要所有类都编译，因为，从Main入口开始编译，就可以调用用到的类去编译
        LogUtils.log("Start using the command to javac the mainClass: " + mainClassPath);
        LogUtils.log("The command is: " + commandStr);

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commandStr, null, srcDir);
            LogUtils.printStream(process);
            if (process.waitFor() != 0) {
                if (process.exitValue() == 0)
                    LogUtils.log("exit...");
            }
            LogUtils.log("Has success compile! The class output file is in " + classesOutputDir.getPath());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void jar(String projectPath, String manifestPath, String outputJarPath) {
        LogUtils.log("Use the jar command!!");

        LogUtils.log("projectPath = " + projectPath);
        LogUtils.log("manifestPath = " + manifestPath);
        LogUtils.log("outputJarPath = " + outputJarPath);

        //查找项目是否存在libs，是否存在依赖的jar
        LogUtils.log("Step 1: Find the dependent jar in libs dir...");
        Set<String> dependentLibPaths = new HashSet<>();//用于保存编译过程中需要依赖的jar包或者其他包路径
        //通过查找项目目录下的libs目录，如果存在，则需要将其目录下的jar文件添加到编译中
        File libsDir = new File(projectPath + File.separator + "libs");
        if (libsDir.exists()){
            LogUtils.log("The project has libs dir, add libs path...");
            File[] files = libsDir.listFiles((dir, name) -> name.endsWith("jar"));
            if (files != null){
                for (File jarFile: files){
                    dependentLibPaths.add(jarFile.getPath());
                }
            }
        }else {
            LogUtils.log("The project doesn't have lib dir...");
        }

        String classesDirPath = projectPath + File.separator + COMPILE_CLASS_DIR_NAME;
        if (!dependentLibPaths.isEmpty()){
            LogUtils.log("Unzip dependent jar to " + classesDirPath);
            LogUtils.log("Move all jar to " + classesDirPath);
            LogUtils.log("Jar count is " + dependentLibPaths.size());
            for (String jarSourcePath: dependentLibPaths){
                String jarTargetPath = jarSourcePath.replace(libsDir.getPath(), classesDirPath);
                FileUtils.copyFile(jarSourcePath, jarTargetPath);
                unzipJar(jarTargetPath);
                FileUtils.removeFile(jarTargetPath);
            }
        }

        LogUtils.log("Step 2: Use jar command to build the class to be a jar...");
        String jarCommand = "jar cvfm " + outputJarPath + " " + manifestPath + " -C " + classesDirPath + File.separator + " .";
        LogUtils.log("The command is " + jarCommand);
        try {
            Process process = Runtime.getRuntime().exec(jarCommand);
            LogUtils.printStream(process);
            if (process.waitFor() != 0) {
                if (process.exitValue() == 0)
                    LogUtils.log("exit...");
            }
            LogUtils.debug("Build jar success!!! Jar is in ->" + outputJarPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 递归查找包名，将有依赖的包名添加到set中
     * @param currentDir
     * @param needCompilePackageSet
     */
    private void findPackage(File currentDir, Set<String> needCompilePackageSet){
       File[] files = currentDir.listFiles();
       if (files == null){
           return;
       }

       for (File childFile: files){
           if (childFile.isDirectory()){
               findPackage(childFile, needCompilePackageSet);
           }else {
               //将父包名添加到set中
               if (childFile.getName().endsWith(".java")){
                   needCompilePackageSet.add(childFile.getParent());
               }
           }
       }
    }

    private void unzipJar(String jarPath){
        File jar = new File(jarPath);
        if (!jar.exists()){
            LogUtils.log("The jar is not exist! path = " + jarPath);
        }
        String command = "jar xf " + jar.getName();
        try {
            Process process = Runtime.getRuntime().exec(command, null, new File(jar.getParent()));
            if (process.waitFor() != 0) {
                if (process.exitValue() == 0)
                    LogUtils.log("exit...");
            }
            LogUtils.debug("Unzip jar success!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
