package com.yigu.opentable.adapter.campaign;

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
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/10.
 */
public class CampaignTypeAdapter extends RecyclerView.Adapter<CampaignTypeAdapter.ViewHolder> {

    LayoutInflater inflater;

    RecyOnItemClickListener recyOnItemClickListener;

    List<MapiResourceResult> mList = new ArrayList<>();

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public CampaignTypeAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_campaing_type, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiResourceResult result = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });


        holder.nameTv.setText(TextUtils.isEmpty(result.getNAME())?"":result.getNAME());

        switch (result.getId()){
            case 1:
                holder.typeIv.setImageResource(R.mipmap.type_one);
                break;
            case 2:
                holder.typeIv.setImageResource(R.mipmap.type_two);
                break;
            case 3:
                holder.typeIv.setImageResource(R.mipmap.type_three);
                break;
            case 4:
                holder.typeIv.setImageResource(R.mipmap.type_four);
                break;
            case 5:
                holder.typeIv.setImageResource(R.mipmap.type_five);
                break;
            case 6:
                holder.typeIv.setImageResource(R.mipmap.type_six);
                break;
            case 7:
                holder.typeIv.setImageResource(R.mipmap.type_seven);
                break;
            case 8:
                holder.typeIv.setImageResource(R.mipmap.type_eight);
                break;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.type_iv)
        ImageView typeIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
