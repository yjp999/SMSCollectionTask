package com.cloudyang.my;

import java.util.ArrayList;

import org.json.JSONException;

import com.cloudyang.info.HistoryInfo;
import com.cloudyang.messageupload.R;
import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HistoryContent;
import com.cloudyang.util.HistoryTaskListAdapter;
import com.swipebacklayout.lib.app.SwipeBackActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class HistoryTaskActivity extends SwipeBackActivity implements OnItemClickListener{
	
	private ListView listView;
	private ArrayList<HistoryInfo> historyinfos;
	private HistoryTaskListAdapter hAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		ActivityCollector.addActivity(this);
		
		TextView tvView = (TextView) findViewById(R.id.tv_get_result);
		tvView.setText("任务记录");
		
		ImageButton imgButton = (ImageButton) findViewById(R.id.img_back2mycount);
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				HistoryTaskActivity.this.finish();
			}
		});
		
		listView = (ListView) this.findViewById(R.id.ListView_history);
		HistoryContent hContent = new HistoryContent(this);
		try {
			historyinfos = hContent.getContent();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		hAdapter = new HistoryTaskListAdapter(this,historyinfos);
		listView.setAdapter(hAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.ListView_history:
			HistoryInfo historyInfo = historyinfos.get(arg2);
			String detail = "一共上传短信 "+historyInfo.getAllNumber()+" 条\n";
			detail += "重复 "+historyInfo.getDuplicate()+" 条\n";
			detail += "无效 "+historyInfo.getInvalid()+" 条\n";
			detail += "有效 "+historyInfo.getValid()+" 条\n\n";
			detail += "共收益 "+historyInfo.getMoney()+" 元\n\n";
			detail += historyInfo.getUploadTime();
			Intent myIntent = new Intent(HistoryTaskActivity.this, HistoryTaskDetail.class);
			myIntent.putExtra("detail", detail);
			startActivityForResult(myIntent, 0);
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
