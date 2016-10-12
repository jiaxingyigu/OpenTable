package com.yigu.opentable.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yigu.commom.result.MapiResourceResult;
import com.yigu.opentable.R;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.TableDataSource;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/11.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{


    private List<MapiResourceResult> mList;
    private LayoutInflater inflater;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public OrderItemAdapter(Context context, List<MapiResourceResult> list) {
        this.mList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mList.size()==0?0:mList.size();
    }

    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_main_order,parent,false));
    }

    @Override
    public void onBindViewHolder(OrderItemAdapter.ViewHolder holder, int position) {
        int resId = R.mipmap.order_unit;
        switch (mList.get(position).getId()){
            case TableDataSource.TYPE_UNIT:
                resId = R.mipmap.order_unit;
                break;
            case TableDataSource.TYPE_TENANT:
                resId = R.mipmap.order_tenant;
                break;
            case TableDataSource.TYPE_LIVE:
                resId = R.mipmap.order_live;
                break;
            case TableDataSource.TYPE_WORKERS:
                resId = R.mipmap.order_workers;
                break;
            case TableDataSource.TYPE_COOK:
                resId = R.mipmap.order_cook;
                break;
            case TableDataSource.TYPE_nutrition:
                resId = R.mipmap.order_nutrition;
                break;
            case TableDataSource.TYPE_personal:
                resId = R.mipmap.order_personal;
                break;

        }
        holder.image.setImageResource(resId);
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view,(Integer)view.getTag());
            }
        });

        holder.title.setText(mList.get(position).getNAME());

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.root_view)
        RelativeLayout root_view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
