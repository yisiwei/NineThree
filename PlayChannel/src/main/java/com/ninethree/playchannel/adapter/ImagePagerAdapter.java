package com.ninethree.playchannel.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ninethree.playchannel.activity.WebViewActivity;
import com.ninethree.playchannel.util.ListUtils;


/**
 * ImagePagerAdapter
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<Integer> imageIdList;
//	private List<String> imageIdList;

    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<Integer> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
                .getSize(imageIdList);
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            ImageView imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // view = holder.imageView = new ImageView(context);
            view = imageView;
            holder.imageView = (ImageView) view;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imageView.setImageResource(imageIdList.get(getPosition(position)));

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                String url = null;
                switch (getPosition(position)) {
                    case 0:
                        url = "http://mp.weixin.qq.com/s?__biz=MzI4NjM2MjMxNQ==&mid=2247483663&idx=1&sn=8f9c0a7e0578a7778fc8bc95cb954bfb&scene=0#wechat_redirect";
                        break;
                    case 1:
                        url = "http://shop.93966.net/h5Store/home/index?s=240816";
                        break;
                    case 2:
                        url = "http://shop.93966.net/h5Store/home/index?s=240900";
                        break;
                    case 3:
                        url = "http://shop.93966.net/H5Store/News/Detail?s=240818&id=66";
                        break;
                }
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

//		MyApplication.getImageLoader().displayImage(
//				imageIdList.get(getPosition(position)), holder.imageView,
//				MyApplication.getDisplayImageOptions());

        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
