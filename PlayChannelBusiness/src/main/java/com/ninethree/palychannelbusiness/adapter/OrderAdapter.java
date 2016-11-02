package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.OrderGoods;
import com.ninethree.palychannelbusiness.bean.SaleOrder;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SaleOrder> mDatas;
    private Context mContext;

    public OrderAdapter(Context context, List<SaleOrder> datas) {
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
            convertView = mInflater.inflate(R.layout.item_order, parent, false);

            holder.mOrderNumber = (TextView) convertView.findViewById(R.id.order_number);
            holder.mDate = (TextView) convertView.findViewById(R.id.order_date);

            holder.mListView = (ListView) convertView.findViewById(R.id.listView);

            holder.mTotalPrice = (TextView) convertView.findViewById(R.id.total_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SaleOrder order = mDatas.get(position);

        holder.mOrderNumber.setText("编号：" + order.getOrderNumber());
        holder.mDate.setText("日期：" + order.getCreate_Time());
        holder.mTotalPrice.setText("￥" + order.getTradePrice());

        GoodsAdapter adapter = new GoodsAdapter(mContext, order.getOrderGoods());
        holder.mListView.setAdapter(adapter);

        return convertView;
    }

    class ViewHolder {
        public TextView mOrderNumber;
        public TextView mDate;

        public ListView mListView;

        public TextView mTotalPrice;
    }

    class GoodsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<OrderGoods> mDatas;
        private Context mContext;

        public GoodsAdapter(Context context, List<OrderGoods> datas) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_order_good, parent, false);

                holder.mImg = (ImageView) convertView.findViewById(R.id.img);
                holder.mPduName = (TextView) convertView.findViewById(R.id.pduName);

                holder.mOldPrice = (TextView) convertView.findViewById(R.id.old_price);
                holder.mNewPrice = (TextView) convertView.findViewById(R.id.new_price);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OrderGoods goods = mDatas.get(position);

            holder.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

            holder.mPduName.setText(goods.getPduName());
            holder.mOldPrice.setText("原价：￥"+goods.getPrice());
            holder.mNewPrice.setText("现价：￥"+goods.getTradePrice()+"    数量："+goods.getTradeNum());

            Glide.with(mContext)
                    .load(goods.getPduLogoUrl())
                    .centerCrop()
                    //.crossFade()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.mImg);

            return convertView;
        }

        class ViewHolder {
            public ImageView mImg;
            public TextView mPduName;
            public TextView mOldPrice;
            public TextView mNewPrice;

        }
    }
}
