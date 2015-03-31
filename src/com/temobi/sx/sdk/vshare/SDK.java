package com.temobi.sx.sdk.vshare;

import com.temobi.sx.sdk.vshare.utils.PrefUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.Build;

public abstract class SDK {
	
	private ProgressDialog dialog = null;
	private Application mApplication = null;
	private int channelId = 0;

	
	protected abstract void onInvalidAppId();
	protected abstract void onInvalidSession();
	protected abstract void onSucceeded(String userId);
	protected abstract void onFaild();
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SDK(Activity activity, String appId, String appPassord, int channelId) {
		this.mApplication = activity.getApplication();
		this.channelId = channelId;
		
		if (Build.VERSION.SDK_INT >= ProgressDialog.THEME_HOLO_LIGHT)
			dialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
		else
			dialog = new ProgressDialog(activity);
		
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("正在启动...");
		dialog.setCancelable(false);
		dialog.show();
		
		SDKInit.init(mApplication, appId, appPassord, new SDKInit.SDKInitCallback() {
			@Override
			public void onInitSucceed(String appName) {
				SDKInit.validateSession(mApplication, new SDKInit.SDKSessionCallback() {
					@Override
					public void onSessionValid(String userId) {
						dialog.dismiss();
						onSucceeded(userId);
					}
					
					@Override
					public void onSessionInvalid() {
						dialog.dismiss();
						onInvalidSession();
					}
				});
			}
			@Override
			public void onInitError() {
				dialog.dismiss();
				onInvalidAppId();
			}
		});
	}
	
	public void login(String mobile, String validataCode) {
		dialog.show();
		SDKInit.login(mApplication, mobile, validataCode, channelId, new SDKInit.SDKLoginCallback() {
			@Override
			public void onLoginSucceed(String userId) {
				dialog.dismiss();
				onSucceeded(userId);
			}
			
			@Override
			public void onLoginError() {
				dialog.dismiss();
				onFaild();
			}
		});
	}

}
