package com.cloudyang.my;

import com.cloudyang.messageupload.R;
import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends SwipeBackActivity {
	
	public EditText et_advise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedbak);
		ActivityCollector.addActivity(this);
		
		et_advise = (EditText) findViewById(R.id.et_feedback);
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("反馈信息");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FeedBackActivity.this.finish();
			}
		});
		
		
		Button submitBtn = (Button) findViewById(R.id.submit_advise);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String adviseString = et_advise.getText().toString();
				if (adviseString.isEmpty()){
					Toast.makeText(FeedBackActivity.this, "反馈信息不能为空，请输入后再提交", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	

}
