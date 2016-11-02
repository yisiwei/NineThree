package com.ninethree.palychannelbusiness.activity.promotion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.activity.BaseActivity;
import com.ninethree.palychannelbusiness.util.Log;
import com.ninethree.palychannelbusiness.webservice.DBPubService;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.List;

public class ShareImgsActivity extends BaseActivity {

    private GridView mGridView;
    private String[] mDatas;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("选择图片");

        mGridView = (GridView) findViewById(R.id.gridView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("img",mDatas[position]);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        new ImgTask().execute("");
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.ac_share_imgs);
    }

    @Override
    public void onClick(View v) {

    }

    private class ImgTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = DBPubService.getShareImgUrlList(params[0]);
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
                success(result);
            } else {
                toast("连接超时，请检查您的网络");
            }
        }
    }

    private void success(String str) {
        mDatas = str.substring(1,str.length()-1).replace("\"","").split(",");

        for (int i=0;i<mDatas.length;i++){
            Log.i("img:"+mDatas[i]);
        }

        mAdapter = new MyAdapter(this,mDatas);
        mGridView.setAdapter(mAdapter);
    }

    class MyAdapter extends BaseAdapter{

        private Context mContext;
        private LayoutInflater mInflater;
        private String[] mDatas;

        public MyAdapter(Context context,String[] datas){
            this.mContext = context;
            this.mDatas = datas;
            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return mDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.item_share_img,parent,false);
                holder.mImg = (ImageView) convertView.findViewById(R.id.img);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(mContext)
                    .load(mDatas[position])
                    .centerCrop()
                    //.crossFade()
                    .dontAnimate()
                    .placeholder(R.drawable.default_head)
                    .into(holder.mImg);

            return convertView;
        }

        class ViewHolder{
            public ImageView mImg;
        }
    }
}
