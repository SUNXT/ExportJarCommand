package com.sy37.sun.utils;

import java.io.*;

public class StreamLog extends Thread{

    InputStream is;
    String type;
    OutputStream os;

    public StreamLog(InputStream is, String type, OutputStream redirect){
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            if (os != null)
                pw = new PrintWriter(os);

            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null){
                    pw.println(line);
                }
                System.out.println(type + ">" + line);
            }

            if (pw != null) {
                pw.flush();
                pw.close();
            }

            br.close();
            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

        }
    }
}
