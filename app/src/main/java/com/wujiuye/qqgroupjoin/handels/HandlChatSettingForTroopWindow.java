package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;
import com.wujiuye.qqgroupjoin.utils.StringUtils;

public class HandlChatSettingForTroopWindow{
	
	private HandlChatSettingForTroopWindow(){};
	public static final HandlChatSettingForTroopWindow instance = new HandlChatSettingForTroopWindow();
	
	
	/**
	 *  群信息页面显示
	 * @param service
	 * @param isReturn 是否直接点击返回按钮返回上一次页面
	 */
	public boolean handelEventType(QQJoinGroupService service,boolean isReturn){
		
		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return false;
		}
		
		if(isReturn){
			//查找返回按钮点击
			List<AccessibilityNodeInfo> ivReturn = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeft");
			if(ivReturn!=null&&ivReturn.size()>0){
				ivReturn.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
			}
			return true;
		}
		
		/**
		 * 过滤筛选条件，如果满足条件则申请加群
		 */
		//群人数条件，不满足则返回
		List<AccessibilityNodeInfo> lyNumbers = nodeInfo.findAccessibilityNodeInfosByText("群成员");
		AccessibilityNodeInfo numbers = null;
		
		if(lyNumbers!=null&&lyNumbers.size()>0){
			List<AccessibilityNodeInfo> numbersList = null;
			for(int i=0;i<lyNumbers.size();i++){
				if(lyNumbers.get(i).getClassName().equals("android.widget.TextView")){
					numbersList = lyNumbers.get(i).findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/info");
				}else{
					numbersList = lyNumbers.get(i).getParent().findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/info");
				}
				if(numbersList!=null&&numbersList.size()>0){
					for(int j=0;j<numbersList.size();j++){
						String text = numbersList.get(j).getText().toString();
						if(text.indexOf("人")>0){
							numbers=numbersList.get(j);
							break;
						}
					}
				}
				numbersList = null;
				if(numbers!=null)
					break;
			}
			
			if(numbers!=null){
				String text = numbers.getText().toString();
				if(!StringUtils.isEmpty(text)){
					text=text.replace("人", "");text=text.trim();
					int n = 0;
					try{
						n = Integer.valueOf(text);
					}catch(NumberFormatException e){
						e.printStackTrace();
					}
					if(n<HandlSearchContactsWindow.instance.getConditionsMode().getGroupPeopleOfNumber()){
						service.sendLogBroadcast("群人数不满足条件，取消添加!");
						//查找返回按钮点击
						List<AccessibilityNodeInfo> ivReturn = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeft");
						if(ivReturn!=null&&ivReturn.size()>0){
							ivReturn.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
						}
						return true;
					}
				}
			}
		}else{
			return false;
		}
		
		
		//满足所有条件后点击申请加群
		List<AccessibilityNodeInfo> ivAddGroup = nodeInfo.findAccessibilityNodeInfosByText("申请加群");
		if(ivAddGroup!=null&&ivAddGroup.size()>0){
			ivAddGroup.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
		return true;
	}

}
