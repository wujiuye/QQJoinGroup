package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlAddFriendVerifyWindow {

	private HandlAddFriendVerifyWindow() {
	};

	public static final HandlAddFriendVerifyWindow instance = new HandlAddFriendVerifyWindow();

	/**
	 * 处理申请好友验证页显示
	 * 
	 * @param service
	 * @param isReurn
	 *            是否直接点击返回按钮返回上一页
	 */
	public void handelEventType(QQJoinGroupService service, boolean isReurn) {

		AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
		if (nodeInfo == null) {
			return;
		}

		if (isReurn) {
			List<AccessibilityNodeInfo> ivReturn = nodeInfo
					.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeft");
			if (ivReturn != null && ivReturn.size() > 0) {
				ivReturn.get(0).performAction(
						AccessibilityNodeInfo.ACTION_CLICK);
			}
			return;
		}

		List<AccessibilityNodeInfo> nameValues = nodeInfo
				.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/name");
		if (nameValues != null && nameValues.size() > 0) {
			AccessibilityNodeInfo node = null;
			for (int i = 0; i < nameValues.size(); i++) {
				if (nameValues.get(i).getClassName()
						.equals("android.widget.EditText")) {
					node = nameValues.get(i);
					break;
				}
			}
			if (node != null) {
				setStringToEditView(nodeInfo, node,
						HandlSearchContactsWindow.instance.getConditionsMode()
								.getJoinGroupMyDesc());
			}
		}

		// 点击发送按钮
		List<AccessibilityNodeInfo> ivASend = nodeInfo
				.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnRightText");
		if (ivASend != null && ivASend.size() > 0) {
			ivASend.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}

	}

	/**
	 * 为输入控件写入文本
	 * 
	 * @param rootNode
	 *            跟节点
	 * @param node
	 *            输入控件节点
	 * @param desc
	 *            输入文本
	 */
	private void setStringToEditView(AccessibilityNodeInfo rootNode,
			AccessibilityNodeInfo node, String desc) {
		// 自动输入用户名和密码
		// android>21 = 5.0时可以用ACTION_SET_TEXT
		// android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
		// 使用剪切板
		ClipboardManager clipboard = (ClipboardManager) App.Instance()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("text", desc);
		clipboard.setPrimaryClip(clip);
		// 焦点 （n是AccessibilityNodeInfo对象）
		node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
		// 粘贴进入内容
		node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
	}

}
