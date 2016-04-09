package com.cloudyang.messageupload;

import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class RulesDetail extends SwipeBackActivity implements OnClickListener{
	
	private Button beginTaskButton;
	private ImageButton imgbackButton;
	private ImageButton imgMeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_rulesdetail);
		ActivityCollector.addActivity(this);
		beginTaskButton = (Button) findViewById(R.id.begin_task);
		beginTaskButton.setOnClickListener(this);
		
		imgbackButton = (ImageButton) findViewById(R.id.img_back2welcome);
		imgbackButton.setOnClickListener(this);
		
		imgMeButton = (ImageButton) findViewById(R.id.btn_me);
		imgMeButton.setOnClickListener(this);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		finish();
		Intent intent = new Intent(RulesDetail.this, MainLayout.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.begin_task:
			Intent intent = new Intent(RulesDetail.this, SmsListActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.img_back2welcome:
			this.finish();
			break;
		case R.id.btn_me:
			Intent intent_me = new Intent(RulesDetail.this, MyCount.class);
			startActivityForResult(intent_me, 0);
		default:
			break;
		}
	}
	
	
	

}
