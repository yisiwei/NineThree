package com.ninethree.palychannelbusiness.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.RecordAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Product;
import com.ninethree.palychannelbusiness.bean.Record;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.MyDateDialog;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务记录
 */
public class MyRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private Button mDayBtn;
    private Button mWeekBtn;
    private Button mMonthBtn;
    private Button mYearBtn;

    private LinearLayout mMenuLayout;
    private Button mBeginDateBtn;
    private Button mEndDateBtn;
    private TextView mPduNameBtn;
    private Button mQueryBtn;

    private ListView mListView;
    private List<Record> mDatas;
    private RecordAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private User mUser;
    private Org mOrg;

    private int mCount;

    private int mPageSize = 10;
    private int mPageIndex = 1;

    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private boolean mIsRefresh;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("服务记录");

        initView();

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) <= mCount) {
                    query();
                } else {
                    mTextView.setText("没有更多数据了");
                }
            }
        });
        mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);

        mListView.addFooterView(mFooter);
        mListView.setOnScrollListener(this);

        mDatas = new ArrayList<Record>();

        mUser = (User) getIntent().getSerializableExtra("user");
        mOrg = (Org) getIntent().getSerializableExtra("org");

        mBeginDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));
        mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

        query();
    }

    private void initView() {
        mDayBtn = (Button) findViewById(R.id.day);
        mWeekBtn = (Button) findViewById(R.id.week);
        mMonthBtn = (Button) findViewById(R.id.month);
        mYearBtn = (Button) findViewById(R.id.year);

        mMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);

        mBeginDateBtn = (Button) findViewById(R.id.beginDate);
        mBeginDateBtn.setOnClickListener(this);
        mEndDateBtn = (Button) findViewById(R.id.endDate);
        mEndDateBtn.setOnClickListener(this);

        mQueryBtn = (Button) findViewById(R.id.query);
        mQueryBtn.setOnClickListener(this);

        mPduNameBtn = (TextView) findViewById(R.id.pduName);
        mPduNameBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this
                .findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout
                .setProgressBackgroundColorSchemeResource(R.color.color_fff);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mDayBtn.setOnClickListener(this);
        mWeekBtn.setOnClickListener(this);
        mMonthBtn.setOnClickListener(this);
        mYearBtn.setOnClickListener(this);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_my_record);
    }

    private void clearBtnColor(){
        mDayBtn.setTextColor(getResources().getColor(R.color.color_64));
        mWeekBtn.setTextColor(getResources().getColor(R.color.color_64));
        mMonthBtn.setTextColor(getResources().getColor(R.color.color_64));
        mYearBtn.setTextColor(getResources().getColor(R.color.color_64));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.day:
                clearBtnColor();
                mDayBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;

                query();
                break;
            case R.id.week:
                clearBtnColor();
                mWeekBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getWeekFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;

                query();
                break;
            case R.id.month:
                clearBtnColor();
                mMonthBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getMonthFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;

                query();

                break;
            case R.id.year:
                clearBtnColor();
                mYearBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getYearFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;

                query();

                break;
            case R.id.beginDate:
                MyDateDialog.show(this,new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String endDate = mEndDateBtn.getText().toString();
                        if (DateUtil.compareDate(date, endDate) > 0){
                            toast("开始日期不能大于结束日期");
                            return;
                        }
                        mBeginDateBtn.setText(date);
                    }
                });
                break;
            case R.id.endDate:
                MyDateDialog.show(this,new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String beginDate = mBeginDateBtn.getText().toString();
                        if (DateUtil.compareDate(beginDate, date) > 0){
                            toast("结束日期不能小于开始日期");
                            return;
                        }
                        mEndDateBtn.setText(date);
                    }
                });
                break;
            case R.id.pduName:
                Intent intent = new Intent(this, ProductActivity.class);
                intent.putExtra("org", mOrg);
                startActivityForResult(intent, 1000);
                break;
            case R.id.query://查询

                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;

                query();

                break;
        }
    }

    private void query() {

        String beginDate = mBeginDateBtn.getText().toString();
        String endDate = mEndDateBtn.getText().toString();
        String pduId = null;
        if (mProduct != null) {
            pduId = String.valueOf(mProduct.getPduId());
        } else {
            pduId = "0";
        }
        new RecordTask().execute(pduId, mOrg.getOrgId(), beginDate, endDate);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候

                // 在这里判断是否到达底部，如果到达就自动加载数据
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
                    Log.i("mPageIndex:" + mPageIndex);
                    if (mPageSize * (mPageIndex - 1) <= mCount) {
                        query();
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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    @Override
    public void onRefresh() {
        mTextView.setText("加载更多");
        mPageIndex = 1;
        mIsRefresh = true;
        query();
    }

    private class RecordTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPageIndex == 1) {
                if (!mIsRefresh) {
                    mProgressDialog.show();
                }
            } else {
                mBar.setVisibility(ProgressBar.VISIBLE);
                mTextView.setText("正在加载");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("PduId", params[0]);
            map.put("SaleOrgId", params[1]);
            map.put("BeginDate", params[2]);
            map.put("EndDate", params[3]);
            map.put("PageSize", "" + mPageSize);
            map.put("PageIndex", "" + mPageIndex);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "ScanHistory_Query_BySaleOrgId", map);
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
            mSwipeRefreshLayout.setRefreshing(false);
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

            JSONArray tableArray = jsonObject.optJSONArray("Table");
            mCount = tableArray.optJSONObject(0).optInt("Column1");
            Log.i("Count:" + mCount);

            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            TypeToken<List<Record>> typeToken = new TypeToken<List<Record>>() {
            };

            List<Record> records = MyApp.getGson().fromJson(tableArray1.toString(),
                    typeToken.getType());

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(records);
                //if (mDatas != null && mDatas.size() > 0) {
                mAdapter = new RecordAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
                //}
            } else {
                Log.i("加载数据成功,mPageIndex=" + mPageIndex);
                mDatas.addAll(records);
                mAdapter.notifyDataSetChanged();
            }
            mPageIndex++;

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                mProduct = (Product) data.getSerializableExtra("product");
                mPduNameBtn.setText(mProduct.getPduName());
            }
        }
    }
}
