package com.wujiuye.qqgroupjoin.rootcmd.devices;

import com.wujiuye.qqgroupjoin.rootcmd.Vevent;


/**
 * 谷歌Nexus5设备
 */
public class Nexus5 extends BaseTouch {

//   Nexus5模拟器getevent信息
//            /dev/input/event5: 0001 014a 00000001
//            /dev/input/event5: 0003 003a 00000001
//            /dev/input/event5: 0003 0035 00000213
//            /dev/input/event5: 0003 0036 000001b4
//            /dev/input/event5: 0000 0002 00000000
//            /dev/input/event5: 0000 0000 00000000

//            /dev/input/event5: 0001 014a 00000000
//            /dev/input/event5: 0000 0002 00000000
//            /dev/input/event5: 0000 0000 00000000

    public Nexus5() {
        this.mEventFileName = "event5";
    }

    public BaseTouch touch(int finger_index, int x, int y) {
        Vevent.sendevent(this.mEventFileName, 0x0001, 0x014a, 0x00000001);
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x003a, 0x00000001);
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0035, x);//x坐标
        Vevent.sendevent(this.mEventFileName, 0x0003, 0x0036, y);//y坐标
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


    public void release() {
        Vevent.sendevent(this.mEventFileName, 0x0001, 0x014a, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0002, 0x00000000);
        Vevent.sendevent(this.mEventFileName, 0x0000, 0x0000, 0x00000000);
    }


}
