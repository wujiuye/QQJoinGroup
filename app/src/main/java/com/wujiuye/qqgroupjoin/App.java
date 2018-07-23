package com.wujiuye.qqgroupjoin;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

public class App extends Application{
	
	private static App instance = null;

	private Size mScreenSize=null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		getScreen();
	}
	
	public static App Instance(){
		return instance;
	}


	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public Size getScreen(){
		if(mScreenSize==null){
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int screenWidth  = display.getWidth();
			int screenHeight = display.getHeight();
			mScreenSize = new Size(screenWidth, screenHeight);
		}
		return mScreenSize;
	}
	
}
