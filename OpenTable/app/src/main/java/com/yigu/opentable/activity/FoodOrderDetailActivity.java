package com.yigu.opentable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.result.MapiHistoryResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.MainAlertDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodOrderDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.tel)
    TextView tel;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.seat)
    TextView seat;
    @Bind(R.id.instruction)
    TextView instruction;
    @Bind(R.id.reply)
    TextView reply;

    MapiHistoryResult itemResult;

    MainAlertDialog callDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_detail);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            itemResult = (MapiHistoryResult) getIntent().getSerializableExtra("item");
        }
        if(null!=itemResult){
            initView();
            initListener();
        }
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("详情");
        tvRight.setText("点菜");
        if(itemResult.getState().equals("1")&&itemResult.getFlag().equals("0"))
            tvRight.setVisibility(View.VISIBLE);
        else
            tvRight.setVisibility(View.INVISIBLE);

        date.setText("用餐时间："+itemResult.getUsedate()+"  "+itemResult.getDinnertime());
//        holder.price.setText("总价：¥"+itemResult.getPrice());
        name.setText(itemResult.getShopname());
        tel.setText("电话："+ (TextUtils.isEmpty(itemResult.getTel())?"暂无":itemResult.getTel()));

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + itemResult.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(76), DPUtil.dip2px(76)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);

        seat.setText("用餐人数："+itemResult.getSeat());
        if(TextUtils.isEmpty(itemResult.getBz())){
            instruction.setVisibility(View.GONE);
        }else {
            instruction.setVisibility(View.VISIBLE);
            instruction.setText("备注："+itemResult.getBz());
        }


        reply.setText(TextUtils.isEmpty(itemResult.getReply())?"":"店家反馈："+itemResult.getReply());

        callDialog = new MainAlertDialog(this);
        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫");
        if(!TextUtils.isEmpty(itemResult.getTel())){
            callDialog.setTitle(itemResult.getTel());
        }

    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callDialog.getTitle()));
                startActivity(intent);
                callDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_right,R.id.tel_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                MapiOrderResult mapiOrderResult = new MapiOrderResult();
                mapiOrderResult.setSeatId(itemResult.getId());
                mapiOrderResult.setNAME(itemResult.getShopname());
                mapiOrderResult.setPIC(itemResult.getPATH());
                mapiOrderResult.setID(itemResult.getSHOP());
                mapiOrderResult.setCompanyId(itemResult.getCompanyId());
                mapiOrderResult.setINTRODUCTION(itemResult.getINTRODUCTION());
                ControllerUtil.go2FoodMenu(mapiOrderResult);
                break;
            case R.id.tel_iv:
                if(!TextUtils.isEmpty(itemResult.getTel())){
                    callDialog.show();
                }else
                    MainToast.showShortToast("暂无联系方式");
                break;
        }
    }
}
