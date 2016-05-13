package com.cloudyang.ftp;

import java.io.File;
import java.io.IOException;

import com.cloudyang.ftp.FTP.UploadProgressListener;
import com.cloudyang.messageupload.SmsListActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UploadTask extends AsyncTask<Void, Integer, Boolean> {
	
	
	private ProgressDialog pDialog;
	private Context context;
	private FTP ftp;
	private String folderName;
	private String fileName;
	
	
	
	
	public UploadTask(Context context, FTP ftp, String folderName, String fileName){
		this.context = context;
		this.ftp = ftp;
		this.folderName = folderName;
		this.fileName = fileName;
		
	}
	
	
	
	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(this.context);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setMessage("正在上传短信...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		Boolean res = false;
		File file = new File(this.fileName);
        try {
			ftp.uploadSingleFile(file, this.folderName, new UploadProgressListener(){

				@Override
				public void onUploadProgress(String currentStep,long uploadSize,File file) {
					Log.d("yjp", currentStep);										
					if(currentStep.equals(SmsListActivity.FTP_UPLOAD_SUCCESS)){
						Log.d("yjp", "-----shanchuan--successful");
					} else if(currentStep.equals(SmsListActivity.FTP_UPLOAD_LOADING)){
						long fize = file.length();
						float num = (float)uploadSize / (float)fize;
						int result = (int)(num * 100);
						Log.d("yjp", "-----shangchuan---"+result + "%");
						publishProgress(result);
					}
					
				}							
			});
			res = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();
		if(result){
			Toast.makeText(context, SmsListActivity.FTP_UPLOAD_SUCCESS, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, SmsListActivity.FTP_UPLOAD_FAIL, Toast.LENGTH_LONG).show();
		}
	}

	

	@Override
	protected void onProgressUpdate(Integer... values) {
		pDialog.setProgress((int) (values[0]));
	}
	
	

}
