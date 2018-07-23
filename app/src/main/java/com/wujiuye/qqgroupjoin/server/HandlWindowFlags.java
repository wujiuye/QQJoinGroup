package com.wujiuye.qqgroupjoin.server;

public final class HandlWindowFlags {
	
	public final static String[] activityNames = new String[]{
		".activity.LoginActivity",//登录页面
		".activity.VerifyCodeActivity",//登录多次用户名密码错误要求输入验证码的页面
		".activity.SplashActivity",//登录后的首页
		".activity.contact.addcontact.AddContactsActivity",//找群页面
		".activity.contact.addcontact.SearchContactsActivity",//搜索页面
		".activity.QQBrowserActivity",//搜索结果列表页面
		".activity.ChatSettingForTroop",//群信息页面
		".activity.AddFriendVerifyActivity",//申请加好友验证
		".activity.QQBrowserActivity",//发送申请好友验证完成
	};
	
	public final static boolean[] HANDL_WINDOW_FLAG = new boolean[]{
		 false,//登录页面
		 false,//登录多次用户名密码错误要求输入验证码的页面
		 false,//登录后的首页
		 false,//找群页面
		 false,//搜索页面
		 false,//搜索结果列表页面
		 false,//群信息页面
		 false,//申请加好友验证
		 false,//发送申请好友验证完成
	 };
	
}
