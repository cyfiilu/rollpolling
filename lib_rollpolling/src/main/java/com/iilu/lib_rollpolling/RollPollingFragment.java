package com.iilu.lib_rollpolling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片轮播
 * Created by chenyf on 2018/1/4.
 */
public class RollPollingFragment extends Fragment implements View.OnClickListener {

    private static final int MSG_EMPTY = 0x0001;
    /**最多轮播图片*/
    private static final int MAX_CAROUSEL_PICTRUE = 6;

    /** 轮播时长 */
    private long mRollPollingPeriod = 3000L;
    private float mCornerRadius = 5f;
    private int mIndicatorGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mIndicatorPadding = 5;
    private int mStartPosition = 1;
    private int mPreIndex;

    private Context mContext;
    private FrameLayout mLayoutContainer;
    private CustomViewPager mViewPager;
    private RollPollingAdapter mAdAdapter;
    private RadioGroup mRadioGroup;
    private List<ImageView> mImgList = new ArrayList<>();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private OnRollPollingClickListener mOnRollPollingClickListener;

    /**
     * 点击图片的回调接口
     */
    public interface OnRollPollingClickListener {
        void onRollPollingClick(int position);
    }

    /**
     * 设置点击图片回调
     * @param listener
     */
    public void setOnRollPollingClickListener(OnRollPollingClickListener listener) {
        this.mOnRollPollingClickListener = listener;
    }

