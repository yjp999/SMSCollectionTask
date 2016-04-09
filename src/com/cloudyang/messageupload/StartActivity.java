package com.cloudyang.messageupload;

import com.cloudyang.util.ActivityCollector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000; //—”≥Ÿ»˝√Î
	
	@Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
        ActivityCollector.addActivity(this);
        new Handler().postDelayed(new Runnable(){ 
 
         @Override
         public void run() { 
             Intent mainIntent = new Intent(StartActivity.this, MainLayout.class); 
             StartActivity.this.startActivity(mainIntent); 
             StartActivity.this.finish(); 
         } 
             
        }, SPLASH_DISPLAY_LENGHT); 
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	} 
	
	

}
