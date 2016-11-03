package com.yigu.opentable.adapter.campaign;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/20.
 */
public class SelJobAdapter extends RecyclerView.Adapter<SelJobAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<MapiCampaignResult> mList = new ArrayList<>();
    RecyOnItemClickListener recyOnItemClickListener;

    public void setSelList(ArrayList<MapiCampaignResult> selList) {
        this.selList = selList;
    }

    ArrayList<MapiCampaignResult> selList = new ArrayList<>();
    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public SelJobAdapter(Context context, List<MapiCampaignResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    public ArrayList<MapiCampaignResult> getSelList(){
        return selList;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_sel_job, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiCampaignResult result = mList.get(position);
        holder.ll_info.setTag(position);
        holder.ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        holder.title.setText(result.getPost());
        holder.company.setText(result.getCompany());
        holder.itemSel.setTag(position);
        holder.itemSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if(mList.get(pos).isSel()) {
                    mList.get(pos).setSel(false);
                    if(selList.contains(mList.get(pos)))
                        selList.remove(mList.get(pos));
                }else {
                    mList.get(pos).setSel(true);
                    if(!selList.contains(mList.get(pos)))
                        selList.add(mList.get(pos));
                }
                notifyItemChanged(pos);
            }
        });

        if(selList.contains(result))
            result.setSel(true);
        else if(!selList.contains(result))
            result.setSel(false);
        if(result.isSel())
            holder.itemSel.setImageResource(R.mipmap.circle_yellow_sel);
        else
            holder.itemSel.setImageResource(R.mipmap.circle_white);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_sel)
        ImageView itemSel;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.company)
        TextView company;
        @Bind(R.id.ll_info)
        LinearLayout ll_info;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
