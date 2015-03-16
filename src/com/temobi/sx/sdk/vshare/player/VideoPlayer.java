package com.temobi.sx.sdk.vshare.player;

import com.temobi.sx.sdk.vshare.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoPlayer extends VideoPlayerBase {

	public VideoPlayer(Context context, String videoId, boolean autoPlay) {
		super(context, videoId, autoPlay);
	}

	public VideoPlayer(Context context, String videoId) {
		super(context, videoId);
	}

	@Override
	protected int getPlayDrawableId() {
		return R.drawable.play;
	}

	@Override
	protected int getLoadingDrawableId() {
		return R.drawable.loading;
	}
	
}
