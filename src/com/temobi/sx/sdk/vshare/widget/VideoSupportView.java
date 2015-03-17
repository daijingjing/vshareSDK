package com.temobi.sx.sdk.vshare.widget;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VideoSupportView extends LovedView {

	public VideoSupportView(Context context, RequestQueue requestQueue) {
		super(context, requestQueue);
	}

	@Override
	protected int getDefaultAvatarId() {
		return R.drawable.default_head;
	}

	@Override
	protected int getLaytouId() {
		return R.layout.video_support;
	}

	@Override
	protected View getLoadErrorView() {
		return findViewById(R.id.load_error);
	}

	@Override
	protected View getLoadingView() {
		return findViewById(R.id.loading);
	}

	@Override
	protected View getLovedButton() {
		return findViewById(R.id.loved_button);
	}

	@Override
	protected ViewGroup getLovedList() {
		return (ViewGroup)findViewById(R.id.loved_list);
	}

	@Override
	protected TextView getLovedTotal() {
		return (TextView)findViewById(R.id.loved_total);
	}

}
