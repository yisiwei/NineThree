package com.ninethree.palychannelbusiness.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.MyCustomerAdapter;
import com.ninethree.palychannelbusiness.bean.MyCustomer;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.StringUtil;
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
 * 我的客户
 */
public class MyCustomerActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private TextView mDayCountTv;

    private TextView mCountTv;
    private EditText mSearchEt;
    private Button mQueryBtn;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private MyCustomerAdapter mAdapter;
    private List<MyCustomer> mDatas;

    private boolean mIsRefresh;

    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private int mCount;
    private int mPageIndex = 1;
    private int mPageSize = 10;

    private String mOrgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle.setText("我的客户");

        initView();

        mOrgId = getIntent().getStringExtra("orgId");

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) < mCount) {
                    queryUsers();
                } else {
                    mTextView.setText("没有更多数据了");
                }
            }
        });
        mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);

        mListView.addFooterView(mFooter);
        mListView.setOnScrollListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),CustomerDetailActivity.class);
                intent.putExtra("customer",mDatas.get(i));
                intent.putExtra("orgId",mOrgId);
                startActivity(intent);
            }
        });

        mDatas = new ArrayList<MyCustomer>();

        new QueryCountTask().execute(mOrgId);
        queryUsers();

    }

    private void initView() {
        mDayCountTv = (TextView) findViewById(R.id.day_count);
        mCountTv = (TextView) findViewById(R.id.count);
        mSearchEt = (EditText) findViewById(R.id.search_text);
        mQueryBtn = (Button) findViewById(R.id.btn_query);
        mQueryBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout
                .setProgressBackgroundColorSchemeResource(R.color.color_fff);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_my_customer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query://查询
                mIsRefresh = false;
                mTextView.setText("加载更多");
                mPageIndex = 1;
                queryUsers();
                break;
        }
    }

    /**
     * @SaleOrgId bigint,
     * @txtSearch varchar(1000),
     * @PageIndex int,
     * @PageSize int
     */
    private void queryUsers() {
        String search = mSearchEt.getText().toString();
        if (StringUtil.isNullOrEmpty(search)) {
            search = "";
        }
        new QueryUserTask().execute(mOrgId, search);
    }

    @Override
    public void onRefresh() {
        mTextView.setText("加载更多");
        mPageIndex = 1;
        mIsRefresh = true;
        queryUsers();
        new QueryCountTask().execute(mOrgId);
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
                        queryUsers();
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

    private class QueryUserTask extends AsyncTask<String, Void, String> {

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
            map.put("SaleOrgId", params[0]);
            map.put("txtSearch", params[1]);
            map.put("PageIndex", mPageIndex + "");
            map.put("PageSize", mPageSize + "");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Unite.Pay.Pass", "User_Query_Customer_BySaleOrgId", map);
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

            //查询总数
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            mCount = tableArray.optJSONObject(0).optInt("RowsCount");
            Log.i("Count:" + mCount);

            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            TypeToken<List<MyCustomer>> typeToken1 = new TypeToken<List<MyCustomer>>() {
            };

            List<MyCustomer> customers = MyApp.getGson().fromJson(tableArray1.toString(),
                    typeToken1.getType());

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(customers);
                mAdapter = new MyCustomerAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
            } else {
                Log.i("加载数据成功,mPageIndex=" + mPageIndex);
                mDatas.addAll(customers);
                mAdapter.notifyDataSetChanged();
            }
            mPageIndex++;

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    private class QueryCountTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Unite.Pay.Pass", "User_Query_Customer_Count_BySaleOrgId", map);
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
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getApplicationContext(), result);
                if (dataStr != null) {
                    success1(dataStr);
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success1(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            //总数
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            int totalCount = tableArray.optJSONObject(0).optInt("TotalCount");
            Log.i("totalCount:" + totalCount);
            mCountTv.setText(totalCount + "");

            //今日总数
            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            int dayAddCount = tableArray1.optJSONObject(0).optInt("DayAddCount");
            Log.i("dayAddCount:" + dayAddCount);
            mDayCountTv.setText(dayAddCount + "");

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

}
