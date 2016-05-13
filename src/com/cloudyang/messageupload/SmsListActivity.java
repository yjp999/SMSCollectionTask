package com.cloudyang.messageupload;

import java.io.BufferedWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudyang.ftp.FTP;
import com.cloudyang.ftp.UploadTask;
import com.cloudyang.info.SmsInfo;
import com.cloudyang.util.ActivityCollector;
import com.cloudyang.util.HttpUtils;
import com.cloudyang.util.SildingFinishLayout;
import com.cloudyang.util.SildingFinishLayout.OnSildingFinishListener;
import com.cloudyang.util.SmsContent;
import com.cloudyang.util.SmsListAdapter;
import com.cloudyang.util.SmsListAdapter.ViewHolder;
import com.cloudyang.messageupload.R;
import com.cloudyang.my.MyCount;
import com.swipebacklayout.lib.app.SwipeBackActivity;

@SuppressLint("HandlerLeak")
public class SmsListActivity extends SwipeBackActivity implements OnClickListener,OnItemClickListener{
	
	private Context context;
	private UploadTask upload;
	public static String IP = "192.168.199.189";
	private static String URL = "http://"+IP+"/TEST/read_msg.php";
	private static String moneyurl = "http://"+IP+"/TEST/write_money.php";
	
	public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
	public static final String FTP_CONNECT_FAIL = "ftp连接失败";
	public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
	public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";
	
	public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
	public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
	public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

	public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
	public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
	public static final String FTP_DOWN_FAIL = "ftp文件下载失败";
	
	public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
	public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";
//	public String FILE_LOCAL_LOCATION = this.getFilesDir().getPath()+"/";
	public static final String FILE_LOCAL_LOCATION = "/data/data/com.cloudyang.messageupload/files/";
	
	
	public String folderName;
	public String fileName;
	public String time_filename;
	private Handler handler;
	private Handler handler_money;
	
	
	public static String jsonFilePath = "record.json";
	public String jsonString;
	
