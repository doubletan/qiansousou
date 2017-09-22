package com.xinhe.qiansousou;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xinhe.qiansousou.bean.ImagerBean;
import com.xinhe.qiansousou.bean.Product;

import org.litepal.LitePalApplication;

public class BaseApplication extends LitePalApplication {

    private static String sCacheDir;
    public static Context sAppContext;
    public static RequestQueue requestQueue;
    public static String userId;

    public static RequestQueue getVolleyRequestQueue(){
        return requestQueue;
    }

    // TODO: 16/8/1 这里的夜间模式 UI 有些没有适配好 暂时放弃夜间模式
    static {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO);
    }
    private static ImagerBean image;

    public static ImagerBean getImage() {
        return image;
    }

    public static void setImage(ImagerBean image) {
        BaseApplication.image = image;
    }

    public static Product getProduct() {
        return product;
    }

    public static void setProduct(Product product) {
        BaseApplication.product = product;
    }

    private static Product product;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }
}
