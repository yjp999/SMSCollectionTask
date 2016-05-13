package com.cloudyang.my;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudyang.messageupload.R;
import com.cloudyang.messageupload.SmsListActivity;
import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HttpUtils;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class FeedBackActivity extends SwipeBackActivity {
	
	private Handler handler;
	
	private JSONObject json = new JSONObject();
	
	public EditText et_advise;
	
	private static String url = "http://"+SmsListActivity.IP+"/TEST/feedback.php";

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
		
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						JSONObject result = new JSONObject(res);
						int success = Integer.parseInt(result.getString("success"));
						if(success == 0){
							Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(FeedBackActivity.this, "提交失败，请重新编辑再提交", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
			
		};
		
		
		Button submitBtn = (Button) findViewById(R.id.submit_advise);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String adviseString = et_advise.getText().toString();
				if (adviseString.isEmpty()){
					Toast.makeText(FeedBackActivity.this, "反馈信息不能为空，请输入后再提交", Toast.LENGTH_SHORT).show();
				}else{
					String contents = et_advise.getText().toString().trim();
					String telephone = getTelephone();
					try {
						json.put("telephone", telephone);
						json.put("contents", contents);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					new Thread(){

						@Override
						public void run() {
							try {
								HttpUtils.httpPostMethod(url, json, handler);
							} catch (IOException
									| JSONException e) {
								e.printStackTrace();
							}
						}
						
					}.start();
					
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	public String getTelephone(){
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getString("account", "");
	}
	
	

}
