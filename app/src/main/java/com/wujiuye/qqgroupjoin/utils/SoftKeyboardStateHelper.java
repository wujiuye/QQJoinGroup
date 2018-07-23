
package com.wujiuye.qqgroupjoin.utils;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author wujiuye
 * 该类有修改，目前没有太多测试，所以只能适用于本项目
 */
public class SoftKeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened();

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<SoftKeyboardStateListener>();
    private final View activityRootView;
    private int maxRootViewHeight=0;
    private boolean isSoftKeyboardOpened;

    public SoftKeyboardStateHelper(View activityRootView) {
        this(activityRootView, false);
    }

    public SoftKeyboardStateHelper(View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);
        maxRootViewHeight = r.bottom>maxRootViewHeight?r.bottom:maxRootViewHeight;

        if (!isSoftKeyboardOpened && r.bottom+100 < maxRootViewHeight ) {
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened();
        } else if (isSoftKeyboardOpened &&  r.bottom == maxRootViewHeight) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }


    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened() {

        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened();
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}
