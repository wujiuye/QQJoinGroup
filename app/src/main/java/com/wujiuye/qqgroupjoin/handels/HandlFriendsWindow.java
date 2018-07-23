package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlFriendsWindow{
	
	private HandlFriendsWindow(){};
	public static final HandlFriendsWindow instance = new HandlFriendsWindow();
	
	
	/**
	 *  处理联系人窗口显示
	 * @param service
	 */
	public boolean handelEventType(QQJoinGroupService service){
		
		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return false;
		}
		
		//点击添加钮
		List<AccessibilityNodeInfo> btnAdd = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnRightText");
		if(btnAdd!=null&&btnAdd.size()>0){
			btnAdd.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
			return true;
		}
		return false;
	}

}
