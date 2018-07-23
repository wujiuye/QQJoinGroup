package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.server.HandlWindowFlags;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

public class HandlAlertDialog {

	private HandlAlertDialog() {
	};

	public static final HandlAlertDialog instance = new HandlAlertDialog();

	/**
	 * 处理窗口内容改变
	 * 对标志位需倒叙处理，因为后打开的窗口在标志数组后面
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service,
			AccessibilityEvent event) {

		AccessibilityNodeInfo root = service.getRootInActiveWindow();
		if (root == null)
			return;

		// 群信息页面
		if (HandlWindowFlags.HANDL_WINDOW_FLAG[6]) {
			HandlChatSettingForTroopWindow.instance.handelEventType(service,
					false);
			return;
		}

		// 搜索结果列表界面
		if (HandlWindowFlags.HANDL_WINDOW_FLAG[5]) {
			HandlFindResultsWindow.instance.handelEventType(service);
			return;
		}

		// 找群页fragment
		if (HandlWindowFlags.HANDL_WINDOW_FLAG[3]) {
			HandlAddContactsWindow.instance.handelEventType(event.getSource(),
					true);
			return;
		}

		// 首页
		if (HandlWindowFlags.HANDL_WINDOW_FLAG[2]) {
			HandlFriendsWindow.instance.handelEventType(service);
			return;
		}

		// 查找登录失败对话框
		if (HandlWindowFlags.HANDL_WINDOW_FLAG[0]) {
			List<AccessibilityNodeInfo> loginErrorDialogTitle = root
					.findAccessibilityNodeInfosByText("登录失败");
			if (loginErrorDialogTitle != null
					&& loginErrorDialogTitle.size() > 0) {
				handleLoginErrorDialog(service, root);
				return;
			}
		}
	}

	/**
	 * 处理登录失败对话框
	 * 
	 * @param service
	 * @param nodeInfo
	 */
	private void handleLoginErrorDialog(QQJoinGroupService service,
			AccessibilityNodeInfo nodeInfo) {
		List<AccessibilityNodeInfo> loginErrorDialogEnterBtn = nodeInfo
				.findAccessibilityNodeInfosByText("确定");
		if (loginErrorDialogEnterBtn != null
				&& loginErrorDialogEnterBtn.size() > 0) {
			// 先取消自动登录
			if (!HandlLoginWindow.Instance().isStopZDLogin()) {
				HandlLoginWindow.Instance().stopZDLogin();
				Toast.makeText(App.Instance().getApplicationContext(),
						"自动登录失败，自动取消自动登录！", Toast.LENGTH_SHORT).show();
			}
			// 点击确定按钮
			loginErrorDialogEnterBtn.get(0).performAction(
					AccessibilityNodeInfo.ACTION_CLICK);
		}
	}

}
