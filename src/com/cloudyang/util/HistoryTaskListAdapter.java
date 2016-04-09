package com.cloudyang.util;

import java.util.List;

import com.cloudyang.info.HistoryInfo;
import com.cloudyang.messageupload.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryTaskListAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater = null;
	private View myview;
	private List<HistoryInfo> historyInfos;
	
	public HistoryTaskListAdapter(Context context, List<HistoryInfo> historyInfos){
		layoutInflater = LayoutInflater.from(context);
		this.historyInfos = historyInfos;
	}

	@Override
	public int getCount() {
		return historyInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return historyInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryInfo info = historyInfos.get(position);
		ViewHolderHistory viewHolder;
		if(convertView == null){
			myview = layoutInflater.inflate(R.layout.historyitem, null);
			viewHolder = new ViewHolderHistory();
			viewHolder.msgCountAll = (TextView) myview.findViewById(R.id.tv_history_ok_num);
			myview.setTag(viewHolder);
		}else{
			myview = convertView;
			viewHolder = (ViewHolderHistory) myview.getTag();
		}
		viewHolder.msgCountAll.setText("∂Ã–≈ ’ºØ "+info.getUploadTime());
		
		return myview;
	}

	
	public static class ViewHolderHistory{
		public TextView msgCountAll;
	}
}
