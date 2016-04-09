package com.cloudyang.messageupload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cloudyang.util.ActivityCollector;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class WalletActivity extends SwipeBackActivity implements OnItemClickListener{
	
	private GridView mGridView;
	public static double accountMoney = 0.0;
	private int[] imageRes = {R.drawable.shouyi,R.drawable.tixian,R.drawable.bank_card};
	private String[] itemName = {"收益","提现","银行卡"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.money);
		ActivityCollector.addActivity(this);
		
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("我的钱包");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WalletActivity.this.finish();
			}
		});
		
		TextView tView =  (TextView) findViewById(R.id.tv_money_account);
		tView.setText("￥ "+Double.toString(accountMoney)+" RMB");
		
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
	
	
	
	
	
	

}
