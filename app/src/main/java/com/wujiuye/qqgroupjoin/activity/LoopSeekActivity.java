package com.wujiuye.qqgroupjoin.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wujiuye.qqgroupjoin.BaseActivity;
import com.wujiuye.qqgroupjoin.R;
import com.wujiuye.qqgroupjoin.handels.HandlLoginWindow;
import com.wujiuye.qqgroupjoin.receiver.ServiceReceiver;
import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;
import com.wujiuye.qqgroupjoin.utils.AccessibilityServiceUtils;
import com.wujiuye.qqgroupjoin.utils.AppUtils;

public class LoopSeekActivity extends BaseActivity implements
		ServiceReceiver.OnLogBroadcastReceiver {

	private ServiceReceiver mServiceReceiver;

	@BindView(R.id.title)
	protected TextView tvTitle;

	@BindView(R.id.tvLog)
	protected TextView tvLog;
	private String username;
	private String password;

	@Override
	protected void onInit() {
		getBundel();
		setContentView(R.layout.activity_loop_seek);
		ButterKnife.bind(this);
		tvTitle.setText(R.string.action_log);
		registerReceiver();
		startService();
	}

	private void getBundel() {
		if (getIntent().getExtras() != null) {
			this.username = getIntent().getExtras().getString("username");
			this.password = getIntent().getExtras().getString("password");
			HandlLoginWindow.Instance().setLoginInfo(username, password);
		}
	}

	private void startService() {
		if(AccessibilityServiceUtils.isAccessibilitySettingsOn(this)){
			Toast.makeText(this, "重启服务才有效！", Toast.LENGTH_LONG).show();
		}
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "找到服务，开启即可", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerReceiver() {
		mServiceReceiver = new ServiceReceiver();
		mServiceReceiver.setOnLogBroadcastReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_LOG);
		filter.addAction(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_CONNECTED);
		registerReceiver(mServiceReceiver, filter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onConnected() {
		// 自动启动QQ应用
		AppUtils.doStartApplicationWithPackageName(this,
				QQJoinGroupService.sListenerPackage);
	}

	@OnClick(R.id.btnStopService)
	protected void onClickBtnStopService(View v) {
		this.finish();
	}

	private void stopServer() {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
			startActivity(intent);
			Toast.makeText(this, "找到服务，关闭即可", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
		}
	}

	@Override
	public void onLog(String msg) {
		tvLog.append("Log=>" + msg + "\n");
	}
	
	@Override
	public void onStopReceiver() {
		Toast.makeText(this, "自动加群任务已完成，找到服务关闭即可！", Toast.LENGTH_LONG).show();
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(AccessibilityServiceUtils.isAccessibilitySettingsOn(this)){
			stopServer();
		}
		unregisterReceiver(mServiceReceiver);
		super.onDestroy();
	}

}
