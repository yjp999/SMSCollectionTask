package com.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.cloudyang.messageupload.SmsListActivity;



public class FTP {
	/**
	 * 服务器地址.
	 */
	private String hostName;

	/**
	 * 端口
	 */
	private int serverPort;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * FTP客户端.
	 */
	private FTPClient ftpClient;

	public FTP() {
		this.hostName = SmsListActivity.IP;
		this.serverPort = 21;
		this.userName = "admin";
		this.password = "1234";
		this.ftpClient = new FTPClient();
	}

	// -------------------------------------------------------鏂囦欢涓婁紶鏂规硶------------------------------------------------

	/**
	 * 涓婁紶鍗曚釜鏂囦欢.
	 * 
	 * @param localFile
	 *            鏈湴鏂囦欢
	 * @param remotePath
	 *            FTP鐩綍
	 * @param listener
	 *            鐩戝惉鍣�
	 * @throws IOException
	 */
	public void uploadSingleFile(File singleFile, String remotePath,
			UploadProgressListener listener) throws IOException {

		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;
		flag = uploadingSingle(singleFile, listener);
		if (flag) {
			listener.onUploadProgress(SmsListActivity.FTP_UPLOAD_SUCCESS, 0,
					singleFile);
		} else {
			listener.onUploadProgress(SmsListActivity.FTP_UPLOAD_FAIL, 0,
					singleFile);
		}

		this.uploadAfterOperate(listener);
	}

	/**
	 * 涓婁紶澶氫釜鏂囦欢.
	 * 
	 * @param localFile
	 *            鏈湴鏂囦欢
	 * @param remotePath
	 *            FTP鐩綍
	 * @param listener
	 *            鐩戝惉鍣�
	 * @throws IOException
	 */
	public void uploadMultiFile(LinkedList<File> fileList, String remotePath,
			UploadProgressListener listener) throws IOException {

		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;

		for (File singleFile : fileList) {
			flag = uploadingSingle(singleFile, listener);
			if (flag) {
				listener.onUploadProgress(SmsListActivity.FTP_UPLOAD_SUCCESS, 0,
						singleFile);
			} else {
				listener.onUploadProgress(SmsListActivity.FTP_UPLOAD_FAIL, 0,
						singleFile);
			}
		}

		// 涓婁紶瀹屾垚涔嬪悗鍏抽棴杩炴帴
		this.uploadAfterOperate(listener);
	}

	/**
	 * 涓婁紶鍗曚釜鏂囦欢.
	 * 
	 * @param localFile
	 *            鏈湴鏂囦欢
	 * @return true涓婁紶鎴愬姛, false涓婁紶澶辫触
	 * @throws IOException
	 */
	private boolean uploadingSingle(File localFile,
			UploadProgressListener listener) throws IOException {
		boolean flag = true;

		BufferedInputStream buffIn = new BufferedInputStream(
				new FileInputStream(localFile));
		ProgressInputStream progressInput = new ProgressInputStream(buffIn,
				listener, localFile);
		
		flag = ftpClient.storeFile(localFile.getName(), progressInput);
		buffIn.close();

		return flag;
	}
	
	/**
	 * 涓婁紶鏂囦欢涔嬪墠鍒濆鍖栫浉鍏冲弬鏁�
	 * 
	 * @param remotePath
	 *            FTP鐩綍
	 * @param listener
	 *            鐩戝惉鍣�
	 * @throws IOException
	 */
	private void uploadBeforeOperate(String remotePath,
			UploadProgressListener listener) throws IOException {

		// 鎵撳紑FTP鏈嶅姟
		try {
			this.openConnect();
			listener.onUploadProgress(SmsListActivity.FTP_CONNECT_SUCCESSS, 0,
					null);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onUploadProgress(SmsListActivity.FTP_CONNECT_FAIL, 0, null);
			return;
		}

		// 璁剧疆妯″紡
		ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
		// FTP涓嬪垱寤烘枃浠跺す
		ftpClient.makeDirectory(remotePath);
		// 鏀瑰彉FTP鐩綍
		ftpClient.changeWorkingDirectory(remotePath);
		// 涓婁紶鍗曚釜鏂囦欢

	}

