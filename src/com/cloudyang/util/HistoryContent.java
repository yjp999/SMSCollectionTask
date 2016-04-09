package com.cloudyang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudyang.info.HistoryInfo;
import com.cloudyang.messageupload.SmsListActivity;

import android.app.Activity;

public class HistoryContent {
	
	@SuppressWarnings("unused")
	private Activity activity;
	
	public Activity smsActivity = new SmsListActivity();
	
	ArrayList<HistoryInfo> historyInfos;
	
	public HistoryContent(Activity activity){
		historyInfos = new ArrayList<HistoryInfo>();
		this.activity = activity;
	}
	
	public ArrayList<HistoryInfo> getContent() throws JSONException{
		String content = readJsonFile(SmsListActivity.FILE_LOCAL_LOCATION + SmsListActivity.jsonFilePath);
		String[] jsonArray = content.trim().split("\\n");
		for(int i = 0; i < jsonArray.length; i++){
			if(jsonArray[i]==""){
				continue;
			}else{
				JSONObject strObject = new JSONObject(jsonArray[i].trim());
				HistoryInfo info = new HistoryInfo();
				info.setAllNumber(strObject.getString("allcount"));
				info.setDuplicate(strObject.getString("duplicate"));
				info.setInvalid(strObject.getString("invalid"));
				info.setMoney(strObject.getString("money"));
				info.setValid(strObject.getString("valid"));
				info.setUploadTime(strObject.getString("uploadTime"));
				
				historyInfos.add(info);
			}
		}
		
		return historyInfos;
	}
	
	public String readJsonFile(String path){
		File file = new File(path);
		BufferedReader reader = null;
		String laststr = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
		
	}

}
