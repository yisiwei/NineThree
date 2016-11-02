package com.ninethree.palychannelbusiness.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.util.DensityUtil;
import com.ninethree.palychannelbusiness.util.StringUtil;


/**
 * @ClassName: MyDialog
 * @Description:一般用于操作确认的提示框，如：删除操作时提示用户确认删除
 */
public class MyDialog {

    public interface OnConfirmListener {
        public void onConfirmClick();
    }

    /**
     * @param activity
     * @param content         提示内容
     * @param rightText       右边按钮文字
     * @param confirmListener void
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity, String content, String rightText,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        if (!StringUtil.isNullOrEmpty(content)) {
            text.setText(content);
        }

        if (!StringUtil.isNullOrEmpty(rightText)) {
            confirm.setText(rightText);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 3 * 2,
                LayoutParams.WRAP_CONTENT);

        // 确定
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param activity
     * @param content         提示内容
     * @param confirmListener void
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity, String content,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        if (!StringUtil.isNullOrEmpty(content)) {
            text.setText(content);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 3 * 2,
                LayoutParams.WRAP_CONTENT);

        // 确定
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param activity
     * @param confirmListener void
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity, final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 3 * 2,
                LayoutParams.WRAP_CONTENT);

        // 确定
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
