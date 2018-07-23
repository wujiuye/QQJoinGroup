package com.wujiuye.qqgroupjoin.handels;

import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;
import com.wujiuye.qqgroupjoin.utils.StringUtils;

public class HandlLoginWindow {
	
	private String username=null; //自动登录的qq用户名
	private String password=null; //自动登录的qq密码
	private boolean isStopZDLogin = false; //是否停止自动登录,当密码输入多次错误时会要求输入验证码，这时候就要停止自动登录了。
	
	private HandlLoginWindow(){}
	
	/**
	 * 写入qq用户名密码
	 * @param username
	 * @param password
	 */
	public void setLoginInfo(String username,String password){
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 取消自动登录
	 */
	public void stopZDLogin(){
		this.isStopZDLogin = true;
	}
	
	/**
	 * 是否已经停止自动登录
	 * @return
	 */
	public boolean isStopZDLogin(){
		return this.isStopZDLogin;
	}
	
	private static HandlLoginWindow instance=null;
	public static HandlLoginWindow Instance(){
		if(instance == null){
			instance = new HandlLoginWindow();
		}
		return instance;
	}
	
	/**
	 * 处理监听到登录窗口显示
	 * @param service
	 */
	public void handelEventType(QQJoinGroupService service){
		service.sendLogBroadcast("自动登录信息：["+this.username+"],["+this.password+"]");
		if(!StringUtils.isEmpty(username)
				&&!StringUtils.isEmpty(password)){
			
			service.sendLogBroadcast("获取窗口根视图信息...");
			//查找用户名和密码输入控件
			AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
			if (nodeInfo == null) {
				return;
			}
			
			handleLogin(service,nodeInfo);
		}
	}
	
	/**
	 * 自动登录
	 * @param service
	 * @param rootNode
	 */
	private void handleLogin(QQJoinGroupService service,AccessibilityNodeInfo rootNode){
		service.sendLogBroadcast("正在查找控件...");
		
		List<AccessibilityNodeInfo> etUsername = rootNode
				.findAccessibilityNodeInfosByText("请输入QQ号码或手机或邮箱");
		
		List<AccessibilityNodeInfo> etPassword = rootNode
				.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/password");
		List<AccessibilityNodeInfo> btnLogin = rootNode
				.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/login");

		service.sendLogBroadcast("正在输入用户名...");
		if(etUsername!=null&&etUsername.size()>0){
			setStringToEditView(rootNode,etUsername.get(0),this.username,1);
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		service.sendLogBroadcast("正在输入密码...");
		if(etPassword!=null&&etPassword.size()>0){
			setStringToEditView(rootNode,etPassword.get(0), this.password,2);
		}
		
		//是否停止自动登录
		if(isStopZDLogin){
			return ;
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service.sendLogBroadcast("尝试登录...");
		if(btnLogin!=null&&btnLogin.size()>0){
			btnLogin.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
		
		Toast.makeText(App.Instance().getApplicationContext(), "正在尝试自动登录...", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 为输入控件写入文本
	 * @param rootNode 跟节点
	 * @param node	输入控件节点
	 * @param text	输入文本
	 * @param type	类型，1为用户名，2为密码
	 */
	private void setStringToEditView(AccessibilityNodeInfo rootNode,AccessibilityNodeInfo node,String text,int type){
		//自动输入用户名和密码
		//android>21 = 5.0时可以用ACTION_SET_TEXT
		//android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
		//使用剪切板
		ClipboardManager clipboard = (ClipboardManager) App.Instance().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("text", text);
		clipboard.setPrimaryClip(clip);
		//焦点    （n是AccessibilityNodeInfo对象）
		node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
		//获取焦点后右边的清除按钮才可点击,这里是争对qq登录的
		if(type == 1){
			clearUsername(rootNode);
		}else{
			clearPassword(rootNode);
		}
		//粘贴进入内容
		node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
	}
	
	/**
	 * 清除用户名
	 * @param rootNode
	 */
	private void clearUsername(AccessibilityNodeInfo rootNode){
		List<AccessibilityNodeInfo> ivCleanUsername = rootNode.findAccessibilityNodeInfosByText("清除帐号");
		if(ivCleanUsername!=null&&ivCleanUsername.size()>0){
			ivCleanUsername.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
	}
	
	/**
	 * 清除密码
	 * @param rootNode
	 */
	private void clearPassword(AccessibilityNodeInfo rootNode){
		List<AccessibilityNodeInfo> ivCleanUsername = rootNode.findAccessibilityNodeInfosByText("删除 按钮");
		if(ivCleanUsername!=null&&ivCleanUsername.size()>0){
			ivCleanUsername.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
		}
	}

}
