package com.wujiuye.qqgroupjoin;

import com.wujiuye.qqgroupjoin.activity.AboutActivity;
import com.wujiuye.qqgroupjoin.activity.HelpActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//当前手机版本为6.0及以上
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			View decorView = this.getWindow().getDecorView();
			if(decorView != null){
				int vis = decorView.getSystemUiVisibility();
				vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				decorView.setSystemUiVisibility(vis);
			}
			//添加Flag把状态栏设为可绘制模式
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			//设置状态栏背景色
			this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.statusbar_bg_color));
		}
		//当前手机版本为5.0及以上
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			//添加Flag把状态栏设为可绘制模式
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			//设置状态栏背景色
			this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.statusbar_bg_color));
		}

		onInit();
	}

	protected abstract void onInit();

	public void doBack(View view){
		this.finish();
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	protected void openActivitie(Class<?> toClass) {
		Intent intent = new Intent(this, toClass);
		startActivity(intent);
	}
	
	protected void openActivitie(Class<?> toClass,Bundle bundle) {
		Intent intent = new Intent(this, toClass);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
