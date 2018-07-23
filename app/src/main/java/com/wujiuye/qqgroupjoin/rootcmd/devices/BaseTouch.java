package com.wujiuye.qqgroupjoin.rootcmd.devices;

/**
 * Created by wjy on 2017/8/21.
 */

public abstract class BaseTouch {

    protected String mEventFileName = null;

    public abstract BaseTouch touch(int finger_index, int x, int y);

    public abstract BaseTouch move(int finger_index, int x, int y);

    public abstract void release();
}
