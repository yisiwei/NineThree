package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.BuyRecord;

import java.util.List;


public class BuyRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<BuyRecord> mDatas;

    public BuyRecordAdapter(Context context, List<BuyRecord> datas) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BuyRecord record = mDatas.get(position);
        holder.mDate.setText(record.getEditTime());
        holder.mProduct.setText(record.getPduName());
        holder.mNum.setText(record.getNum()+"次");
        holder.mPrice.setText(record.getPayPar()+"元");

        return convertView;
    }

    class ViewHolder {
        public TextView mDate;
        public TextView mProduct;
        public TextView mNum;
        public TextView mPrice;
    }

}
