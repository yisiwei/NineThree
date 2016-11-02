package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.Promotion;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.view.CircleImageView;

import java.util.List;

public class PromotionUseAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<User> mDatas;
    private Context mContext;


    public PromotionUseAdapter(Context context, List<User> datas) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_promotion_use, parent, false);

            holder.mHeadImg = (CircleImageView) convertView.findViewById(R.id.headImg);
            holder.mNickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.mDate = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = mDatas.get(position);

//        holder.mNickname.setText(user.getNickName());
        //holder.mDate.setText("");

//        Glide.with(mContext)
//                .load(user.getPhoto())
//                .centerCrop()
//                .dontAnimate()
//                .placeholder(R.drawable.default_head)
//                .into(holder.mHeadImg);

        return convertView;
    }

    class ViewHolder {
        public CircleImageView mHeadImg;
        public TextView mNickname;
        public TextView mDate;
    }

}
