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
import com.ninethree.palychannelbusiness.bean.OrderCard;
import com.ninethree.palychannelbusiness.bean.OrderGoods;
import com.ninethree.palychannelbusiness.bean.SaleOrder;

import java.util.List;

public class OrderCardAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<OrderCard> mDatas;
    private Context mContext;

    public OrderCardAdapter(Context context, List<OrderCard> datas) {
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
            convertView = mInflater.inflate(R.layout.item_order_card, parent, false);

            holder.mCardNumber = (TextView) convertView.findViewById(R.id.card_number);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);

            holder.mImg = (ImageView) convertView.findViewById(R.id.img);
            holder.mCardName = (TextView) convertView.findViewById(R.id.card_name);

            holder.mOldPrice = (TextView) convertView.findViewById(R.id.old_price);
            holder.mNewPrice = (TextView) convertView.findViewById(R.id.new_price);

            holder.mTotalPrice = (TextView) convertView.findViewById(R.id.total_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderCard card = mDatas.get(position);

        holder.mCardNumber.setText("卡号：" + card.getCardNum());
        holder.mDate.setText("日期：" + card.getTimeAdd());

        holder.mCardName.setText(card.getCardText());

        holder.mOldPrice.setText("原价：￥"+card.getPar());
        holder.mNewPrice.setText("现价：￥"+ card.getCash() +"    数量：1");

        holder.mTotalPrice.setText("￥" + card.getCash());

        Glide.with(mContext)
                .load(card.getImgUrl())
                .centerCrop()
                //.crossFade()
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.mImg);

        holder.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

        return convertView;
    }

    class ViewHolder {
        public TextView mCardNumber;
        public TextView mDate;

        public ImageView mImg;
        public TextView mCardName;
        public TextView mOldPrice;
        public TextView mNewPrice;

        public TextView mTotalPrice;
    }

}
