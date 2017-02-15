package com.yigu.commom.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yigu.commom.util.FileUtil;

import org.xutils.BuildConfig;
import org.xutils.x;


/**
 * Created by brain on 2016/6/14.
 */
public class AppContext extends Application {

    private static AppContext appContext;

    public IWXAPI api;
    public static final String APP_ID = "wx0c720d2693852f3b";

    public static AppContext getInstance(){
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = (AppContext) getApplicationContext();


        String dir = "";
        try{
            if(FileUtil.hasSDCard()){
                dir = getApplicationContext()
                        .getExternalCacheDir().getAbsolutePath();
            }else{
                dir = getApplicationContext()
                        .getFilesDir().getAbsolutePath();
            }

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    this)
                    .memoryCacheExtraOptions(600,600)
                    // default = device screen dimensions .
                    .diskCacheExtraOptions(600,600,null)
                    .threadPoolSize(5)
                    // default Thread.NORM_PRIORITY - 1
                    .threadPriority(Thread.NORM_PRIORITY-2)
                    // default FIFO
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13)
                    // default
                    .diskCache(
                            new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(getApplicationContext(), dir.substring(11)+"/imageloaderCache")))//StorageUtils.getCacheDirectory(this, true))
                    // default
                    .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    // default
                    .imageDownloader(new BaseImageDownloader(this))
                    // default
                    .imageDecoder(new BaseImageDecoder(false))
                    // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    // default
                    //						.defaultDisplayImageOptions(imageOptions)
                    .build();

            ImageLoader.getInstance().init(config);
        }catch(Exception e){
            e.printStackTrace();
        }

        Fresco.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush

        regToWx();

    }

    /**
     * 微信初始化，注册
     */
    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this,"wxd930ea5d5a258f4f", false);//通过wxapiFactory工厂，获取wxapi的实例
        api.registerApp(APP_ID);//将应用的id注册到微信
    }

}
