package com.xinhe.qiansousou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xinhe.qiansousou.BaseApplication;
import com.xinhe.qiansousou.R;
import com.xinhe.qiansousou.util.CheckUtil;
import com.xinhe.qiansousou.util.CodeUtils;
import com.xinhe.qiansousou.util.Constants;
import com.xinhe.qiansousou.util.DeviceUtil;
import com.xinhe.qiansousou.util.GetMyKey;
import com.xinhe.qiansousou.util.SPUtils;
import com.xinhe.qiansousou.view.MyCircleProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.et_yanzheng)
    EditText etYanzheng;
    @Bind(R.id.iv_yanzheng)
    ImageView ivYanzheng;
    @Bind(R.id.login_yanzheng)
    RelativeLayout loginYanzheng;
    private Button btnLogin;
    private RelativeLayout rl;
    private long firstTime;
    private TimeCount time;
    private Button btnLogin1;
    private EditText etPhone;
    private String phone;
    private String detail;
    private String code2;
    private EditText etCode;
    private String code1;
    private JSONObject json1;
    private String myUrl;
    private RelativeLayout rl2;
    private String savePhone;
    private CodeUtils codeUtils;
    private String etYanZhengCode;
    private String yanZhengCode;
    private String yanZhengResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        setViews();
        setlistener();
        // 构造CountDownTimer对象
        time = new TimeCount(60000, 1000);
    }

    private void setlistener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(code2)) {
                    sendCode();
                } else {
                    login();
                }
            }
        });
        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        ivYanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initYanzheng();
            }
        });
    }

    private void login() {
        if (DeviceUtil.IsNetWork(this) == false) {
            Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        phone = etPhone.getText().toString();
        if ("".equals(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!CheckUtil.isMobile(phone)) {
            Toast.makeText(this, "手机号输入错误", Toast.LENGTH_LONG).show();
            return;
        }

        code1 = etCode.getText().toString();
        if (TextUtils.isEmpty(code2)) {
            Toast.makeText(this, "请获取手机验证码", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(code1)) {
            Toast.makeText(this, "请输入手机验证码", Toast.LENGTH_LONG).show();
            return;
        }
        if (!code2.equals(code1)) {
            Toast.makeText(this, "验证码输入错误", Toast.LENGTH_LONG).show();
            return;
        }
        if (!phone.equals(savePhone)) {
            Toast.makeText(this, "手机号与验证码不匹配", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject js1 = new JSONObject();
        final JSONObject js2 = new JSONObject();
        try {
            js1.put("username", phone);
            js1.put("password", "");
            js1.put("channel", Constants.Times.channel);
            js1.put("qudao", Constants.Times.channel1);
            js2.put("Register", js1);
        } catch (JSONException e) {

        }

        // 获取数据的进度
        final MyCircleProgressDialog pd = new MyCircleProgressDialog(LoginActivity.this, "登陆中...", R.style.CustomDialog, R.anim.rotating);
        pd.setMessage("登陆中...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                String URL = Constants.Times.URL;
                String nameSpace = Constants.Times.nameSpace;
                String method_Name = "QuickLgn";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                rpc.addProperty("strJson", js2.toString());
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    detail = object.getProperty("QuickLgnResult").toString();
                    if (!TextUtils.isEmpty(detail) && detail.startsWith("0,")) {
                        BaseApplication.userId = detail.substring(2);
                        SPUtils.put(LoginActivity.this, "userId", BaseApplication.userId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    private void sendCode() {
        try {
            if (DeviceUtil.IsNetWork(this) == false) {
                Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
                return;
            }
            phone = etPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show();
                return;
            }
            if (!CheckUtil.isMobile(phone)) {
                Toast.makeText(this, "手机号输入错误", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(yanZhengResult)) {
                initYanzheng();
                loginYanzheng.setVisibility(View.VISIBLE);
                return;
            }
            etYanZhengCode = etYanzheng.getText().toString().trim();

            if (TextUtils.isEmpty(etYanZhengCode)) {
                Toast.makeText(this, "请输入图片里的结果", Toast.LENGTH_LONG).show();
                return;
            }

            if (!yanZhengResult.equals(etYanZhengCode)) {
                Toast.makeText(this, "图片结果输入有误", Toast.LENGTH_LONG).show();
                initYanzheng();
                return;
            }


            JSONObject js1 = new JSONObject();
            final JSONObject js2 = new JSONObject();
            try {
                js1.put("username", phone);
                js1.put("password", GetMyKey.getKey());
                js1.put("channel", Constants.Times.channel);
                js1.put("qudao", Constants.Times.channel1);
                js2.put("Register", js1);
            } catch (JSONException e) {

            }

            // 获取数据的进度
            final MyCircleProgressDialog pd = new MyCircleProgressDialog(LoginActivity.this, "登陆中...", R.style.CustomDialog, R.anim.rotating);
            pd.setMessage("登陆中...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            new Thread(new Runnable() {

                @Override
                public void run() {
                    String nameSpace = Constants.Times.nameSpace;
                    String methodName = "QuickLgnMsg";
                    String URL = Constants.Times.URL;
                    String SOAP_ACTION = nameSpace + methodName;
                    SoapObject rpc = new SoapObject(nameSpace, methodName);
                    rpc.addProperty("strJson", js2.toString());
                    HttpTransportSE transport = new HttpTransportSE(URL);
                    transport.debug = true;
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.bodyOut = rpc;
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(rpc);

                    try {
                        transport.call(SOAP_ACTION, envelope);
                        SoapObject object = (SoapObject) envelope.bodyIn;
                        detail = object.getProperty("QuickLgnMsgResult").toString();
                        if (!TextUtils.isEmpty(detail) && detail.startsWith("0,")) {
                            code2 = detail.substring(2);
                            savePhone = phone;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rl2.setVisibility(View.VISIBLE);
                                    // 开始计时
                                    time.start();
                                    Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                            });
                        } else if (!TextUtils.isEmpty(detail) && detail.startsWith("1,")) {
                            BaseApplication.userId = detail.substring(2);
                            SPUtils.put(LoginActivity.this, "userId", BaseApplication.userId);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                    finish();
                                    pd.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                            });
                        }

                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }

                }
            }).start();


        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_LONG).show();
        }
    }

    private void initYanzheng() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        ivYanzheng.setImageBitmap(bitmap);
        yanZhengCode = codeUtils.getCode();
        yanZhengResult = codeUtils.getResult() + "";
    }

    private void setViews() {
        initYanzheng();
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin1 = (Button) findViewById(R.id.login_btn1);
        rl = (RelativeLayout) findViewById(R.id.login_rl);
        rl2 = (RelativeLayout) findViewById(R.id.login_rl2);
        etPhone = (EditText) findViewById(R.id.login_et1);
        etCode = (EditText) findViewById(R.id.Login_et2);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else { // 两次按键小于2秒时，退出应用
                // 友盟
                MobclickAgent.onKillProcess(this);
                finish();
                System.exit(0);
            }
        }
        return false;
    }

    // 定义一个倒计时的内部类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btnLogin1.setText("重新验证");
            btnLogin1.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnLogin1.setClickable(false);
            btnLogin1.setText(millisUntilFinished / 1000 + "s重新发送");
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

}
