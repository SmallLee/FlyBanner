package com.fang.flybanner;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.flybanner.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hecun on 2016/8/25.
 */
public class FlyBanner extends RelativeLayout {
    private static final String TAG = "FlyBanner";
    /**默认延迟时间*/
    public static final long DEFAULT_DELAYTIME = 2;
    /**默认轮播间隔时间*/
    public static final long DEFAULT_INTERVALTIME = 2;
    /**触摸时是否停止轮播*/
    public static final boolean STOP_WHEN_TOUCH = true;
    /**起始时是否可以相左滑动*/
    public static final boolean LEFT_SCROLL = false;
    /**默认圆点指示器样式*/
    public static final int DEFAULT_DRAWABLE_ID = R.drawable.rb_selector;
    /**默认圆点指示器位置*/
    public static final int DEFAULT_INDICATOR_GRAVITY = Gravity.CENTER_HORIZONTAL;
    /*默认圆点指示器半径*/
    public static final float DEFAULT_INDICATOR_RADIUS = 20;
    /**默认指示器距离左边距离*/
    public static final float DEFAULT_INDICATOR_MARGIN_LEFT = 15;
    /**默认指示器距离下边距离*/
    public static final float DEFAULT_INDICATOR_MARGIN_BOTTOM = 20;
    /**默认指示器距离右边距离*/
    public static final float DEFAULT_INDICATOR_MARGIN_RIGHT = 15;
    /**默认指示器之间的间隔距离*/
    public static final float DEFAULT_INDICATOR_SPACING = 15;


    private Context mContext;
    private static int mIndex;
    private Timer bannerTimer = new Timer();
    private long mIntervalTime = 0;
    private long mDelayTime = 0;
    private boolean mStopWhenTouch;
    private boolean mLeftScroll;
    private boolean mIsContinue = true;
    private BannerHandler mHandler ;
    private int mDrawableId = DEFAULT_DRAWABLE_ID;
    private List<? extends BannerItem> mBannerItems;
    private List<View> mViews = new ArrayList<>();
    private float mIndicatorRadius = DEFAULT_INDICATOR_RADIUS;
    private int mIndicatorGravity = DEFAULT_INDICATOR_GRAVITY;
    private static ViewPager viewPager;
    private LinearLayout linearLayout;
    private int preIndex = 0;
    private float mLeftMaring = DEFAULT_INDICATOR_MARGIN_LEFT;
    private float mRightMaring = DEFAULT_INDICATOR_MARGIN_RIGHT;
    private float mBottomMaring = DEFAULT_INDICATOR_MARGIN_BOTTOM;
    private float mIndicatorSpacing = DEFAULT_INDICATOR_SPACING;

    public  static class BannerHandler extends Handler {
        //弱引用防止Handler泄漏
        private WeakReference<Activity> weakReference;
        public BannerHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                viewPager.setCurrentItem(++mIndex);
            }
            super.handleMessage(msg);
        }
    }
