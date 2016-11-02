package com.ninethree.playchannel.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.playchannel.MyApp;
import com.ninethree.playchannel.R;
import com.ninethree.playchannel.adapter.RecordAdapter;
import com.ninethree.playchannel.bean.Record;
import com.ninethree.playchannel.bean.User;
import com.ninethree.playchannel.util.AppUtil;
import com.ninethree.playchannel.util.Log;
import com.ninethree.playchannel.util.StringUtil;
import com.ninethree.playchannel.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener {

    private ListView mListView;
    private List<Record> mDatas;
    private RecordAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private User mUser;

    private int mCount;

    private int mPageSize = 10;
    private int mPageIndex = 1;

    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private boolean mIsRefresh;
    private Record mSelectRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("游玩记录");

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

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex-1) <= mCount) {
                    new RecordTask().execute(mUser.getSeftOrgID(),mUser.getSeftOrgGuid());
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectRecord = mDatas.get(position);
                if (mSelectRecord.getScanTypeId() == 4000 && mSelectRecord.getScanMethodTypeId() == 6000){
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                    intent.putExtra("url", "http://shop.93966.net/H5User/Pdu/UseDetail?id="+mSelectRecord.getScanHistoryId());
                    startActivity(intent);
                }
//                if (!StringUtil.isNullOrEmpty(mDatas.get(position).getPduId())){
//                    new CheangeStateTask().execute(mSelectRecord.getScanHistoryId(),mUser.getSeftOrgID());
//                }
            }
        });

        mDatas = new ArrayList<Record>();

        mUser = (User) getIntent().getSerializableExtra("user");

        new RecordTask().execute(mUser.getSeftOrgID(),mUser.getSeftOrgGuid());

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_my_record);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 滚动条停止的时候
                // 在这里判断是否到达底部，如果到达就自动加载数据
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    // 先获取能看到的最下边的那个条目的位置,看是否等于list所有条目数
                    Log.i("mPageIndex:"+mPageIndex);
                    if (mPageSize * (mPageIndex-1) <= mCount) {
                        new RecordTask().execute(mUser.getSeftOrgID(),mUser.getSeftOrgGuid());
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
        new RecordTask().execute(mUser.getSeftOrgID(),mUser.getSeftOrgGuid());
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
            map.put("BuyOrgId", params[0]);
            map.put("BuyOrgGuid", params[1]);
            map.put("PageSize", ""+mPageSize);
            map.put("PageIndex", ""+mPageIndex);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "ScanHistory_Query_List_ByBuyOrgId", map);
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
            mSwipeRefreshLayout.setRefreshing(false);
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

            JSONArray tableArray = jsonObject.optJSONArray("Table");
            mCount = tableArray.optJSONObject(0).optInt("RowsCount");
            Log.i("Count:" + mCount);

            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            TypeToken<List<Record>> typeToken = new TypeToken<List<Record>>() {
            };

            List<Record> records = MyApp.getGson().fromJson(tableArray1.toString(),
                    typeToken.getType());

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(records);
                if (mDatas != null && mDatas.size() > 0) {
                    mAdapter = new RecordAdapter(this, mDatas);
                    mListView.setAdapter(mAdapter);
                }
            }else{
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

    private class CheangeStateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ScanHistoryId", params[0]);
            map.put("BuyOrgId", params[1]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "ScanHistory_Edit_IsSelect_ByScanHistoryId", map);
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
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getApplicationContext(), result);
                if (dataStr != null) {
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                    intent.putExtra("url", "http://shop.93966.net/H5User/Pdu/UseDetail?id="+mSelectRecord.getScanHistoryId());
                    startActivity(intent);
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

}
