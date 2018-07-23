package com.wujiuye.qqgroupjoin.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.wujiuye.qqgroupjoin.BaseActivity;
import com.wujiuye.qqgroupjoin.R;
import com.wujiuye.qqgroupjoin.rootcmd.TouchDevice;
import com.wujiuye.qqgroupjoin.rootcmd.Vevent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wjy on 2017/8/20.
 */

public class SettingActivity extends BaseActivity
implements Vevent.OnRootShellExecuteCmdResultListener {
    private static final int ROOT_STATU_NO = 0;
    private static final int ROOT_STATU_TRUE = 1;
    private static final int ROOT_STATU_FALSE = 2;

    @BindView(R.id.title)
    protected TextView tvTitle;

    @BindView(R.id.tv_device_info)
    protected TextView tvDeviceInfo;
    @BindView(R.id.tv_zcdevice_info)
    protected TextView tvZcdeviceInfo;

    @BindView(R.id.tv_moniclick_info)
    protected TextView tvMoniclickInfo;
    @BindView(R.id.tv_root_info)
    protected TextView tvRootInfo;

    @BindView(R.id.tv_event_info)
    protected TextView tvEventInfo;

    private static int isRootStatu = ROOT_STATU_NO;

    @Override
    protected void onInit() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.action_setting);
        initDeviceInfo();
    }

    private void initDeviceInfo(){
        String info = TouchDevice.instance.initDeviceInfo();
        tvDeviceInfo.setText(info);
        tvZcdeviceInfo.setText(TouchDevice.instance.initIsSupportDevice()?"完美支持":"很抱歉，暂不支持");
    }

    @OnClick(R.id.btn_getmoniclick_info)
    public void btnGetmoniclickInfo_click(View view){
        Vevent vevent = new Vevent();
        vevent.setOnRootShellExecuteCmdResultListener(this);
        vevent.getRootAndEnevtRW();
    }


    @Override
    public void onGetRootAndEnevtRWFinish(boolean statu) {
        isRootStatu = statu?ROOT_STATU_TRUE:ROOT_STATU_FALSE;
        tvRootInfo.setText(statu?"已获取":"权限获取失败");
        tvMoniclickInfo.setText(statu?"已获取":"权限获取失败");
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (isRootStatu){
            case ROOT_STATU_NO:
                tvRootInfo.setText("未获取");
                tvMoniclickInfo.setText("未获取");
                break;
            case ROOT_STATU_TRUE:
                tvRootInfo.setText("已获取");
                tvMoniclickInfo.setText("已获取");
                break;
            case ROOT_STATU_FALSE:
                tvRootInfo.setText("权限获取失败");
                tvMoniclickInfo.setText("权限获取失败");
                break;
        }
    }


    @OnClick(R.id.btn_getevent_info)
    public void btnGeteventInfo_click(View view){
        view.setOnClickListener(null);
        tvEventInfo.setText("");
        TouchDevice.instance.getevent(new TouchDevice.OnGeteventWriteListener() {
            @Override
            public void onGeteventWrite(String line) {
                tvEventInfo.append(line+"\n");
            }
        });
    }
}
