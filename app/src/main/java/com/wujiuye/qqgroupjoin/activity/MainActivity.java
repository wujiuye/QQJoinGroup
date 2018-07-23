package com.wujiuye.qqgroupjoin.activity;

import java.util.ArrayList;
import java.util.List;

import com.wujiuye.qqgroupjoin.BaseActivity;
import com.wujiuye.qqgroupjoin.R;
import com.wujiuye.qqgroupjoin.bean.ConditionsMode;
import com.wujiuye.qqgroupjoin.handels.HandlSearchContactsWindow;
import com.wujiuye.qqgroupjoin.utils.SoftKeyboardStateHelper;
import com.wujiuye.qqgroupjoin.utils.StringUtils;

import butterknife.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements
		SoftKeyboardStateHelper.SoftKeyboardStateListener ,
		View.OnClickListener{

	@BindView(R.id.activityRootView)
	protected View activityRootView;
	@BindView(R.id.btnStartService)
	protected Button btnStartService;


	@BindView(R.id.title)
	protected TextView tvTitle;
	@BindView(R.id.btnMenu)
	protected TextView btnMenu;

	@BindView(R.id.etQQUsername)
	protected EditText etQQUsername;
	@BindView(R.id.etQQPassword)
	protected EditText etQQPassword;

	@BindView(R.id.etJoinGroupKeyword)
	protected EditText etJoinGroupKeyword;
	@BindView(R.id.etJoinGroupMyDesc)
	protected EditText etJoinGroupMyDesc;
	@BindView(R.id.etKeywordJoinGroupCount)
	protected EditText etKeywordJoinGroupCount;
	@BindView(R.id.etGroupNumberMinPeople)
	protected EditText etGroupNumberMinPeople;

	@Override
	protected void onInit() {
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		tvTitle.setText(R.string.app_name);
		btnMenu.setVisibility(View.VISIBLE);
		btnMenu.setOnClickListener(this);
		new SoftKeyboardStateHelper(activityRootView)
				.addSoftKeyboardStateListener(this);
	}

	@OnClick(R.id.btnStartService)
	protected void onClickBtnStartService(View v) {
		if (!getJoinGroupConditions()) {
			return;
		}
		Bundle bundleLoginInfo = null;
		if ((bundleLoginInfo = checkInputQQLoginInfo()) != null) {
			openActivitie(LoopSeekActivity.class, bundleLoginInfo);
			return;
		}
		openActivitie(LoopSeekActivity.class);
	}

	private Bundle checkInputQQLoginInfo() {
		String username = etQQUsername.getText().toString().trim();
		String password = etQQPassword.getText().toString().trim();
		if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			Bundle bundle = new Bundle();
			bundle.putString("username", username);
			bundle.putString("password", password);
			return bundle;
		}
		return null;
	}

	/**
	 * 获取输入的加群筛选条件
	 */
	private boolean getJoinGroupConditions() {
		ConditionsMode conditionsMode = new ConditionsMode();
		String etJoinGroupMyDescStr = this.etJoinGroupMyDesc.getText()
				.toString();
		String etGroupNumberMinPeopleStr = this.etGroupNumberMinPeople
				.getText().toString();
		String etKeywordJoinGroupCountStr = this.etKeywordJoinGroupCount
				.getText().toString();
		String etJoinGroupKeywordStr = this.etJoinGroupKeyword.getText()
				.toString();

		if (StringUtils.isEmpty(etJoinGroupMyDescStr = etJoinGroupMyDescStr
				.trim())) {
			Toast.makeText(this, "请输入加群个人介绍!", Toast.LENGTH_LONG).show();
			return false;
		}
		if (StringUtils.isEmpty(etJoinGroupKeywordStr = etJoinGroupKeywordStr
				.trim())) {
			Toast.makeText(this, "请输入加群关键词，多个用|隔开！", Toast.LENGTH_LONG).show();
			return false;
		}

		try {
			int intGroupNumberMinPeople = Integer
					.valueOf(etGroupNumberMinPeopleStr);
			int intKeywordJoinGroupCount = Integer
					.valueOf(etKeywordJoinGroupCountStr);
			
			List<String> keywordList = new ArrayList<String>();
			String[] keywords = etJoinGroupKeywordStr.split("\\|");//正则表达式分割,按|分割,|要加上转义符号
			for(String keyword:keywords){
				if(!StringUtils.isEmpty(keyword)){
					keywordList.add(keyword);
				}
			}
			
			conditionsMode.setJoinGroupMyDesc(etJoinGroupMyDescStr);
			conditionsMode.setEachKeywordJoinGroupNumber(intKeywordJoinGroupCount);
			conditionsMode.setGroupPeopleOfNumber(intGroupNumberMinPeople);
			conditionsMode.setJoinGroupKeywords(keywordList);
			
			HandlSearchContactsWindow.instance.setConditionsMode(conditionsMode);
			return true;
		} catch (NumberFormatException e) {
			Toast.makeText(this, "条件输入有误，请输入正确的整数!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onSoftKeyboardOpened() {
		// TODO Auto-generated method stub
		btnStartService.setVisibility(View.GONE);
	}

	@Override
	public void onSoftKeyboardClosed() {
		// TODO Auto-generated method stub
		btnStartService.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(btnMenu)){
			PopupMenu popup = new PopupMenu(this, v);
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()){
						case R.id.action_help:
							openActivitie(HelpActivity.class);
							return true;
						case R.id.action_about:
							openActivitie(AboutActivity.class);
							return true;
						case R.id.action_setting:
							openActivitie(SettingActivity.class);
							return true;
					}
					return false;
				}
			});
			popup.inflate(R.menu.menu_main);
			popup.show();
		}
	}
}
