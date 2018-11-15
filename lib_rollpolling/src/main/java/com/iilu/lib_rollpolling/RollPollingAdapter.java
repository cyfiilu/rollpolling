package com.iilu.lib_rollpolling;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyf on 2018/1/8.
 */
public class RollPollingAdapter extends PagerAdapter {

    private Context mContext;
    private List<ImageView> mImgList = new ArrayList<>();

    public RollPollingAdapter(Context context, List<ImageView> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
    }

    public void updateDatas(List<ImageView> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImgList.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //if (position > mImgList.size() - 1) return;
        //container.removeView(mImgList.get(position));
    }
}