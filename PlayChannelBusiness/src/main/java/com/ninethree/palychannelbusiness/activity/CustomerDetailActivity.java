package com.ninethree.palychannelbusiness.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.CustomerRecordAdapter;
import com.ninethree.palychannelbusiness.bean.CustomerRecord;
import com.ninethree.palychannelbusiness.bean.MyCustomer;
import com.ninethree.palychannelbusiness.bean.RecordPdu;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.view.CircleImageView;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户详情
 */
public class CustomerDetailActivity extends BaseActivity implements AbsListView.OnScrollListener {

    private CircleImageView mHeadImg;
    private TextView mNickNameTv;
    private TextView mPhoneTv;
    private TextView mTotalPriceTv;

    private ListView mListView;
    private CustomerRecordAdapter mAdapter;
    private List<CustomerRecord> mDatas;

    private String mOrgId;
    private MyCustomer mCustomer;

    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private int mCount;
    private int mPageIndex = 1;
    private int mPageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("客户详情");

        initView();

        mCustomer = (MyCustomer) getIntent().getSerializableExtra("customer");
        mOrgId = getIntent().getStringExtra("orgId");

        mDatas = new ArrayList<CustomerRecord>();

        Glide.with(this)
                .load(mCustomer.getPhoto())
                .centerCrop()
                //.crossFade()
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(mHeadImg);

        mNickNameTv.setText(mCustomer.getNickName());
        if (!StringUtil.isNullOrEmpty(mCustomer.getMobile())) {
            mPhoneTv.setText(StringUtil.replacePhone(mCustomer.getMobile()));
        } else {
            mPhoneTv.setText("无");
        }
        mTotalPriceTv.setText("累计消费："+mCustomer.getCash() + "元");

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) < mCount) {
                    queryDatas();
                } else {
                    mTextView.setText("没有更多数据了");
                }
            }
        });
        mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);

        mListView.addFooterView(mFooter);
        mListView.setOnScrollListener(this);

        queryDatas();
    }

    private void queryDatas() {
        new QueryTask().execute(mCustomer.getSeftOrgID(), mOrgId);
    }

    private void initView() {
        mHeadImg = (CircleImageView) findViewById(R.id.headImg);
        mNickNameTv = (TextView) findViewById(R.id.nickname);
        mPhoneTv = (TextView) findViewById(R.id.phone);
        mTotalPriceTv = (TextView) findViewById(R.id.total_price);

        mListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_customer_detail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候

                // 在这里判断是否到达底部，如果到达就自动加载数据
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
                    Log.i("mPageIndex:" + mPageIndex);
                    if (mPageSize * (mPageIndex - 1) < mCount) {
                        queryDatas();
                    } else {
                        mTextView.setText("没有更多数据了");
                    }
                }

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 滚动条正在滚动

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动

                break;

            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    /**
     * @BuyOrgId bigint,
     * @SaleOrgId BIGINT,
     * @PageSize INT,
     * @PageIndex int
     */
    private class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPageIndex == 1) {
                mProgressDialog.show();
            } else {
                mBar.setVisibility(ProgressBar.VISIBLE);
                mTextView.setText("正在加载");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("BuyOrgId", params[0]);
            map.put("SaleOrgId", params[1]);
            map.put("PageSize", mPageSize + "");
            map.put("PageIndex", mPageIndex + "");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Unite.Pay.Pass", "User_Query_Customer_ByBuyOrgId", map);
            SoapObject result = DBPubService.getPubDB(getApplicationContext(),
                    param);
            String code = null;
            try {
                code = result.getProperty(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            mBar.setVisibility(ProgressBar.INVISIBLE);
            mTextView.setText("加载更多");
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getApplicationContext(), result);
                if (dataStr != null) {
                    success(dataStr);
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            //查询总数
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            mCount = tableArray.optJSONObject(0).optInt("RowsCount");
            Log.i("Count:" + mCount);

            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            TypeToken<List<CustomerRecord>> typeToken1 = new TypeToken<List<CustomerRecord>>() {
            };

            List<CustomerRecord> records = MyApp.getGson().fromJson(tableArray1.toString(),
                    typeToken1.getType());

            List<CustomerRecord> records1 = new ArrayList<CustomerRecord>();
            int dataId = -1;
            for (int i = 0;i < records.size();i++){
                CustomerRecord record = records.get(i);
                if (dataId == record.getDataId()){
                    CustomerRecord record1 = records1.get(records1.size()-1);
                    RecordPdu pdu = new RecordPdu();
                    pdu.setPduName(record.getPduName());
                    pdu.setPduLogoUrl(record.getPduLogoUrl());
                    pdu.setTradeNum(record.getTradeNum());
                    pdu.setCardModelText(record.getCardModelText());
                    pdu.setImgUrl(record.getImgUrl());
                    pdu.setCardTradeNum(record.getCardTradeNum());
                    List<RecordPdu> pdus = record1.getPdus();
                    pdus.add(pdu);
                    record1.setPdus(pdus);
                }else{
                    List<RecordPdu> pdus = new ArrayList<RecordPdu>();
                    RecordPdu pdu = new RecordPdu();
                    pdu.setPduName(record.getPduName());
                    pdu.setPduLogoUrl(record.getPduLogoUrl());
                    pdu.setTradeNum(record.getTradeNum());
                    pdu.setCardModelText(record.getCardModelText());
                    pdu.setImgUrl(record.getImgUrl());
                    pdu.setCardTradeNum(record.getCardTradeNum());
                    pdus.add(pdu);
                    record.setPdus(pdus);
                    records1.add(record);
                }
                dataId = record.getDataId();
            }
            Log.i("转化后的json:"+MyApp.getGson().toJson(records1));

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(records1);
                mAdapter = new CustomerRecordAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
            } else {
                Log.i("加载数据成功,mPageIndex=" + mPageIndex);
                mDatas.addAll(records1);
                mAdapter.notifyDataSetChanged();
            }
            mPageIndex++;

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

}
