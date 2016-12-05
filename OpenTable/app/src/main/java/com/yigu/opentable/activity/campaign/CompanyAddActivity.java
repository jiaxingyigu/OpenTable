package com.yigu.opentable.activity.campaign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.soundcloud.android.crop.Crop;
import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.result.MapiResourceResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.JGJBitmapUtils;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.campaign.CompanyJobAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.Config;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.ItemDialog;
import com.yigu.opentable.widget.MainAlertDialog;
import com.yigu.opentable.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyAddActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.type)
    TextView typeTV;
    @Bind(R.id.typeLL)
    LinearLayout typeLL;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.addr)
    EditText addr;
    @Bind(R.id.size)
    EditText size;
    @Bind(R.id.tel)
    EditText tel;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.add)
    LinearLayout add;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.train_yes)
    RadioButton trainYes;
    @Bind(R.id.train_no)
    RadioButton trainNo;
    @Bind(R.id.ll_train)
    LinearLayout ll_train;

    String actid;
    String type;

    PhotoDialog photoDialog;
    ItemDialog itemDialog;

    ArrayList<MapiImageResult> mList;
    MapiItemResult itemResult;

    CompanyJobAdapter mAdapter;
    ArrayList<MapiCampaignResult> list;

    int comType = -1;

    MainAlertDialog dialog;

    MainAlertDialog deleteDialog;
    private int deletePos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add);
        ExitApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            type = getIntent().getStringExtra("type");
        }
        if (!TextUtils.isEmpty(actid)) {
            initView();
            initListener();
        }
    }

    private void initView() {
        mList = new ArrayList<>();
        list = new ArrayList<>();
        itemResult = new MapiItemResult();
        back.setImageResource(R.mipmap.back);
        tvRight.setText("确定");
        center.setText("企业报名");
        trainYes.setChecked(true);
        if (type.equals("1") || type.equals("7")) {
            add.setVisibility(View.VISIBLE);
        } else
            add.setVisibility(View.GONE);

        if(type.equals("3"))
            ll_train.setVisibility(View.GONE);
        else
            ll_train.setVisibility(View.VISIBLE);

        itemResult.setActid(actid);

        if(null!=userSP.getResource())
        {
            JSONObject jsonObject = JSONObject.parseObject(userSP.getResource());
            String json;
            try{
                json = jsonObject.getJSONObject("data").getJSONObject("COMPANY").toJSONString();
                MapiItemResult result = JSONObject.parseObject(json,MapiItemResult.class);
                if(null!=result)
                    setData(result);
            }catch (Exception e){

            }
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new CompanyJobAdapter(this, list);
        recyclerView.setAdapter(mAdapter);

        if (itemDialog == null)
            itemDialog = new ItemDialog(this, R.style.image_dialog_theme);

        dialog = new MainAlertDialog(this);
        dialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除这张图片?");

        deleteDialog = new MainAlertDialog(this);
        deleteDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除这个岗位?");

    }

    private void setData(MapiItemResult result){
        comType = Integer.valueOf(result.getType());
        if (comType == 0)
            typeTV.setText("企事业单位");
        if (comType == 1)
            typeTV.setText("机关部门");
        if (comType == 2)
            typeTV.setText("医院");
        name.setText(result.getName());
        addr.setText(result.getAddress());
        size.setText(result.getScale());
        tel.setText(result.getTel());
        content.setText(result.getIntroduction());

        MapiImageResult imageResult = new MapiImageResult();
        imageResult.setID(result.getLicense());
        imageResult.setPATH(result.getPATH());

        mList.clear();
        mList.add(imageResult);

        image.setVisibility(View.VISIBLE);
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE+result.getPATH());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(120), DPUtil.dip2px(120)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController( image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);


    }

    private void initListener() {
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                deletePos = position;
                deleteDialog.show();

            }
        });

        itemDialog.setDialogItemClickListner(new ItemDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int type) {
                comType = type;
                if (type == 0)
                    typeTV.setText("企事业单位");
                if (type == 1)
                    typeTV.setText("机关部门");
                if (type == 2)
                    typeTV.setText("医院");
            }
        });

        dialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(0);
                image.setImageResource(R.mipmap.default_item);
                image.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        deleteDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deletePos>=0){
                    list.remove(deletePos);
                    mAdapter.notifyItemRemoved(deletePos);
                    mAdapter.notifyItemRangeRemoved(deletePos,list.size()+1);
                }
                deleteDialog.dismiss();
            }
        });

    }

    @OnClick({R.id.back, R.id.tv_right, R.id.typeLL, R.id.imageLL, R.id.add, R.id.image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                String nameStr = name.getText().toString();
                String addrStr = addr.getText().toString();
                String sizeStr = size.getText().toString();
                String telStr = tel.getText().toString();
                String contentStr = content.getText().toString();
                String train = "1";
                if (comType < 0) {
                    MainToast.showShortToast("请选择企业类型");
                    return;
                }
                if (TextUtils.isEmpty(nameStr)) {
                    MainToast.showShortToast("请输入企业名称");
                    return;
                }
                if (TextUtils.isEmpty(addrStr)) {
                    MainToast.showShortToast("请输入企业地址");
                    return;
                }
                if (TextUtils.isEmpty(sizeStr)) {
                    MainToast.showShortToast("请输入企业规模");
                    return;
                }
                if (TextUtils.isEmpty(telStr)) {
                    MainToast.showShortToast("请输入联系电话");
                    return;
                }
                if (TextUtils.isEmpty(contentStr)) {
                    MainToast.showShortToast("请输入企业简介");
                    return;
                }
                if(ll_train.getVisibility()==View.VISIBLE) {
                    if (trainYes.isChecked()) {
                        train = "1";
                    } else {
                        train = "0";
                    }
                }
                if(mList.size()<=0){
                    MainToast.showShortToast("请上传营业执照");
                    return;
                }

                if(add.getVisibility()==View.VISIBLE){
                    if(null!=list&&list.size()<=0){
                        MainToast.showShortToast("请添加岗位");
                        return;
                    }
                }
                getImages();
                getItems();
                itemResult.setName(nameStr);
                itemResult.setAddress(addrStr);
                itemResult.setScale(sizeStr);
                itemResult.setTel(telStr);
                itemResult.setIntroduction(contentStr);
                itemResult.setTrain(train);
                itemResult.setType(comType+"");
                showLoading();

                CampaignApi.comsign(this, itemResult.getActid(), userSP.getUserBean().getUSER_ID(), itemResult.getPosts(), itemResult.getName(), itemResult.getScale(), itemResult.getAddress()
                        , itemResult.getIntroduction(), itemResult.getLicense(), itemResult.getTel(), "", itemResult.getTrain(), itemResult.getType(), new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("报名成功");
                                ExitApplication.getInstance().exit();
                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(String code, String message) {
                                hideLoading();
                                MainToast.showShortToast(message);
                            }
                        });


                break;
            case R.id.typeLL:
                itemDialog.showDialog();
                break;
            case R.id.imageLL:
                if (null != mList && mList.size() == 0) {
                    if (photoDialog == null)
                        photoDialog = new PhotoDialog(this, R.style.image_dialog_theme);
                    photoDialog.setImagePath("daily_image.jpg");
                    photoDialog.showDialog();
                }
                break;
            case R.id.add:
                if(list.size()>= Config.MAX_VALUE){
                    MainToast.showShortToast("最多上传"+Config.MAX_VALUE+"个岗位");
                }else{
                    Intent intent = new Intent(this, AddJobActivity.class);
                    intent.putExtra("list",list);
                    startActivityForResult(intent, RequestCode.job_add);
                }

                break;
            case R.id.image:
                dialog.show();
//                ControllerUtil.go2ShopPic(0, mList);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.i("requestCode=" + requestCode + "resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.CAMERA:
                    File picture = FileUtil.createFile(this, "daily_image.jpg", FileUtil.TYPE_IMAGE);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case RequestCode.PICTURE:
                    if (data != null)
                        startPhotoZoom(data.getData());
                    break;
                case Crop.REQUEST_CROP: //缩放以后
                    if (data != null) {
                        File picture2 = FileUtil.createFile(this, "daily_image_crop.jpg", FileUtil.TYPE_IMAGE);
                        String filename = picture2.getAbsolutePath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filename);
//                        JGJBitmapUtils.saveBitmap2file(bitmap, filename);
                        if (JGJBitmapUtils.rotateBitmapByDegree(filename, filename, JGJBitmapUtils.getBitmapDegree(filename))) {
                            uploadImage(picture2);
                        } else
                            DebugLog.i("图片保存失败");
                    }
                    break;
                case RequestCode.job_add:

                    ArrayList<MapiCampaignResult> jobList = (ArrayList<MapiCampaignResult>) data.getSerializableExtra("list");
                    if (null != jobList && !jobList.isEmpty()) {
                        list.clear();
                        list.addAll(jobList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }

    }

    private void uploadImage(File file) {
        showLoading();
        CampaignApi.uploadImage(this, file, new RequestCallback<MapiImageResult>() {
            @Override
            public void success(MapiImageResult success) {
                hideLoading();
                if (null != success) {
                    mList.add(success);
                    image.setVisibility(View.VISIBLE);
//                    image.setImageURI(BasicApi.BASIC_IMAGE + Uri.parse(mList.get(0).getPATH()));

                    //创建将要下载的图片的URI
                    Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE+mList.get(0).getPATH());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(120), DPUtil.dip2px(120)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController( image.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    image.setController(controller);

                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                hideLoading();
                DebugLog.i(message);
            }
        });
    }

    private void getImages() {
        String images = "";
        StringBuilder sb = new StringBuilder();
        for (MapiImageResult imageResult : mList) {
            if (sb.length() != 0)
                sb.append(",");
            sb.append(imageResult.getID());
        }
        images = sb.toString();
        itemResult.setLicense(images);
    }

    private void getItems() {
        String images = "";
        StringBuilder sb = new StringBuilder();
        for (MapiCampaignResult imageResult : list) {
            if (sb.length() != 0)
                sb.append(",");
            sb.append(imageResult.getId());
        }
        images = sb.toString();
        itemResult.setPosts(images);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Uri outUrl = Uri
                .fromFile(FileUtil.createFile(this, "daily_image_crop.jpg", FileUtil.TYPE_IMAGE));
        Crop.of(uri, outUrl).asSquare().withMaxSize(600, 600).start(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", itemResult);
        outState.putSerializable("list", mList);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemResult = (MapiItemResult) savedInstanceState.getSerializable("item");
        mList = (ArrayList<MapiImageResult>) savedInstanceState.getSerializable("list");
    }


}