	/**
	 * 涓婁紶瀹屾垚涔嬪悗鍏抽棴杩炴帴
	 * 
	 * @param listener
	 * @throws IOException
	 */
	private void uploadAfterOperate(UploadProgressListener listener)
			throws IOException {
		this.closeConnect();
		listener.onUploadProgress(SmsListActivity.FTP_DISCONNECT_SUCCESS, 0, null);
	}

	// -------------------------------------------------------鏂囦欢涓嬭浇鏂规硶------------------------------------------------

	/**
	 * 涓嬭浇鍗曚釜鏂囦欢锛屽彲瀹炵幇鏂偣涓嬭浇.
	 * 
	 * @param serverPath
	 *            Ftp鐩綍鍙婃枃浠惰矾寰�
	 * @param localPath
	 *            鏈湴鐩綍
	 * @param fileName       
	 *            涓嬭浇涔嬪悗鐨勬枃浠跺悕绉�
	 * @param listener
	 *            鐩戝惉鍣�
	 * @throws IOException
	 */
	public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
			throws Exception {

		// 鎵撳紑FTP鏈嶅姟
		try {
			this.openConnect();
			listener.onDownLoadProgress(SmsListActivity.FTP_CONNECT_SUCCESSS, 0, null);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onDownLoadProgress(SmsListActivity.FTP_CONNECT_FAIL, 0, null);
			return;
		}

		// 鍏堝垽鏂湇鍔″櫒鏂囦欢鏄惁瀛樺湪
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDownLoadProgress(SmsListActivity.FTP_FILE_NOTEXISTS, 0, null);
			return;
		}

		//鍒涘缓鏈湴鏂囦欢澶�
		File mkFile = new File(localPath);
		if (!mkFile.exists()) {
			mkFile.mkdirs();
		}

		localPath = localPath + fileName;
		// 鎺ョ潃鍒ゆ柇涓嬭浇鐨勬枃浠舵槸鍚﹁兘鏂偣涓嬭浇
		long serverSize = files[0].getSize(); // 鑾峰彇杩滅▼鏂囦欢鐨勯暱搴�
		File localFile = new File(localPath);
		long localSize = 0;
		if (localFile.exists()) {
			localSize = localFile.length(); // 濡傛灉鏈湴鏂囦欢瀛樺湪锛岃幏鍙栨湰鍦版枃浠剁殑闀垮害
			if (localSize >= serverSize) {
				File file = new File(localPath);
				file.delete();
			}
		}
		
