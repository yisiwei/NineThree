package com.ninethree.palychannelbusiness.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.OrderActivity;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.MyDateDialog;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 销售订单--统计
 */
public class OrderTotalFragment extends Fragment implements View.OnClickListener {

    private Button mDayBtn;
    private Button mWeekBtn;
    private Button mMonthBtn;
    private Button mYearBtn;

    private Button mBeginDateBtn;
    private Button mEndDateBtn;
    private Button mQueryBtn;

    private LinearLayout mPduLayout;
    private LinearLayout mCardLayout;

    private TextView mPduCountTv;
    private TextView mPduPriceTv;

    private TextView mCardCountTv;
    private TextView mCardPriceTv;

    private TextView mTotalCountTv;
    private TextView mTotalPriceTv;

    private MyProgressDialog mProgressDialog;

    private String mOrgId;

    public OrderTotalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_order_total, container, false);

        initView(view);
        initEvent();

        mProgressDialog = new MyProgressDialog(getActivity());

        Bundle bundle = getArguments();
        mOrgId = bundle.getString("orgId");
        Log.i("Activity传过来的OrgId：" + mOrgId);

        mEndDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));
        mBeginDateBtn.setText(DateUtil.getDate("yyyy-MM-dd"));

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

        mPduLayout = (LinearLayout) view.findViewById(R.id.ll_pdu);
        mCardLayout = (LinearLayout) view.findViewById(R.id.ll_card);

        mPduCountTv = (TextView) view.findViewById(R.id.pdu_count);
        mPduPriceTv = (TextView) view.findViewById(R.id.pdu_price);

        mCardCountTv = (TextView) view.findViewById(R.id.card_count);
        mCardPriceTv = (TextView) view.findViewById(R.id.card_price);

        mTotalCountTv = (TextView) view.findViewById(R.id.total_count);
        mTotalPriceTv = (TextView) view.findViewById(R.id.total_price);
    }

    private void initEvent() {
        mDayBtn.setOnClickListener(this);
        mWeekBtn.setOnClickListener(this);
        mMonthBtn.setOnClickListener(this);
        mYearBtn.setOnClickListener(this);

        mBeginDateBtn.setOnClickListener(this);
        mEndDateBtn.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);

        mPduLayout.setOnClickListener(this);
        mCardLayout.setOnClickListener(this);
    }

    private void clearBtnColor() {
        mDayBtn.setTextColor(getResources().getColor(R.color.color_64));
        mWeekBtn.setTextColor(getResources().getColor(R.color.color_64));
        mMonthBtn.setTextColor(getResources().getColor(R.color.color_64));
        mYearBtn.setTextColor(getResources().getColor(R.color.color_64));
    }

    private void query() {
        String beginDate = mBeginDateBtn.getText().toString();
        String endDate = mEndDateBtn.getText().toString();
        new TotalTask().execute(mOrgId, beginDate, endDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                MyDateDialog.show(getActivity(), new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String endDate = mEndDateBtn.getText().toString();
                        if (DateUtil.compareDate(date, endDate) > 0) {
                            toast("开始日期不能大于结束日期");
                            return;
                        }
                        mBeginDateBtn.setText(date);
                    }
                });
                break;
            case R.id.endDate:
                MyDateDialog.show(getActivity(), new Date().getTime(), new MyDateDialog.OnDateConfirmListener() {
                    @Override
                    public void onConfirmClick(String date) {
                        String beginDate = mBeginDateBtn.getText().toString();
                        if (DateUtil.compareDate(beginDate, date) > 0) {
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
            case R.id.ll_pdu:
                ((OrderActivity) getActivity()).setTab(2);
                break;
            case R.id.ll_card:
                ((OrderActivity) getActivity()).setTab(3);
                break;
        }
    }

    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
            map.put("TypeId", "0");
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

            //卡券总数
            JSONArray tableArray = jsonObject.optJSONArray("Table");
            double cardTotalCash = tableArray.optJSONObject(0).getDouble("TotalCash");
            int cardTotalCount = tableArray.optJSONObject(0).getInt("TotalCount");
            Log.i("cardTotalCash:" + cardTotalCash + ",cardTotalCount:" + cardTotalCount);

            mCardCountTv.setText("" + cardTotalCount);
            mCardPriceTv.setText("￥" + cardTotalCash);

            //产品总数
            JSONArray tableArray1 = jsonObject.optJSONArray("Table1");
            double pduTotalCash = tableArray1.optJSONObject(0).getDouble("TotalCash");
            int pduTotalCount = tableArray1.optJSONObject(0).getInt("TotalCount");
            Log.i("pduTotalCash:" + pduTotalCash + ",pduTotalCount:" + pduTotalCount);

            mPduCountTv.setText("" + pduTotalCount);
            mPduPriceTv.setText("￥" + pduTotalCash);

            mTotalCountTv.setText("" + (cardTotalCount + pduTotalCount));
            mTotalPriceTv.setText("￥" + BigDecimal.valueOf(cardTotalCash).add(BigDecimal.valueOf(pduTotalCash)));


        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }
}
