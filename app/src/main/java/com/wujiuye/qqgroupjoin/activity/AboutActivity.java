package com.wujiuye.qqgroupjoin.activity;

import android.widget.TextView;

import com.wujiuye.qqgroupjoin.BaseActivity;
import com.wujiuye.qqgroupjoin.R;
import com.wujiuye.qqgroupjoin.R.layout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutActivity extends BaseActivity{


	@BindView(R.id.title)
	protected TextView tvTitle;

	@Override
	protected void onInit() {
		setContentView(R.layout.activity_about);
		ButterKnife.bind(this);
		tvTitle.setText(R.string.action_about);
	}
	
}
