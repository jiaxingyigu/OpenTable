package com.yigu.opentable.adapter.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yigu.commom.result.MapiItemResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/1.
 */
public class OrderListAadpter extends RecyclerView.Adapter<OrderListAadpter.ViewHolder>{

    LayoutInflater inflater;

    private List<MapiItemResult> mList;

    RecyOnItemClickListener recyOnItemClickListener;

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
        }

    public OrderListAadpter(Context context, List<MapiItemResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != recyOnItemClickListener)
                    recyOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.root_view)
        LinearLayout root_view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
