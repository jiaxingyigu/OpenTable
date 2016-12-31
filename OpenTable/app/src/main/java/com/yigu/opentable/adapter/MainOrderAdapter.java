package com.yigu.opentable.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.view.OrderItemLayout;
import com.yigu.opentable.view.OrderSliderLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/10/11.
 */
public class MainOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int SLIDER_IMAGE = 0;
    private final static int ITEM = 1;

    LayoutInflater inflater;


    private List<IndexData> mList;

    Context context;

    public MainOrderAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SLIDER_IMAGE:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_order_slider, parent, false));
            case ITEM:
                return new OrderItemViewHolder(inflater.inflate(R.layout.lay_order_item, parent, false));
            default:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_order_slider, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderViewHolder) {
            ((SliderViewHolder) holder).orderSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        } else if (holder instanceof OrderItemViewHolder) {
            ((OrderItemViewHolder) holder).orderItemLayout.load((BaseActivity)context);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "SCROLL":
                return SLIDER_IMAGE;
            case "ITEM":
                return ITEM;
            default:
                return SLIDER_IMAGE;
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.orderSliderLayout)
        OrderSliderLayout orderSliderLayout;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.orderItemLayout)
        OrderItemLayout orderItemLayout;
        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
