package com.wujiuye.qqgroupjoin.handels;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.util.Size;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;

import com.wujiuye.qqgroupjoin.App;
import com.wujiuye.qqgroupjoin.rootcmd.TouchDevice;
import com.wujiuye.qqgroupjoin.rootcmd.devices.Nexus5;
import com.wujiuye.qqgroupjoin.server.HandlWindowFlags;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;
import com.wujiuye.qqgroupjoin.utils.ScreenUtils;

public class HandlFindResultsWindow {

    private HandlFindResultsWindow() {
    }

    ;
    public static final HandlFindResultsWindow instance = new HandlFindResultsWindow();

    //已加群数
    private static int joinGroupCount = 0;

    //屏幕大小
    private static final Size scrennSize = App.Instance().getScreen();
    //listview每一项的高度
    private int itemHeight;
    //触摸的y坐标
    private int touchY;
    private int touchX = ScreenUtils.convertDpToPixel(App.Instance(), 120);

    public void setInit() {
        touchY = itemHeight;
        joinGroupCount = 0;
    }

    //是否允许模拟触摸屏幕
    private boolean isTouch = false;

    public void resetIsTouch() {
        isTouch = true;
        listview = null;
    }

    AccessibilityNodeInfo listview = null;

    /**
     * 处理搜索结果页面显示
     *
     * @param service
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public synchronized void handelEventType(QQJoinGroupService service) {

        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        // 每个关键词加够指定数量的群就按返回上一页
        boolean isSingmeetFinish = joinGroupCount + 1 >= HandlSearchContactsWindow.instance
                .getConditionsMode().getEachKeywordJoinGroupNumber() ? true
                : false;
        if (isSingmeetFinish) {
            List<AccessibilityNodeInfo> ivRetrun = nodeInfo
                    .findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeft");
            if (ivRetrun != null && ivRetrun.size() > 0) {
                // 点击返回按钮
                ivRetrun.get(0).performAction(
                        AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        }

        // 查找到列表控件
        if (listview == null) {
            try {
                listview = findListView(nodeInfo);
                AccessibilityNodeInfo item = listview.getChild(0);
                if (item != null) {
                    Rect rect = new Rect();
                    item.getBoundsInScreen(rect);
                    itemHeight = rect.height();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (listview == null)
            return;

        if (isTouch) {
            isTouch = false;
            if (touchY + itemHeight > scrennSize.getHeight()) {
                // 判断是否滚动到末尾了
                List<AccessibilityNodeInfo> lastitem = nodeInfo
                        .findAccessibilityNodeInfosByText("已全部加载，想要更多？换个词试试");
                if (lastitem.size() == 0) {
                    //使用模拟触摸屏移动来实现listview的滚动
                    if (TouchDevice.instance.getBaseTouch() != null) {
                        //从下往上滑动
                        TouchDevice.instance.getBaseTouch()
                                .touch(0, touchX, 2 * itemHeight)
                                .move(0, touchX, (int) (1.8 * itemHeight))//模拟移动路径
                                .move(0, touchX, (int) (1.5 * itemHeight))
                                .move(0, touchX, (int) (1.3 * itemHeight))
                                .move(0, touchX, itemHeight)
                                .release();
                        //模拟点击
                        TouchDevice.instance.getBaseTouch()
                                .touch(0, touchX, touchY)
                                .release();
                    }
                } else {
                    List<AccessibilityNodeInfo> ivRetrun = nodeInfo
                            .findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/ivTitleBtnLeft");
                    if (ivRetrun != null && ivRetrun.size() > 0) {
                        // 点击返回按钮
                        ivRetrun.get(0).performAction(
                                AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            } else {
                //模拟点击
                touchY += itemHeight;
                if (TouchDevice.instance.getBaseTouch() != null) {
                    TouchDevice.instance.getBaseTouch()
                            .touch(0, touchX, touchY)
                            .release();
                }
            }
        }
    }

    /**
     * 查找listview控件
     *
     * @param node
     * @return
     */
    private AccessibilityNodeInfo findListView(AccessibilityNodeInfo node)
            throws Exception {
        if (node == null)
            return null;
        Vector<AccessibilityNodeInfo> queue = new Vector<AccessibilityNodeInfo>();
        queue.add(node);
        do {
            AccessibilityNodeInfo item = queue.get(0);
            queue.remove(0);
            if (item.getClassName().equals(ListView.class.getCanonicalName())) {
                return item;
            }
            for (int i = 0; i < item.getChildCount(); i++) {
                queue.add(item.getChild(i));
            }
        } while (queue.size() > 0);
        return null;
    }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>webview的操作，弃用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 查找webview
     *
     * @param nodeInfo
     * @return
     */
    private AccessibilityNodeInfo findWebviewWithListview(
            AccessibilityNodeInfo nodeInfo) {
        while (nodeInfo != null) {
            if (nodeInfo.getClassName().equals("android.webkit.WebView")) {
                return nodeInfo;
            }
            nodeInfo = nodeInfo.getParent();
        }
        return null;
    }

    /**
     * 不能滚动异常
     *
     * @author wjy
     */
    public class NotScrollable extends Exception {
        private static final long serialVersionUID = 1L;

        public NotScrollable(String smg) {
            super(smg);
        }
    }

    /**
     * webview内容向下滚动 Action to scroll the node content backward.
     *
     * @param node
     * @throws NotScrollable
     */
    private void scrollBackward(AccessibilityNodeInfo node)
            throws NotScrollable {
        while (node != null) {
            // 找到webview滚动
            if (node.isScrollable()) {
                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                return;
            }
            node = node.getParent();
        }
        throw new NotScrollable("This node cannot be scrolled");
    }

    /**
     * webview内容向上滚动 Action to scroll the node content forward.
     *
     * @param node
     * @throws NotScrollable
     */
    private void scrollForward(AccessibilityNodeInfo node) throws NotScrollable {
        while (node != null) {
            // 找到webview滚动
            if (node.isScrollable()) {
                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                return;
            }
            node = node.getParent();
        }
        throw new NotScrollable("This node cannot be scrolled");
    }

}
