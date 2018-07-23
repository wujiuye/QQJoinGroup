package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlSplashWindow{
	
	private HandlSplashWindow(){};
	public static final HandlSplashWindow instance = new HandlSplashWindow();
	
	
	/**
	 *  处理首页显示
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service){

		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		
		List<AccessibilityNodeInfo> tabs = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/tabs");
		if(tabs!=null&&tabs.size()>0){
			//点击联系人
			tabs.get(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
			return ;
		}
		
	}

}
