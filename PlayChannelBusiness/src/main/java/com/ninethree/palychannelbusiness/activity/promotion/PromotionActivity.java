package com.ninethree.palychannelbusiness.activity.promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.ninethree.palychannelbusiness.MyApp;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.adapter.PromotionAdapter;
import com.ninethree.palychannelbusiness.bean.Org;
import com.ninethree.palychannelbusiness.bean.Promotion;
import com.ninethree.palychannelbusiness.bean.QueryParams;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.AppUtil;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.view.MyDialog;
import com.ninethree.palychannelbusiness.webservice.DBPubService;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠促销
 */
public class PromotionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,PromotionAdapter.OnPromotionListener,AbsListView.OnScrollListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefresh;

    private ListView mListView;
    private PromotionAdapter mAdapter;
    private List<Promotion> mDatas;

    private Org mOrg;
    private User mUser;

    //footer
    private View mFooter;
    private TextView mTextView;
    private ProgressBar mBar;

    private int mCount;
    private int mPageSize = 10;
    private int mPageIndex = 1;

    private PopupWindow window;

    private Promotion mPromotion;
    private int scene;//微信好友或朋友圈

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);// 设置是否可滑动

        mTitle.setText("优惠促销");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setOnClickListener(this);

        init();

        mOrg = (Org) getIntent().getSerializableExtra("org");
        mUser = (User) getIntent().getSerializableExtra("user");

        mFooter = View.inflate(this, R.layout.list_footer, null);
        mTextView = (TextView) mFooter.findViewById(R.id.footer_text);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPageSize * (mPageIndex - 1) <= mCount) {
                    query();
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
                Intent intent = new Intent(getApplicationContext(),PromotionUseActivity.class);
                Log.i(">>>"+mDatas.get(position).getPromotionId());
                intent.putExtra("promotionId",""+mDatas.get(position).getPromotionId());
                startActivity(intent);
            }
        });

        mDatas = new ArrayList<Promotion>();

        initPopwindow();

        query();

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_promotion);
    }

    //初始化
    private void init() {

        mListView = (ListView) findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout
                .setProgressBackgroundColorSchemeResource(R.color.color_fff);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right_btn://新增
                Intent intent = new Intent(this,PromotionAddActivity.class);
                intent.putExtra("org",mOrg);
                intent.putExtra("user",mUser);
                startActivityForResult(intent,1000);
                break;
        }
    }

    //查询
    private void query(){
        QueryParams params = new QueryParams();
        params.setOrgId(Integer.valueOf(mOrg.getOrgId()));
        params.setPageIndex(mPageIndex);
        params.setPageSize(mPageSize);
        String json = MyApp.getGson().toJson(params);
        Log.i("json:"+json);
        new QueryListTask().execute(json);
    }

    @Override
    public void onRefresh() {
        mTextView.setText("加载更多");
        mPageIndex = 1;
        mIsRefresh = true;
        query();
    }

    //查看
    @Override
    public void OnQueryClick(int position) {
        Intent intent = new Intent(this,PromotionQueryActivity.class);
        intent.putExtra("promotion",mDatas.get(position));
        startActivity(intent);
    }

    //编辑
    @Override
    public void OnEditClick(int position) {
        Intent intent = new Intent(this,PromotionEditActivity.class);
        intent.putExtra("org",mOrg);
        intent.putExtra("user",mUser);
        intent.putExtra("promotion",mDatas.get(position));
        startActivityForResult(intent,3000);
    }

    //关闭
    @Override
    public void OnCloseClick(final int position) {
        MyDialog.show(this, "确定要关闭吗？", new MyDialog.OnConfirmListener() {
            @Override
            public void onConfirmClick() {
                new CloseTask().execute(mDatas.get(position).getPromotionId()+"");
            }
        });
    }

    //分享      http://shop.93966.net/H5Store/Promotion/Index?s=142008&id=126
    @Override
    public void OnPreviewClick(int position) {

        mPromotion = mDatas.get(position);

        backgroundAlpha(0.8f);
        window.showAtLocation(findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);

    }

    //初始化分享Popwindow
    private void initPopwindow(){
        View view = View.inflate(this,R.layout.pop_share, null);

        TextView wechat = (TextView) view.findViewById(R.id.share_wechat);
        TextView wxcircle = (TextView) view.findViewById(R.id.share_wxcircle);
        TextView qq = (TextView) view.findViewById(R.id.share_qq);
        TextView qzone = (TextView) view.findViewById(R.id.share_qzone);

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信好友
                scene = SendMessageToWX.Req.WXSceneSession;
                new ShareTask().execute();
            }
        });

        wxcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                scene = SendMessageToWX.Req.WXSceneTimeline;
                new ShareTask().execute();
            }
        });

        //QQ好友
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQQ();
            }
        });

        //QQ空间
        qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQZone();
            }
        });

        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,true);

        // 实例化一个ColorDrawable颜色
        ColorDrawable dw = new ColorDrawable(0000000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.pop_anim);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });

    }

    private void backgroundAlpha(float alpha){
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    //分享
    private class ShareTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {

            Bitmap thumb = Bitmap.createScaledBitmap(GetLocalOrNetBitmap(mPromotion.getShareImgUrl()), 120, 120, true);//压缩Bitmap

            return thumb;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = "http://shop.93966.net/H5Store/Promotion/Index?s="+mPromotion.getOrgId()+"&id="+mPromotion.getPromotionId();
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = mPromotion.getShareTitle();
            msg.description = mPromotion.getShareNote();

            msg.thumbData = Util.bmpToByteArray(result,true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = scene;
            MyApp.api.sendReq(req);

            window.dismiss();
        }
    }

    //获取分享图片
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,dataStream);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void shareToQQ(){
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://shop.93966.net/H5Store/Promotion/Index?s="+mPromotion.getOrgId()+"&id="+mPromotion.getPromotionId());
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mPromotion.getShareTitle());
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,mPromotion.getShareImgUrl());
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, mPromotion.getShareNote());
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
        //标识该消息的来源应用，值为应用名称+AppId。
        //bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + AppId);

        MyApp.mTencent.shareToQQ(this, bundle , qqShareListener);
    }

    private void shareToQZone(){

        Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mPromotion.getShareTitle());//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mPromotion.getShareNote());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://shop.93966.net/H5Store/Promotion/Index?s="+mPromotion.getOrgId()+"&id="+mPromotion.getPromotionId());//必填
        ArrayList<String> images = new ArrayList<String>();
        images.add(mPromotion.getShareImgUrl());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);

        MyApp.mTencent.shareToQzone(this, params, qqShareListener);

    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
