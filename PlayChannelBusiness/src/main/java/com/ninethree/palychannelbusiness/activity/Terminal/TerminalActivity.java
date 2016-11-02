package com.ninethree.palychannelbusiness.activity.Terminal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.adapter.TerminalAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Terminal;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备管理
 */
public class TerminalActivity extends BaseActivity {

    private Button mAddBtn;
    private Button mQueryBtn;
    private Button mStartBtn;
    private Button mStopBtn;

    private ListView mListView;
    private TerminalAdapter mAdapter;
    private List<Terminal> mDatas;

    private User mUser;
    private Org mOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("设备管理");

        initView();

        mUser = (User) getIntent().getSerializableExtra("user");
        mOrg = (Org) getIntent().getSerializableExtra("org");

    }

    @Override
    protected void onResume() {
        super.onResume();
        new TerminalTask().execute(mOrg.getOrgId(),mOrg.getOrgGuid());
    }

    private void initView() {
        mAddBtn = (Button) findViewById(R.id.add);
        mQueryBtn = (Button) findViewById(R.id.query);
        mStartBtn = (Button) findViewById(R.id.start);
        mStopBtn = (Button) findViewById(R.id.stop);

        mListView = (ListView) findViewById(R.id.listView);

        mAddBtn.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
    }


    @Override
    public void setLayout() {
        setContentView(R.layout.ac_terminal);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.add:
                intent = new Intent(this,PduBindListActivity.class);
                intent.putExtra("user",mUser);
                intent.putExtra("org",mOrg);
                startActivity(intent);
                break;
            case R.id.query://查看
                intent = new Intent(this,TerminalQueryActivity.class);
                intent.putExtra("user",mUser);
                intent.putExtra("org",mOrg);
                intent.putExtra("commandCode","50100101");
                intent.putExtra("type","查看设备");
                startActivity(intent);
                break;
            case R.id.start://启用
                intent = new Intent(this,TerminalQueryActivity.class);
                intent.putExtra("user",mUser);
                intent.putExtra("org",mOrg);
                intent.putExtra("commandCode","50100201");
                intent.putExtra("type","启用设备");
                startActivity(intent);
                break;
            case R.id.stop://禁用
                intent = new Intent(this,TerminalQueryActivity.class);
                intent.putExtra("user",mUser);
                intent.putExtra("org",mOrg);
                intent.putExtra("commandCode","50100202");
                intent.putExtra("type","禁用设备");
                startActivity(intent);
                break;
        }
    }

    private class TerminalTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("OrgId", params[0]);
            map.put("OrgGuid", params[1]);
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "Terminal_Query_ByOrgId", map);
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
            TypeToken<List<Terminal>> typeToken = new TypeToken<List<Terminal>>() {
            };

            mDatas = MyApp.getGson().fromJson(tableArray.toString(),
                    typeToken.getType());

            if (mDatas != null && mDatas.size() > 0) {
                mAdapter = new TerminalAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }
}
