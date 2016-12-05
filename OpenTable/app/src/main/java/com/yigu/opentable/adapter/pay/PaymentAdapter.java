package com.yigu.opentable.adapter.pay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiCartResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.opentable.R;
import com.yigu.opentable.view.PurcaseSheetLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/9/8.
 */
public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    List<MapiCartResult> mList = new ArrayList<>();
    List<IndexData> list = new ArrayList<>();


    public List<MapiCartResult> getmList() {
        return mList;
    }

    public PaymentAdapter(Context context, List<MapiCartResult> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        int count = 0;
//        list.add(new IndexData(count++,"divider", new Object()));
        for (MapiCartResult ware : mList) {
            list.add(new IndexData(count++, "head", ware));
            for (int i = 0; i < ware.getItems().size(); i++) {

                list.add(new IndexData(count++, "item", ware.getItems().get(i)));

            }
            list.add(new IndexData(count++, "divider", new Object()));
        }
        return count;

    }

    @Override
    public int getItemViewType(int position) {
        String type = list.get(position).getType();
        if (type.equals("item")) {
            return 2;
        } else if (type.equals("head")) {
            return 1;
        }
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new HeadViewHolder(inflater.inflate(R.layout.item_purcase_head, parent, false));
            case 2:
                return new ItemViewHolder(inflater.inflate(R.layout.item_payment_item, parent, false));
            case 3:
                return new DividerViewHolder(inflater.inflate(R.layout.item_purcase_divider, parent, false));
            default:
                return new HeadViewHolder(inflater.inflate(R.layout.item_purcase_head, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            setHead((HeadViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.order_name)
        TextView orderName;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setHead(HeadViewHolder holder, int position) {
        DebugLog.i("HeadViewHolder=load");

    }

    private void setItem(ItemViewHolder holder, int position) {
        DebugLog.i("ItemViewHolder=load");
    }


}
