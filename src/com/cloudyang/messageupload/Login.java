package com.cloudyang.messageupload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HttpUtils;
import com.cloudyang.util.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener{
	
	private ImageView loginImage;
	private Handler handler;
	private EditText username;
	private EditText password;
	private Button loginButton;
	
	private Drawable mIconPerson;
    private Drawable mIconLock;
    
	private TextView createAccounTextView;
	private String url = "http://"+SmsListActivity.IP+"/TEST/login.php";
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ActivityCollector.addActivity(this);
		
		mIconPerson=getResources().getDrawable(R.drawable.txt_person_icon);
		mIconPerson.setBounds(5, 1, 60, 50);
		mIconLock=getResources().getDrawable(R.drawable.txt_lock_icon);
		mIconLock.setBounds(5, 1, 60, 50);
		
		username=(EditText)findViewById(R.id.telephoneEdit);
		username.setCompoundDrawables(mIconPerson, null, null, null);
	    password=(EditText)findViewById(R.id.passwdText);
	    password.setCompoundDrawables(mIconLock, null, null, null);
		
		loginImage=(ImageView)findViewById(R.id.loginImage);
    	loginImage.setBackgroundDrawable(new BitmapDrawable(Util.toRoundBitmap(this, "putao2.jpg")));
    	loginImage.getBackground().setAlpha(0);
    	loginImage.setImageBitmap(Util.toRoundBitmap(this, "putao2.jpg"));
    	
		
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						JSONObject result = new JSONObject(res);
						int success = Integer.parseInt(result.getString("success"));
						/*String unameString = msg.getData().getString("username");
						String pwdString = msg.getData().getString("passwd");*/
//							Toast.makeText(Login.this, result.toString(), Toast.LENGTH_SHORT).show();
							// TODO Auto-generated catch block
						if(success == 0){
							SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
							Editor editor = sharedPreferences.edit();
							editor.putString("account", username.getText().toString().trim());
							editor.putString("passwd", password.getText().toString().trim());
							editor.commit();
							Intent intent = new Intent(Login.this, RulesDetail.class);
							startActivityForResult(intent, success);
						}else{
							Toast.makeText(Login.this, "输入的手机号或密码有错", Toast.LENGTH_LONG).show();
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
		
		Bundle bundle = this.getIntent().getExtras();

		
		username = (EditText) findViewById(R.id.telephoneEdit);
		password = (EditText) findViewById(R.id.passwdText);
		
		loginButton = (Button) findViewById(R.id.btnLogin);
		loginButton.setOnClickListener(this);
		
		createAccounTextView = (TextView) findViewById(R.id.register_tv);
		createAccounTextView.setOnClickListener(this);
		

		if(bundle != null){
			username.setText(bundle.getString("TelePhone"));
			password.setText(bundle.getString("PassWord"));
		}
		
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						JSONObject json = new JSONObject();
						json.put("TelePhone", username.getText().toString().trim());
						json.put("PassWord", password.getText().toString().trim());
						HttpUtils.httpPostMethod(url, json, handler);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("json", "解析JSON出错");
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			break;
			
		case R.id.register_tv:
			Intent createAccountIntent = new Intent(Login.this, RegisterActivity.class);
			startActivityForResult(createAccountIntent, 0);
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
