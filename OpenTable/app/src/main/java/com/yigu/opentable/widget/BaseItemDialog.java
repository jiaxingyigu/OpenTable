package com.yigu.opentable.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.DialogItemAdapter;
import com.yigu.opentable.adapter.order.OrderListAadpter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zaoren on 2015/6/26.
 */
public class BaseItemDialog extends Dialog {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private BaseActivity mActivity;

    public void setmList(List<MapiResourceResult> mList) {
        DebugLog.i("setmList");
        this.mList.clear();
        this.mList.addAll(mList);
        mAdapter.notifyDataSetChanged();
    }

    private List<MapiResourceResult> mList = new ArrayList<>();
    DialogItemAdapter mAdapter;
    Context mContext;

    public BaseItemDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        mActivity = (BaseActivity) context;
        initView();
        initListtener();
    }

    private void initView() {
        setContentView(R.layout.dialog_base_item);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new DialogItemAdapter(mActivity, mList);
        recyclerView.setAdapter(mAdapter);

        DebugLog.i("initView");

    }

    private void initListtener(){
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(null!=dialogItemClickListner)
                    dialogItemClickListner.onItemClick(view,position);
                dismiss();
            }
        });
    }


    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    /*@OnClick({R.id.typeOne, R.id.typeTwo, R.id.typeThree})
    public void onClick(View view) {
        int type=0;
        switch (view.getId()) {
            case R.id.typeOne:
                type = 0;
                break;
            case R.id.typeTwo:
                type = 1;
                break;
            case R.id.typeThree:
                type = 2;
                break;
        }
        if(null!=dialogItemClickListner)
            dialogItemClickListner.onItemClick(view,type);
        dismiss();
    }*/

    public interface DialogItemClickListner {
        void onItemClick(View view, int position);
    }

    private DialogItemClickListner dialogItemClickListner;

    public void setDialogItemClickListner(DialogItemClickListner dialogItemClickListner){
        this.dialogItemClickListner = dialogItemClickListner;
    }

}
