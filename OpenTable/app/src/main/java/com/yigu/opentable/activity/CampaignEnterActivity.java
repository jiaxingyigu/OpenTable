package com.yigu.opentable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.util.DPUtil;
import com.yigu.commom.util.DebugLog;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.JGJBitmapUtils;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.widget.ItemDialog;
import com.yigu.opentable.widget.MainAlertDialog;
import com.yigu.opentable.widget.PhotoDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CampaignEnterActivity extends BaseActivity {

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
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.size)
    EditText size;

    ItemDialog itemDialog;
    int comType = -1;

    PhotoDialog photoDialog;

    ArrayList<MapiImageResult> mList;

    MainAlertDialog dialog;

    MapiItemResult itemResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_enter);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        itemResult = new MapiItemResult();
        mList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("单位入驻");
        tvRight.setText("确定");

        if (itemDialog == null)
            itemDialog = new ItemDialog(this, R.style.image_dialog_theme);
        if (dialog == null) {
            dialog = new MainAlertDialog(this);
            dialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除这张图片?");
        }

    }

    private void initListener() {
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

    }

    @OnClick({R.id.back, R.id.tv_right, R.id.typeLL, R.id.imageLL, R.id.image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                String nameStr = name.getText().toString();
                String addrStr = addr.getText().toString();
                String sizeStr = size.getText().toString();
                String telStr = phone.getText().toString();

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
                    MainToast.showShortToast("请输入联系方式");
                    return;
                }

                if(mList.size()<=0){
                    MainToast.showShortToast("请上传营业执照");
                    return;
                }

                getImages();
                itemResult.setName(nameStr);
                itemResult.setAddress(addrStr);
                itemResult.setScale(sizeStr);
                itemResult.setTel(telStr);
                itemResult.setType(comType+"");
                showLoading();

                CampaignApi.enterCampaign(this, userSP.getUserBean().getUSER_ID(), itemResult.getName(), itemResult.getScale(), itemResult.getAddress()
                        , itemResult.getLicense(), itemResult.getTel(), "",  itemResult.getType(), new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("审核中，请耐心等待！");
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
            case R.id.image:
                dialog.show();
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
            }
        }

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
                    Uri imageUri = Uri.parse(BasicApi.BASIC_IMAGE + mList.get(0).getPATH());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setResizeOptions(new ResizeOptions(DPUtil.dip2px(120), DPUtil.dip2px(120)))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(image.getController())
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
        outState.putSerializable("list", mList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mList = (ArrayList<MapiImageResult>) savedInstanceState.getSerializable("list");
    }

}