		// 杩涘害
		long step = serverSize / 100;
		long process = 0;
		long currentSize = 0;
		// 寮�鍑嗗涓嬭浇鏂囦欢
		OutputStream out = new FileOutputStream(localFile, true);
		ftpClient.setRestartOffset(localSize);
		InputStream input = ftpClient.retrieveFileStream(serverPath);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = input.read(b)) != -1) {
			out.write(b, 0, length);
			currentSize = currentSize + length;
			if (currentSize / step != process) {
				process = currentSize / step;
				if (process % 5 == 0) {  //姣忛殧%5鐨勮繘搴﹁繑鍥炰竴娆�
					listener.onDownLoadProgress(SmsListActivity.FTP_DOWN_LOADING, process, null);
				}
			}
		}
		out.flush();
		out.close();
		input.close();
		
		// 姝ゆ柟娉曟槸鏉ョ‘淇濇祦澶勭悊瀹屾瘯锛屽鏋滄病鏈夋鏂规硶锛屽彲鑳戒細閫犳垚鐜扮▼搴忔鎺�
		if (ftpClient.completePendingCommand()) {
			listener.onDownLoadProgress(SmsListActivity.FTP_DOWN_SUCCESS, 0, new File(localPath));
		} else {
			listener.onDownLoadProgress(SmsListActivity.FTP_DOWN_FAIL, 0, null);
		}

		// 涓嬭浇瀹屾垚涔嬪悗鍏抽棴杩炴帴
		this.closeConnect();
		listener.onDownLoadProgress(SmsListActivity.FTP_DISCONNECT_SUCCESS, 0, null);

		return;
	}

	// -------------------------------------------------------鏂囦欢鍒犻櫎鏂规硶------------------------------------------------

	/**
	 * 鍒犻櫎Ftp涓嬬殑鏂囦欢.
	 * 
	 * @param serverPath
	 *            Ftp鐩綍鍙婃枃浠惰矾寰�
	 * @param listener
	 *            鐩戝惉鍣�
	 * @throws IOException
	 */
	public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener)
			throws Exception {

		// 鎵撳紑FTP鏈嶅姟
		try {
			this.openConnect();
			listener.onDeleteProgress(SmsListActivity.FTP_CONNECT_SUCCESSS);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onDeleteProgress(SmsListActivity.FTP_CONNECT_FAIL);
			return;
		}

		// 鍏堝垽鏂湇鍔″櫒鏂囦欢鏄惁瀛樺湪
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDeleteProgress(SmsListActivity.FTP_FILE_NOTEXISTS);
			return;
		}
		
		//杩涜鍒犻櫎鎿嶄綔
		boolean flag = true;
		flag = ftpClient.deleteFile(serverPath);
		if (flag) {
			listener.onDeleteProgress(SmsListActivity.FTP_DELETEFILE_SUCCESS);
		} else {
			listener.onDeleteProgress(SmsListActivity.FTP_DELETEFILE_FAIL);
		}
		
		// 鍒犻櫎瀹屾垚涔嬪悗鍏抽棴杩炴帴
		this.closeConnect();
		listener.onDeleteProgress(SmsListActivity.FTP_DISCONNECT_SUCCESS);
		
		return;
	}

	// -------------------------------------------------------鎵撳紑鍏抽棴杩炴帴------------------------------------------------

	/**
	 * 鎵撳紑FTP鏈嶅姟.
	 * 
	 * @throws IOException
	 */
	public void openConnect() throws IOException {
		// 涓枃杞爜
		ftpClient.setControlEncoding("UTF-8");
		int reply; // 鏈嶅姟鍣ㄥ搷搴斿�
		// 杩炴帴鑷虫湇鍔″櫒
		ftpClient.connect(hostName, serverPort);
		// 鑾峰彇鍝嶅簲鍊�
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 鏂紑杩炴帴
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		}
		// 鐧诲綍鍒版湇鍔″櫒
		ftpClient.login(userName, password);
		// 鑾峰彇鍝嶅簲鍊�
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 鏂紑杩炴帴
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		} else {
			// 鑾峰彇鐧诲綍淇℃伅
			FTPClientConfig config = new FTPClientConfig(ftpClient
					.getSystemType().split(" ")[0]);
			config.setServerLanguageCode("zh");
			ftpClient.configure(config);
			// 浣跨敤琚姩妯″紡璁句负榛樿
			ftpClient.enterLocalPassiveMode();
			// 浜岃繘鍒舵枃浠舵敮鎸�
			ftpClient
					.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
	}

	/**
	 * 鍏抽棴FTP鏈嶅姟.
	 * 
	 * @throws IOException
	 */
	public void closeConnect() throws IOException {
		if (ftpClient != null) {
			// 閫�嚭FTP
			ftpClient.logout();
			// 鏂紑杩炴帴
			ftpClient.disconnect();
		}
	}

	// ---------------------------------------------------涓婁紶銆佷笅杞姐�鍒犻櫎鐩戝惉---------------------------------------------
	
	/*
	 * 涓婁紶杩涘害鐩戝惉
	 */
	public interface UploadProgressListener {
		public void onUploadProgress(String currentStep, long uploadSize, File file);
	}

	/*
	 * 涓嬭浇杩涘害鐩戝惉
	 */
	public interface DownLoadProgressListener {
		public void onDownLoadProgress(String currentStep, long downProcess, File file);
	}

	/*
	 * 鏂囦欢鍒犻櫎鐩戝惉
	 */
	public interface DeleteFileProgressListener {
		public void onDeleteProgress(String currentStep);
	}

}
