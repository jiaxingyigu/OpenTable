package com.yigu.opentable.adapter.platform;

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
import com.yigu.commom.result.MapiPlatformResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/1/12.
 */
public class PlatFormAdapter extends RecyclerView.Adapter<PlatFormAdapter.ViewHolder> {

    LayoutInflater inflater;

    List<MapiPlatformResult> mList = new ArrayList<>();

    RecyOnItemClickListener recyOnItemClickListener;


    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public PlatFormAdapter(Context context, List<MapiPlatformResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_platform, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiPlatformResult mapiPlatformResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position  = (int) view.getTag();
                if(null!=recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view,position);
            }
        });

        holder.dateTv.setText(TextUtils.isEmpty(mapiPlatformResult.getCreated())?"":mapiPlatformResult.getCreated());
        holder.contentTv.setText(TextUtils.isEmpty(mapiPlatformResult.getTitle())?"":mapiPlatformResult.getTitle());

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + mapiPlatformResult.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(76), DPUtil.dip2px(76)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.content_tv)
        TextView contentTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
