package com.cloudyang.messageupload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.cloudyang.messageupload.R;
import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HttpUtils;
import com.swipebacklayout.lib.app.SwipeBackActivity;

public class RegisterActivity extends SwipeBackActivity implements OnClickListener, Callback{
	
	private static String APPKEY = "1134e13cb25d2";
	
	private static String APPSECRET = "2e9f1b32dd27e9e556c4b6f900a51a5f";
	
	private String phoneNumber;
	private String etCode;
	
	private Boolean ready;
	private Boolean captcha_OK = false;
	
	private EditText codeEditText;
	private Button getcodeBtn;
	private Button submitCodeBtn;
	private EditText passwd2EditText;
	private ImageView ivClear;
	private ImageView pwdOC; //Password open/close
	private int pwdFLAG = 1;
	
	private TimeCount time;

	
	private EditText passwdEditView;
	private EditText usernameEditView;
	
	private ImageButton goBack;
	
	private JSONObject json = new JSONObject();
	private Handler handler;
	private String url = "http://"+SmsListActivity.IP+"/TEST/register.php";

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		ActivityCollector.addActivity(this);
		
		time = new TimeCount(60000, 1000);
		initSDK();
		
		handler = new Handler(){
			@SuppressLint("HandlerLeak")
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
//						Toast.makeText(RegisterActivity.this, res, Toast.LENGTH_LONG).show();
						if(success == 0 && captcha_OK){
							Intent intent = new Intent(RegisterActivity.this, Login.class);
							Bundle bundle = new Bundle();
							bundle.putString("TelePhone", usernameEditView.getText().toString().trim());
							bundle.putString("PassWord", passwdEditView.getText().toString().trim());
							intent.putExtras(bundle);
							startActivityForResult(intent, success);
						}else if(success == 1){
							Toast.makeText(RegisterActivity.this, "注册失败，您输入的手机号已经被注册", Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(RegisterActivity.this, "注册失败，请检查您输入的手机号位数是否为11位", Toast.LENGTH_LONG).show();
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
		usernameEditView = (EditText) findViewById(R.id.et_write_phone);
		passwdEditView = (EditText) findViewById(R.id.et_passwd);
		codeEditText = (EditText) findViewById(R.id.et_sms_captcha);
		passwdEditView = (EditText) findViewById(R.id.et_passwd);
		passwd2EditText = (EditText) findViewById(R.id.et_passwd_again);
		ivClear = (ImageView) findViewById(R.id.iv_clear);
		pwdOC = (ImageView) findViewById(R.id.iv_passwd_1);
		
		usernameEditView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				refreshViews(arg0);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		passwdEditView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				if(s.length() > 0){
					pwdOC.setVisibility(View.VISIBLE);
				}else{
					pwdOC.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		passwd2EditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					
				}else {
					String passwdString1 = passwdEditView.getText().toString().trim();
					String passwdString2 = passwd2EditText.getText().toString().trim();
					if(!passwdString1.equals(passwdString2)){
						Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		
		
		getcodeBtn = (Button) findViewById(R.id.btn_next);
		submitCodeBtn = (Button) findViewById(R.id.btn_submit);
		
		getcodeBtn.setOnClickListener(this);
		submitCodeBtn.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		pwdOC.setOnClickListener(this);
		
		goBack = (ImageButton) findViewById(R.id.imgback2login);
		goBack.setOnClickListener(this);
		
	}
	
	private void refreshViews(CharSequence s){
		if(s.length() > 0){
			getcodeBtn.setEnabled(true);
			ivClear.setVisibility(View.VISIBLE);
		}else{
			getcodeBtn.setEnabled(false);
			ivClear.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_submit:
			if(TextUtils.isEmpty(usernameEditView.getText().toString().trim()) && TextUtils.isEmpty(passwdEditView.getText().toString().trim())){
				Toast.makeText(this, "手机号和密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(TextUtils.isEmpty(usernameEditView.getText().toString().trim())){
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(TextUtils.isEmpty(passwdEditView.getText().toString().trim())){
				Toast.makeText(this, "密码不能为空", TRIM_MEMORY_BACKGROUND).show();
				return;
			}
			try {
				phoneNumber = usernameEditView.getText().toString().trim();
				etCode = codeEditText.getText().toString().trim();
				SMSSDK.submitVerificationCode("86", phoneNumber, etCode);
				json.put("PassWord", passwdEditView.getText().toString().trim());
				json.put("TelePhone", usernameEditView.getText().toString().trim());
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
			Log.d("json", json.toString());
			break;
			
		case R.id.imgback2login:
			this.finish();
			break;
			
		case R.id.btn_next:
			phoneNumber = usernameEditView.getText().toString().trim();
			if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11){
				time.start(); //开始计时
				SMSSDK.getVerificationCode("86", phoneNumber);
			}else if(TextUtils.isEmpty(phoneNumber)){
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}else if(phoneNumber.length() != 11){
				Toast.makeText(this, "手机号码应该为11位", Toast.LENGTH_SHORT).show();
				return;
			}
			break;
			
		case R.id.iv_clear:
			usernameEditView.getText().clear();
			break;
		case R.id.iv_passwd_1:
			
			if(pwdFLAG==1){
				passwdEditView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				pwdOC.setImageDrawable(getResources().getDrawable(R.drawable.pwd_close));
				pwdOC.setVisibility(View.VISIBLE);
				pwdFLAG = 2;
				break;
			}else{
				passwdEditView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				pwdOC.setImageDrawable(getResources().getDrawable(R.drawable.pwd_open));
				pwdOC.setVisibility(View.VISIBLE);
				pwdFLAG = 1;
				break;
			}
		default:
			break;
		}
	}

	private void initSDK() {

		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
		if (APPKEY.equalsIgnoreCase("f3fc6baa9ac4") ) {
			Toast.makeText(this,"此APPKEY仅供测试使用，且不定期失效，请到mob.com后台申请正式APPKEY",Toast.LENGTH_SHORT).show();
		}
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            //提交验证码成功
            	captcha_OK = true;
            	Toast.makeText(RegisterActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
            //获取验证码成功
            	Toast.makeText(RegisterActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
            //返回支持发送验证码的国家列表
            } 
        }else{
	       	try {
					String description = data.toString();
					Pattern pattern = Pattern.compile("(\\{.*?\\})");
					Matcher matcher = pattern.matcher(description);
					String res = matcher.find()?matcher.group():"";
					JSONObject resultjson = new JSONObject(res);
					String desc = resultjson.getString("detail");
					Toast.makeText(RegisterActivity.this, desc, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	((Throwable)data).printStackTrace(); 
        }
		return false;
	}
	
	@Override
	protected void onDestroy() {
		if(ready){
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() { //计时结束
			getcodeBtn.setText("获取验证码");
			getcodeBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) { //计时过程
			getcodeBtn.setClickable(false);
			getcodeBtn.setText(millisUntilFinished/1000 + "秒");
		}
	}

}
