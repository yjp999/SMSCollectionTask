package com.cloudyang.my;

import com.cloudyang.messageupload.R;
import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class HistoryTaskDetail extends SwipeBackActivity implements OnClickListener{
	
	private TextView displayDetail;
	private ImageButton imagBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historytask_detail);
		ActivityCollector.addActivity(this);
		
		displayDetail = (TextView) findViewById(R.id.tv_ht_detail);
		Intent intent = getIntent();
		String detail = intent.getStringExtra("detail");
		displayDetail.setText(detail);
		
		imagBack = (ImageButton) findViewById(R.id.img_back2mycount);
		imagBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_back2mycount:
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
	
	
}
