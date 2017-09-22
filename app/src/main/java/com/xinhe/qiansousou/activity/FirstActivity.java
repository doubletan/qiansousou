package com.xinhe.qiansousou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.umeng.analytics.MobclickAgent;
import com.xinhe.qiansousou.BaseApplication;
import com.xinhe.qiansousou.task.GetImageBean;
import com.xinhe.qiansousou.task.GetProduct;
import com.xinhe.qiansousou.task.GetProduct1;
import com.xinhe.qiansousou.task.GetProduct2;
import com.xinhe.qiansousou.task.GetProduct3;
import com.xinhe.qiansousou.util.SPUtils;
import com.xinhe.qiansousou.util.SharedPreferencesUtil;

import java.lang.ref.WeakReference;

/**
 * Created by hugo on 2015/10/25 0025.
 * 闪屏页
 * @see <a herf="http://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html">How to Leak a Context: Handlers & Inner Classes</a>
 */
public class FirstActivity extends Activity {
    private static  final String URL="http://www.shoujijiekuan.com/tantan/app125.html";
    private SharedPreferences sp;
    private SwitchHandler mHandler = new SwitchHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity切换的淡入淡出效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);

        boolean url1 = SPUtils.contains(this, "url");
        if(!url1){
            setUrl();
        }else {
            TextNet();

            boolean flag = SPUtils.contains(this, "userId");
            if(flag){
                BaseApplication.userId= (String) SPUtils.get(this,"userId","");
                mHandler.sendEmptyMessageDelayed(3, 1000);
            }else {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }

    private void TextNet() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            //执行相关操作
            new GetImageBean(this).execute();
            new GetProduct(this).execute();
            new GetProduct3(this).execute();
            new GetProduct1(this).execute();
            new GetProduct2(this).execute();
        } else {
            Toast.makeText(this,
                    "亲，网络连接失败咯！", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void setUrl() {
      StringRequest request=new StringRequest(URL, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              SPUtils.put(FirstActivity.this,"url",URL);
              new GetImageBean(FirstActivity.this).execute();
              new GetProduct(FirstActivity.this).execute();
              new GetProduct3(FirstActivity.this).execute();
              new GetProduct1(FirstActivity.this).execute();
              new GetProduct2(FirstActivity.this).execute();
              setWelcome();
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              mHandler.sendEmptyMessageDelayed(2, 1000);
          }
      });
        BaseApplication.getVolleyRequestQueue().add(request);

    }
    private void setWelcome(){
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(FirstActivity.this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen) {
            mHandler.sendEmptyMessageDelayed(4, 1000);
            return;
        }else {
            mHandler.sendEmptyMessageDelayed(2, 1000);
        }
    }
    private static class SwitchHandler extends Handler {
        private WeakReference<FirstActivity> mWeakReference;

        SwitchHandler(FirstActivity activity) {
            mWeakReference = new WeakReference<FirstActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FirstActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what){
                    case 1:
                        activity.startActivity(new Intent(activity,LoginActivity.class));
                        break;
                    case 2:
                        activity.startActivity(new Intent(activity,MainActivity.class));
                        break;
                    case 3:
                        HomeActivity.launch(activity);
                        break;
                    case 4:
                        Intent intent = new Intent(activity, GuideActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                }
                activity.finish();
            }
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    private long mLastBackTime = 0;
    @Override
    public void onBackPressed() {
        // finish while click back key 2 times during 1s.
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Toast.makeText(this, "请再确认一次", Toast.LENGTH_SHORT).show();
        }

    }
}