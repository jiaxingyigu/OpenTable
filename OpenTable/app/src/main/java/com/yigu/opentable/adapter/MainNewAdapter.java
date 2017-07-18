package com.yigu.opentable.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yigu.commom.result.IndexData;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.result.MapiPlatformResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.opentable.R;
import com.yigu.opentable.view.HomeFlatLayout;
import com.yigu.opentable.view.HomeFoodLayout;
import com.yigu.opentable.view.HomeServicelayout;
import com.yigu.opentable.view.HomeSliderLayout;
import com.yigu.opentable.view.HomeTenantLayout;
import com.yigu.opentable.view.HomeUnitLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/6/17.
 */
public class MainNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int SLIDER_IMAGE = 0;
    private final static int SERVICE = 1;
    private final static int ITEM_FLAT = 2;
    private final static int ITEM_UNIT = 3;
    private final static int ITEM_TENANT = 4;
    private final static int ITEM_FOOD = 5;
    private final static int UNIT_SLIDER_IMAGE = 6;

    LayoutInflater inflater;

    private List<IndexData> mList;

    public MainNewAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SLIDER_IMAGE:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_home_slider, parent, false));
            case SERVICE:
                return new ServiceHolder(inflater.inflate(R.layout.lay_home_service, parent, false));
            case ITEM_FLAT:
                return new FlatViewHolder(inflater.inflate(R.layout.lay_home_flat, parent, false));
            case ITEM_UNIT:
                return new UnitViewHolder(inflater.inflate(R.layout.lay_home_unit, parent, false));
            case ITEM_TENANT:
                return new TenantViewHolder(inflater.inflate(R.layout.lay_home_tenant, parent, false));
            case ITEM_FOOD:
                return new FoodViewHolder(inflater.inflate(R.layout.lay_home_food, parent, false));
            case UNIT_SLIDER_IMAGE:
                return new UnitSliderViewHolder(inflater.inflate(R.layout.lay_home_unit_slider, parent, false));
            default:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_home_slider, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderViewHolder) {
            ((SliderViewHolder)holder).homeSliderLayout.setSlider(true);
            ((SliderViewHolder)holder).homeSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }else if(holder instanceof FlatViewHolder){
            ((FlatViewHolder)holder).homeFlatLayout.load((List<MapiPlatformResult>) mList.get(position).getData());
        }else if(holder instanceof UnitViewHolder){
            ((UnitViewHolder)holder).homeUnitLayout.load((List<MapiCampaignResult>) mList.get(position).getData());
        }else if(holder instanceof TenantViewHolder){
            ((TenantViewHolder)holder).homeTenantLayout.load((List<MapiOrderResult>) mList.get(position).getData());
        }else if(holder instanceof FoodViewHolder){
            ((FoodViewHolder)holder).homeFoodLayout.load((List<MapiOrderResult>) mList.get(position).getData());
        }else if (holder instanceof UnitSliderViewHolder) {
            ((UnitSliderViewHolder)holder).homeSliderLayout.setSlider(true);
            ((UnitSliderViewHolder)holder).homeSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "SCROLL":
                return SLIDER_IMAGE;
            case "SERVICE":
                return SERVICE;
            case "ITEM_FLAT":
                return ITEM_FLAT;
            case "ITEM_UNIT":
                return ITEM_UNIT;
            case "ITEM_TENANT":
                return ITEM_TENANT;
            case "ITEM_FOOD":
                return ITEM_FOOD;
            case "UNIT_SLIDER_IMAGE":
                return UNIT_SLIDER_IMAGE;
            default:
                return SLIDER_IMAGE;
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeSliderLayout)
        HomeSliderLayout homeSliderLayout;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class UnitSliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeSliderLayout)
        HomeSliderLayout homeSliderLayout;
        public UnitSliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ServiceHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeServiceLayout)
        HomeServicelayout homeServiceLayout;
        public ServiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FlatViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeFlatLayout)
        HomeFlatLayout homeFlatLayout;
        public FlatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class UnitViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeUnitLayout)
        HomeUnitLayout homeUnitLayout;
        public UnitViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TenantViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeTenantLayout)
        HomeTenantLayout homeTenantLayout;
        public TenantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeFoodLayout)
        HomeFoodLayout homeFoodLayout;
        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
