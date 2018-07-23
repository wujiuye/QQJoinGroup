package com.wujiuye.qqgroupjoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 快速生成适配dimens工具类
 * 将该文件放到项目的包下，在项目发布之前打开该文件，右键点击Run->DimenTool.main()即可
 * 以sw320dp为标准  即values文件下的dimens
 *
 * @author  wujiuye
 * 该类需要使用Terminal编译运行，否则会报错，右键运行DimentUtil.main会编译整个工程，醉了，最后还提示运行失败，找不到类。
 * cd到java目录下，然后javac -d . com/longsunyum/lbwlply/DimenUtil.java
 * 运行(在java目录下): java com.longsunyum.lbwlply.DimenUtil
 */
public class DimenUtil {

    public static void gen() {

        //获取当前Classes 的绝对路径
        String filePath=System.getProperty("user.dir");
        String rootPath = filePath.substring(0,filePath.length()-4);
        System.out.print(rootPath+"\n");

        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File(rootPath+"res/values/dimens.xml");

        BufferedReader reader = null;
        StringBuilder sw240 = new StringBuilder();
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder w820 = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;

            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {

                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<"));

                    String subStr = tempString.substring(tempString.indexOf(">") + 1,
                            tempString.indexOf("</dimen>"));
                    if(subStr.indexOf("dip")>0){
                    	subStr = subStr.substring(0, subStr.length()-1);
                    }
                    Double num = Double.parseDouble(subStr.substring(0, subStr.length()-2));

                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw240.append(start).append( num * 0.75).append("dip").append(end).append("\r\n");
                    sw360.append(start).append( num * 1.125).append("dip").append(end).append("\r\n");
                    sw480.append(start).append(num * 1.5).append("dip").append(end).append("\r\n");
                    sw600.append(start).append(num * 1.87).append("dip").append(end).append("\r\n");
                    sw720.append(start).append(num * 2.25).append("dip").append(end).append("\r\n");
                    sw800.append(start).append(num * 2.5).append("dip").append(end).append("\r\n");
                    w820.append(start).append(num * 2.56).append("dip").append(end).append("\r\n");
                } else {
                    sw240.append(tempString).append("").append("\r\n");
                    sw360.append(tempString).append("").append("\r\n");
                    sw480.append(tempString).append("").append("\r\n");
                    sw600.append(tempString).append("").append("\r\n");
                    sw720.append(tempString).append("").append("\r\n");
                    sw800.append(tempString).append("").append("\r\n");
                    w820.append(tempString).append("").append("\r\n");
                }
                line++;
            }

            reader.close();
            System.out.println("<!--  sw240 -->");
            System.out.println(sw240);
            System.out.println("<!--  sw360 -->");
            System.out.println(sw360);
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);
            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);

            //land 横屏（代表平板设备）。
            String sw240file = rootPath+"./res/values-sw240dp/dimens.xml";
            String sw360file = rootPath+"./res/values-sw360dp/dimens.xml";
            String sw480file = rootPath+"./res/values-sw480dp/dimens.xml";
            String sw600file = rootPath+"./res/values-sw600dp/dimens.xml";
            String sw720file = rootPath+"./res/values-sw720dp/dimens.xml";
            String sw800file = rootPath+"./res/values-sw800dp/dimens.xml";
            String w820file  = rootPath+"./res/values-w820dp/dimens.xml";

            //将新的内容，写入到指定的文件中去
            writeFile(sw240file, sw240.toString());
            writeFile(sw360file, sw360.toString());
            writeFile(sw480file, sw480.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw800file, sw800.toString());
            writeFile(w820file, w820.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 如果文件存在就先销毁再创建
     * @param filePath 文件路径
     */
    public static boolean refreshFile(String filePath){
        File file=new File(filePath);
        deleteFile(file);
        if(!file.isDirectory()){
            file.mkdirs();
        }
        return true;
    }


    /**
     * 递归删除文件夹
     * @param file
     */
    private static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0;i < files.length;i ++) {
                    deleteFile(files[i]);
                }
                file.delete();
            }
        }
    }


    /**
     * 写入方法
     *
     */
    public static void writeFile(String file, String text) {
        PrintWriter out;
        try {
            if (!refreshFile((String) file.subSequence(0, file.length() - "/dimens.xml".length()))) {
                return;
            }
            out = new PrintWriter(new FileOutputStream(new File(file)));
            out.println(text);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        gen();
    }

}