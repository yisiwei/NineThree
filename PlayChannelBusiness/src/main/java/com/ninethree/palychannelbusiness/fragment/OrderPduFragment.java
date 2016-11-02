package com.ninethree.palychannelbusiness.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.OrderAdapter;
import com.ninethree.palychannelbusiness.bean.OrderGoods;
import com.ninethree.palychannelbusiness.bean.SaleOrder;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.util.PullParseXmlUtil;
import com.ninethree.palychannelbusiness.view.MyDateDialog;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售订单--产品
 */
public class OrderPduFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {

    private Button mDayBtn;
    private Button mWeekBtn;
    private Button mMonthBtn;
    private Button mYearBtn;

    private Button mBeginDateBtn;
    private Button mEndDateBtn;
    private Button mQueryBtn;

    private TextView mTotalCountTv;
    private TextView mTotalPriceTv;

    private ListView mListView;
    private OrderAdapter mAdapter;
    private List<SaleOrder> mDatas;

    private String mOrgId;

    private int mCount;
    private int mPageIndex = 1;
    private int mPageSize = 10;

    // ListView footer 加载更多
    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private MyProgressDialog mProgressDialog;

    public OrderPduFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_order_pdu, container, false);

        initView(view);
        initEvent();

        Bundle bundle = getArguments();
        mOrgId = bundle.getString("orgId");
        Log.i("Activity传过来的OrgId：" + mOrgId);

        mBeginDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));
        mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

        mDatas = new ArrayList<SaleOrder>();

        // footer
        mFooter = View.inflate(getActivity(), R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) < mCount) {
                    loadMore();
                } else {
                    mTextView.setText("没有更多数据了");
                }
            }
        });
        mBar = (ProgressBar) mFooter.findViewById(R.id.footer_progressBar);

        mListView.addFooterView(mFooter);
        mListView.setOnScrollListener(this);

        mProgressDialog = new MyProgressDialog(getActivity());

        query();

        return view;
    }

    private void initView(View view) {
        mDayBtn = (Button) view.findViewById(R.id.day);
        mWeekBtn = (Button) view.findViewById(R.id.week);
        mMonthBtn = (Button) view.findViewById(R.id.month);
        mYearBtn = (Button) view.findViewById(R.id.year);

        mBeginDateBtn = (Button) view.findViewById(R.id.beginDate);
        mEndDateBtn = (Button) view.findViewById(R.id.endDate);
        mQueryBtn = (Button) view.findViewById(R.id.query);

        mTotalCountTv = (TextView) view.findViewById(R.id.total_count);
        mTotalPriceTv = (TextView) view.findViewById(R.id.total_price);

        mListView = (ListView) view.findViewById(R.id.listView);
    }

    private void initEvent() {
        mDayBtn.setOnClickListener(this);
        mWeekBtn.setOnClickListener(this);
        mMonthBtn.setOnClickListener(this);
        mYearBtn.setOnClickListener(this);

        mBeginDateBtn.setOnClickListener(this);
        mEndDateBtn.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);
    }

    private void clearBtnColor() {
        mDayBtn.setTextColor(getResources().getColor(R.color.color_64));
        mWeekBtn.setTextColor(getResources().getColor(R.color.color_64));
        mMonthBtn.setTextColor(getResources().getColor(R.color.color_64));
        mYearBtn.setTextColor(getResources().getColor(R.color.color_64));
    }

    private void query(){
        mTextView.setText("加载更多");
        mPageIndex = 1;

        String beginDate = mBeginDateBtn.getText().toString();
        String endDate = mEndDateBtn.getText().toString();
        new TotalTask().execute(mOrgId, beginDate, endDate);
        new OrderPduTask().execute(mOrgId,beginDate, endDate);
    }

    private void loadMore(){
        String beginDate = mBeginDateBtn.getText().toString();
        String endDate = mEndDateBtn.getText().toString();
        new OrderPduTask().execute(mOrgId,beginDate, endDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.day:
                clearBtnColor();
                mDayBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                query();

                break;
            case R.id.week:
                clearBtnColor();
                mWeekBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getWeekFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                query();

                break;
            case R.id.month:
                clearBtnColor();
                mMonthBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getMonthFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                query();

                break;
            case R.id.year:
                clearBtnColor();
                mYearBtn.setTextColor(getResources().getColor(R.color.color_main));

                mBeginDateBtn.setText(DateUtil.getYearFirstDate());
                mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

                query();

                break;

            case R.id.beginDate:
                MyDateDialog.show(getActivity(),new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
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
                MyDateDialog.show(getActivity(),new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
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
            case R.id.query:
                query();
                break;
        }
    }

    private void toast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
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
                        loadMore();
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

    private class TotalTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        /**
         * @SaleOrgId bigint,
         * @TypeId int,--0查询所有,1查询卡券,-1查询产品
         * @DateBegin date,
         * @DateEnd date
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            map.put("TypeId", "-1");
            map.put("DateBegin", params[1]);
            map.put("DateEnd", params[2]);
            String param = DBPubService.pubDbParamPack(getActivity(),
                    1, "Bm.Unite.Pay.Card", "CardHistory_Query_SaleOrder_Total", map);
            SoapObject result = DBPubService.getPubDB(getActivity(),
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
                String dataStr = DBPubService.ascPackDown(getActivity(), result);
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

            //产品总数
            JSONArray tableArray1 = jsonObject.optJSONArray("Table");
            double pduTotalCash = tableArray1.optJSONObject(0).getDouble("TotalCash");
            int pduTotalCount = tableArray1.optJSONObject(0).getInt("TotalCount");
            Log.i("pduTotalCash:" + pduTotalCash + ",pduTotalCount:" + pduTotalCount);

            mTotalCountTv.setText("订单总计：" + pduTotalCount);
            mTotalPriceTv.setText("累计销售：￥" + pduTotalCash);

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    private class OrderPduTask extends AsyncTask<String, Void, String> {

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

        /**
         * 	@SaleOrgId			bigint,
         * 	@DateBegin			date,
         * 	@DateEnd			date,
         * 	@PageIndex			int,
         * 	@PageSize			int
         */
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("SaleOrgId", params[0]);
            map.put("DateBegin", params[1]);
            map.put("DateEnd", params[2]);
            map.put("PageIndex", mPageIndex + "");
            map.put("PageSize", mPageSize + "");
            String param = DBPubService.pubDbParamPack(getActivity(),
                    1, "Bm.Unite.Pay.Pass", "Pass_Query_GetPduOrderByDateTime", map);
            SoapObject result = DBPubService.getPubDB(getActivity(),
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
                String dataStr = DBPubService.ascPackDown(getActivity(), result);
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
            mCount = tableArray.optJSONObject(0).optInt("Column1");
            Log.i("Count:" + mCount);

            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            TypeToken<List<SaleOrder>> typeToken1 = new TypeToken<List<SaleOrder>>() {
            };

            List<SaleOrder> orders = MyApp.getGson().fromJson(tableArray1.toString(),
                    typeToken1.getType());

            for (int i = 0;i < orders.size();i++){
                try {
                    SaleOrder order = orders.get(i);
                    List<OrderGoods> goods = PullParseXmlUtil.getOrderGoods(order.getPdus());
                    Log.i("Goods:" + MyApp.getGson().toJson(goods));
                    orders.get(i).setOrderGoods(goods);
                    //Log.i("OrderGoods:"+MyApp.getGson().toJson(orders.get(i)));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(orders);
                mAdapter = new OrderAdapter(getActivity(),mDatas);
                mListView.setAdapter(mAdapter);
            } else {
                Log.i("加载数据成功,mPageIndex=" + mPageIndex);
                mDatas.addAll(orders);
                mAdapter.notifyDataSetChanged();
            }
            mPageIndex++;

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

}
