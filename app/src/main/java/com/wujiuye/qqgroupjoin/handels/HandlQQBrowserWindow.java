package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlQQBrowserWindow{
	
	private HandlQQBrowserWindow(){};
	public static final HandlQQBrowserWindow instance = new HandlQQBrowserWindow();
	
	
	/**
	 *  处理发送申请验证完成页面显示
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service){

		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		
		List<AccessibilityNodeInfo> ivFinish = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnRightText");
		if(ivFinish!=null&&ivFinish.size()>0){
			//点击完成按钮
			ivFinish.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
			return ;
		}
		
	}

}
