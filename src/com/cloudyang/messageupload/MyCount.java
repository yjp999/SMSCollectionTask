package com.cloudyang.messageupload;

import com.cloudyang.my.ChangePasswdActivity;
import com.cloudyang.my.FAQActivity;
import com.cloudyang.my.FeedBackActivity;
import com.cloudyang.my.HistoryTaskActivity;
import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MyCount extends SwipeBackActivity implements OnClickListener{
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_info);
		ActivityCollector.addActivity(this);
		
		LinearLayout ll_wallet = (LinearLayout) findViewById(R.id.ll_my_wallet);
		LinearLayout ll_record_history = (LinearLayout) findViewById(R.id.ll_record_task);
		LinearLayout ll_faq = (LinearLayout) findViewById(R.id.ll_faq);
		LinearLayout ll_per_settingLayout = (LinearLayout) findViewById(R.id.per_setting);
		LinearLayout ll_feedback = (LinearLayout) findViewById(R.id.feedback);
		
		Button btQuit = (Button) findViewById(R.id.btn_quit);
		
		ImageButton btBack = (ImageButton) findViewById(R.id.imgbtn_back);
		
		ll_wallet.setOnClickListener(this);
		ll_record_history.setOnClickListener(this);
		ll_faq.setOnClickListener(this);
		ll_per_settingLayout.setOnClickListener(this);
		ll_feedback.setOnClickListener(this);
		
		btQuit.setOnClickListener(this);
		btBack.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_my_wallet:
			Intent intent = new Intent(MyCount.this, WalletActivity.class);
			startActivityForResult(intent, 0);
			break;
			
		case R.id.ll_record_task:
			Intent historyIntent = new Intent(MyCount.this, HistoryTaskActivity.class);
			startActivityForResult(historyIntent, 0);
			break;
			
		case R.id.per_setting:
			Intent cpIntent = new Intent(MyCount.this, ChangePasswdActivity.class);
			startActivityForResult(cpIntent, 0);
			break;
			
		case R.id.ll_faq:
			Intent faqIntent = new Intent(MyCount.this,FAQActivity.class);
			startActivityForResult(faqIntent, 0);
			break;
			
		case R.id.feedback:
			Intent fbIntent = new Intent(MyCount.this,FeedBackActivity.class);
			startActivityForResult(fbIntent, 0);
			break;
			
		case R.id.btn_quit:
			AlertDialog.Builder dialog = new AlertDialog.Builder(MyCount.this);
			dialog.setTitle("提示");
			dialog.setMessage("您确定要退出登录吗？");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					removeSharedPreference();
					ActivityCollector.finishAll();
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});
			dialog.show();
			break;
		case R.id.imgbtn_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	public void removeSharedPreference() {
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove("account");
		editor.remove("passwd");
		editor.commit();// 提交修改
	}
	
	

}
