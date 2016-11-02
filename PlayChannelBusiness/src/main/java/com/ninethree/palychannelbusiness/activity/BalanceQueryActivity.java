package com.ninethree.palychannelbusiness.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.BuyRecordAdapter;
import com.ninethree.palychannelbusiness.adapter.RecordAdapter;
import com.ninethree.palychannelbusiness.bean.BuyRecord;
import com.ninethree.palychannelbusiness.bean.Nfc;
import com.ninethree.palychannelbusiness.bean.Record;
import com.ninethree.palychannelbusiness.bean.Result;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询
 */
public class BalanceQueryActivity extends BaseActivity implements AbsListView.OnScrollListener{

    private TextView mCard;
    private TextView mPhone;
    private TextView mNickname;
    private TextView mBalance;

    private NfcAdapter nfcAdapter;
    private Intent intents;

    private boolean isnews = true;
    private PendingIntent pendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Nfc mynfc;

    private String hex;
    private String dec;

    private String mOrgId;
    private String mOrgGuid;

    private User mBuyUser;

    private int mCount;
    private int mPageSize = 10;
    private int mPageIndex = 1;

    //记录
    private ListView mListView;
    private List<BuyRecord> mDatas;
    private BuyRecordAdapter mAdapter;

    //footer
    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle.setText("余额查询");

        mCard = (TextView) findViewById(R.id.card);
        mPhone = (TextView) findViewById(R.id.phone);
        mNickname = (TextView) findViewById(R.id.nickname);
        mBalance = (TextView) findViewById(R.id.balance);
        mListView = (ListView) findViewById(R.id.listView);

        mOrgId = getIntent().getStringExtra("orgId");
        mOrgGuid = getIntent().getStringExtra("orgGuid");

        // 获取默认的NFC控制器
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "设备不支持NFC", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在设置中开启NFC", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        ndef.addCategory("*/*");
        mFilters = new IntentFilter[]{ndef};// 过滤器
        mTechLists = new String[][]{
                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()}};// 允许扫描的标签类型

        mDatas = new ArrayList<BuyRecord>();

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) <= mCount) {
                    new RecordTask().execute(mBuyUser.getSeftOrgID(),mBuyUser.getSeftOrgGuid());
                } else {
                    mTextView.setText("没有更多数据了");
                }
            }
        });
        mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);

        mListView.addFooterView(mFooter);
        mListView.setOnScrollListener(this);

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_balance_query);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 得到是否检测到ACTION_TECH_DISCOVERED触发
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
                mTechLists);
        if (isnews) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
                // 处理该intent
                processIntent(getIntent());
                intents = getIntent();
                isnews = false;
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            intents = intent;
        }

    }

    /**
     * 读取NFC信息数据
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String cardStr = "";
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        cardStr = "ID：" + bytesToHexString(tag.getId());
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        cardStr += "\r\nTECH：";
        for (String tech : techList) {
            cardStr += tech + ",";
            if (tech.indexOf("MifareClassic") >= 0) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            mCard.setText("卡号：" + cardStr);
            Toast.makeText(this, "非法卡", Toast.LENGTH_LONG).show();
            return;
        }

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            long before = Long.parseLong(bytesToHexString(myNFCID), 16);
            dec = "" + before;
            hex = bytesToHexString(myNFCID);
            mCard.setText("卡号：" + dec);
            mPageIndex = 1;
            new QueryUserTask().execute(mOrgId, mOrgGuid, hex, dec, "", "", "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mfc != null) {
                    mfc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);

        }
        return stringBuilder.toString();
    }


    @Override
    public void onClick(View v) {

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
                        new RecordTask().execute(mBuyUser.getSeftOrgID(),mBuyUser.getSeftOrgGuid());
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

    private class QueryUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            map.put("SaleOrgGuid", params[1]);
            map.put("IdHex", params[2]);
            map.put("IdTag", params[3]);
            map.put("Data01", params[4]);
            map.put("Data02", params[5]);
            map.put("Data03", params[6]);
            map.put("Data04", "");
            String param = DBPubService.pubDbParamPack(getApplicationContext(),
                    1, "Bm.Scan", "BuyOrg_QueryBlance_byIC", map);
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
                Toast.makeText(getApplicationContext(), "连接超时，请检查您的网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);

            JSONArray tableArray = jsonObject.optJSONArray("Table");
            TypeToken<List<Result>> typeToken = new TypeToken<List<Result>>() {
            };
            List<Result> results = MyApp.getGson().fromJson(tableArray.toString(), typeToken.getType());
            Result result = results.get(0);

            if (result.getRet_Value() == 0) {
                JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
                TypeToken<List<User>> typeToken1 = new TypeToken<List<User>>() {
                };
                List<User> users = MyApp.getGson().fromJson(tableArray1.toString(), typeToken1.getType());
                mBuyUser = users.get(0);
                String mobile = mBuyUser.getMobile();
                if (StringUtil.isNullOrEmpty(mobile)) {
                    mPhone.setText("手机号：未绑定");
                } else {
                    mPhone.setText("手机号：" + mBuyUser.getMobile());
                }
                mNickname.setText("昵称：" + mBuyUser.getNickName());
                if (StringUtil.isNullOrEmpty(mBuyUser.getBalancePar())) {
                    mBalance.setText("余额：0元");
                } else {
                    mBalance.setText("余额：" + mBuyUser.getBalancePar() + "元");
                }

                new RecordTask().execute(mBuyUser.getSeftOrgID(),mBuyUser.getSeftOrgGuid());

            } else {
                toast(result.getRet_Msg());
                mPhone.setText("手机号：");
                mNickname.setText("昵称：");
                mBalance.setText("余额：");
            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class RecordTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPageIndex == 1) {
                mProgressDialog.show();
            }else {
                mBar.setVisibility(ProgressBar.VISIBLE);
                mTextView.setText("正在加载");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("BuyOrgId", params[0]);
            map.put("BuyOrgGuid", params[1]);
            map.put("PageSize", mPageSize+"");
            map.put("PageIndex", mPageIndex+"");
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
            mTextView.setText("加载更多");
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getApplicationContext(), result);
                if (dataStr != null) {
                    success2(dataStr);
                }
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success2(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray countJson = jsonObject.optJSONArray("Table");
            mCount = countJson.optJSONObject(0).optInt("RowsCount");

            JSONArray tableArray = jsonObject.optJSONArray("Table1");
            TypeToken<List<BuyRecord>> typeToken = new TypeToken<List<BuyRecord>>() {
            };

            List<BuyRecord> records = MyApp.getGson().fromJson(tableArray.toString(),
                    typeToken.getType());

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(records);
                mAdapter = new BuyRecordAdapter(this, mDatas);
                mListView.setAdapter(mAdapter);
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

}
