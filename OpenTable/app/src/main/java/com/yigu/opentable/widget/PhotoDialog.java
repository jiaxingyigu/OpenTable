package com.yigu.opentable.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.yigu.commom.util.FileUtil;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zaoren on 2015/6/26.
 */
public class PhotoDialog extends Dialog {
    @Bind(R.id.camera)
    TextView camera;
    @Bind(R.id.photo)
    TextView photo;
    @Bind(R.id.cancel)
    TextView cancel;

    private String imagePath;
    private BaseActivity mActivity;

    public PhotoDialog(Context context,int theme) {
        super(context,theme);
        mActivity = (BaseActivity) context;
        initView();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private void initView() {
        setContentView(R.layout.dialog_photo);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.camera, R.id.photo, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera:
                goCamera();
                break;
            case R.id.photo:
                goPicture();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }


    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {// 尽可能调用系统相机
            String cameraPackageName = getCameraPhoneAppInfos(mActivity);
            if (cameraPackageName == null) {
                cameraPackageName = "com.android.camera";
            }
            final Intent intent_camera = mActivity.getPackageManager()
                    .getLaunchIntentForPackage(cameraPackageName);
            if (intent_camera != null) {
                intent.setPackage(cameraPackageName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtil.createFile(mActivity, imagePath,FileUtil.TYPE_IMAGE)));
        mActivity.startActivityForResult(intent, RequestCode.CAMERA);
//        launchCamera();
        dismiss();
    }

    private void goPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(intent, RequestCode.PICTURE);
        dismiss();
    }

    //启动相机
    private void launchCamera()
    {
        try{
            //获取相机包名
            Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ResolveInfo res = mActivity.getPackageManager().
                    resolveActivity(infoIntent, 0);
            if (res != null)
            {
                String packageName=res.activityInfo.packageName;
                if(packageName.equals("android"))
                {
                    infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);

                    res = mActivity.getPackageManager().
                            resolveActivity(infoIntent, 0);
                    if (res != null)
                        packageName=res.activityInfo.packageName;
                }
                //启动相机
                startApplicationByPackageName(packageName);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    //通过包名启动应用
    private void startApplicationByPackageName(String packName)
    {
        PackageInfo packageInfo=null;
        try{
            packageInfo=mActivity.getPackageManager().getPackageInfo(packName, 0);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(null==packageInfo){
            return;
        }
        Intent resolveIntent=new Intent();
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);
        List<ResolveInfo> resolveInfoList =mActivity.getPackageManager().queryIntentActivities(resolveIntent, 0);
        if(null==resolveInfoList){
            return;
        }
        Iterator<ResolveInfo> iter=resolveInfoList.iterator();
        while(iter.hasNext()){
            ResolveInfo resolveInfo=(ResolveInfo) iter.next();
            if(null==resolveInfo){
                return;
            }
            String packageName=resolveInfo.activityInfo.packageName;
            String className=resolveInfo.activityInfo.name;
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn=new ComponentName(packageName, className);
            intent.setComponent(cn);
//            mActivity.startActivityForResult(intent, RequestCode.CAMERA);
            mActivity.startActivity(intent);
        }//while
    }//method

    // 对使用系统拍照的处理

    public String getCameraPhoneAppInfos(Activity context) {
        try {
            String strCamera = "";
            List<PackageInfo> packages = context.getPackageManager()
                    .getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                try {
                    PackageInfo packageInfo = packages.get(i);
                    String strLabel = packageInfo.applicationInfo.loadLabel(
                            context.getPackageManager()).toString();
                    // 一般手机系统中拍照软件的名字
                    if ("相机,照相机,照相,拍照,摄像,Camera,camera".contains(strLabel)) {
                        strCamera = packageInfo.packageName;
                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (strCamera != null) {
                return strCamera;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
