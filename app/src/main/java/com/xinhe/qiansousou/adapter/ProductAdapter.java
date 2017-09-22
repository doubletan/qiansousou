package com.xinhe.qiansousou.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinhe.qiansousou.R;
import com.xinhe.qiansousou.bean.Product;
import com.xinhe.qiansousou.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/4/11.
 */

public class ProductAdapter extends BaseQuickAdapter<Product.PrdListBean,BaseViewHolder> {
    private ArrayList<String>list;
    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ProductAdapter(List<Product.PrdListBean> data) {
        super(R.layout.product_item, data);
        list=new ArrayList<>();
        list.add("2123");
        list.add("3204");
        list.add("10394");
        list.add("4154");
        list.add("12982");
        list.add("8732");
        list.add("6340");
        list.add("7893");
        list.add("3092");
        list.add("4857");
        list.add("36281");
        list.add("8981");
        list.add("12135");
        list.add("9764");
        list.add("2453");
        list.add("3234");
        list.add("10564");
        list.add("4674");
        list.add("18682");
        list.add("8362");
        list.add("6860");
        list.add("7253");
        list.add("3472");
        list.add("4257");
        list.add("35781");
        list.add("8371");
        list.add("14835");
        list.add("9694");
        list.add("4857");
        list.add("36281");
        list.add("8981");
        list.add("12135");
        list.add("9764");
        list.add("2453");
        list.add("3234");
        list.add("10564");
        list.add("4674");
        list.add("18682");
        list.add("8362");
        list.add("6860");
        list.add("7253");
        list.add("3472");
    }
    @Override
    protected void convert(BaseViewHolder helper, Product.PrdListBean item) {
        helper.setText(R.id.tv_ProductName,item.getName())
                        .setText(R.id.tv_Summry,"-"+item.getSummary());

        int layoutPosition = helper.getLayoutPosition();

        if(item.getRate().length()>10){
            helper.setText(R.id.rate,"0.09%/月");
        }else {
            helper.setText(R.id.rate,item.getRate());
        }
            helper.setText(R.id.tv_people,list.get(layoutPosition+1));

        Glide.with(mContext).load(Constants.Times.piURL+item.getLogo()).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into((ImageView) helper.getView(R.id.head));
    }
}
