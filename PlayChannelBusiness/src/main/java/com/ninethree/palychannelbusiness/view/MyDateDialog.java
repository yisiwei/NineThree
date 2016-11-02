package com.ninethree.palychannelbusiness.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.util.DensityUtil;

import java.lang.reflect.Field;
import java.util.Calendar;


/**
 * @ClassName: MyDateDialog
 * @Description:选择日期
 */
public class MyDateDialog {

    private static String date;

    public interface OnDateConfirmListener {
        public void onConfirmClick(String date);
    }

    /**
     * @param activity
     * @param confirmListener
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity,
                            final OnDateConfirmListener confirmListener) {
        date = null;
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog_date, null);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 4 * 3,
                LayoutParams.WRAP_CONTENT);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, monthOfYear, dayOfMonth,
                new OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(year);
                        int newMonth = monthOfYear + 1;
                        if (newMonth < 10) {
                            stringBuilder.append("-0").append(newMonth);
                        } else {
                            stringBuilder.append("-").append(newMonth);
                        }
                        if (dayOfMonth < 10) {
                            stringBuilder.append("-0").append(dayOfMonth);
                        } else {
                            stringBuilder.append("-").append(dayOfMonth);
                        }
                        date = stringBuilder.toString();
                    }
                });

        // 确定
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (date != null) {
                    confirmListener.onConfirmClick(date);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(year);
                    int newMonth = monthOfYear + 1;
                    if (newMonth < 10) {
                        stringBuilder.append("-0").append(newMonth);
                    } else {
                        stringBuilder.append("-").append(newMonth);
                    }
                    if (dayOfMonth < 10) {
                        stringBuilder.append("-0").append(dayOfMonth);
                    } else {
                        stringBuilder.append("-").append(dayOfMonth);
                    }
                    confirmListener.onConfirmClick(stringBuilder.toString());
                }

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
     * @param confirmListener
     * @throws
     * @Title: show
     * @Description: 显示Dialog
     */
    public static void show(Activity activity,long maxDate,
                            final OnDateConfirmListener confirmListener) {
        date = null;
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog_date, null);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        setDatePickerDividerColor(activity,datePicker);

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(DensityUtil.getWidth(activity) / 4 * 3,
                LayoutParams.WRAP_CONTENT);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.setMaxDate(maxDate);//设置当前时间为最大时间

        datePicker.init(year, monthOfYear, dayOfMonth,
                new OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(year);
                        int newMonth = monthOfYear + 1;
                        if (newMonth < 10) {
                            stringBuilder.append("-0").append(newMonth);
                        } else {
                            stringBuilder.append("-").append(newMonth);
                        }
                        if (dayOfMonth < 10) {
                            stringBuilder.append("-0").append(dayOfMonth);
                        } else {
                            stringBuilder.append("-").append(dayOfMonth);
                        }
                        date = stringBuilder.toString();
                    }
                });

        // 确定
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (date != null) {
                    confirmListener.onConfirmClick(date);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(year);
                    int newMonth = monthOfYear + 1;
                    if (newMonth < 10) {
                        stringBuilder.append("-0").append(newMonth);
                    } else {
                        stringBuilder.append("-").append(newMonth);
                    }
                    if (dayOfMonth < 10) {
                        stringBuilder.append("-0").append(dayOfMonth);
                    } else {
                        stringBuilder.append("-").append(dayOfMonth);
                    }
                    confirmListener.onConfirmClick(stringBuilder.toString());
                }

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

    private static void setDatePickerDividerColor(Context context,DatePicker datePicker) {

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(context.getResources()
                                .getColor(R.color.color_main)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

}
