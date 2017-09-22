package com.xinhe.qiansousou.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinhe.qiansousou.R;


public class MyCircleProgressDialog extends ProgressDialog {

	private Context mContext;
	private String mLoadingTip;
	private int mResid;
	private RotateAnimation refreshingAnimation;
	private TextView mLoadingTv;
	private ImageView mImageView;

	public MyCircleProgressDialog(Context context, String content, int id) {
		super(context);
		this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}
	public MyCircleProgressDialog(Context context, String content, int theme , int id) {
		super(context,theme);
		this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(mContext, mResid);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		refreshingAnimation.setInterpolator(lir);
		mImageView.startAnimation(refreshingAnimation);
		mLoadingTv.setText(mLoadingTip);

	}

	public void setContent(String str) {
		mLoadingTv.setText(str);
	}

	private void initView() {
		setContentView(R.layout.my_circle_progressdialog);
		mLoadingTv = (TextView) findViewById(R.id.my_circle_progress_tv);
		mImageView = (ImageView) findViewById(R.id.my_circle_progress_iv);
	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) { // TODO
	 * Auto-generated method stub mAnimation.start();
	 * super.onWindowFocusChanged(hasFocus); }
	 */
}
