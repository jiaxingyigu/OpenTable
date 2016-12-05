package com.yigu.opentable.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/1.
 */
public class DialogItemAdapter extends RecyclerView.Adapter<DialogItemAdapter.ViewHolder>{

    LayoutInflater inflater;

    private List<MapiResourceResult> mList;

    RecyOnItemClickListener recyOnItemClickListener;

    public void setRecyOnItemClickListener(RecyOnItemClickListener recyOnItemClickListener) {
        this.recyOnItemClickListener = recyOnItemClickListener;
    }

    public DialogItemAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.root_view.setText(mList.get(position).getNAME());
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
        TextView root_view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
