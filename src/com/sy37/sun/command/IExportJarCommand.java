package com.sy37.sun.command;

public interface IExportJarCommand {

    String COMPILE_CLASS_DIR_NAME = "classesOutput";//编译后的类的文件夹
    void javac(String projectPath, String manifestPath);//使用javac命令编译java文件
    void jar(String projectPath, String manifestPath, String outputJarPath);//使用jar命令导出jar包
}
