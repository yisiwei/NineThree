package com.ninethree.palychannelbusiness.activity.promotion;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseNoTitleActivity;
import com.ninethree.palychannelbusiness.fragment.PromotionGetFragment;
import com.ninethree.palychannelbusiness.fragment.PromotionUseFragment;
import com.ninethree.palychannelbusiness.util.DensityUtil;
import com.ninethree.palychannelbusiness.util.Log;

public class PromotionUseActivity extends BaseNoTitleActivity {

    private LinearLayout mTitleLayout;
    private ImageButton mLeftBtn;
    private RadioGroup mRadioGroup;

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;

    private PromotionUseFragment mPromotionUseFragment;
    private PromotionGetFragment mPromotionGetFragment;

    private String mPromotionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_promotion_use);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mLeftBtn = (ImageButton) findViewById(R.id.title_left_btn);
        mLeftBtn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mTitleLayout.setPadding(0, DensityUtil.getStatusBarHeight(this), 0,
                    0);
        }

        mPromotionId = getIntent().getStringExtra("promotionId");
        Log.i("promotionId:" + mPromotionId);

        mFragmentManager = getSupportFragmentManager();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.use:
                        setTab(1);
                        break;
                    case R.id.get:
                        setTab(2);
                        break;
                }
            }
        });

        setTab(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_btn://返回
                finish();
                break;
        }
    }

    public void setTab(int position) {
        mTransaction = mFragmentManager.beginTransaction();
        hideFragments(mTransaction);

        switch (position) {
            case 1:

                if (mPromotionUseFragment == null) {
                    mPromotionUseFragment = new PromotionUseFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("promotionId",mPromotionId);
                    mPromotionUseFragment.setArguments(bundle);

                    mTransaction.add(R.id.content, mPromotionUseFragment);
                } else {
                    mTransaction.show(mPromotionUseFragment);
                }
                break;
            case 2:

                if (mPromotionGetFragment == null) {
                    mPromotionGetFragment = new PromotionGetFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("promotionId",mPromotionId);
                    mPromotionGetFragment.setArguments(bundle);

                    mTransaction.add(R.id.content, mPromotionGetFragment);
                } else {
                    mTransaction.show(mPromotionGetFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    //隐藏所有fragment
    private void hideFragments(FragmentTransaction transaction) {
        if (mPromotionUseFragment != null) {
            transaction.hide(mPromotionUseFragment);
        }
        if (mPromotionGetFragment != null) {
            transaction.hide(mPromotionGetFragment);
        }
    }
}
