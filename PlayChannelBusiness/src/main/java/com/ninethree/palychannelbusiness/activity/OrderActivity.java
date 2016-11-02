package com.ninethree.palychannelbusiness.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.fragment.OrderCardFragment;
import com.ninethree.palychannelbusiness.fragment.OrderPduFragment;
import com.ninethree.palychannelbusiness.fragment.OrderTotalFragment;
import com.ninethree.palychannelbusiness.util.DateUtil;

/**
 * 销售订单
 */
public class OrderActivity extends BaseActivity {

    private TextView mTotalTv;
    private TextView mPduTv;
    private TextView mCardTv;

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;

    private OrderTotalFragment mOrderTotalFragment;
    private OrderPduFragment mOrderPduFragment;
    private OrderCardFragment mOrderCardFragment;

    private String mOrgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("销售订单");

        initView();

        mOrgId = getIntent().getStringExtra("orgId");

        mFragmentManager = getSupportFragmentManager();

        setTab(1);

    }

    private void initView() {
        mTotalTv = (TextView) findViewById(R.id.total);
        mPduTv = (TextView) findViewById(R.id.pdu);
        mCardTv = (TextView) findViewById(R.id.card);

        mTotalTv.setOnClickListener(this);
        mPduTv.setOnClickListener(this);
        mCardTv.setOnClickListener(this);

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_order);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.total:

                setTab(1);

                break;
            case R.id.pdu:

                setTab(2);

                break;
            case R.id.card:

                setTab(3);

                break;
        }
    }

    /**
     *
     * @param position
     */
    public void setTab(int position){
        mTransaction = mFragmentManager.beginTransaction();
        hideFragments(mTransaction);

        mTotalTv.setTextColor(getResources().getColor(R.color.color_32));
        mPduTv.setTextColor(getResources().getColor(R.color.color_32));
        mCardTv.setTextColor(getResources().getColor(R.color.color_32));

        switch (position){
            case 1:
                mTotalTv.setTextColor(getResources().getColor(R.color.color_main));

                if (mOrderTotalFragment == null){
                    mOrderTotalFragment = new OrderTotalFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgId",mOrgId);
                    mOrderTotalFragment.setArguments(bundle);
                    mTransaction.add(R.id.content,mOrderTotalFragment);
                }else{
                    mTransaction.show(mOrderTotalFragment);
                }
                break;
            case 2:
                mPduTv.setTextColor(getResources().getColor(R.color.color_main));

                if (mOrderPduFragment == null){
                    mOrderPduFragment = new OrderPduFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgId",mOrgId);
                    mOrderPduFragment.setArguments(bundle);
                    mTransaction.add(R.id.content,mOrderPduFragment);
                }else{
                    mTransaction.show(mOrderPduFragment);
                }
                break;
            case 3:
                mCardTv.setTextColor(getResources().getColor(R.color.color_main));

                if (mOrderCardFragment == null) {
                    mOrderCardFragment = new OrderCardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("orgId",mOrgId);
                    mOrderCardFragment.setArguments(bundle);
                    mTransaction.add(R.id.content,mOrderCardFragment);
                }else{
                    mTransaction.show(mOrderCardFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    //隐藏所有fragment
    private void hideFragments(FragmentTransaction transaction) {
        if (mOrderTotalFragment != null){
            transaction.hide(mOrderTotalFragment);
        }
        if (mOrderPduFragment != null){
            transaction.hide(mOrderPduFragment);
        }
        if (mOrderCardFragment != null){
            transaction.hide(mOrderCardFragment);
        }

    }

}