	private ListView listView;
	private ArrayList<SmsInfo> infos;
	private ArrayList<SmsInfo> selectedInfos = new ArrayList<SmsInfo>();
	private SmsListAdapter mAdapter;
	private Button bt_selectall;
	private Button bt_cancel;
	private Button bt_deselectall;
	private Button bt_upload;
	private ImageButton imgbt_Back;
	private ImageButton imgbt_me;
	private int checkNum; //记录选中的条目数量
	private TextView tv_show; //用于显示选中的条目数量
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.sms);
		ActivityCollector.addActivity(this);
		/*实例化各个控件 */
		bt_selectall = (Button) findViewById(R.id.bt_selectall);
		bt_selectall.setOnClickListener(this);
		bt_cancel = (Button) findViewById(R.id.bt_cancelselectall);
		bt_cancel.setOnClickListener(this);
		bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
		bt_deselectall.setOnClickListener(this);
		bt_upload = (Button) findViewById(R.id.bt_upload);
		bt_upload.setOnClickListener(this);
		
		imgbt_Back = (ImageButton) findViewById(R.id.btn_back);
		imgbt_Back.setOnClickListener(this);
		imgbt_me = (ImageButton) findViewById(R.id.btn_me);
		imgbt_me.setOnClickListener(this);
		
		
		
		
		Uri uri = Uri.parse("content://sms/");
		SmsContent sc = new SmsContent(this, uri);
		infos = sc.getSmsInfos();
		
		for(int i=0;i<infos.size();i++){
			selectedInfos.add(infos.get(i));
		}
		
		tv_show = (TextView) findViewById(R.id.tv_show_checkNum);
		checkNum = infos.size();
		
		tv_show.setText("已选中" + checkNum + "条");
		listView = (ListView) this.findViewById(R.id.ListView_Sms);
		
		mAdapter = new SmsListAdapter(this,infos);
		listView.setAdapter(mAdapter);
		
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);  
        mSildingFinishLayout.setOnSildingFinishListener(new OnSildingFinishListener() {  
  
                    @Override  
                    public void onSildingFinish() {  
                       SmsListActivity.this.finish();
                       overridePendingTransition(0,  
                               R.anim.base_slide_right_out);
                    }  
                });
		
    	// touchView要设置到ListView上面  
        mSildingFinishLayout.setTouchView(listView);
        listView.setOnItemClickListener(this);
        
        handler_money = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						JSONObject result = new JSONObject(res);
						int success = Integer.parseInt(result.getString("success"));
						if(success == 0){
							Toast.makeText(SmsListActivity.this, "零钱已经存入您的账户", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(SmsListActivity.this, "存入失败", Toast.LENGTH_LONG).show();
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
        
        handler = new Handler(){
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						JSONObject result = new JSONObject(res);
						
						int duplicate = Integer.parseInt(result.getString("duplicate"));
						int invalid = Integer.parseInt(result.getString("invalid"));
						int all = Integer.parseInt(result.getString("allcount"));
						int valid = Integer.parseInt(result.getString("valid"));
						final double earn = 0.03 * valid;
						WalletActivity.accountMoney += earn; //calculate how much money get 
						
						result.put("money", earn);
						String timeString = time_filename.replace(".txt", "");
						result.put("uploadTime", timeString);
						jsonString = result.toString();
						
						
						AlertDialog.Builder dialog = new AlertDialog.Builder(SmsListActivity.this);
						dialog.setTitle("上传成功");
						dialog.setMessage("您一共上传了"+String.valueOf(all)+"条\n"+"重复条数:"+String.valueOf(duplicate)+"\t"+"无效条数:"+String.valueOf(invalid)+"\t"+"有效条数:"+String.valueOf(valid)+"\n点击右上角进入'我的钱包'查看酬金");
						dialog.setCancelable(false);
						dialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								//delete file to free space
								File file = new File(fileName);
								deleteFile(file);
								
								writeJsonFile(jsonFilePath, jsonString);
								
								final JSONObject json = new JSONObject();
								try {
									json.put("telephone", getTelephone());
									json.put("earnings", earn);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								new Thread(){

									@Override
									public void run() {
										try {
											HttpUtils.httpPostMethod(moneyurl, json, handler_money);
										} catch (IOException
												| JSONException e) {
											e.printStackTrace();
										}
									}
									
								}.start();
							}
						});
						dialog.show();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		};
        
        
        
        
        
	}
	
	


	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_selectall:
			for(int i = 0; i < infos.size(); i++){
				SmsListAdapter.getIsSelected().put(i, true);
				if(!selectedInfos.contains(infos.get(i))){
					selectedInfos.add(infos.get(i));
				}
				
			}
			checkNum = infos.size();
			dataChanged();
			break;
		case R.id.bt_cancelselectall:
			for(int i=0;i<infos.size();i++){
				if(SmsListAdapter.getIsSelected().get(i)){
					SmsListAdapter.getIsSelected().put(i, false);
					checkNum--;
					selectedInfos.remove(infos.get(i));
				}else{
					SmsListAdapter.getIsSelected().put(i, true);
					checkNum++;
					selectedInfos.add(infos.get(i));
				}
			}
			dataChanged();
			break;
		case R.id.bt_deselectall:
			for(int i=0; i<infos.size();i++){
				if(SmsListAdapter.getIsSelected().get(i)){
					SmsListAdapter.getIsSelected().put(i, false);
					checkNum--;
				}
				selectedInfos.remove(infos.get(i));
			}
			dataChanged();
			break;
		case R.id.bt_upload:
			if(checkNum==0){
				Toast.makeText(this, "请先选择信息再上传", Toast.LENGTH_SHORT).show();
				break;
			}
			AlertDialog.Builder dialog = new AlertDialog.Builder(SmsListActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("您选择了"+checkNum+"条，确定上传吗？");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@SuppressLint("SimpleDateFormat")
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String message = "";
					for(int i = 0; i<selectedInfos.size();i++){
						message = message + selectedInfos.get(i).getSmsbody().trim()+"\r\n";
					}
					
					//文件夹名以手机号(唯一标识)命名
					SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
					folderName = sharedPreferences.getString("account", "");
					
					//文件名以时间戳命名
					SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
					time_filename = dFormat.format(new Date())+".txt";
					fileName = SmsListActivity.FILE_LOCAL_LOCATION + time_filename;
					
					
					saveFile(message,time_filename);
					FTP ftp = new FTP();
					upload = new UploadTask(context, ftp, folderName, fileName);
					Boolean result = false;
					
					try {
						result = upload.execute().get();
						if(result){
							new Thread(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									super.run();
									try {
										JSONObject json = new JSONObject();
										json.put("TelePhone", folderName);
										json.put("FileName", time_filename);
										HttpUtils.httpPostMethod(SmsListActivity.URL, json, handler);
									} catch (JSONException e) {
										Log.d("json", "解析JSON出错");
										e.printStackTrace();
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									} catch (ClientProtocolException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}.start();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});
			dialog.show();
			break;
			
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_me:
			Intent intent = new Intent(SmsListActivity.this, MyCount.class);
			startActivityForResult(intent, 0);
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0,  
                R.anim.base_slide_right_out);
	}




	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.ListView_Sms:
			ViewHolder holder = (ViewHolder) arg1.getTag();
			holder.cBox.toggle();
			SmsListAdapter.getIsSelected().put(arg2, holder.cBox.isChecked());
			if(holder.cBox.isChecked() == true){
				checkNum++;
				selectedInfos.add(infos.get(arg2));
			}else{
				checkNum--;
				selectedInfos.remove(infos.get(arg2));
			}
			dataChanged();
//			tv_show.setText("已选中" + checkNum + "条");
			break;

		default:
			break;
		}
		
	}


	private void dataChanged(){
		mAdapter.notifyDataSetChanged();
		tv_show.setText("已选中" + checkNum + "条");
		Log.d("yjp", "checkNum:"+selectedInfos.size());
		Log.d("yjp", "infos:"+infos.size());
	}
	
	/*
	 * 存储文件
	*/
	public void saveFile(String string,String filename){
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			
			out = openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(string.trim());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 存储json文件
	*/
	public void writeJsonFile(String filePath, String sets){
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			
			out = openFileOutput(filePath, Context.MODE_APPEND);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(sets+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			Log.e("yjp", "文件不存在！"+"\n");
		}
	}
	
	public String getTelephone(){
		SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getString("account", "");
	}

	
}