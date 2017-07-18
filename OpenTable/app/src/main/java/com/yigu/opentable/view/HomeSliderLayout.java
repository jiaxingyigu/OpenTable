package com.yigu.opentable.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.ShopPagerAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.util.CountDownTimerUtil;
import com.yigu.opentable.widget.LoopViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/8/30.
 */
public class HomeSliderLayout extends RelativeLayout {
    @Bind(R.id.index_viewpager)
    LoopViewPager indexViewpager;
    @Bind(R.id.guide_dot)
    LinearLayout guideDot;

    private Context mContext;
    private View view;
    CountDownTimerUtil countDownTimerUtil;
    BaseActivity activity;
    private boolean isSlider = false;
    private boolean isSliderPlay = false;
    List<MapiResourceResult> imgs;
    public void setSlider(boolean isSlider){
        this.isSlider = isSlider;
    }

    public HomeSliderLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_slider, this);
        ButterKnife.bind(this, view);
        imgs = new ArrayList<>();
    }

    public void load(final List<MapiResourceResult> list) {
        if(null!=countDownTimerUtil) {
            countDownTimerUtil.cancel();
        }
        if (null != list) {
            imgs.clear();
            imgs.addAll(list);

        }
       /* for (int i = 0; i < list.size(); i++) {

            SimpleDraweeView  view = (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.item_image,null);
            view.setImageURI(Uri.parse(BasicApi.BASIC_IMAGE+list.get(i).getPATH()));
//            SimpleDraweeView view = new SimpleDraweeView(mContext);
//            view.setImageResource(R.mipmap.default_item_big);
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = list.get((Integer) v.getTag()).getUrl();
                    if(!TextUtils.isEmpty(url))
                        ControllerUtil.go2WebView(url,"网页详情","","","",false);
                }
            });
            sliderViewList.add(view);
        }*/
        ImagePagerAdapter sliderAdapter = new ImagePagerAdapter(imgs);
        indexViewpager.setAdapter(sliderAdapter);

        guideDot.removeAllViews();
        for (int i = 0; i < imgs.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPUtil.dip2px(8), DPUtil.dip2px(8));
            params.setMargins(DPUtil.dip2px(6), 0, DPUtil.dip2px(6), DPUtil.dip2px(6));
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.selector_item_dot);
            guideDot.addView(imageView);
        }
        guideDot.getChildAt(0).setSelected(true);
        indexViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(null!=guideDot){
                    //这是重点
                    int newPosition = (position - 1 + imgs.size())%imgs.size();
                    //修改全部的position长度
//                    int newPosition = position % sliderViewList.size();

                    for (int i = 0; i < imgs.size(); i++) {
                        if (newPosition == i) {
                            if(null!= guideDot.getChildAt(i))
                                guideDot.getChildAt(i).setSelected(true);
                        }else {
                            if(null!= guideDot.getChildAt(i))
                                guideDot.getChildAt(i).setSelected(false);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        if(isSlider) {
           /* ViewPagerScroller viewPagerScroller = new ViewPagerScroller(getContext(),new AccelerateDecelerateInterpolator());
            //调整速率
            viewPagerScroller.setScrollDuration(200);
            viewPagerScroller.initViewPagerScroll(indexViewpager);           //初始化ViewPager时,反射修改滑动速度*/
            isSliderPlay = true;
            slideImage();
        }

    }

    private void slideImage(){

        if(null!=countDownTimerUtil)
            countDownTimerUtil.start();
        else{
            countDownTimerUtil = new CountDownTimerUtil(5 * 1000, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    if(null==mContext||null==activity||activity.isFinishing()||getVisibility()==GONE||getVisibility()==INVISIBLE) {
                        DebugLog.i("CountDownTimerUtil==cancel");
                        isSliderPlay = false;
                        cancel();
                    }
                }

                @Override
                public void onFinish() {

                    //这里是设置当前页的下一页
                    indexViewpager.setCurrentItem(indexViewpager.getCurrentItem()+1,true);
                    indexViewpager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isSliderPlay)
                                slideImage();
                        }
                    }, 500);

                }
            }.start();
        }

    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<MapiResourceResult> imageViewList ;

        public ImagePagerAdapter(List<MapiResourceResult> imageViewList) {
            this.imageViewList = imageViewList;
        }

        @Override
        public int getCount() {
            return null==imageViewList?0:imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.item_image,null);

            String url = TextUtils.isEmpty(imageViewList.get(position%imageViewList.size()).getPATH())?"":imageViewList.get(position%imageViewList.size()).getPATH();

            String imgUrl = BasicApi.BASIC_IMAGE + url;

            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(imgUrl);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setResizeOptions(new ResizeOptions(DPUtil.dip2px(375), DPUtil.dip2px(200)))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(view.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            view.setController(controller);

            view.setTag(position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = imageViewList.get((Integer) v.getTag()).getUrl();
                    if(!TextUtils.isEmpty(url))
                        ControllerUtil.go2WebView(url,"网页详情","","","",false);
                }
            });
            ((ViewPager) container).addView(view, 0);
            return view;

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //第三处修改，移除的索引为集合的长度
//            int newPosition = position % imageViewList.size();
//            container.removeView(imageViewList.get(newPosition));
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!hasWindowFocus) {
            DebugLog.i("onWindowFocusChanged=>"+hasWindowFocus);
            DebugLog.i("CountDownTimerUtil==cancel");
            isSliderPlay = false;
            countDownTimerUtil.cancel();
            countDownTimerUtil = null;
        }else{
            isSliderPlay = true;
            if(null==countDownTimerUtil){
                slideImage();
            }

        }

    }

}
