package com.yigu.opentable.adapter.campaign;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/11.
 */
public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder> {

    LayoutInflater inflater;

    RecyOnItemClickListener recyOnItemClickListener;

    List<MapiCampaignResult> mList = new ArrayList<>();


    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public CampaignAdapter(Context context, List<MapiCampaignResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_campaign, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiCampaignResult result = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        if(!TextUtils.isEmpty(result.getSpic()))
            holder.image.setImageURI(Uri.parse(BasicApi.BASIC_IMAGE+""+result.getSpic()));
        holder.name.setText(result.getName());
        holder.content.setText(result.getBz());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.content)
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
