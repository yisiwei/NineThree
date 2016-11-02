package com.ninethree.palychannelbusiness.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.adapter.MyCustomerAdapter;
import com.ninethree.palychannelbusiness.adapter.ProductAdapter;
import com.ninethree.palychannelbusiness.adapter.PromotionGetAdapter;
import com.ninethree.palychannelbusiness.bean.Product;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.MyProgressDialog;
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
 * 优惠促销--领取
 */
public class PromotionGetFragment extends Fragment {

    private View emptyView;
    private XRecyclerView mRecyclerView;
    private PromotionGetAdapter mAdapter;
    private List<User> mDatas;

    private int mCount;
    private int mPageSize = 14;
    private int mPageIndex = 1;

    private String mPromotionId;
    private boolean isRefresh;//是否是下拉刷新

    private MyProgressDialog mProgressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_promotion_get, container, false);

        initView(view);

        mPromotionId = getArguments().getString("promotionId");
        Log.i("mPromotionId:" + mPromotionId);

        mProgressDialog = new MyProgressDialog(getActivity());

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        mRecyclerView.setEmptyView(emptyView);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                mPageIndex = 1;
                isRefresh = true;
                new QueryTask().execute(mPromotionId);
            }

            @Override
            public void onLoadMore() {
                if (mPageSize * (mPageIndex - 1) <= mCount) {
                    new QueryTask().execute(mPromotionId);
                } else {//没有更多数据了
                    mRecyclerView.setIsnomore(true);
                }
            }
        });

        mDatas = new ArrayList<User>();

        new QueryTask().execute(mPromotionId);

        return view;
    }

    private void initView(View view) {
        emptyView = view.findViewById(R.id.text_empty);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
    }

    private class QueryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPageIndex == 1 && !isRefresh) {
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("PromotionId", params[0]);
            map.put("PageSize", "" + mPageSize);
            map.put("PageIndex", "" + mPageIndex);
            String param = DBPubService.pubDbParamPack(getActivity(),
                    1, "Bm.Store", "StorePromotion_QueryReceiveUserList_ById", map);
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
            if (mPageIndex == 1 && !isRefresh) {
                mProgressDialog.dismiss();
            }
            if (null != result) {
                String dataStr = DBPubService.ascPackDown(
                        getActivity(), result);
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

            JSONArray countArr = jsonObject.optJSONArray("Table");
            mCount = countArr.optJSONObject(0).optInt("ReceiveNum");
            Log.i("Count:" + mCount);

            if (mCount > 0) {

                JSONArray tableArray = jsonObject.optJSONArray("Table1");
                TypeToken<List<User>> typeToken = new TypeToken<List<User>>() {
                };

                List<User> users = MyApp.getGson().fromJson(tableArray.toString(),
                        typeToken.getType());

                if (mPageIndex == 1) {
                    mDatas.clear();
                    mDatas.addAll(users);
                    mAdapter = new PromotionGetAdapter(getActivity(), mDatas);
                    mRecyclerView.setAdapter(mAdapter);

                    if (isRefresh) {
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setIsnomore(false);
                        mRecyclerView.loadMoreComplete();
                    }

                } else {
                    mDatas.addAll(users);
                    mRecyclerView.loadMoreComplete();
                    mAdapter.notifyDataSetChanged();

                }
                isRefresh = false;
                mPageIndex++;
            }else {
                mAdapter = new PromotionGetAdapter(getActivity(), mDatas);
                mRecyclerView.setAdapter(mAdapter);
            }

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
