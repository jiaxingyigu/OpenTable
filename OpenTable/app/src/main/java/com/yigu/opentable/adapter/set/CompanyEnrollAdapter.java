package com.yigu.opentable.adapter.set;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yigu.commom.result.MapiItemResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/22.
 */
public class CompanyEnrollAdapter extends RecyclerView.Adapter<CompanyEnrollAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<MapiItemResult> mList = new ArrayList<>();
    RecyOnItemClickListener recyOnItemClickListener;



    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public CompanyEnrollAdapter(Context context, List<MapiItemResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_company_enroll, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiItemResult itemResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        if (!TextUtils.isEmpty(itemResult.getCreated())) {
            holder.dateTv.setText(itemResult.getCreated());
        }
        holder.name.setText(itemResult.getActname());
        if (null != itemResult.getState()) {
            if (itemResult.getState() == 0) {
                holder.statusTv.setText("审核中");
                holder.statusTv.setTextColor(Color.parseColor("#4FC0E8"));
            } else if (itemResult.getState() == 1) {
                holder.statusTv.setText("已通过");
                holder.statusTv.setTextColor(Color.parseColor("#4FC0E8"));
            } else if (itemResult.getState() == 2) {
                holder.statusTv.setText("未通过");
                holder.statusTv.setTextColor(Color.parseColor("#EA5036"));
            }
        }
        holder.reason.setText(itemResult.getReason());
        if(!TextUtils.isEmpty(itemResult.getReason())) {
            holder.reason.setVisibility(View.VISIBLE);
            holder.reason.setText(TextUtils.isEmpty(itemResult.getReason())?"":"原因："+itemResult.getReason());
        }else{
            holder.reason.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.statusTv)
        TextView statusTv;
        @Bind(R.id.dateTv)
        TextView dateTv;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.reason)
        TextView reason;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
