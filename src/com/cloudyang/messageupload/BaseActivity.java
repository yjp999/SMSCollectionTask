package com.cloudyang.messageupload;

import com.cloudyang.util.ActivityCollector;

import android.app.Activity;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	private int mBackKeyPressedTimes = 0;
	@Override
	public void onBackPressed() {
		if(mBackKeyPressedTimes == 0){
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mBackKeyPressedTimes = 1;
			new Thread(){
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finally{
						mBackKeyPressedTimes = 0;
					}
				}
			}.start();
			return;
		}
		else {
			ActivityCollector.finishAll();
		}
		super.onBackPressed();
	}

}
