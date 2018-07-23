package com.wujiuye.qqgroupjoin.handels;

import java.util.List;
import java.util.Vector;

import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

public class HandlAddContactsWindow {

	private HandlAddContactsWindow() {
	};

	public static final HandlAddContactsWindow instance = new HandlAddContactsWindow();

	/**
	 * 处理找群页面显示
	 * 
	 * @param service
	 */
	public boolean handelEventType(AccessibilityNodeInfo nodeInfo,boolean isFragment) {

		if (nodeInfo == null) {
			return false;
		}
		if(isFragment){
			return handelFragemntChange(nodeInfo);
		}
		
		List<AccessibilityNodeInfo> btnFindGriup = nodeInfo
				.findAccessibilityNodeInfosByText("找群");
		if(btnFindGriup!=null&&btnFindGriup.size()>0){
			btnFindGriup.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
			return true;
		}
		return false;
	}
	
	/**
	 * 处理碎片切换
	 * @param nodeInfo
	 * @return
	 */
	private boolean handelFragemntChange(AccessibilityNodeInfo nodeInfo){
		AccessibilityNodeInfo etFind = null;
		try{
			etFind = findEditViewBy(nodeInfo);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		if (etFind != null) {
			etFind.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			return true;
		}
		return false;
	}
	
	/**
	 * 查找搜索输入框控件
	 * @param nodeInfo
	 * @return
	 */
	private AccessibilityNodeInfo findEditViewBy(AccessibilityNodeInfo nodeInfo) throws NullPointerException{
		if(nodeInfo==null)
			return null;
		Vector<AccessibilityNodeInfo> queue = new Vector<AccessibilityNodeInfo>();
		queue.add(nodeInfo);
		do{
			AccessibilityNodeInfo info = queue.get(0);queue.remove(0);
			if(info==null)
				continue;
			if(info.getClassName().equals(EditText.class.getCanonicalName()))
				return info;
			for(int i=0;i<info.getChildCount();i++){
				queue.add(info.getChild(i));
			}
		}while(queue.size()>0);
		return null;
	}

}
