package com.xinhe.qiansousou.task;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xinhe.qiansousou.BaseApplication;
import com.xinhe.qiansousou.bean.Product;
import com.xinhe.qiansousou.util.Constants;
import com.xinhe.qiansousou.util.TinyDB;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Collections;

/**
 * Created by apple on 2017/4/12.
 * Product 列表以及详情
 */

public class GetProduct {

    public GetProduct(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    public  void execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String URL = Constants.Times.URL;
                String nameSpace =  Constants.Times.nameSpace;
                String method_Name = "GetProduct";
                String SOAP_ACTION = nameSpace + method_Name;
                SoapObject rpc = new SoapObject(nameSpace, method_Name);
                rpc.addProperty("sAppName", Constants.Times.sAppName);
                rpc.addProperty("sPage", "2");
                rpc.addProperty("channel", "3");
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                try {
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject object = (SoapObject) envelope.bodyIn;
                    String str = object.getProperty("GetProductResult").toString();

                    if (!TextUtils.isEmpty(str)&&!str.startsWith("1")&&!str.startsWith("2")) {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(str, Product.class);
                        Collections.sort(product.getPrdList());
                        BaseApplication.setProduct(product);

                        TinyDB db = new TinyDB(mContext);
                        db.remove("Product");
                        db.putObject("Product",product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
