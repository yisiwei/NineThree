package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.CustomerRecord;
import com.ninethree.palychannelbusiness.bean.MyCustomer;
import com.ninethree.palychannelbusiness.bean.RecordPdu;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.view.CircleImageView;

import java.util.List;

public class CustomerRecordAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CustomerRecord> mDatas;
    private Context mContext;

    public CustomerRecordAdapter(Context context, List<CustomerRecord> datas) {
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
            convertView = mInflater.inflate(R.layout.item_customer_record, parent, false);

            //holder.mImg = (ImageView) convertView.findViewById(R.id.img);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);
            //holder.mPdu = (TextView) convertView.findViewById(R.id.pdu);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            holder.mListView = (ListView) convertView.findViewById(R.id.listView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CustomerRecord record = mDatas.get(position);
        holder.mPrice.setText(record.getCash() + "元");
        holder.mDate.setText(DateUtil.formatDateString(record.getTimeAdd()));

        List<RecordPdu> datas = record.getPdus();
        PduAdapter adapter = new PduAdapter(mContext,datas);
        holder.mListView.setAdapter(adapter);

//        if (!StringUtil.isNullOrEmpty(record.getPduName())){
//            holder.mPdu.setText(record.getPduName());
//        }else{
//            holder.mPdu.setText(record.getCardModelText());
//        }

//        String url = null;
//        if (!StringUtil.isNullOrEmpty(record.getPduLogoUrl())){
//            url = record.getPduLogoUrl();
//        }else {
//            url = record.getImgUrl();
//        }

//        Glide.with(mContext)
//                .load(url)
//                .centerCrop()
//                .crossFade()
//                //.dontAnimate()
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder.mImg);

        return convertView;
    }

    class ViewHolder {
        public TextView mDate;
//        public ImageView mImg;
//        public TextView mPdu;
        public ListView mListView;
        public TextView mPrice;
    }

    class PduAdapter extends BaseAdapter{

        private LayoutInflater mInflater;
        private List<RecordPdu> mDatas;

        public PduAdapter(Context context,List<RecordPdu> datas){
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
                convertView = mInflater.inflate(R.layout.item_customer_record_pdu, parent, false);

                holder.mImg = (ImageView) convertView.findViewById(R.id.img);
                holder.mPdu = (TextView) convertView.findViewById(R.id.pdu);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RecordPdu pdu = mDatas.get(position);

            if (!StringUtil.isNullOrEmpty(pdu.getPduName())){
                holder.mPdu.setText(pdu.getPduName()+"x"+pdu.getTradeNum());
            }else{
                holder.mPdu.setText(pdu.getCardModelText()+"x"+pdu.getCardTradeNum());
            }

            String url = null;
            if (!StringUtil.isNullOrEmpty(pdu.getPduLogoUrl())){
                url = pdu.getPduLogoUrl();
            }else {
                url = pdu.getImgUrl();
            }

            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    //.dontAnimate()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.mImg);

            return convertView;
        }

        class ViewHolder{
            public ImageView mImg;
            public TextView mPdu;
        }
    }

}
