package com.wujiuye.qqgroupjoin.rootcmd.devices;

import com.wujiuye.qqgroupjoin.rootcmd.Vevent;

/**
 * Created by wjy on 2017/8/23.
 */

public class MIUI_H60_L01 extends BaseTouch{

    public static final String DEVICE_MODEL="H60-L01";

//            /dev/input/event4: 0001 014a 00000001
//            /dev/input/event4: 0003 0039 00000000
//            /dev/input/event4: 0003 0030 00000020
//            /dev/input/event4: 0003 0035 00000208
//            /dev/input/event4: 0003 0036 00000427
//            /dev/input/event4: 0003 003a 00000050
//            /dev/input/event4: 0000 0002 00000000
//            /dev/input/event4: 0000 0000 00000000

//            /dev/input/event4: 0001 014a 00000000
//            /dev/input/event4: 0000 0002 00000000
//            /dev/input/event4: 0000 0000 00000000

    public MIUI_H60_L01(){
        mEventFileName = "event4";
    }

    @Override
    public BaseTouch touch(int finger_index, int x, int y) {
        Vevent.sendevent(this.mEventFileName, 0x0001, 0x014a, 0x00000001);
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0039, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0030, 0x00000020);
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0035, x);//x坐标
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0036, y);//y坐标
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x003a, 0x00000050);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0002, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0000, 0x00000000);
        return this;
    }

    @Override
    public BaseTouch move(int finger_index, int x, int y) {
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0035, x);//x坐标
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0036, y);//y坐标
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0002, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0000, 0x00000000);
        return this;
    }

    @Override
    public void release() {
        Vevent.sendevent(this.mEventFileName, 0x0001, 0x014a, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0002, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0000, 0x00000000);
    }
}
