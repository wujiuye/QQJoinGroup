package com.wujiuye.qqgroupjoin.server;

import java.util.List;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.handels.HandlAddContactsWindow;
import com.wujiuye.qqgroupjoin.handels.HandlAddFriendVerifyWindow;
import com.wujiuye.qqgroupjoin.handels.HandlAlertDialog;
import com.wujiuye.qqgroupjoin.handels.HandlChatSettingForTroopWindow;
import com.wujiuye.qqgroupjoin.handels.HandlFindResultsWindow;
import com.wujiuye.qqgroupjoin.handels.HandlLoginWindow;
import com.wujiuye.qqgroupjoin.handels.HandlQQBrowserWindow;
import com.wujiuye.qqgroupjoin.handels.HandlSearchContactsWindow;
import com.wujiuye.qqgroupjoin.handels.HandlSplashWindow;
import com.wujiuye.qqgroupjoin.handels.HandlVerifyCodeWindow;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class QQJoinGroupService extends AccessibilityService {

    // 打印日记
    public final static String ACTION_QQJOINGROUPSERVICE_LOG = "com.wujiuye.qqgroupjoin.QQJoinGroupService";
    // 服务器开启
    public final static String ACTION_QQJOINGROUPSERVICE_CONNECTED = "com.wujiuye.qqgroupjoin.QQJoinGroupServiceConnected";
    // 请求停止服务
    public final static String ACTION_QQJOINGROUPSERVICE_STOP = "com.wujiuye.qqgroupjoin.QQJoinGroupServiceStop";

    private String TAG = QQJoinGroupService.class.getSimpleName();
    public final static String sListenerPackage = "com.tencent.mobileqq";

    private boolean isStopAccessibilityService = false;// 停止自动加群服务，只是停止事件响应

    public void stopAccessibilityService() {
        this.isStopAccessibilityService = true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        lastTapTime = System.currentTimeMillis();

        // 如果停止服务了不做任何处理
        if (isStopAccessibilityService)
            return;
        int eventEventType = event.getEventType();
        switch (eventEventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                handleWindowStateChanged(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//		case AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION:
//		case AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE:
//		case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT:
//		case AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED:
                // 对话框弹出引发窗口内容改变TYPE_WINDOW_CONTENT_CHANGED
                HandlAlertDialog.instance.handelEventType(this, event);
                break;
            default:
                break;
        }
    }

    /**
     * 窗口状态发生改变
     *
     * @param event
     */
    private synchronized void handleWindowStateChanged(AccessibilityEvent event) {

        List<AccessibilityNodeInfo> titleNode = null;
        try {
            titleNode = this.getRootInActiveWindow() == null ? null : this.getRootInActiveWindow().findAccessibilityNodeInfosByText("查找结果");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 判断窗口是否是登录窗口
        if ((sListenerPackage + HandlWindowFlags.activityNames[0]).equals(event
                .getClassName())) {
            //重置标志位
            for (int i = 1; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[0] = true;
            HandlLoginWindow.Instance().handelEventType(this);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[1])
                .equals(event.getClassName())) {
            //重置标志位
            for (int i = 2; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[1] = true;
            // 先取消自动登录
            if (!HandlLoginWindow.Instance().isStopZDLogin()) {
                HandlLoginWindow.Instance().stopZDLogin();
                sendLogBroadcast("自动登录失败，自动取消自动登录！");
                Toast.makeText(App.Instance().getApplicationContext(),
                        "自动登录失败，自动取消自动登录！", Toast.LENGTH_SHORT).show();
            }
            HandlVerifyCodeWindow.instance.handelEventType(this);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[2])
                .equals(event.getClassName())) {
            //重置标志位
            for (int i = 3; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[2] = true;
            // 首页
            HandlSplashWindow.instance.handelEventType(this);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[3])
                .equals(event.getClassName())) {
            //重置标志位
            for (int i = 4; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[3] = true;
            // 找群页面
            HandlAddContactsWindow.instance.handelEventType(event.getSource(),
                    false);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[4])
                .equals(event.getClassName())) {
            //重置标志位
            for (int i = 5; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            //初始化搜索结果列表页信息
            HandlFindResultsWindow.instance.setInit();

            HandlWindowFlags.HANDL_WINDOW_FLAG[4] = true;
            // 搜索页面
            HandlSearchContactsWindow.instance.handelEventType(this);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[5])
                .equals(event.getClassName()) && titleNode != null) {
            //重置标志位
            for (int i = 6; i < HandlWindowFlags.HANDL_WINDOW_FLAG.length; i++) {
                HandlWindowFlags.HANDL_WINDOW_FLAG[i] = false;
            }
            // 标志该页面已打开
            HandlWindowFlags.HANDL_WINDOW_FLAG[5] = true;
            HandlFindResultsWindow.instance.resetIsTouch();
            //搜索结果页面
            HandlFindResultsWindow.instance.handelEventType(this);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[6])
                .equals(event.getClassName())) {
            boolean isReurn = false;
            // 标志该页面已打开
            HandlWindowFlags.HANDL_WINDOW_FLAG[6] = true;
            // 判断当前页面的下一个页面是否已经打开，如果打开那么不做处理，直接点击返回按钮返回
            if (HandlWindowFlags.HANDL_WINDOW_FLAG[7] == true) {
                isReurn = true;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[7] = false;
            // 群信息页面
            HandlChatSettingForTroopWindow.instance.handelEventType(this,
                    isReurn);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[7])
                .equals(event.getClassName())) {
            boolean isReurn = false;
            // 标志该页面已打开
            HandlWindowFlags.HANDL_WINDOW_FLAG[7] = true;
            // 判断当前页面的下一个页面是否已经打开，如果打开那么不做处理，直接点击返回按钮返回
            if (HandlWindowFlags.HANDL_WINDOW_FLAG[8] == true) {
                isReurn = true;
            }
            HandlWindowFlags.HANDL_WINDOW_FLAG[8] = false;
            // 申请入群验证页面
            HandlAddFriendVerifyWindow.instance.handelEventType(this, isReurn);
        } else if ((sListenerPackage + HandlWindowFlags.activityNames[7 + 1])
                .equals(event.getClassName())) {
            // 标志该页面已打开
            HandlWindowFlags.HANDL_WINDOW_FLAG[8] = true;
            // 申请入群验证页面
            HandlQQBrowserWindow.instance.handelEventType(this);
        }
    }

    @Override
    public void onInterrupt() {
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        settingAccessibilityInfo();
        sendBroadcast(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_CONNECTED,
                null);
        startListenerThread();
    }

    private void settingAccessibilityInfo() {
        String[] packageNames = {sListenerPackage};
        AccessibilityServiceInfo mAccessibilityServiceInfo = new AccessibilityServiceInfo();
        // 响应事件的类型，这里是全部的响应事件（长按，单击，滑动等）
        mAccessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        // 反馈给用户的类型，这里是语音提示
        mAccessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        // 过滤的包名
        mAccessibilityServiceInfo.packageNames = packageNames;
        setServiceInfo(mAccessibilityServiceInfo);
    }

    public void sendLogBroadcast(String message) {
        // 发送特定action的广播
        Intent intent = new Intent();
        intent.setAction(ACTION_QQJOINGROUPSERVICE_LOG);
        intent.putExtra("TAG", TAG);
        intent.putExtra("MESSAGE", message);
        sendBroadcast(intent);
    }

    public void sendBroadcast(String action, Bundle bundle) {
        // 发送特定action的广播
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        sendBroadcast(intent);
    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>守护线程>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    /**
     * 开启一个监听进程，如果当前页面是搜索结果列表页，
     * 并且1秒钟内无反应，那么重置页面信息，模拟一次页面内容改变事件
     */
    private long lastTapTime = System.currentTimeMillis();

    private void startListenerThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(isStopAccessibilityService)
                            break;
                        Thread.sleep(1000 * 3);
                        long currentTapTime = System.currentTimeMillis();
                        if (currentTapTime - lastTapTime > 1000 * 1) {
                            if (HandlWindowFlags.HANDL_WINDOW_FLAG[5] && !HandlWindowFlags.HANDL_WINDOW_FLAG[6]) {
                                listenerThreadHandler.sendEmptyMessage(1);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler listenerThreadHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //模拟事件，求活机器人
                HandlFindResultsWindow.instance.resetIsTouch();
                HandlFindResultsWindow.instance.handelEventType(QQJoinGroupService.this);
            }
        }
    };

}
