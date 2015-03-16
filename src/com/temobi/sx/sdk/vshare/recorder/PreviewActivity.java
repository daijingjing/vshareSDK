package com.temobi.sx.sdk.vshare.recorder;

import com.temobi.sx.sdk.vshare.R;

public class PreviewActivity extends PreviewActivityBase {

	public PreviewActivity() {
		super();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_preview;
	}

	@Override
	protected int getPublishButtonId() {
		return R.id.preview_next;
	}

	@Override
	protected int getPlayButtonId() {
		return R.id.preview_play;
	}

	@Override
	protected int getCancelButtonId() {
		return R.id.preview_cancel;
	}

	@Override
	protected int getPreviewContainerId() {
		return R.id.preview_video_parent;
	}

	@Override
	protected int getSurfaceViewId() {
		return R.id.preview_video;
	}

	@Override
	protected int getProgressBarId() {
		return R.id.preview_progress;
	}
	
}