package com.ninethree.playchannel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.playchannel.R;
import com.ninethree.playchannel.activity.WebViewActivity;
import com.ninethree.playchannel.bean.Record;
import com.ninethree.playchannel.util.StringUtil;

import java.util.List;

public class RecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Record> mDatas;
    private Context mContext;

    public RecordAdapter(Context context, List<Record> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record, parent, false);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);
            holder.mProduct = (TextView) convertView.findViewById(R.id.product);
            holder.mNum = (TextView) convertView.findViewById(R.id.num);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            holder.mSale = (TextView) convertView.findViewById(R.id.sale);
            holder.mPduImg = (ImageView) convertView.findViewById(R.id.pdu_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Record record = mDatas.get(position);
        holder.mDate.setText("消费时间："+record.getEditTime());
        holder.mProduct.setText(record.getPduName());
        holder.mNum.setText("数量："+record.getNum()+"次");
        holder.mPrice.setText("消费金额："+record.getPayPar()+"元");
        holder.mSale.setText(record.getStoreName());

        if (!StringUtil.isNullOrEmpty(record.getPduLogoUrl())){
            Glide.with(mContext)
                    .load(record.getPduLogoUrl())
                    .centerCrop()
                    //.crossFade()
                    .dontAnimate()
                    .into(holder.mPduImg);
        }else{
            holder.mPduImg.setImageResource(R.drawable.img_card);
        }

        holder.mSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", "http://shop.93966.net/H5Store/Home/Index?s="+record.getSaleOrgId());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView mDate;
        public TextView mProduct;
        public TextView mNum;
        public TextView mPrice;
        public TextView mSale;
        public ImageView mPduImg;
    }

}
