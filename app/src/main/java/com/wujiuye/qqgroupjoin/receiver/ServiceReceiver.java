package com.wujiuye.qqgroupjoin.receiver;

import com.wujiuye.qqgroupjoin.server.QQJoinGroupService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceReceiver extends BroadcastReceiver{
	
	public interface OnLogBroadcastReceiver{
		void onLog(String msg);
		
		void onConnected();
		
		void onStopReceiver();
	}

	private OnLogBroadcastReceiver mOnLogBroadcastReceiver;
	public void setOnLogBroadcastReceiver(OnLogBroadcastReceiver iLogBroadcastReceiver){
		this.mOnLogBroadcastReceiver = iLogBroadcastReceiver;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_LOG.equals(action)){
			if(this.mOnLogBroadcastReceiver!=null){
				this.mOnLogBroadcastReceiver.onLog(intent.getStringExtra("MESSAGE"));
			}
		}else if(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_CONNECTED.equals(action)){
			if(this.mOnLogBroadcastReceiver!=null){
				this.mOnLogBroadcastReceiver.onConnected();
			}
		}else if(QQJoinGroupService.ACTION_QQJOINGROUPSERVICE_STOP.equals(action)){
			if(this.mOnLogBroadcastReceiver!=null){
				this.mOnLogBroadcastReceiver.onStopReceiver();
			}
		}
	}

}
