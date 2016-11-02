package com.ninethree.palychannelbusiness.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseNoTitleActivity extends AppCompatActivity implements OnClickListener {

	public MyProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mProgressDialog = new MyProgressDialog(this);

		//侧滑返回
		SwipeBackHelper.onCreate(this);
		SwipeBackHelper.getCurrentPage(this)//获取当前页面
				.setSwipeBackEnable(true)//设置是否可滑动
				.setSwipeEdge(100)//可滑动的范围。px。200表示为左边200px的屏幕
				//.setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
				.setSwipeSensitivity(1)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
				//.setScrimColor(Color.BLUE)//底层阴影颜色
				.setClosePercent(0.6f);//触发关闭Activity百分比
				//.setSwipeRelateEnable(false)//是否与下一级activity联动(微信效果)。默认关
				//.setSwipeRelateOffset(500)//activity联动时的偏移量。默认500px。
				//.setDisallowInterceptTouchEvent(true);//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			// tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.transparent);
		}

	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		SwipeBackHelper.onPostCreate(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SwipeBackHelper.onDestroy(this);
	}

	protected void toast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}
}
