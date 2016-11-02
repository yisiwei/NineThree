package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.Record;

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
            holder.mNickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.mHeadImg = (ImageView) convertView.findViewById(R.id.headImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Record record = mDatas.get(position);
        holder.mDate.setText("时间：" + record.getAddTime());
        holder.mProduct.setText("产品：" + record.getPduName());
        holder.mNum.setText("数量：" + record.getNum() + "次");
        holder.mPrice.setText("金额：" + record.getPayPar() + "元");
        holder.mNickname.setText(record.getNickName());

        Glide.with(mContext)
                .load(record.getPhoto())
                .centerCrop()
                //.crossFade()
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(holder.mHeadImg);

        return convertView;
    }

    class ViewHolder {
        public ImageView mHeadImg;
        public TextView mDate;
        public TextView mProduct;
        public TextView mNum;
        public TextView mPrice;
        public TextView mNickname;
    }

}
