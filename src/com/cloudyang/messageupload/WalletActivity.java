package com.cloudyang.messageupload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WalletActivity extends SwipeBackActivity implements OnItemClickListener{
	
	private GridView mGridView;
	private Handler handler;
	public JSONObject json = new JSONObject();
	private static String url = "http://"+SmsListActivity.IP+"/TEST/read_money.php";
	public static double accountMoney = 0.0;
	private int[] imageRes = {R.drawable.shouyi,R.drawable.tixian,R.drawable.bank_card};
	private String[] itemName = {"收益","提现","银行卡"};

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.money);
		ActivityCollector.addActivity(this);
		
		final TextView tView =  (TextView) findViewById(R.id.tv_money_account);
		if(!StartActivity.isConnect(this)){
			Toast.makeText(WalletActivity.this, "网络连接不可用", Toast.LENGTH_LONG).show();
			tView.setText("￥ "+"--"+" RMB");
		}
		
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
						double lefts = Double.parseDouble(result.getString("lefts"));
						if(success != 0){
							//Toast.makeText(WalletActivity.this, "网络连接出现问题", Toast.LENGTH_LONG).show();
							tView.setText("￥ "+"--"+" RMB");
						}
						else{
							tView.setText("￥ "+Double.toString(lefts)+" RMB");
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
		
		/*
		 * 请求余额信息--begin
		*/
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					json.put("telephone", getTelephone());
					HttpUtils.httpPostMethod(url, json, handler);
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("我的钱包");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WalletActivity.this.finish();
			}
		});
		
		
		mGridView = (GridView) findViewById(R.id.MyGridView);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < itemName.length; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImageView", imageRes[i]);
			map.put("ItemTextView", itemName[i]);
			data.add(map);
		}
		
		//为money_item 添加适配器
		SimpleAdapter simpleAdapter = new SimpleAdapter(WalletActivity.this, data, R.layout.money_item, new String[]{"ItemImageView","ItemTextView"}, new int[]{R.id.ItemImageView,R.id.ItemTextView});
		mGridView.setAdapter(simpleAdapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
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
