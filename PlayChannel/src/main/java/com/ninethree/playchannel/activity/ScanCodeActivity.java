package com.ninethree.playchannel.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninethree.playchannel.R;
import com.ninethree.playchannel.util.CodeUtil;
import com.ninethree.playchannel.util.DensityUtil;

public class ScanCodeActivity extends BaseActivity {

    private ImageView mBarCode;
    private ImageView mQrCode;
    private TextView mCodeTv;

    private String mCode = "123523267823123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("扫码消费");

        initView();

        //生成条形码
        Bitmap barcode = CodeUtil.creatBarcode(this, mCode, 720, 280, false);
        mBarCode.setImageBitmap(barcode);

        //生成二维码 DensityUtil.getWidth(this)
        Bitmap bitmap = CodeUtil.createQRImage(mCode, 500, 500);
        mQrCode.setImageBitmap(bitmap);

        mCodeTv.setText(mCode);

    }

    private void initView() {
        mQrCode = (ImageView) findViewById(R.id.qr_code);
        mBarCode = (ImageView) findViewById(R.id.bar_code);

        mCodeTv = (TextView) findViewById(R.id.code);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_scan_code);
    }

    @Override
    public void onClick(View v) {

    }
}
