package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.User;
import com.ninethree.palychannelbusiness.util.DateUtil;
import com.ninethree.palychannelbusiness.util.StringUtil;
import com.ninethree.palychannelbusiness.view.CircleImageView;

import java.util.List;

public class PromotionGetAdapter extends RecyclerView.Adapter<PromotionGetAdapter.ViewHolder> {

    private List<User> mDatas;
    private Context mContext;

    public PromotionGetAdapter(Context context, List<User> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_promotion_use, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        User user = mDatas.get(position);

        viewHolder.mNum.setText("" + (position + 1));
        viewHolder.mNickname.setText(user.getNickName());
        String usedTime = "";
        if (!StringUtil.isNullOrEmpty(user.getUsedTime())){
            usedTime = DateUtil.formatDateString(user.getUsedTime());
        }
        String addTime = "";
        if (!StringUtil.isNullOrEmpty(user.getAddTime())){
            addTime = DateUtil.formatDateString(user.getAddTime());
        }
        viewHolder.mDate.setText(StringUtil.isNullOrEmpty(user.getUsedTime()) ? addTime : usedTime);

        Glide.with(mContext)
                .load(user.getPhoto())
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(viewHolder.mHeadImg);

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mHeadImg;
        public TextView mNum;
        public TextView mNickname;
        public TextView mDate;

        public ViewHolder(View view) {
            super(view);
            mHeadImg = (CircleImageView) view.findViewById(R.id.headImg);
            mNum = (TextView) view.findViewById(R.id.num);
            mNickname = (TextView) view.findViewById(R.id.nickname);
            mDate = (TextView) view.findViewById(R.id.date);
        }
    }

}