    private Handler mHandler  = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_EMPTY:
                    int currIndex = mViewPager.getCurrentItem();
                    currIndex = ++currIndex % mImgList.size();
                    mViewPager.setCurrentItem(currIndex);
            }
            return false;
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mCornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCornerRadius, mContext.getResources().getDisplayMetrics());
        this.mIndicatorPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorPadding, mContext.getResources().getDisplayMetrics());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rollpolling, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mLayoutContainer = (FrameLayout) view.findViewById(R.id.fl_container);
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mAdAdapter = new RollPollingAdapter(mContext, mImgList);
        mViewPager.setAdapter(mAdAdapter);
        mViewPager.setPagerEnabled(false);
    }

    private void showUI() {
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .show(RollPollingFragment.this)
                .commitAllowingStateLoss();
    }

    /**
     * 开始轮播
     * @param imgAddressArr 轮播图片路径  length至少为1
     * @param indicatorResId 指示器资源id  -1：不创建指示器
     */
    public void startRollPolling(String[] imgAddressArr, int indicatorResId) {
        if (imgAddressArr == null || imgAddressArr.length < 1) return;

        String[] tempArr;
        if (imgAddressArr.length > MAX_CAROUSEL_PICTRUE) {
            tempArr = new String[MAX_CAROUSEL_PICTRUE];
            System.arraycopy(imgAddressArr, 0, tempArr, 0, MAX_CAROUSEL_PICTRUE);
        } else {
            tempArr = imgAddressArr;
        }

        String[] newImgAddressArr = new String[tempArr.length + 2];
        newImgAddressArr[0] = tempArr[tempArr.length - 1];
        for (int i = 0; i < tempArr.length; i++) {
            newImgAddressArr[i + 1] = tempArr[i];
        }
        newImgAddressArr[newImgAddressArr.length - 1] = tempArr[0];

        downloadImage(newImgAddressArr, indicatorResId);
    }

    private void downloadImage(String[] newImgAddressArr, int indicatorResId) {
        if (getActivity() == null || getActivity().isDestroyed()) return;
        if (!isAdded()) return;
        new DownImgAsyncTask(this, indicatorResId).execute(newImgAddressArr);
    }

    private void handleData(List<Bitmap> bitmapList) {
        mImgList.clear();
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            drawable.setCornerRadius(mCornerRadius);
            drawable.setAntiAlias(true);
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable(drawable);
            if (i > 0 && i < bitmapList.size() - 1) {
                imageView.setOnClickListener(this);
                imageView.setId(i); // 将position的位置作为Id的参数
            }
            mImgList.add(imageView);
        }
    }

    private void updateUI(int indicatorResId) {
        stopTimerTask();
        mViewPager.removeAllViews();
        mAdAdapter.updateDatas(mImgList);
        mPreIndex = 0;
        initRadioButton(mImgList.size(), indicatorResId);
        if (mImgList.size() > 3) {
            mViewPager.setPagerEnabled(true);
            mViewPager.addOnPageChangeListener(mOnPagerChangeListener);
            mViewPager.setOnTouchListener(mOnTouchListener);
            mViewPager.setCurrentItem(mStartPosition, false);
            setCurrentIndicator(mViewPager.getCurrentItem());
            startTimerTask();
        } else {
            mViewPager.setPagerEnabled(false);
            mViewPager.addOnPageChangeListener(null);
            mViewPager.setOnTouchListener(null);
            mViewPager.setCurrentItem(mStartPosition, false);
        }
    }

    private void initRadioButton(int count, int indicatorResId) {
        if (count <= 0 || indicatorResId <= 0) return;
        mRadioGroup.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageview = new ImageView(mContext);
            // 设置背景选择器
            imageview.setImageResource(indicatorResId);
            // 初始化每个dot的enabled为false
            imageview.setEnabled(false);
            // 设置每个按钮之间的间距
            imageview.setPadding(mIndicatorPadding, 0, mIndicatorPadding, 0);
            // 将第一个和最后一个设置为不可见
            if (i == 0 || i == count - 1) {
                imageview.setVisibility(View.INVISIBLE);
            }
            // 将按钮依次添加到RadioGroup中
            mRadioGroup.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 默认选中第一个按钮，因为默认显示第一张图片
            mRadioGroup.getChildAt(0).setEnabled(true);
            // 设置Indicator位置（只能下边：左、中、右）
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRadioGroup.getLayoutParams();
            lp.gravity = mIndicatorGravity;
            mRadioGroup.setLayoutParams(lp);
        }
    }

    private void setCurrentIndicator(int i) {
        if (mPreIndex == i) return;
        View currView = mRadioGroup.getChildAt(i);
        if (currView != null) {
            currView.setEnabled(true); // 当前按钮选中
        }
        View preView = mRadioGroup.getChildAt(mPreIndex);
        if (preView != null) {
            // 取消上一个选中
            preView.setEnabled(false);
            // 当前位置变为上一个，继续下次轮播
            mPreIndex = i;
        }
    }

    private void startTimerTask() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(MSG_EMPTY);
                }
            };
            mTimer.schedule(mTimerTask, mRollPollingPeriod, mRollPollingPeriod);
        }
    }

    private void stopTimerTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
            mTimerTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRollPollingClickListener != null) {
            mOnRollPollingClickListener.onRollPollingClick(v.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
    }

    private static class DownImgAsyncTask extends AsyncTask<String, Void, List<Bitmap>> {

        private WeakReference<RollPollingFragment> mWeekReference;
        private int mIndicatorResId;

        public DownImgAsyncTask(RollPollingFragment fragment, int indicatorResId) {
            this.mWeekReference = new WeakReference<>(fragment);
            this.mIndicatorResId = indicatorResId;
        }

        @Override
        protected List<Bitmap> doInBackground(String... params) {
            List<Bitmap> bitmapList = new ArrayList<>();
            for (int i = 0; i < params.length; i++) {
                if (params != null && params[i] != null) {
                    String imgAddress = params[i].toString();
                    Bitmap bitmap = RollPollingUtil.getBitmapFromUrl(imgAddress);
                    if (bitmap != null) {
                        bitmapList.add(bitmap);
                    } else {
                        RollPollingFragment rpFragment = mWeekReference.get();
                        if (rpFragment != null) {
                            bitmapList.add(BitmapFactory.decodeResource(rpFragment.getResources(), R.mipmap.img_default));
                        }
                    }
                }
            }
            return bitmapList;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmapList) {
            super.onPostExecute(bitmapList);
            RollPollingFragment rpFragment = mWeekReference.get();
            if (rpFragment != null) {
                // 处理数据
                rpFragment.handleData(bitmapList);
                // 更新界面
                rpFragment.updateUI(mIndicatorResId);
                // 将此界面显示出来
                // 放在最后的原因：图片下载需要一点时间，避免图片加载过程中，界面的短暂空白
                rpFragment.showUI();
            }
        }
    }

    private ViewPager.OnPageChangeListener mOnPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    if (mViewPager.getCurrentItem() == 0) {
                        mViewPager.setCurrentItem(mImgList.size() - 2, false);
                    } else if (mViewPager.getCurrentItem() == mAdAdapter.getCount() - 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                    setCurrentIndicator(mViewPager.getCurrentItem());
                    break;
            }
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                // 手指按下和划动的时候停止图片的轮播
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    stopTimerTask();
                    break;
                default:
                    stopTimerTask();
                    startTimerTask();
            }
            return false;
        }
    };

    /**
     * 设置图片的高度
     * @param height
     */
    public RollPollingFragment setImgHeight(int height) {
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                height, mContext.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayoutContainer.getLayoutParams();
        lp.height = height;
        mLayoutContainer.setLayoutParams(lp);
        return this;
    }

    /**
     * 设置图片的左右边距
     * @param imgMargin
     */
    public RollPollingFragment setImgMargin(int imgMargin) {
        imgMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                imgMargin, mContext.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayoutContainer.getLayoutParams();
        lp.leftMargin = imgMargin;
        lp.rightMargin = imgMargin;
        mLayoutContainer.setLayoutParams(lp);
        return this;
    }

    /**
     * 设置图片圆角弧度
     * @param cornerRadius
     */
    public RollPollingFragment setCornerRadius(float cornerRadius) {
        this.mCornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                cornerRadius, mContext.getResources().getDisplayMetrics());
        return this;
    }

    /**
     * 设置指示器的位置
     * @param indicatorGravity
     */
    public RollPollingFragment setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        return this;
    }

    /**
     * 设置指示器的左右边距
     * @param indicatorPadding
     */
    public RollPollingFragment setIndicatorPadding(int indicatorPadding) {
        this.mIndicatorPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                indicatorPadding, mContext.getResources().getDisplayMetrics());
        return this;
    }

    /**
     * 设置开始轮播的位置，默认是第一个
     * @param startPosition
     */
    public RollPollingFragment setStartPosition(int startPosition) {
        this.mStartPosition = startPosition;
        return this;
    }

    /**
     * 轮播间隔
     * @param rollPollingPeriod 单位：ms
     * @return
     */
    public RollPollingFragment setRollPollingPeriod(long rollPollingPeriod) {
        this.mRollPollingPeriod = rollPollingPeriod;
        return this;
    }
}
