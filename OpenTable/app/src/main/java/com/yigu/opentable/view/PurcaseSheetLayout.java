package com.yigu.opentable.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yigu.opentable.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/9/8.
 */
public class PurcaseSheetLayout extends RelativeLayout {
    @Bind(R.id.cut)
    TextView cut;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.add)
    TextView add;
    private Context mContext;
    private View view;
    private int num = 0;

    public PurcaseSheetLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PurcaseSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public PurcaseSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_purcase_sheet, this);
        ButterKnife.bind(this, view);
        count.setText(num+"");
    }

    public void load() {

    }

    private void cut() {
        if(num-1<0){
            num = 0;
        }else{
            num--;
        }
        count.setText(num+"");
    }

    private void add() {
        count.setText(++num+"");
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @OnClick({R.id.cut, R.id.count, R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cut:
                cut();
                break;
            case R.id.count:
                break;
            case R.id.add:
                add();
                break;
        }
    }
}
