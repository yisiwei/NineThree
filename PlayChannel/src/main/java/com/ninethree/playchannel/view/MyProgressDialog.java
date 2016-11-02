package com.ninethree.playchannel.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.ninethree.playchannel.R;

public class MyProgressDialog {

	public Dialog mDialog;
	
	private AnimationDrawable animationDrawable = null;

	public MyProgressDialog(Context context) {

		View view = View.inflate(context, R.layout.progress_view, null);

		ImageView loadingImage = (ImageView) view
				.findViewById(R.id.progress_view);
		loadingImage.setImageResource(R.drawable.loading_animation);
		animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
		animationDrawable.setOneShot(false);

		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		
		mDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				animationDrawable.stop();
			}
		});
		
		mDialog.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				if (animationDrawable != null) {
					animationDrawable.start();
				}
			}
		});

	}

	/**
	 * 显示对话框
	 */
	public void show() {
		mDialog.show();
	}

	/**
	 * 点击对话框外面是否取消对话框
	 * 
	 * @param cancel
	 */
	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	/**
	 * 取消对话框
	 */
	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	/**
	 * 是否已经显示
	 * 
	 * @return
	 */
	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}
}
