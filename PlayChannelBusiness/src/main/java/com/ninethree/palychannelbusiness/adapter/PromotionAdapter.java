package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.Promotion;
import com.ninethree.palychannelbusiness.bean.Record;

import java.util.List;

public class PromotionAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Promotion> mDatas;
    private Context mContext;
    private OnPromotionListener onPromotionListener;

    public interface OnPromotionListener{

        public void OnQueryClick(int position);

        public void OnEditClick(int position);

        public void OnCloseClick(int position);

        public void OnPreviewClick(int position);
    }

    public PromotionAdapter(Context context, List<Promotion> datas,OnPromotionListener onPromotionListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.onPromotionListener = onPromotionListener;
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
            convertView = mInflater.inflate(R.layout.item_promotion, parent, false);

            holder.mActivityName = (TextView) convertView.findViewById(R.id.activity_name);
            holder.mType = (TextView) convertView.findViewById(R.id.type);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mNumber = (TextView) convertView.findViewById(R.id.number);
            holder.mUseNumber = (TextView) convertView.findViewById(R.id.use_number);

            holder.mQueryBtn = (Button) convertView.findViewById(R.id.query);
            holder.mEditBtn = (Button) convertView.findViewById(R.id.edit);
            holder.mCloseBtn = (Button) convertView.findViewById(R.id.close);
            holder.mPreviewBtn = (Button) convertView.findViewById(R.id.preview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Promotion promotion = mDatas.get(position);

        holder.mActivityName.setText(promotion.getTitle());
        holder.mType.setText("促销类型："+(promotion.getPromotionType() == 0?"面值卡":"产品"));
        holder.mStatus.setText(getStatus(promotion.getStatus(),holder.mStatus));
        holder.mName.setText("赠品名称："+promotion.getDataTitle());
        holder.mNumber.setText("发行数量："+promotion.getIssueNum());
        holder.mUseNumber.setText("使用/领取："+promotion.getUsedNum()+"/"+promotion.getReceiveNum());

        if (promotion.getStatus() != 0){
            holder.mCloseBtn.setVisibility(View.GONE);
            holder.mEditBtn.setVisibility(View.GONE);
            holder.mPreviewBtn.setVisibility(View.GONE);
        }else{
            holder.mCloseBtn.setVisibility(View.VISIBLE);
            holder.mEditBtn.setVisibility(View.VISIBLE);
            holder.mPreviewBtn.setVisibility(View.VISIBLE);
        }

        //查看
        holder.mQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromotionListener.OnQueryClick(position);
            }
        });

        //编辑
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromotionListener.OnEditClick(position);
            }
        });

        //关闭
        holder.mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromotionListener.OnCloseClick(position);
            }
        });

        //预览
        holder.mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPromotionListener.OnPreviewClick(position);
            }
        });

        return convertView;
    }

    private String getStatus(int statusId,TextView tv){
        String status ="";
        switch (statusId){
            case 0:
                status = "发布中";
                tv.setTextColor(Color.parseColor("#3CC57F"));
                break;
            case -1:
                status = "关闭";
                tv.setTextColor(Color.parseColor("#E52E04"));
                break;
            case 1:
                status = "结束";
                tv.setTextColor(Color.parseColor("#A2A2A2"));
                break;
        }
        return status;
    }

    class ViewHolder {
        public TextView mActivityName;
        public TextView mType;
        public TextView mStatus;
        public TextView mName;
        public TextView mNumber;
        public TextView mUseNumber;

        public Button mQueryBtn;
        public Button mCloseBtn;
        public Button mEditBtn;
        public Button mPreviewBtn;
    }

}
