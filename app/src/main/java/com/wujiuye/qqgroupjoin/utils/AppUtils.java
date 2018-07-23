package com.wujiuye.qqgroupjoin.utils;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

public class AppUtils {

	/**
	 * 打开其它应用
	 * @param context
	 * @param packagename 应用的包名
	 */
	public static void doStartApplicationWithPackageName(Context context ,String packagename) {  
		  
	    // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等  
	    PackageInfo packageinfo = null;  
	    try {  
	        packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);  
	    } catch (NameNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	    if (packageinfo == null) {  
	        return;  
	    }  
	  
	    // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  
	    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);  
	    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	    resolveIntent.setPackage(packageinfo.packageName);  
	  
	    // 通过getPackageManager()的queryIntentActivities方法遍历  
	    List<ResolveInfo> resolveinfoList = context.getPackageManager()  
	            .queryIntentActivities(resolveIntent, 0);  
	  
	    ResolveInfo resolveinfo = resolveinfoList.iterator().next();  
	    if (resolveinfo != null) {  
	        // packagename = 参数packname  
	        String packageName = resolveinfo.activityInfo.packageName;  
	        // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]  
	        String className = resolveinfo.activityInfo.name;  
	        // LAUNCHER Intent  
	        Intent intent = new Intent(Intent.ACTION_MAIN);  
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);  
	  
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	        		
	        // 设置ComponentName参数1:packagename参数2:MainActivity路径  
	        ComponentName cn = new ComponentName(packageName, className);  
	  
	        intent.setComponent(cn);  
	        context.startActivity(intent);  
	    }  
	}  
	
}
