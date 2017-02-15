package com.yigu.opentable.activity.order;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.yigu.commom.application.AppContext;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.purcase.LivePurcaseActivity;
import com.yigu.opentable.activity.purcase.PurcaseActivity;
import com.yigu.opentable.activity.purcase.TenantPurcaseActivity;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.util.ShareModule;
import com.yigu.opentable.view.PurcaseSheetLayout;
import com.yigu.opentable.widget.BuyAnimUtil;
import com.yigu.opentable.widget.ShareDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.purcaseSheetLayout)
    PurcaseSheetLayout purcaseSheetLayout;
    @Bind(R.id.allNum)
    TextView allNum;
    @Bind(R.id.rl_purcase)
    RelativeLayout rlPurcase;
    @Bind(R.id.deel)
    TextView deel;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.iv_right_two)
    ImageView ivRightTwo;

    String SHOP = "";
    String id = "";
    String title = "";
    int position = 0;
    String all = "0";


    private DbManager db;
    MapiOrderResult orderResult;

    private ImageView buyImg;// 这是在界面上跑的小图片

    private String type = "";

    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this, FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        if (null != getIntent()) {
            SHOP = getIntent().getStringExtra("SHOP");
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            position = getIntent().getIntExtra("position", 0);
            all = getIntent().getStringExtra("all");
            type = getIntent().getStringExtra("type");
            DebugLog.i("type==>" + type);
        }
        if (!TextUtils.isEmpty(id)) {
            try {
                orderResult = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid", "=", SHOP).findFirst();
                if (null != orderResult) {
                    initView();
                    initListener();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText(title);

        ivRightTwo.setImageResource(R.mipmap.share_logo);
        ivRightTwo.setVisibility(View.VISIBLE);

        name.setText(orderResult.getFOOD());
        price.setText("价格：¥" + orderResult.getPRICE());
        account.setText("剩余数量：" + orderResult.getAMOUNT() + "份");
        detail.setText(orderResult.getRemark());
        String numStr = TextUtils.isEmpty(orderResult.getNum()) ? "0" : orderResult.getNum();
        int numInt = Integer.parseInt(numStr);
        purcaseSheetLayout.setNum(numInt);

        int allInt = Integer.parseInt(all);

        if (allInt > 0) {
            allNum.setText(allInt + "");
            allNum.setVisibility(View.VISIBLE);
        }


        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + orderResult.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(320), DPUtil.dip2px(320)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);

        if (shareDialog == null)
            shareDialog = new ShareDialog(this, R.style.image_dialog_theme);

    }

    private void initListener() {
        purcaseSheetLayout.setNunerListener(new PurcaseSheetLayout.NumberListener() {
            @Override
            public void numerAdd(View view, View rootView) {
                int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                view.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                buyImg = new ImageView(OrderDetailActivity.this);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                buyImg.setImageResource(R.drawable.sign);// 设置buyImg的图片
                BuyAnimUtil.setAnim(buyImg, rlPurcase, OrderDetailActivity.this, start_location);// 开始执行动画

                addNum();

            }

            @Override
            public void numberCut(View view, View rootView) {
                cuNum();
            }
        });

        shareDialog.setDialogItemClickListner(new ShareDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                String SHARE_ORDER_DETAIL =BasicApi.BASIC_URL+ BasicApi.SHARE_ORDER_DETAIL+id;
                switch (position) {
                    case 0://微信好友
                        ShareModule shareModule1 = new ShareModule(OrderDetailActivity.this, userSP.getUserBean().getCOMPANYNAME()+"-" + title+"-"+orderResult.getFOOD(), orderResult.getRemark(), BasicApi.BASIC_IMAGE + orderResult.getPATH(), SHARE_ORDER_DETAIL);
                        shareModule1.startShare(1);
                        break;
                    case 1:
                        ShareModule shareModule2 = new ShareModule(OrderDetailActivity.this, userSP.getUserBean().getCOMPANYNAME()+"-" + title+"-"+orderResult.getFOOD(), orderResult.getRemark(), BasicApi.BASIC_IMAGE + orderResult.getPATH(),SHARE_ORDER_DETAIL);
                        shareModule2.startShare(2);
                        break;
                    case 2:
                        ShareModule shareModule3 = new ShareModule(OrderDetailActivity.this, userSP.getUserBean().getCOMPANYNAME()+"-" + title+"-"+orderResult.getFOOD(), orderResult.getRemark(), BasicApi.BASIC_IMAGE + orderResult.getPATH(), SHARE_ORDER_DETAIL);
                        shareModule3.startShare(3);
                        break;
                    case 3:
                        ShareModule shareModule4 = new ShareModule(OrderDetailActivity.this, userSP.getUserBean().getCOMPANYNAME()+"-" + title+"-"+orderResult.getFOOD(), orderResult.getRemark(), BasicApi.BASIC_IMAGE + orderResult.getPATH(), SHARE_ORDER_DETAIL);
                        shareModule4.startShare(4);
                        break;
                }
            }
        });

    }

    private void addNum() {
        String num = TextUtils.isEmpty(allNum.getText().toString()) ? "0" : allNum.getText().toString();
        int numInt = Integer.parseInt(num);
        allNum.setText(++numInt + "");
        if (allNum.getVisibility() == View.INVISIBLE)
            allNum.setVisibility(View.VISIBLE);

        try {
            MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid", "=", SHOP).findFirst();
            if (null != history) {
                DebugLog.i("history=>" + history.getNum());
                String numStr = TextUtils.isEmpty(history.getNum()) ? "0" : history.getNum();
                int numInteger = Integer.parseInt(numStr);
                history.setNum(++numInteger + "");
                db.update(history);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void cuNum() {


        try {
            MapiOrderResult history = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid", "=", SHOP).findFirst();
            if (null != history) {
                DebugLog.i("history=>" + history.getNum());
                String numStr = TextUtils.isEmpty(history.getNum()) ? "0" : history.getNum();
                int numInteger = Integer.parseInt(numStr);
                if (numInteger == 0) {
                    return;
                } else {
                    history.setNum(--numInteger + "");

                    String num = TextUtils.isEmpty(allNum.getText()) ? "0" : allNum.getText().toString();
                    int numInt = Integer.parseInt(num);
                    if (numInt <= 1) {
                        allNum.setText("0");
                        allNum.setVisibility(View.INVISIBLE);
                    } else {

                        allNum.setText(--numInt + "");
                    }

                }

                db.update(history);
            }

            MapiOrderResult history2 = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid", "=", SHOP).findFirst();
            if (null != history) {
                DebugLog.i("history2=>" + history2.getNum());
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("num", TextUtils.isEmpty(allNum.getText()) ? "0" : allNum.getText().toString());
        intent.putExtra("itemNum", purcaseSheetLayout.getNum());
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.back, R.id.purcase, R.id.deel,R.id.iv_right_two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("num", TextUtils.isEmpty(allNum.getText()) ? "0" : allNum.getText().toString());
                intent.putExtra("itemNum", purcaseSheetLayout.getNum());
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.deel:
                if (type.equals("pay"))
                    ControllerUtil.go2Payment(SHOP);
                else if (type.equals("tenant"))
                    ControllerUtil.go2TenantPay(SHOP, true);
                else if (type.equals("live"))
                    ControllerUtil.go2LivePay(SHOP, true);
                break;
            case R.id.purcase:
                if (type.equals("pay")) {
                    Intent payIntent = new Intent(AppContext.getInstance(), PurcaseActivity.class);
                    payIntent.putExtra("SHOP", SHOP);
                    startActivityForResult(payIntent, RequestCode.purcase_list);
                } else if (type.equals("tenant")) {
                    Intent tenantIntent = new Intent(AppContext.getInstance(), TenantPurcaseActivity.class);
                    tenantIntent.putExtra("SHOP", SHOP);
                    tenantIntent.putExtra("hasAddr", true);
                    startActivityForResult(tenantIntent, RequestCode.purcase_list);
                } else if (type.equals("live")) {
                    Intent liveIntent = new Intent(AppContext.getInstance(), LivePurcaseActivity.class);
                    liveIntent.putExtra("SHOP", SHOP);
                    liveIntent.putExtra("hasBZ", true);
                    startActivityForResult(liveIntent, RequestCode.purcase_list);
                }
                break;
            case R.id.iv_right_two:
                shareDialog.showDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.purcase_list:
                    try {
                        orderResult = db.selector(MapiOrderResult.class).where("ID", "=", id).and("sid", "=", SHOP).findFirst();
                        Cursor cursor = db.execQuery("select sum(num) as count from MapiOrderResult where num <> '0' and sid = '" + SHOP + "'");
                        long count = 0;
                        if (cursor.moveToFirst()) {
                            int nameColumnIndex = cursor.getColumnIndex("count");
                            DebugLog.i("nameColumnIndex=>" + nameColumnIndex);
                            count = cursor.getLong(nameColumnIndex);
                            DebugLog.i("count===>" + count);
                        }

                        if (count > 0) {
                            allNum.setText(count + "");
                            allNum.setVisibility(View.VISIBLE);
                        } else {
                            allNum.setText("0");
                            allNum.setVisibility(View.GONE);
                        }
                        if (null != orderResult) {
                            price.setText("价格：¥" + orderResult.getPRICE());
                            account.setText("剩余数量：" + orderResult.getAMOUNT() + "份");
                            String numStr = TextUtils.isEmpty(orderResult.getNum()) ? "0" : orderResult.getNum();
                            int numInt = Integer.parseInt(numStr);
                            purcaseSheetLayout.setNum(numInt);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
