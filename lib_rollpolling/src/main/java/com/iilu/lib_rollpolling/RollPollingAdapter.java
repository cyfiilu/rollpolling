package com.iilu.lib_rollpolling;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by chenyf on 2018/1/8.
 */
public class RollPollingAdapter extends PagerAdapter {

    private List<ImageView> mImgList;

    RollPollingAdapter(List<ImageView> imgList) {
        this.mImgList = imgList;
    }

    void updateDatas(List<ImageView> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = mImgList.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        //if (position > mImgList.size() - 1) return;
        //container.removeView(mImgList.get(position));
    }
}