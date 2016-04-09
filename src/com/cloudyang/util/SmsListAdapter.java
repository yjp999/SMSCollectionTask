package com.cloudyang.util;

import java.util.HashMap;
import java.util.List;
import com.cloudyang.info.SmsInfo;
import com.cloudyang.messageupload.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SmsListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private View myview;
	private List<SmsInfo> infos;
	private static HashMap<Integer, Boolean> isSelected; //用来控制CheckBox的选中状况
	
	public SmsListAdapter(Context context, List<SmsInfo> infos){
		layoutInflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		this.infos = infos;
		//初始化数据
		initData();
	}
	
	private void initData(){
		for(int i=0;i<infos.size();i++){
			getIsSelected().put(i, true);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SmsInfo info = infos.get(position);
		ViewHolder viewHolder;
		if(convertView == null){
			myview = layoutInflater.inflate(R.layout.smsitem, null);
			viewHolder = new ViewHolder();
			viewHolder.smsPhoneNumberTextView = (TextView) myview.findViewById(R.id.TextView_SmsName);
			viewHolder.smsBodyTextView = (TextView) myview.findViewById(R.id.TextView_SmsBody);
			viewHolder.cBox = (CheckBox) myview.findViewById(R.id.item_cb);
			myview.setTag(viewHolder);
		}else{
			myview = convertView;
			viewHolder = (ViewHolder) myview.getTag();
		}
		viewHolder.smsPhoneNumberTextView.setText(info.getPhoneNumber());
		viewHolder.smsBodyTextView.setText(info.getSmsbody());
		viewHolder.cBox.setChecked(getIsSelected().get(position));
		return myview;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected){
		SmsListAdapter.isSelected = isSelected;
	}
	
	public static class ViewHolder{
		TextView smsPhoneNumberTextView;
		TextView smsBodyTextView;
		public CheckBox cBox;
	}

}
