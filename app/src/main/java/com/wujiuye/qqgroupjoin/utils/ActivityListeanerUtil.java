package com.wujiuye.qqgroupjoin.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class ActivityListeanerUtil {
	/**
     *判断指定包名的应用程序处于前台还是后台
     *android4.4以下才能用
     */
    public static boolean isApplicationBroughtToBackground(final Context context,String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 返回app运行状态 
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return int 1:前台  2:后台  0:不存在
     */
    public static int isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> listInfos
                = activityManager.getRunningTasks(20);
      //判断程序是否在栈顶  
        if (listInfos.get(0).topActivity.getPackageName().equals(packageName)) {  
            return 1;  
        } else {  
            //判断程序是否在栈里  
            for (ActivityManager.RunningTaskInfo info : listInfos) {  
                if (info.topActivity.getPackageName().equals(packageName)) {  
                    return 2;  
                }  
            }  
            return 0;//栈里找不到，返回3  
        }  
    }
    
    /**
     * 获取指定包名的应用程序处于前台的当前显示activity的类名
     * @param context
     * @param packageName
     * @return
     */
    public static String getApplicationBroughtActivityName(final Context context,String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(packageName)) {
                return topActivity.getClassName();
            }
        }
        return null;
    }
}
