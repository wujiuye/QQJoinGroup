package com.wujiuye.qqgroupjoin.rootcmd;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.wujiuye.qqgroupjoin.rootcmd.devices.BaseTouch;
import com.wujiuye.qqgroupjoin.rootcmd.devices.MIUI_H60_L01;
import com.wujiuye.qqgroupjoin.rootcmd.devices.Nexus5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by wjy on 2017/8/21.
 */

public class TouchDevice {

    private TouchDevice() {
    }

    public static final TouchDevice instance = new TouchDevice();


    private BaseTouch mBaseTouch = null;
    public static final String[] DEVICES = new String[]{
            "Nexus 5",
            MIUI_H60_L01.DEVICE_MODEL
    };
    public static final Map<String, BaseTouch> DEVICE_EVENT = new HashMap<String, BaseTouch>() {
        {
            put("Nexus 5", new Nexus5());
            put(MIUI_H60_L01.DEVICE_MODEL,new MIUI_H60_L01());
        }
    };

    /**
     * 获取设备信息
     *
     * @return
     */
    public String initDeviceInfo() {
        String model = android.os.Build.MODEL;
        return model;
    }


    /**
     * 是否支持的设备
     *
     * @return
     */
    public boolean initIsSupportDevice() {
        String model = initDeviceInfo();
        for (String m : DEVICES) {
            if (model.indexOf(m) >= 0) {
                model = m;
                break;
            }
        }
        mBaseTouch = DEVICE_EVENT.get(model);
        return mBaseTouch == null ? false : true;
    }

    /**
     * 获取虚拟触屏助手
     *
     * @return
     */
    public BaseTouch getBaseTouch() {
        return this.mBaseTouch;
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public interface OnGeteventWriteListener {
        void onGeteventWrite(String line);
    }

    private OnGeteventWriteListener mOnGeteventWriteListener = null;

    public synchronized void getevent(OnGeteventWriteListener onGeteventWriteListener) {
        this.mOnGeteventWriteListener = onGeteventWriteListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                execCommand("getevent");//行不通，哎
            }
        }).start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                if (mOnGeteventWriteListener != null)
                    mOnGeteventWriteListener.onGeteventWrite(msg.obj.toString());
            }
        }
    };

    private void execCommand(String command) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        InputStream input = null;
        try {
            //执行命令，并且获得Process对象
            process = runtime.exec(command);
            //获得结果的输入流
            input = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String strLine;
            while (null != (strLine = br.readLine())) {
                Message msg = new Message();
                msg.what = 100;
                msg.obj = strLine;
                handler.sendMessage(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
