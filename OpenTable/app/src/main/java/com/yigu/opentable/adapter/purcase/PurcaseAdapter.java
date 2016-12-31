package com.yigu.opentable.adapter.purcase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiCartResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.opentable.R;
import com.yigu.opentable.view.PurcaseSheetLayout;
import com.yigu.opentable.widget.BuyAnimUtil;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/9/8.
 */
public class PurcaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    List<IndexData> mList = new ArrayList<>();
    private ImageView buyImg;// 这是在界面上跑的小图片
    public PurcaseAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = mList.get(position).getType();
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
                return new ItemViewHolder(inflater.inflate(R.layout.item_purcase_item, parent, false));
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
        @Bind(R.id.purcaseSheetLayout)
        PurcaseSheetLayout purcaseSheetLayout;
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
        MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
        holder.orderName.setText(orderResult.getStardate()+"    "+orderResult.getDinnertime());
    }

    private void setItem(ItemViewHolder holder, int position) {
        MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
        holder.name.setText(orderResult.getFOOD());
        holder.price.setText("¥"+orderResult.getPRICE());
        String numStr = TextUtils.isEmpty(orderResult.getNum())?"0":orderResult.getNum();
        holder.purcaseSheetLayout.setNum(Integer.parseInt(numStr));

        holder.purcaseSheetLayout.setTag(position);
        holder.purcaseSheetLayout.setNunerListener(new PurcaseSheetLayout.NumberListener() {
            @Override
            public void numerAdd(View view,View rootView) {
                if(null!=numberListener)
                    numberListener.numerAdd(view, (Integer) rootView.getTag());
            }

            @Override
            public void numberCut(View view,View rootView) {
                if(null!=numberListener)
                    numberListener.numberCut(view, (Integer) rootView.getTag());
            }
        });

    }


    private  NumberListener numberListener;

    public interface NumberListener{
        void numerAdd(View view,int position);
        void numberCut(View view,int position);
    }

    public void setNunerListener(NumberListener numberListener){
        this.numberListener = numberListener;
    }

}
