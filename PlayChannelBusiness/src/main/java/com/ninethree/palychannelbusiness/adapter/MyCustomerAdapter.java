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
import com.ninethree.palychannelbusiness.bean.MyCustomer;
import com.ninethree.palychannelbusiness.bean.Record;
import com.ninethree.palychannelbusiness.view.CircleImageView;

import java.util.List;

public class MyCustomerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MyCustomer> mDatas;
    private Context mContext;

    public MyCustomerAdapter(Context context, List<MyCustomer> datas) {
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
            convertView = mInflater.inflate(R.layout.item_my_customer, parent, false);
            holder.mHeadImg = (CircleImageView) convertView.findViewById(R.id.headImg);
            holder.mNickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCustomer customer = mDatas.get(position);
        holder.mPrice.setText("消费金额：" + customer.getCash() + "元");
        holder.mNickname.setText(customer.getNickName());

        Glide.with(mContext)
                .load(customer.getPhoto())
                .centerCrop()
                //.crossFade()
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(holder.mHeadImg);

        return convertView;
    }

    class ViewHolder {
        public CircleImageView mHeadImg;
        public TextView mNickname;
        public TextView mPrice;
    }

}