//            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
//                Util.toastMessage(QQShareActivity.this, "onCancel: ");
//            }
            toast("分享取消");
            window.dismiss();
        }
        @Override
        public void onComplete(Object response) {
//            Util.toastMessage(QQShareActivity.this, "onComplete: " + response.toString());
            toast("分享成功");
            Log.i("onComplete: " + response.toString());
            window.dismiss();
        }
        @Override
        public void onError(UiError e) {
//            Util.toastMessage(QQShareActivity.this, "onError: " + e.errorMessage, "e");
            toast("分享失败");
            Log.i("onError: " + e.errorMessage);
            window.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1000://新增促销
                    //刷新
                    mPageIndex = 1;
                    query();
                    break;
                case 3000:
                    //刷新
                    mPageIndex = 1;
                    query();
                    break;
            }
        }
//        MyApp.mTencent.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,qqShareListener);
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
                        query();
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

    //促销列表查询
    private class QueryListTask extends AsyncTask<String, Void, String> {

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

            SoapObject result = DBPubService.getStorePromotionList(params[0]);
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
            mIsRefresh = false;

            Log.i("result:" + result);

            if (null != result) {
                success(result);
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            mCount = jsonObject.optInt("RowCount");

            JSONArray jsonArray = jsonObject.optJSONArray("Rows");

            TypeToken<List<Promotion>> typeToken = new TypeToken<List<Promotion>>() {
            };

            List<Promotion> promotions = MyApp.getGson().fromJson(jsonArray.toString(),
                    typeToken.getType());

            if (mPageIndex == 1) {
                mDatas.clear();
                mDatas.addAll(promotions);
                mAdapter = new PromotionAdapter(this, mDatas,this);
                mListView.setAdapter(mAdapter);
            } else {
                Log.i("加载数据成功,mPageIndex=" + mPageIndex);
                mDatas.addAll(promotions);
                mAdapter.notifyDataSetChanged();
            }
            mPageIndex++;

        } catch (JSONException e) {
            toast("服务器错误");
            e.printStackTrace();
        }
    }

    //关闭
    private class CloseTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = DBPubService.closePromotionByOrg(params[0]);
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

            Log.i("result:" + result);

            if (null != result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    boolean isError = jsonObject.optBoolean("HasError");
                    String message = jsonObject.optString("Message");

                    toast(message);
                    if (!isError) {
                        mPageIndex = 1;
                        query();
                    }

                } catch (JSONException e) {
                    toast("服务器错误");
                    e.printStackTrace();
                }

            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }
}