//    //指示器的位置，默认为中间
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int RIGTH = 2;

    public FlyBanner(Context context) {
        this(context,null);
    }

    public FlyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHandler = new BannerHandler((Activity) mContext);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlyBanner);
        mDelayTime = (long) typedArray.getFloat(R.styleable.FlyBanner_delayTime, DEFAULT_DELAYTIME);
        mIntervalTime = (long) typedArray.getFloat(R.styleable.FlyBanner_intervalTime, DEFAULT_INTERVALTIME);
        mStopWhenTouch = typedArray.getBoolean(R.styleable.FlyBanner_stopWhenTouch, STOP_WHEN_TOUCH);
        mLeftScroll = typedArray.getBoolean(R.styleable.FlyBanner_delayTime, LEFT_SCROLL);
        typedArray.recycle();
    }

    /**
     * 设置轮播图的数据来源
     * @param bannerItems 数据集合
     * @return
     */
    public FlyBanner setSource(List<? extends BannerItem> bannerItems){
        mBannerItems = bannerItems;
        initContent();
        addListener();
        if(mLeftScroll){
            viewPager.setCurrentItem(mBannerItems.size()*100);
        }
        viewPager.setAdapter(pagerAdapter);
        initIndicator(mBannerItems.size());
        return this;
    }

    public int getIndicatorGravity(int mIndicatorGravity){
        switch (mIndicatorGravity){
            case LEFT:
                return RelativeLayout.ALIGN_PARENT_LEFT;
            case CENTER:
                return RelativeLayout.CENTER_HORIZONTAL;
            case RIGTH:
                return RelativeLayout.ALIGN_PARENT_RIGHT;
            default:
                return RelativeLayout.CENTER_HORIZONTAL;
        }
    }


    /**
     * 设置轮播延迟时间
     * @param delayTime 延迟时间
     * @return
     */
    public FlyBanner setDelayTime(long delayTime){
        this.mDelayTime = delayTime;
        return this;
    }

    /**
     * 设置指示器距离四周的距离
     * @param leftMarging 左边距
     * @param rightMarging 右边距
     * @param bottomMarging 下边距
     */
    public FlyBanner setMargins(float leftMarging,float rightMarging,float bottomMarging){
        if(mIndicatorGravity!=FlyBanner.CENTER){
            this.mLeftMaring = leftMarging;
            this.mRightMaring = rightMarging;
        }
        this.mBottomMaring = bottomMarging;
        return this;
    }

    /**
     * 设置轮播图间隔时间
     * @param intervalTime 间隔时间
     * @return
     */
    public FlyBanner setIntervalTime(long intervalTime){
        this.mIntervalTime = intervalTime;
        return this;
    }

    /**
     * 设置起始是否可以左滑
     * @param leftScroll
     * @return
     */
    public FlyBanner setLeftScroll(boolean leftScroll){
        this.mLeftScroll = leftScroll;
        return this;
    }

    /**
     * 触摸时是否停止轮播
     * @param stopWhenTouch 是否停止轮播
     * @return
     */
    public FlyBanner setStopWhenTouch(boolean stopWhenTouch){
        this.mStopWhenTouch = stopWhenTouch;
        return this;
    }

    /**
     * 设置指示器的样式
     * @param drawableId 指示器资源
     * @param radius 指示器半径
     * @param spacing 指示器之间的间距
     * @param gravity 指示器位置
     * @return
     */
    public FlyBanner setIndicator(int drawableId,int radius,int spacing,int gravity){
        mDrawableId = drawableId;
        mIndicatorRadius = radius;
        mIndicatorSpacing = spacing;
        mIndicatorGravity = gravity;
        return this;
    }

    public void initContent(){
        viewPager = new ViewPager(mContext);
        ViewGroup.LayoutParams pagerParams = new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(viewPager,pagerParams);
        linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (int) mLeftMaring;
        layoutParams.bottomMargin = (int) mBottomMaring;
        layoutParams.rightMargin = (int) mRightMaring;
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(getIndicatorGravity(mIndicatorGravity));
        addView(linearLayout,layoutParams);
    }

    public void addListener(){
        viewPager.setOnTouchListener(onTouchListener);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    public void startScroll(){
        bannerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mIsContinue){
                    mHandler.obtainMessage().sendToTarget();
                }
            }//mDelayTime不能<=0,否则会报非法参数异常
        },mDelayTime*1000,mIntervalTime*1000);

    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //手指按下和划动的时候停止图片的轮播
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (mStopWhenTouch) {
                        mIsContinue = false;
                    }
                    break;
                default:
                    mIsContinue = true;
            }
            return false;
        }
    };


    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //因为position非常大，而我们需要的position不能大于图片集合长度所以在此取余
            position = position % mBannerItems.size();
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(mBannerItems.get(position).imageUrl)
                    .into(imageView);
            container.addView(imageView);
            mViews.add(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(!mLeftScroll){
                container.removeView(mViews.get(position));
            }
        }
    };

    /**
     * 初始化ViewPager指示器
     * @param size
     */
    public void initIndicator(int size){
        for (int i = 0; i < size; i++) {
            ImageView imageview = new ImageView(mContext);
            if(size == 1){
                //只有一张图片的情况下不显示指示器
                imageview.setVisibility(View.GONE);
                return;
            }
            imageview.setImageResource(mDrawableId);//设置背景选择器
            //将按钮依次添加到RadioGroup中
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = (int) mIndicatorRadius;
            params.height = (int) mIndicatorRadius;
            params.leftMargin = (int) mIndicatorSpacing;
            linearLayout.addView(imageview,params);
            //默认选中第一个按钮，因为默认显示第一张图片
            ((ImageView)(linearLayout.getChildAt(0))).setImageResource(R.drawable.iv_selected);
        }
    }



    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            mIndex = position;
            setCurrentIndicator(position%mBannerItems.size());
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public void setCurrentIndicator(int position){
        if (linearLayout.getChildAt(position) != null) {
            ((ImageView)(linearLayout.getChildAt(position))).setImageResource(R.drawable.iv_selected);//当前按钮选中,显示蓝色
        }
        if (linearLayout.getChildAt(preIndex) != null) {
            ((ImageView)(linearLayout.getChildAt(preIndex))).setImageResource(R.drawable.iv_unselected);//上一个取消选中。显示灰色
            preIndex = position;//当前位置变为上一个，继续下次轮播
        }
    }


}
