package com.ninethree.palychannelbusiness.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.OrgListAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.SessionInfo;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.PreferencesUtil;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgListActivity extends BaseActivity {

    private ListView mListView;
    private List<Org> mDatas;
    private OrgListAdapter mAdapter;

    private User mUser;
    private Org mOrg;
    private String mMac;

    private Org mChooseOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("选择游乐场");

        mListView = (ListView) findViewById(R.id.listView);

        mUser = (User) getIntent().getSerializableExtra("user");
        mOrg = (Org) getIntent().getSerializableExtra("org");
        mMac = getIntent().getStringExtra("mac");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChooseOrg = mDatas.get(position);
                Intent intent = new Intent(getApplicationContext(),TicketListActivity.class);
                intent.putExtra("user",mUser);
                intent.putExtra("org",mOrg);
                intent.putExtra("mac",mMac);
                startActivity(intent);
                finish();
            }
        });

        new OrgTask().execute(mUser.getUserID());
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_org_list);
    }

    @Override
    public void onClick(View v) {

    }

    private class OrgTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("UserId", params[0]);
            map.put("ChannelId", "2");
            String param = DBPubService.pubDbParamPack(getApplicationContext(), 1,
                    "Bm.HR", "OrgList_Query_ByUser", map);
            SoapObject result = DBPubService.getPubDB(getApplicationContext(), param);
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
                String dataStr = DBPubService.ascPackDown(getApplicationContext(),
                        result);
                if (dataStr != null) {
                    success(dataStr);
                }

            } else {
                toast("连接失败，请检查您的网络");
            }
        }
    }

    /**
     * @Title: success
     * @Description: 请求成功处理
     * @param str
     */
    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            TypeToken<List<Org>> typeToken = new TypeToken<List<Org>>() {
            };
            mDatas = MyApp.getGson().fromJson(
                    tableArray.toString(), typeToken.getType());

            mAdapter = new OrgListAdapter(this,mDatas);
            mListView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
