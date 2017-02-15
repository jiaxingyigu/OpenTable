package com.yigu.opentable.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.ShopPagerAdapter;
import com.yigu.opentable.util.ControllerUtil;

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
    ViewPager indexViewpager;
    @Bind(R.id.guide_dot)
    LinearLayout guideDot;

    private Context mContext;
    private View view;
    List<View> sliderViewList;

    public HomeSliderLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public HomeSliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_slider, this);
        ButterKnife.bind(this, view);
    }

    public void load(final List<MapiResourceResult> list) {
        sliderViewList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

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
        }
        ShopPagerAdapter sliderAdapter = new ShopPagerAdapter(sliderViewList);
        indexViewpager.setAdapter(sliderAdapter);
        guideDot.removeAllViews();
        for (int i = 0; i < sliderViewList.size(); i++) {
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
                for (int i = 0; i < sliderViewList.size(); i++) {
                    if (position == i)
                        guideDot.getChildAt(i).setSelected(true);
                    else
                        guideDot.getChildAt(i).setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

}
