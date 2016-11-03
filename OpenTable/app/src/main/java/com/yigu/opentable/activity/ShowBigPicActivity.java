package com.yigu.opentable.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/8/3.
 */
public class ShowBigPicActivity extends BaseActivity {

    @Bind(R.id.lay_header)
    RelativeLayout headerLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private int pos = 0;
    private List<MapiImageResult> mList;
    DisplayImageOptions options = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_bigpic);
        ButterKnife.bind(this);
        if (null != getIntent().getExtras()) {
            pos = getIntent().getIntExtra("position", 0);
            mList = (List<MapiImageResult>) getIntent().getSerializableExtra("list");
        }
        initView();
    }

    private void initView() {
        headerLayout.setBackgroundColor(Color.parseColor("#0D000000"));
        viewpager.setAdapter(new ViewPagerAdapter());
        viewpager.setCurrentItem(pos);
        initOptions();
    }

    private void initOptions(){
        options = new DisplayImageOptions.Builder()
                //				.showImageOnLoading(R.drawable.design_default)
                .showImageForEmptyUri(R.mipmap.default_item)
                .showImageOnFail(R.mipmap.default_item)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.NONE)//EXACTLY
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())//Bitmap.Config.RGB_565
                .bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList==null?0:mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View channelView = null;
            try{
                channelView = LayoutInflater.from(ShowBigPicActivity.this).inflate(
                        R.layout.viewpager_bigpic,container,false);
                //GestureImageView
                final GestureImageView mImageView = (GestureImageView) channelView
                        .findViewById(R.id.pager_img); //手势缩放类
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                final ProgressBar progressBar = (ProgressBar)channelView.findViewById(R.id.image_zoom_progressbar);
                ImageLoader.getInstance().displayImage(
                        BasicApi.BASIC_IMAGE+mList.get(position).getPATH(), mImageView, options,new ImageLoadingListener() {

                            @Override
                            public void onLoadingCancelled(String arg0, View arg1) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingStarted(String arg0, View arg1) {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                        }
                );
                container.addView(channelView);
            }catch(Exception e){
                e.printStackTrace();
            }
            return channelView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    protected void onDestroy() {
        //退出当前页面清除内存
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearMemoryCache();
        super.onDestroy();
    }

}
