package com.cloudyang.my;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudyang.messageupload.Login;
import com.cloudyang.messageupload.R;
import com.cloudyang.messageupload.SmsListActivity;
import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HttpUtils;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class ChangePasswdActivity extends SwipeBackActivity implements OnClickListener {
	
	private EditText phonenumberEditText;
	private EditText oldPasswdEditText;
	private EditText newPasswdEditText;
	private Button changePasswdButton;
	
	private JSONObject json = new JSONObject();
	private Handler handler;
	private static String url = "http://"+SmsListActivity.IP+"/TEST/changePasswd.php";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepasswd);
		ActivityCollector.addActivity(this);
		
		phonenumberEditText = (EditText) findViewById(R.id.et_phone_cp);
		oldPasswdEditText = (EditText) findViewById(R.id.et_old_passwd);
		newPasswdEditText = (EditText) findViewById(R.id.et_new_passwd);
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("修改密码");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ChangePasswdActivity.this.finish();
			}
		});
		
		changePasswdButton = (Button) findViewById(R.id.btn_changePasswd);
		changePasswdButton.setOnClickListener(this);
		
		
		
		handler = new Handler(){
			@SuppressLint("HandlerLeak")
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
							Toast.makeText(ChangePasswdActivity.this, "修改成功,将重新登录", Toast.LENGTH_SHORT).show();
							
							Intent intent = new Intent(ChangePasswdActivity.this, Login.class);
							Bundle bundle = new Bundle();
							bundle.putString("TelePhone", phonenumberEditText.getText().toString().trim());
							bundle.putString("PassWord", newPasswdEditText.getText().toString().trim());
							intent.putExtras(bundle);
							startActivityForResult(intent, success);
						}else{
							Toast.makeText(ChangePasswdActivity.this, "修改失败,手机号或原密码有错误", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_changePasswd:
			try {
				json.put("phoneNum", phonenumberEditText.getText().toString());
				json.put("oldpassWd", oldPasswdEditText.getText().toString());
				json.put("newpassWd", newPasswdEditText.getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			new Thread(){
				@Override
				public void run() {
					try {
						HttpUtils.httpPostMethod(url, json, handler);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				};
			}.start();
			
			break;

		default:
			break;
		}

	}
	
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

}
