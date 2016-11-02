package com.ninethree.palychannelbusiness.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.util.DensityUtil;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by user on 2016/7/8.
 */
public abstract class BaseWebActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * title布局
     */
    public LinearLayout mTitleLayout;
    /**
     * title栏返回按钮
     */
    public ImageButton mLeftBtn;
    /**
     * title栏右边操作按钮
     */
    public ImageButton mRightBtn;
    /**
     * title文字
     */
    public TextView mTitle;

    /**
     * 正在加载ProgressDialog
     */
    public MyProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();
        // 初始化
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mTitle = (TextView) findViewById(R.id.title_text);
        mLeftBtn = (ImageButton) findViewById(R.id.title_left_btn);
        mRightBtn = (ImageButton) findViewById(R.id.title_right_btn);

        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            // tintManager.setNavigationBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.transparent);
//
//            mTitleLayout.setPadding(0, DensityUtil.getStatusBarHeight(this), 0,
//                    0);
//

        //侧滑返回
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true)// 设置是否可滑动
                .setSwipeEdge(60)// 可滑动的范围。px。200表示为左边200px的屏幕
                // .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                // .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                // .setScrimColor(Color.BLUE)//底层阴影颜色
                .setClosePercent(0.5f)// 触发关闭Activity百分比
                .setSwipeRelateEnable(false)// 是否与下一级activity联动(微信效果)。默认关
                // .setSwipeRelateOffset(500)//activity联动时的偏移量。默认500px。
                .setSwipeSensitivity(1);

        // 设置返回按钮点击监听事件，即点击返回按钮关闭当前Activity
        mLeftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                //closeAnimRight();
            }
        });

        mProgressDialog = new MyProgressDialog(this);
    }

    /**
     * @Title: setLayout
     * @Description: 设置布局文件
     */
    public abstract void setLayout();

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

    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                .show();
    }
}
