package com.ninethree.palychannelbusiness.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

import com.ninethree.palychannelbusiness.util.Constants;
import com.ninethree.palychannelbusiness.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadService extends IntentService {

	private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
	private NotificationManager mNotifyManager;
	private Builder mBuilder;

	public DownLoadService() {
		super("DownLoadService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("DownLoadService onCreate");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);

		String appName = getString(getApplicationInfo().labelRes);
		int icon = getApplicationInfo().icon;

		mBuilder.setContentTitle(appName).setSmallIcon(icon);

		String urlStr = intent.getStringExtra("downloadUrl");
		Log.i("url:"+urlStr);
		InputStream in=null;
		FileOutputStream out = null;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(10 * 1000);
			urlConnection.setReadTimeout(10 * 1000);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			urlConnection.connect();
			long bytetotal = urlConnection.getContentLength();
			long bytesum = 0;
			int byteread = 0;
			in = urlConnection.getInputStream();
			File dir = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				dir = new File(Constants.UPGRADE_DOWNLOAD_PATH);
			}else{
				Toast.makeText(getApplicationContext(), "检测不到SD卡无法下载更新",
						Toast.LENGTH_LONG).show();
				return;
			}
			File apkFile = new File(dir, Constants.APK_NAME);
			out = new FileOutputStream(apkFile);
			byte[] buffer = new byte[BUFFER_SIZE];

			int oldProgress = 0;

			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread);

				int progress = (int) (bytesum * 100L / bytetotal);
				// 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
				if (progress != oldProgress) {
					updateProgress(progress);
				}
				oldProgress = progress;
			}
			// 下载完成
			mBuilder.setContentText("下载完成，点击安装").setProgress(0, 0, false);

			mNotifyManager.cancel(0);//取消notification

			install(apkFile);

		} catch (Exception e) {
			Log.e("download apk file error", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateProgress(int progress) {
		//"正在下载:" + progress + "%"
		mBuilder.setContentText("正在下载："+progress+"%").setProgress(100, progress, false);
		//setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
		mBuilder.setContentIntent(pendingintent);
		mNotifyManager.notify(0, mBuilder.build());
	}

	/**
	 * 安装APK文件
	 *
	 * @param apkFile
	 */
	private void install(File apkFile) {
		Log.i("安装");
		Uri uri = Uri.fromFile(apkFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("DownLoadService onDestroy");
	}
}
