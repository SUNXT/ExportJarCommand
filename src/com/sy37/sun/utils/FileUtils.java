package com.sy37.sun.utils;

import java.io.*;

public class FileUtils {

    public static void removeFile(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
    }

    public static void copyFile(String sourceFilePath, String targetFilePath){
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()){
            LogUtils.log("The source file is not exist!");
            return;
        }

        copyFile(sourceFile, new File(targetFilePath));
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) {

        LogUtils.log("Copy file sourceFile: " + sourceFile.getPath() + ", targetFile: " + targetFile.getPath());

        try {
            // 新建文件输入流并对它进行缓冲
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

            // 关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void delDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    delDir(files[i]);
                }
            }
            file.delete();
        } else {
            LogUtils.log("所删除的文件不存在！" + '\n');
        }
    }
}
