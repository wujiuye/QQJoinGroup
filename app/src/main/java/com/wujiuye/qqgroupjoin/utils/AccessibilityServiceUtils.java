package com.wujiuye.qqgroupjoin.utils;

import com.wujiuye.qqgroupjoin.App;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;

public class AccessibilityServiceUtils {
	
	/**
	 * 判断AccessibilityService服务是否已打开
	 * @param mContext
	 * @return
	 */
	public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = App.Instance().getPackageName()+"/"+App.Instance().getPackageName()+".server.QQJoinGroupService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (SettingNotFoundException e) {
          
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
           
        }

        return accessibilityFound;      
    }

}
