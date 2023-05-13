package com.crazymakercircle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;


public class LinuxCmdUtils {


    public static boolean executeLinuxCmd(String cmd) {

        boolean result = false;

        System.out.println("got cmd : " + cmd);
        Runtime run = Runtime.getRuntime();
        //InputStream in=null;
        try {
            Process process = run.exec(cmd);
            //执行结果 0 表示正常退出
            int exeResult = process.waitFor();
            if (exeResult == 0) {
                System.out.println("执行成功");
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取linux命令执行的结果,cat 之类
     *
     * @param cmd
     * @return
     */
    public static String getCmdResult(String cmd) {

        String result = "";
        try {

            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                result = line;
            }

        } catch (java.io.IOException e) {

            System.err.println("IOException " + e.getMessage());

        }
        return result;
    }

    /**
     * grep 类的shell命令
     *
     * @param cmdStr
     * @return
     */
    public static String getGrepCmdReturn(String cmdStr) {

        String[] cmd = new String[3];

        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = null;
        try {

            Process process = runtime.exec(cmd);

            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            stringBuffer = new StringBuffer();

            String temp = null;
            System.out.println("命令的输出："+cmdStr);
            while ((temp = bufferReader.readLine()) != null) {
                System.out.println(temp);
                stringBuffer.append(temp);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }
    public static void main(String[] args) {

        System.out.println(LinuxCmdUtils.getGrepCmdReturn("pwd"));
    }


}