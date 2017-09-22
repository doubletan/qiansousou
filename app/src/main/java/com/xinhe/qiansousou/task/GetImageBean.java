package com.xinhe.qiansousou.task;

import android.content.Context;

import com.google.gson.Gson;
import com.xinhe.qiansousou.BaseApplication;
import com.xinhe.qiansousou.bean.ImagerBean;
import com.xinhe.qiansousou.util.Constants;
import com.xinhe.qiansousou.util.TinyDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Collections;

/**
 * Created by apple on 2017/4/12.
 *   bunder
 *
 */

public class GetImageBean {
    private Context mContext;

    public GetImageBean(Context mContext) {
        this.mContext = mContext;
    }

    public  void execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = Constants.Times.URL;
                String nameSpace = Constants.Times.nameSpace;
                String method_Name = "Daohang";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("DaohangResult").toString();
                    if (str != null) {
                        Gson gson = new Gson();
                        ImagerBean bean = gson.fromJson(str, ImagerBean.class);
                        Collections.sort(bean.getDaohang());
                        BaseApplication.setImage(bean);
                        TinyDB db = new TinyDB(mContext);
                        db.remove("ImagerBean");
                        db.putObject("ImagerBean",bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
