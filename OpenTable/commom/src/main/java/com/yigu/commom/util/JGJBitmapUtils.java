package com.yigu.commom.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/2/27.
 */
public class JGJBitmapUtils {
    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);//压缩图片，不压缩为100
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param srcPath 需要旋转的图片
     * @param srcPath 旋转图片输出的位置
     * @param degree  旋转角度
     * @return 旋转后的图片是否保存到本地
     */

    public static boolean rotateBitmapByDegree(String srcPath, String outPath, int degree) {
        Bitmap srcBitmap = BitmapFactory.decodeFile(srcPath);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                options -= 10;// 每次都减少10
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap returnBm = rotateBitmapByDegree(srcBitmap, degree);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(outPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        boolean isSucc = returnBm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        if (null != returnBm)
            returnBm.recycle();
        return isSucc;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap rotatedBitmap = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (rotatedBitmap == null) {
            rotatedBitmap = bm;
        }
        if (bm != rotatedBitmap) {
            bm.recycle();
        }
//        DebugLog.i("rotatedBitmap=" + rotatedBitmap.getByteCount() + "width=" + rotatedBitmap.getWidth() + "height=" + rotatedBitmap.getHeight());
        return rotatedBitmap;
    }


    /**
     * 保存图片到图库
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        final String fileName = System.currentTimeMillis() + ".jpg";
        File appDir = FileUtil.createFolder();
        final File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            DebugLog.e(e.getMessage());
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }

}
