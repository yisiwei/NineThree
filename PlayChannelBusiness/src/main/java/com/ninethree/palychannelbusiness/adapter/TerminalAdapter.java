package com.ninethree.palychannelbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninethree.palychannelbusiness.R;
import com.ninethree.palychannelbusiness.bean.Terminal;
import com.ninethree.palychannelbusiness.view.CircleImageView;

import java.util.List;

public class TerminalAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Terminal> mDatas;
    private Context mContext;

    public TerminalAdapter(Context context, List<Terminal> datas) {
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
            convertView = mInflater.inflate(R.layout.item_terminal, parent, false);

            holder.mTermina = (TextView) convertView.findViewById(R.id.termina_code);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            holder.mOrgName = (TextView) convertView.findViewById(R.id.orgName);
            holder.mPduName = (TextView) convertView.findViewById(R.id.pduName);
            holder.mUpdateTime = (TextView) convertView.findViewById(R.id.update_time);
            holder.mEditTime = (TextView) convertView.findViewById(R.id.edit_time);
            holder.mUsername = (TextView) convertView.findViewById(R.id.username);
            holder.mHeadImg = (CircleImageView) convertView.findViewById(R.id.headImg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Terminal termina = mDatas.get(position);

        holder.mTermina.setText("设备："+termina.getTerminalNum());
        holder.mStatus.setText("状态："+termina.getDelFlag());
        holder.mOrgName.setText("商家："+termina.getOrgName());
        holder.mPduName.setText("产品："+termina.getPduName());
        holder.mUpdateTime.setText("访问时间："+termina.getUpdateTime());
        holder.mUsername.setText("配置者："+termina.getUserName());
        holder.mEditTime.setText("配置时间："+termina.getEditTime());

        Glide.with(mContext)
                .load(termina.getPhoto())
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_head)
                .into(holder.mHeadImg);

        return convertView;
    }

    class ViewHolder {
        public TextView mTermina;
        public TextView mStatus;
        public TextView mOrgName;
        public TextView mPduName;
        public TextView mUpdateTime;
        public TextView mEditTime;
        public TextView mUsername;
        public CircleImageView mHeadImg;
    }

}
