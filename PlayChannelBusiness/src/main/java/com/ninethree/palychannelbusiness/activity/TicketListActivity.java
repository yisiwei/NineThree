package com.ninethree.palychannelbusiness.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.TicketAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Ticket;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketListActivity extends BaseActivity {

    private ListView mListView;
    private TicketAdapter mAdapter;
    private List<Ticket> mDatas;

    private TextView mEmptyTv;

    private Org mOrg;
    private User mUser;
    private String mMac;

    private Ticket mTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle.setText("绑定售票机");

        mEmptyTv = (TextView) findViewById(R.id.empty);
        mListView = (ListView) findViewById(R.id.listView);

        mOrg = (Org) getIntent().getSerializableExtra("org");
        mUser = (User) getIntent().getSerializableExtra("user");
        mMac = getIntent().getStringExtra("mac");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTicket = mDatas.get(position);
                new BindTask().execute(mOrg.getOrgId(), mOrg.getOrgGuid(), mTicket.getTicketMacId(), mTicket.getTicketMacGuid(),mMac, mUser.getUserID());
            }
        });

        new QueryTask().execute(mOrg.getOrgId(), mOrg.getOrgGuid());

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_ticket_list);
    }

    @Override
    public void onClick(View v) {

    }

    private class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        /**
         * @PduId bigint,
         * @PduGuid uniqueidentifier,
         * @UserId bigint,
         * @UserGuid uniqueidentifier
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            map.put("SaleOrgGuid", params[1]);
            map.put("StateId", "0");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "TicketMac_Query_ByStateId", map);
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

            TypeToken<List<Ticket>> typeToken1 = new TypeToken<List<Ticket>>() {
            };

            mDatas = MyApp.getGson().fromJson(tableArray.toString(),
                    typeToken1.getType());

            if (mDatas != null && mDatas.size() > 0) {
                mAdapter = new TicketAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
            } else {
                mEmptyTv.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    private class BindTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        /**
         * @PduId bigint,
         * @PduGuid uniqueidentifier,
         * @UserId bigint,
         * @UserGuid uniqueidentifier
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            map.put("SaleOrgGuid", params[1]);
            map.put("TicketMacId", params[2]);
            map.put("TicketMacGuid", params[3]);
            map.put("TicketMacFlag", params[4]);
            map.put("UserId", params[5]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "TicketMac_Bind", map);
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

            JSONArray tableArray = jsonObject.optJSONArray("Table");
            int retNum = tableArray.optJSONObject(0).getInt("RetNum");
            String retMsg = tableArray.optJSONObject(0).getString("RetMsg");

            if (retNum == 0) {
                toast("绑定成功");
                finish();
            } else {
                toast(retMsg);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

}
