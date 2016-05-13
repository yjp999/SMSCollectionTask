package com.cloudyang.messageupload;

import com.cloudyang.util.ActivityCollector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒
	
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
	
	public static boolean isConnect(Context context) { 
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
	    try { 
	        ConnectivityManager connectivity = (ConnectivityManager) context 
	                .getSystemService(Context.CONNECTIVITY_SERVICE); 
	        if (connectivity != null) { 
	            // 获取网络连接管理的对象 
	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
	            if (info != null&& info.isConnected()) { 
	                // 判断当前网络是否已经连接 
	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
	                    return true; 
	                } 
	            } 
	        } 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } 
        return false; 
    } 
	

}
