package com.cloudyang.my;

import com.cloudyang.messageupload.R;
import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class FAQActivity extends SwipeBackActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq);
		ActivityCollector.addActivity(this);
		
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("常见问题");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FAQActivity.this.finish();
			}
		});
		
		
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	

}
