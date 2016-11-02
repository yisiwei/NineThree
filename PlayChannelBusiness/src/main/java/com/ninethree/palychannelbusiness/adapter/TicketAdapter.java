package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.Ticket;

import java.util.List;

/**
 * Created by user on 2016/8/26.
 */
public class TicketAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Ticket> mDatas;

    public TicketAdapter(Context context, List<Ticket> datas) {
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(context);
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_product,parent,false);

            holder.mText = (TextView) convertView.findViewById(R.id.tv_text);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Ticket ticket = mDatas.get(position);

        holder.mText.setText(ticket.getTicketMacText());

        return convertView;
    }

    class ViewHolder{
        public TextView mText;
    }

}
