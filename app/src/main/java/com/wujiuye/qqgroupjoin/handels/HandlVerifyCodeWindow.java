package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlVerifyCodeWindow{
	
	private boolean isStopZDLogin = false;
	
	private HandlVerifyCodeWindow(){};
	public static final HandlVerifyCodeWindow instance = new HandlVerifyCodeWindow();
	
	
	/**
	 *  处理监听到验证码窗口显示
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service){
	
		//如果已经取消自动登录就不做处理
		if(isStopZDLogin){
			return;
		}
		
		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		
		//查找取消按钮点击返回
		List<AccessibilityNodeInfo> btnEsc = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeftButton");
		if(btnEsc!=null&&btnEsc.size()>0){
			btnEsc.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
		
		isStopZDLogin = HandlLoginWindow.Instance().isStopZDLogin();//保证至少执行一次
	}

}
