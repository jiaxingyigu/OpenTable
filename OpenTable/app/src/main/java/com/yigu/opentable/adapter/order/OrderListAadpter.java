package com.yigu.opentable.adapter.order;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.view.PurcaseSheetLayout;
import com.yigu.opentable.widget.BuyAnimUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/1.
 */
public class OrderListAadpter extends RecyclerView.Adapter<OrderListAadpter.ViewHolder> {

    LayoutInflater inflater;


    private List<MapiOrderResult> mList;

    RecyOnItemClickListener recyOnItemClickListener;

    private ImageView buyImg;// 这是在界面上跑的小图片

    private Context mContext;

    private BaseActivity activity;

    public void setShopCart(View shopCart) {
        this.shopCart = shopCart;
    }

    private View shopCart;//购物车

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public OrderListAadpter(Context context, List<MapiOrderResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
        activity = (BaseActivity) context;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiOrderResult mapiOrderResult =  mList.get(position);
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + mapiOrderResult.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(76), DPUtil.dip2px(76)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.name.setText(mapiOrderResult.getFOOD());
        holder.account.setText("剩余数量："+(TextUtils.isEmpty(mapiOrderResult.getAMOUNT())?"0":mapiOrderResult.getAMOUNT())+"份");
        holder.price.setText("价格：¥"+ (TextUtils.isEmpty(mapiOrderResult.getPRICE())?"0":mapiOrderResult.getPRICE()));
        String numStr = TextUtils.isEmpty(mapiOrderResult.getNum())?"0":mapiOrderResult.getNum();
        holder.purcaseSheetLayout.setNum(Integer.parseInt(numStr));
        holder.purcaseSheetLayout.setTag(position);
        holder.purcaseSheetLayout.setNunerListener(new PurcaseSheetLayout.NumberListener() {
            @Override
            public void numerAdd(View view,View rootView) {
                int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                view.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                buyImg = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                buyImg.setImageResource(R.drawable.sign);// 设置buyImg的图片
                BuyAnimUtil.setAnim(buyImg,shopCart,activity,start_location);// 开始执行动画
                if(null!=numberListener)
                    numberListener.numerAdd(view, (Integer) rootView.getTag());
            }

            @Override
            public void numberCut(View view,View rootView) {
                if(null!=numberListener)
                    numberListener.numberCut(view, (Integer) rootView.getTag());
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout root_view;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.account)
        TextView account;
        @Bind(R.id.purcaseSheetLayout)
        PurcaseSheetLayout purcaseSheetLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private  NumberListener numberListener;

    public interface NumberListener{
        void numerAdd(View view,int position);
        void numberCut(View view,int position);
    }

    public void setNunerListener(NumberListener numberListener){
        this.numberListener = numberListener;
    }


}
