package com.ninethree.palychannelbusiness.activity.Terminal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.activity.Terminal.TerminalAddActivity;
import com.ninethree.palychannelbusiness.adapter.PduBindAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Product;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PduBindListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private List<Product> mDatas;
    private PduBindAdapter mAdapter;

    private boolean mIsRefresh;

    private Org mOrg;
    private User mUser;

    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle.setText("选择产品");

        initView();

        initEvents();

        mOrg = (Org) getIntent().getSerializableExtra("org");
        mUser = (User) getIntent().getSerializableExtra("user");
        mFrom = getIntent().getStringExtra("from");

        if (mOrg != null){
            queryProducts();
        }

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_product);
    }

    /**
     * @Title: initView
     * @Description: 初始化View
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout
                .setProgressBackgroundColorSchemeResource(R.color.color_fff);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * @Title: initEvents
     * @Description: 初始化事件
     */
    private void initEvents() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mFrom != null){
                    Intent data = new Intent();
                    data.putExtra("product",mDatas.get(position));
                    setResult(RESULT_OK,data);
                    finish();
                }else {
                    Log.i("选择了："+position+"--"+mDatas.get(position).getPduId());
                    Intent intent = new Intent(getApplicationContext(),TerminalAddActivity.class);
                    intent.putExtra("user",mUser);
                    intent.putExtra("org",mOrg);
                    intent.putExtra("product",mDatas.get(position));
                    startActivity(intent);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * @Title: queryProducts
     * @Description: 查询商品列表
     */
    private void queryProducts() {
        new ProductTask().execute(mOrg.getOrgId(),
                mOrg.getOrgGuid());
    }

    /**
     * @ClassName: ProductTask
     * @Description: 查询商品列表请求
     */
    private class ProductTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mIsRefresh) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("OrgId", params[0]);
            map.put("OrgGuid", params[1]);
            map.put("PageSize", "100");
            map.put("PageIndex", "1");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Goods", "Pdu_Query_IsBindTerminal_ByOrgId", map);
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
            if (!mIsRefresh) {
                mProgressDialog.dismiss();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }
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

            JSONArray tableArray = jsonObject.optJSONArray("Table1");
            TypeToken<List<Product>> typeToken = new TypeToken<List<Product>>() {
            };

            mDatas = MyApp.getGson().fromJson(tableArray.toString(),
                    typeToken.getType());

            if (mDatas != null && mDatas.size() > 0) {
                mAdapter = new PduBindAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onRefresh() {
        mIsRefresh = true;
        queryProducts();
    }
}
