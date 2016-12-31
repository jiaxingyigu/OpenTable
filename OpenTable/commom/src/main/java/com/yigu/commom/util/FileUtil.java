package com.yigu.commom.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import com.yigu.commom.R;

import java.io.File;

/**
 * Created by brain on 16/6/1.
 */
public class FileUtil {
    public final static String TYPE_IMAGE = "image";
    public final static String TYPE_DB = "d";
    private final static String rootPath = "jgj";

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建文件夹，创建文件
     *
     * @param context
     * @param imagePath
     * @return
     */
    public static File createFile(Context context, String imagePath,String type) {
        String path = FileUtil.getFolderPath(context,type);
        File dir = new File(path);
        File imageFile = new File(path, imagePath);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    public static File  createFolder(){
        File appDir = new File(Environment.getExternalStorageDirectory(), "jgj");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    /**
     * 获取文件夹路径
     * 需要自定一个文件夹类别和文件夹名字
     *
     * @return
     */
    public static String getFolderPath(Context context,String type) {
        // 本业务文件主目录

        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(hasSDCard() ? context
                .getExternalFilesDir("").getAbsolutePath()
                : context.getFilesDir());
        pathBuilder.append(File.separator);
        pathBuilder.append(rootPath);
        pathBuilder.append(File.separator);
        switch (type) {
            case TYPE_IMAGE:
                pathBuilder.append(context.getString(R.string.image));
                break;
            case TYPE_DB:
                pathBuilder.append(context.getString(R.string.db));
                break;
            default:
                break;
        }
        return pathBuilder.toString();
    }

    /**
     * 根据Uri获取绝对路径
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {

        final String scheme = contentUri.getScheme();
        String data = null;
        if (scheme == null)
            data = contentUri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = contentUri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(contentUri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;

    }

}
