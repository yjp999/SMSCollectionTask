package com.cloudyang.util;

import java.util.ArrayList;

import com.cloudyang.info.SmsInfo;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;

public class SmsContent {
	private Activity activity;
	
	private Uri uri;
	
	ArrayList<SmsInfo> infos;
	
	public SmsContent(Activity activity, Uri uri){
		infos = new ArrayList<SmsInfo>();
		this.activity = activity;
		this.uri = uri;
	}
	
	@SuppressWarnings("deprecation")
	public ArrayList<SmsInfo> getSmsInfos(){
		String[] projection = new String[] {"_id","address","person","body","date","type"};
		Cursor cursor = activity.managedQuery(uri, projection, null, null, "date desc");
		int nameColumn = cursor.getColumnIndex("person");
		int phoneNumberColumn = cursor.getColumnIndex("address");
		int smsbodyColumn = cursor.getColumnIndex("body");
		int dateColumn = cursor.getColumnIndex("date");
		int typeColumn = cursor.getColumnIndex("type");
		if(cursor!=null){
			while(cursor.moveToNext()){
				SmsInfo smsInfo = new SmsInfo();
				smsInfo.setName(cursor.getString(nameColumn));
				smsInfo.setDate(cursor.getString(dateColumn));
				smsInfo.setPhoneNumber(cursor.getString(phoneNumberColumn));
				smsInfo.setSmsbody(cursor.getString(smsbodyColumn));
				smsInfo.setType(cursor.getString(typeColumn));
				infos.add(smsInfo);
			}
			if(VERSION.SDK_INT<14){
				cursor.close();
			}
		}
		return infos;
	}

}
