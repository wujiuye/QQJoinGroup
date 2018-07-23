package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.bean.ConditionsMode;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlSearchContactsWindow{
	
	private HandlSearchContactsWindow(){};
	public static final HandlSearchContactsWindow instance = new HandlSearchContactsWindow();
	
	//关键词游标
	private static int inputKeywordIndex = 0;
	
	/**
	 * 加群条件
	 */
	private ConditionsMode mConditionsMode=null;
	public ConditionsMode getConditionsMode() {
		return mConditionsMode;
	}
	public void setConditionsMode(ConditionsMode mConditionsMode) {
		this.mConditionsMode = mConditionsMode;
		inputKeywordIndex = 0;
	}


	/**
	 *  搜索页面显示
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service){

		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}
		
		if(inputKeywordIndex<0||inputKeywordIndex==this.mConditionsMode.getJoinGroupKeywords().size()){
			//结束自动加群服务。
			List<AccessibilityNodeInfo> btn_cancel_search = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/btn_cancel_search");
			if(btn_cancel_search!=null&&btn_cancel_search.size()>0){
				btn_cancel_search.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
				service.stopAccessibilityService();//停止事件响应
				service.sendBroadcast(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_STOP,null);
			}
			return;
		}
		
		String keyword = this.mConditionsMode.getJoinGroupKeywords().get(inputKeywordIndex);
		List<AccessibilityNodeInfo> et_search_keyword = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/et_search_keyword");
		if(et_search_keyword!=null&&et_search_keyword.size()>0){
			//搜索输入框，输入关键词
			et_search_keyword.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
			setStringToEditView(nodeInfo,et_search_keyword.get(0),keyword);
			inputKeywordIndex++;
			
			//输入文本之后，内容会改变，获取搜索群选项点击
			List<AccessibilityNodeInfo> ivFindGroup = nodeInfo.findAccessibilityNodeInfosByText("找群:");
			if(ivFindGroup!=null&&ivFindGroup.size()>0){
				if("android.widget.TextView".equals(ivFindGroup.get(0).getClassName())){
					ivFindGroup.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
				}else{
					ivFindGroup.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
				}
			}
		}
	}
	

	/**
	 * 为输入控件写入文本
	 * @param rootNode 跟节点
	 * @param node	输入控件节点
	 * @param keyword	输入文本
	 */
	private void setStringToEditView(AccessibilityNodeInfo rootNode,AccessibilityNodeInfo node,String keyword){
		//自动输入用户名和密码
		//android>21 = 5.0时可以用ACTION_SET_TEXT
		//android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
		//使用剪切板
		ClipboardManager clipboard = (ClipboardManager) App.Instance().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("text", keyword);
		clipboard.setPrimaryClip(clip);
		//焦点    （n是AccessibilityNodeInfo对象）
		node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
		//获取焦点后右边的清除按钮才可点击
		clearEditView(rootNode);
		//粘贴进入内容
		node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
	}
	
	/**
	 * 清除EditView的内容
	 * @param rootNode
	 */
	private void clearEditView(AccessibilityNodeInfo rootNode){
		List<AccessibilityNodeInfo> ivClean = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ib_clear_text");
		if(ivClean!=null&&ivClean.size()>0){
			ivClean.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
	}

}
