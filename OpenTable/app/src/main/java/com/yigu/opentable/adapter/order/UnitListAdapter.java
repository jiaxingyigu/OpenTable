package com.yigu.opentable.adapter.order;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yigu.commom.result.MapiHistoryResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/27.
 */
public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<MapiHistoryResult> mList = new ArrayList<>();
    RecyOnItemClickListener recyOnItemClickListener;

    String type = "0";

    public void setType(String type) {
        this.type = type;
    }

    boolean cancel = false;

    public void setCancel(boolean cancel){
        this.cancel = cancel;
    }

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    CancelOnItemClickListener cancelOnItemClickListener;

    public interface CancelOnItemClickListener {
        void onItemCancelClick(View view, int position);
    }

    public void setRecyOnItemCancelClickListener(CancelOnItemClickListener cancelOnItemClickListener) {
        this.cancelOnItemClickListener = cancelOnItemClickListener;
    }

    public UnitListAdapter(Context context, List<MapiHistoryResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_unit_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MapiHistoryResult itemResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.date.setText("下单时间："+itemResult.getCreated());

        if(TextUtils.isEmpty(itemResult.getStardate2())){
            holder.takeTime.setVisibility(View.GONE);
        }else{
            holder.takeTime.setVisibility(View.VISIBLE);
            holder.takeTime.setText("取货时间："+itemResult.getStardate2());
        }

        holder.price.setText("总价：¥" + itemResult.getPrice());
        holder.name.setText(itemResult.getName());
        holder.tel.setText("电话：" + itemResult.getTel());

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + itemResult.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(80), DPUtil.dip2px(80)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.salesno.setText("订单号："+itemResult.getSalesno());
        holder.zhifu.setText(itemResult.getZhifu());
        holder.cancelTv.setTag(position);
        holder.cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=cancelOnItemClickListener)
                    cancelOnItemClickListener.onItemCancelClick(view, (Integer) view.getTag());
            }
        });
        if(cancel){
            holder.cancelTv.setVisibility(View.VISIBLE);

        }else{
            holder.cancelTv.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.tel)
        TextView tel;
        @Bind(R.id.salesno)
        TextView salesno;
        @Bind(R.id.zhifu)
        TextView zhifu;
        @Bind(R.id.takeTime)
        TextView takeTime;
        @Bind(R.id.cancel_tv)
        TextView cancelTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
